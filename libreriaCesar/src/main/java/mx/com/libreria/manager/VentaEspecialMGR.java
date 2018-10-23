package mx.com.libreria.manager;

import java.util.ArrayList;

import java.util.List;
import java.util.Set;

import mx.com.libreria.beans.CompraInventarioBean;

import mx.com.libreria.interfases.persistencia.dao.BaseDao;

import mx.com.libreria.model.Abono;
import mx.com.libreria.model.InventarioActual;
import mx.com.libreria.model.VentaEspecial;

public class VentaEspecialMGR implements Constants {

	SessionMGR sessionMGR;

	BaseDao baseDao;
	
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;		
	}

	public void setSessionMGR(SessionMGR sessionMGR) {
		this.sessionMGR = sessionMGR;		
	}
	
	public float getAbonosVE(CatalogMGR catalogoMGR, VentaEspecial ve) { 
		Set<Abono> abonos = ve.getListaAbonos();
		float abonosTotales = 0;
		if (!Utilerias.isNullOrUndefined(abonos)) {
			for (Abono elemento : abonos) { 
				abonosTotales += elemento.getMonto();
			}
		}
		return abonosTotales;
	}
	
	@SuppressWarnings("unchecked")
	public List<VentaEspecial> getVentaEspecial(CatalogMGR catalogoMGR, String fechaInicial, String fechaFinal) { 

		List<Object[]> lista = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"   select ve.ventaEspecialId, 1 " + 
			    "     from VentaEspecial ve " +
			    "    where ve.fechaInicialVentaEspecial >= ? " +
			    "      and ve.fechaInicialVentaEspecial <= ? " +
			    "    order by ve.fechaInicialVentaEspecial asc ",
			    new Object[]{ Utilerias.transformStringToDate(Utilerias.startDate(fechaInicial), FORMAT_DATE_WITH_HOUR),
						  	  Utilerias.transformStringToDate(Utilerias.endDate(fechaFinal), FORMAT_DATE_WITH_HOUR) 
						  	});
			
		List<VentaEspecial> list = new ArrayList<VentaEspecial>();
		for (int i=0; i < lista.size(); i++) { 
			Object[] values = lista.get(i);
			VentaEspecial ve = new VentaEspecial();
			ve.setVentaEspecialId((Integer) values[0]);
			ve = (VentaEspecial) catalogoMGR.getObjectData(ve);
			list.add(ve);
		}		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<VentaEspecial> getVentaEspecial(CatalogMGR catalogoMGR) { 

		List<Object[]> lista = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"   select v.ventaEspecialId, 1 " + 
			    "     from VentaEspecial v " +
			    "    where v.activo = ? " + 
			    "    order by v.fechaFinalVentaEspecial asc ",
			    new Object[]{ 
								Utilerias.YES_VALUE
						  	});
			
		List<VentaEspecial> listVE = new ArrayList<VentaEspecial>();
		for (int i=0; i < lista.size(); i++) { 
			Object[] values = lista.get(i);
			VentaEspecial ve = new VentaEspecial();
			ve.setVentaEspecialId((Integer) values[0]);
			ve = (VentaEspecial) catalogoMGR.getObjectData(ve);
			listVE.add(ve);
		}		
		return listVE;
	}

	@SuppressWarnings("unchecked")
	public List<VentaEspecial> getVentaEspecialPorFecha(CatalogMGR catalogoMGR, String fechaParametroInicial, String fechaParametroFinal) { 

		List<Object[]> lista = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"   select v.ventaEspecialId, 1 " + 
			    "     from VentaEspecial v " +
			    "    where v.fechaInicialVentaEspecial >= ? " +
			    "      and v.fechaInicialVentaEspecial <= ? " + 
			    "      and v.activo = ? " + 
			    "    order by v.fechaInicialVentaEspecial asc ",
			    new Object[]{ 
								Utilerias.transformStringToDate(Utilerias.startDate(fechaParametroInicial), Utilerias.FORMAT_DATE_WITH_HOUR),
								Utilerias.transformStringToDate(Utilerias.endDate(fechaParametroFinal), Utilerias.FORMAT_DATE_WITH_HOUR),
								Utilerias.YES_VALUE
						  	});
			
		List<VentaEspecial> listVE = new ArrayList<VentaEspecial>();
		for (int i=0; i < lista.size(); i++) { 
			Object[] values = lista.get(i);
			VentaEspecial ve = new VentaEspecial();
			ve.setVentaEspecialId((Integer) values[0]);
			ve = (VentaEspecial) catalogoMGR.getObjectData(ve);
			listVE.add(ve);
		}		
		return listVE;
	}
	@SuppressWarnings("unchecked")
	public boolean validarExistenciaProductoParaVE(CatalogMGR catalogoMGR, VentaEspecial ve, int cantidadRequerida) {
		
		if (cantidadRequerida <= 0) { 
			return false;
		}
		
		List<Object[]> lista = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"   select sum(inv.cantidad), inv.producto.productoId " + 
			    "     from Compras compras " +
			    "     join compras.listaInventario inv " +
			    "    where compras.activo = 'Y' " +
			    "      and inv.producto.productoId = ? " + 
			    "      and inv.estatus = 'E'" + 
			    "    group by inv.producto.productoId ",
			    new Object[]{ ve.getProducto().getProductoId() });
		
		for (int i=0; i < lista.size(); i++) {  
			Object[] elementos = (Object[]) lista.get(i);
			long cantidad = (Long) elementos[0];
			if (cantidadRequerida <= cantidad) { 
				return true;
			}
		}
		return false;
	}
	
	public String abonarVentaEspecial(CatalogMGR catalogoMGR, VentaEspecial ven, String abono) {
		StringBuffer sb = new StringBuffer();
		float cantAbonada = Utilerias.strToFloat(abono, Utilerias.INFINITY_VALUE_FLOAT);
		if (cantAbonada == Utilerias.INFINITY_VALUE_FLOAT) { 
			sb.append("- La cantidad introducida abonada contiene valores incorrectos, favor de verificarlo -");
			return sb.toString();
		}
		
		Set<Abono> abonos = ven.getListaAbonos();
		
		Abono ele = new Abono();
		ele.setAplicaCompraOVenta("V");
		ele.setFechaAbono(Utilerias.getDateToday(Utilerias.FORMAT_DATE_WITH_HOUR));
		ele.setMonto(cantAbonada);
		
		abonos.add(ele);
		
		int result = catalogoMGR.dmlOperations(3, ven);
		if (result > 0) { 
			sb.append("Se actualizaron correctamente los abonos");
		} else { 
			sb.append("Hubo problemas al actualizar los abonos");
		}
		
		return sb.toString();
	}
	
	private void actualizarVEInventario(CatalogMGR catalogoMGR, InventarioMGR invMGR, VentaEspecial ve, int cantidad) {
		
		List<CompraInventarioBean> inventarioComprasBD = (ArrayList<CompraInventarioBean>) 
			invMGR.getCantidadPrecioFechaEntradaXCadaInventario(ve.getProducto(), catalogoMGR, 0);
	
		//Set<InventarioActual> inventarioActualizadoAVE = new HashSet<InventarioActual>();
		
		InventarioActual elementoBD = null;
		InventarioActual elementoActual = null;
		
		if (inventarioComprasBD.size() > 0) { 
			
			int cantidadInicial = cantidad;
			
			for (int i = 0; i <= inventarioComprasBD.size() && cantidadInicial > 0; i++) {
				
				CompraInventarioBean bean = inventarioComprasBD.get(i);
				elementoBD = new InventarioActual();
				elementoBD.setInventarioId(bean.getInventarioId());
				elementoBD = (InventarioActual) catalogoMGR.getObjectData(elementoBD);
			
				//caso de 3 sopas
				if (cantidadInicial < elementoBD.getCantidad()) { //cantidad requerida es suficiente con este elemento de inventario
					
					//descuento al de bd la cantidad que voy a tomar
					elementoBD.setCantidad(elementoBD.getCantidad() - cantidadInicial);
					
					//actualizo a bd el inventario de bd
					catalogoMGR.dmlOperations(1, elementoBD);
										
					//se creara un nuevo inventario
					//member to be inserted
					elementoActual = new InventarioActual();					
					
					elementoActual.setCantidad(cantidadInicial);
					
					//member to recover from database element
					elementoActual.setCostoProducto(elementoBD.getCostoProducto());
					elementoActual.setFechaEntrada(elementoBD.getFechaEntrada());
					elementoActual.setProducto(elementoBD.getProducto());
					elementoActual.setDefectuoso(NO_VALUE);
					
					//validar si esta regla es correcta
					elementoActual.setPrecioProducto(ve.getMontoInicial() / cantidad);
					
					//para saber que compra actualizar posteriormente
					//tomando como referencia el id de la remision
					elementoActual.setControlComprasId(bean.getComprasId());
										
					//actualizo la cantidad
					cantidadInicial = 0;
					
					//para saber que compra actualizar se agrega un nuevo inventario
					elementoActual.setFechaSalida(Utilerias.getDateToday(Utilerias.FORMAT_DATE));
					elementoActual.setEstatus("S");
					
					ve.getListaInventario().add(elementoActual);
					
				} else if (cantidadInicial == elementoBD.getCantidad()) { //cantidades iguales 
					
					//se actualizara el inventario con salida
					elementoActual = elementoBD;

					//validar todavia esto
					elementoActual.setPrecioProducto(ve.getMontoInicial() / cantidad);
					elementoActual.setDefectuoso(NO_VALUE);
					
					//actualizamos la cantidad
					cantidadInicial = 0;
					
					elementoActual.setFechaSalida(Utilerias.getDateToday(Utilerias.FORMAT_DATE));
					elementoActual.setEstatus("S");
					//inventarioActualizadoAVE.add(elementoActual);
					ve.getListaInventario().add(elementoActual);
					
				} else if (cantidadInicial > elementoBD.getCantidad()) {
					
					//se actualizara el inventario con salida
					elementoActual = elementoBD;

					//validar todavia esto
					elementoActual.setPrecioProducto(ve.getMontoInicial() / cantidad);
					elementoActual.setDefectuoso(NO_VALUE);
					
					//actualizamos la cantidad de los que ya se asignaron
					cantidadInicial = cantidadInicial - elementoBD.getCantidad();
					
					elementoActual.setFechaSalida(Utilerias.getDateToday(Utilerias.FORMAT_DATE));
					elementoActual.setEstatus("S");
					
					//inventarioActualizadoAVE.add(elementoActual);
					ve.getListaInventario().add(elementoActual);
					
				} else { 
					Logs.debug(RemisionesMGR.class, "Problema fuerte en ventas especiales, favor de validar mauro");
				}
			} //del for
		} else {
			Logs.debug(RemisionesMGR.class, "Problema fuerte en ventas especiales, no hay suficiente inventario");
		}
		
		//return inventarioActualizadoAVE;
	}
	
	public int agregarSalidaInventarioSistema(CatalogMGR catalogoMGR, InventarioMGR invMGR, VentaEspecial ve, int cantidadInicial) {
		
		int operacionExitosa = -1;		
		
		//este es el metodo interesante
		//ve.setListaInventario(actualizarVEInventario(catalogoMGR, invMGR, ve, cantidadInicial));
		actualizarVEInventario(catalogoMGR, invMGR, ve, cantidadInicial);
		
		Logs.debug(VentaEspecialMGR.class, "Sice ve: " + ve.getListaInventario().size());
		
		operacionExitosa = catalogoMGR.dmlOperations(3, ve);
		
		Logs.debug(VentaEspecialMGR.class, "Esperemos lo logre en VentaEspecialMGR: " + ve.getVentaEspecialId() + " " + operacionExitosa);
		
		//si todo fue exitoso
		if (operacionExitosa > 0) {
			
			//actualizar si hubo algunos cortes de compra que tienen que ser actualizados
			//para el inventario de esta compra, porque sino habra diferencias de inventarios.
			List<CompraInventarioBean> listaComprasCortadas = getCortesCompraDeVE(ve.getVentaEspecialId(), catalogoMGR);
			
			//este metodo es el complementario del corte, para actualizar las compras si se movieron algunos
			//inventarios a algunas remisiones, y se queden actualizadas correctamente las compras
			if (listaComprasCortadas.size() > 0) { 
				Logs.debug(VentaEspecialMGR.class, "Entro al ciclo para actualizar el inventario de las compras referente a una venta especial - mauro");
				invMGR.procesarCortesActualizarInventarioParaCompra(listaComprasCortadas, catalogoMGR);
			}			
		}
		
		return operacionExitosa;
	}
	
	@SuppressWarnings({ "unchecked" })
	private List<CompraInventarioBean> getCortesCompraDeVE(int idVE, CatalogMGR catalogoMGR) {
		
		Logs.debug(VentaEspecialMGR.class, "ID para getCortes: " + idVE);
		
		List<Object[]> lista = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"   select inv.inventarioId, inv.controlComprasId " + 
			    "     from VentaEspecial ve " +
			    "     join ve.listaInventario inv " +
			    "    where ve.ventaEspecialId = ? " +
			    "      and ve.activo = 'Y' " + 
			    "      and inv.controlComprasId is not null " + 
			    "      and inv.controlComprasId <> 0 ",			    
		        new Object[]{ idVE });
		
		List<CompraInventarioBean> listBean = new ArrayList<CompraInventarioBean>();
		
		for (int i=0; i < lista.size(); i++) { 
			Object[] values = lista.get(i);
			CompraInventarioBean bean = new CompraInventarioBean();
			bean.setInventarioId(Utilerias.strToInt(values[0].toString()));
			bean.setComprasId(Utilerias.strToInt(values[1].toString()));
			listBean.add(bean);
		}
		return listBean;
	}

}
