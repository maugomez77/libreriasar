package mx.com.libreria.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import mx.com.libreria.factory.ObjectFactory;

import mx.com.libreria.interfases.persistencia.dao.BaseDao;

import mx.com.libreria.model.InventarioHistorico;
import mx.com.libreria.model.Producto;
import mx.com.libreria.model.SnapShotInventarioHistorico;
import mx.com.libreria.model.TipoGastoEnum;

public class InventarioHistoricoMGR implements Constants {

	SessionMGR sessionMGR;

	BaseDao baseDao;
	
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;		
	}

	public void setSessionMGR(SessionMGR sessionMGR) {
		this.sessionMGR = sessionMGR;		
	}
	
	@SuppressWarnings("unchecked")
	public int eliminarSnapShotNoNecesarios(CatalogMGR catalogoMGR) {
		
		List<Object[]> listaGenerica = 
				(ArrayList<Object[]>)
					catalogoMGR.getListParameters(
					"   select snap.snapShotId, snap.fechaSnapShot " +
					"     from SnapShotInventarioHistorico as snap " +  
					"    where snap.fechaSnapShot >= ? " +
					"      and snap.fechaSnapShot <= ? " +
					"    order by snap.fechaSnapShot asc ", 		        			 
			        new Object[]{ Utilerias.transformStringToDate(Utilerias.startDate(Utilerias.getDateStringToday(Utilerias.FORMAT_DATE)), Utilerias.FORMAT_DATE_WITH_HOUR), 
							      Utilerias.transformStringToDate(Utilerias.endDate(Utilerias.getDateStringToday(Utilerias.FORMAT_DATE)), Utilerias.FORMAT_DATE_WITH_HOUR)
							    });					
		
		int operacion = INFINITY_VALUE_INTEGER;
		
		//dejar solo un inventario mas actual
		if (listaGenerica.size() > 1) { 
			for (int i=0; i < listaGenerica.size() - 1; i++) { //quiero dejar al menos un inventario por dia, por esto la condicion del for. 			
				Object[] obj = listaGenerica.get(i);
				SnapShotInventarioHistorico historicoInv = new SnapShotInventarioHistorico();
				historicoInv.setSnapShotId(Utilerias.strToInt(obj[0].toString()));
				
				//se elimina
				operacion = catalogoMGR.dmlOperations(-1, historicoInv);
			}
		}
		
		return operacion;
	}
	
	@SuppressWarnings("unchecked")
	public int moverInventarioActualAHistorico(CatalogMGR catalogoMGR) {
						
		List<Object[]> listaGenerica = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"   select inv.inventarioId, inv.producto, inv.fechaEntrada, inv.fechaSalida, inv.costoProducto, inv.precioProducto, inv.iva, " +
				"          inv.cantidad, inv.estatus, inv.controlComprasId, inv.defectuoso " +
				"     from Compras compras " +
			    "     join compras.listaInventario inv " + 
				"    where compras.activo = 'Y' ",
		        //"    where inv.estatus = ? ", 		        			 
		        new Object[]{ });
		
		List<InventarioHistorico> listaHistorica = new ArrayList<InventarioHistorico>();
		
		for (int i=0; i < listaGenerica.size(); i++) { 			
			Object[] obj = listaGenerica.get(i);
			
			InventarioHistorico historico = new InventarioHistorico();			
			historico.setInventarioId((Integer) obj[0]);			
			historico.setProducto((Producto) obj[1]);			
			historico.setFechaEntrada((Date) obj[2]);
			historico.setFechaSalida((Date) obj[3]);
			historico.setCostoProducto((Float) obj[4]);
			historico.setPrecioProducto((Float) obj[5]);
			historico.setIva((Float) obj[6]);
			historico.setCantidad((Integer) obj[7]);
			historico.setEstatus((String) obj[8]);
			historico.setControlComprasId((Integer) obj[9]);
			historico.setDefectuoso((String) obj[10]);
			
			historico.setFechaInventarioHistorico(Utilerias.getDateToday(Utilerias.FORMAT_DATE));
			
			listaHistorica.add(historico);
		}
		
		SnapShotInventarioHistorico listaGuardar = new SnapShotInventarioHistorico();
		listaGuardar.setFechaSnapShot(Utilerias.getDateToday(Utilerias.FORMAT_DATE));
		listaGuardar.setListaInventario((Set<InventarioHistorico>) Utilerias.convertListToSet(listaHistorica, new InventarioHistorico()));
		
		return catalogoMGR.dmlOperations(2, listaGuardar);						
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private int obtenerSnapShotMasProximo(String fechaParametro, CatalogMGR catalogoMGR) {
		
		List<Object[]> listaGenerica = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"   select foto.snapShotId, foto.fechaSnapShot " +
				"     from SnapShotInventarioHistorico foto " + 
		        "    where foto.fechaSnapShot >= ? " + //para verificar rango start como fecha parametro, ya que no hubo generado en esta fecha.
		        "      and foto.fechaSnapShot < current_date() " + //para verificar rango end final, que no sea la fecha actual
		        "    order by foto.fechaSnapShot asc ",//quiero la mas viejita generadas, por esto el ascedente
		        new Object[]{ Utilerias.transformStringToDate(Utilerias.startDate(fechaParametro), FORMAT_DATE_WITH_HOUR) });
	
		Object[] values = (Object[]) new Integer[]{ 0 };
		
		if (Utilerias.checkList(listaGenerica)) { 
			values = listaGenerica.get(0);
			if (Utilerias.isNullOrUndefined(values[0])) { 
				values[0] = "0";
			}
		}		
		return Utilerias.strToInt(values[0].toString());		
	}	
	
	
	@SuppressWarnings("unchecked")
	public float getInventarioHistoricoDeUnDia(String fechaParametro, CatalogMGR catalogoMGR) { 
		
		int fotoDiaId = getSnapShotDeUnDia(fechaParametro, catalogoMGR);
		
		//significa que no encontramos un id de esta fecha 
		//if (fotoDiaId <= 0) {
		//	fotoDiaId = obtenerSnapShotMasProximo(fechaParametro, catalogoMGR);
		//}
		
		//if (fotoDiaId <= 0) {
			
			//posiblemente es de mayor a hoy, si fuera hoy desde la primer sentencia hubiera agarrado el id
			
			//se toma como mayor el que se tenga a la fecha del dia de hoy
			//fotoDiaId = getSnapShotDeUnDia(Utilerias.getDateStringToday(Utilerias.FORMAT_DATE), catalogoMGR);						
		//}
		
		if (fotoDiaId <= 0) { 
			return 0;
		}
			
		//aqui se copiara todo el inventario, no tomando en cuenta si la compra, remision, venta especial o suscripcion estan
		//desactivadas, pero posteriormente los reportes checaran si se encuentran activos los elementos de negocio
		List<Object[]> listaGenerica = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"   select sum(inv.costoProducto * inv.cantidad) as costoTotalInventario, inv.costoProducto, inv.cantidad " +
				"     from SnapShotInventarioHistorico foto " +
				"     join foto.listaInventario inv " +			    
		        "    where foto.snapShotId = ? " + 
				"      and inv.estatus = 'E' ",		    
		        new Object[]{ fotoDiaId });
		
		Object[] values = (Object[]) new Double []{ 0.0 };
		
		if (Utilerias.checkList(listaGenerica)) { 
			values = listaGenerica.get(0);
			if (Utilerias.isNullOrUndefined(values[0])) { 
				values[0] = "0.0";
			}
		}
		
		return Utilerias.strToFloat(values[0].toString());		
	}
	
	@SuppressWarnings("unchecked")
	private int getSnapShotDeUnDia(String fechaParametro, CatalogMGR catalogoMGR) {
		
		List<Object[]> listaGenerica = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"   select foto.snapShotId, foto.fechaSnapShot " +
				"     from SnapShotInventarioHistorico foto " + 
		        "    where foto.fechaSnapShot >= ? " + //para verificar rango start inicial
		        "      and foto.fechaSnapShot <= ? " + //para verificar rango end final.
		        "    order by foto.fechaSnapShot desc ",
		        new Object[]{ Utilerias.transformStringToDate(Utilerias.startDate(fechaParametro), FORMAT_DATE_WITH_HOUR),
						      Utilerias.transformStringToDate(Utilerias.endDate(fechaParametro), FORMAT_DATE_WITH_HOUR) 
						    });
	
		Object[] values = (Object[]) new Integer[]{ 0 };
		
		if (Utilerias.checkList(listaGenerica)) { 
			values = listaGenerica.get(0);
			if (Utilerias.isNullOrUndefined(values[0])) { 
				values[0] = "0";
			}
		}		
		return Utilerias.strToInt(values[0].toString());		
	}	
	
	@SuppressWarnings({ "unchecked" })
	public float getTipoGastosMontoTotal(String fechaInicial, String fechaFinal, TipoGastoEnum tipoGasto, CatalogMGR catalogoMGR) {
		
		List<Object[]> listaGenerica = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"   select sum(gastos.montoGasto) as montoTotalGasto, count(*) as cuantos " +
				"     from Gastos gastos " +
				"    where gastos.fechaAplicacion >= ? " +
		        "      and gastos.fechaAplicacion <= ? " + 
		        "      and gastos.activo = 'Y' " + 
		        "      and gastos.tipoGasto.tipoGastoEnum = ? ",		    
		        new Object[]{ 
								Utilerias.transformStringToDate(Utilerias.startDate(fechaInicial), FORMAT_DATE_WITH_HOUR),
						  	  	Utilerias.transformStringToDate(Utilerias.endDate(fechaFinal), FORMAT_DATE_WITH_HOUR), 
						  	  	tipoGasto
					        });
		Object[] values = (Object[]) new Double[] { 0.0 };
		
		if (Utilerias.checkList(listaGenerica)) { 
			values = listaGenerica.get(0);
			if (Utilerias.isNullOrUndefined(values[0])) { 
				values[0] = "0.0";
			}
		}
		return Utilerias.strToFloat(values[0].toString());
	}
	
	public static void main(String[] args) {
		
		InventarioHistoricoMGR hist = (InventarioHistoricoMGR) ObjectFactory.getBean("inventarioHistoricoMGR");
		CatalogMGR catalogoMGR = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");
    	System.out.println("Resultado: " + hist.moverInventarioActualAHistorico(catalogoMGR));
	}
}
