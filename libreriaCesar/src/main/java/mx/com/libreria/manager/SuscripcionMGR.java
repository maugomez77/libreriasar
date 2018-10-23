package mx.com.libreria.manager;

import java.util.ArrayList;
import java.util.List;

import mx.com.libreria.beans.CompraInventarioBean;
import mx.com.libreria.interfases.persistencia.dao.BaseDao;

import mx.com.libreria.model.InventarioActual;
import mx.com.libreria.model.Suscripcion;

public class SuscripcionMGR implements Constants {

	SessionMGR sessionMGR;

	BaseDao baseDao;
	
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;		
	}

	public void setSessionMGR(SessionMGR sessionMGR) {
		this.sessionMGR = sessionMGR;		
	}
	
	@SuppressWarnings("unchecked")
	public List<Suscripcion> getSuscripciones(CatalogMGR catalogoMGR, String fechaInicial, String fechaFinal) { 

		List<Object[]> lista = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"   select sus.suscripcionId, 1 " + 
			    "     from Suscripcion sus " +
			    "    where sus.fechaInicialSuscripcion >= ? " +
			    "      and sus.fechaInicialSuscripcion <= ? " +
			    "    order by sus.fechaInicialSuscripcion asc ",
			    new Object[]{ Utilerias.transformStringToDate(Utilerias.startDate(fechaInicial), FORMAT_DATE_WITH_HOUR),
						  	  Utilerias.transformStringToDate(Utilerias.endDate(fechaFinal), FORMAT_DATE_WITH_HOUR) 
						  	});
			
		List<Suscripcion> listSus = new ArrayList<Suscripcion>();
		for (int i=0; i < lista.size(); i++) { 
			Object[] values = lista.get(i);
			Suscripcion sus = new Suscripcion();
			sus.setSuscripcionId((Integer) values[0]);
			sus = (Suscripcion) catalogoMGR.getObjectData(sus);
			listSus.add(sus);
		}		
		return listSus;
	}
	
	public int agregarSalidaInventarioSistema(CatalogMGR catalogoMGR, InventarioMGR invMGR, Suscripcion sus) {
				
		int operacionExitosa = -1;		
		
		//este es el metodo interesante
		//sus.setListaInventario(actualizarSuscripcionInventario(catalogoMGR, invMGR, sus));
		actualizarSuscripcionInventario(catalogoMGR, invMGR, sus);
		
		operacionExitosa = catalogoMGR.dmlOperations(3, sus);
		
		Logs.debug(SuscripcionMGR.class, "Esperemos lo logre en SuscripcionMGR: " + sus.getSuscripcionId());
		
		//si todo fue exitoso
		if (operacionExitosa > 0) {
			
			//actualizar si hubo algunos cortes de compra que tienen que ser actualizados
			//para el inventario de esta compra, porque sino habra diferencias de inventarios.
			List<CompraInventarioBean> listaComprasCortadas = getCortesCompraDeSuscripcion(operacionExitosa, catalogoMGR);
			
			//este metodo es el complementario del corte, para actualizar las compras si se movieron algunos
			//inventarios a algunas remisiones, y se queden actualizadas correctamente las compras
			if (listaComprasCortadas.size() > 0) { 
				Logs.debug(SuscripcionMGR.class, "Entro al ciclo para actualizar el inventario de las compras referente a una suscripcion - mauro");
				invMGR.procesarCortesActualizarInventarioParaCompra(listaComprasCortadas, catalogoMGR);
			}			
		}
		
		return operacionExitosa;
	} 
	
	@SuppressWarnings("unchecked")
	private List<CompraInventarioBean> getCortesCompraDeSuscripcion(int idSuscripcion, CatalogMGR catalogoMGR) {
		
		List<Object[]> lista = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"   select inv.inventarioId, inv.controlComprasId " + 
			    "     from Suscripcion sus " +
			    "     join sus.listaInventario inv " +
			    "    where sus.suscripcionId = ? " +
			    "      and sus.activo = 'Y' " + 
			    "      and inv.controlComprasId is not null " + 
			    "      and inv.controlComprasId <> 0 ",			    
		        new Object[]{ idSuscripcion });

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
	
	private void actualizarSuscripcionInventario(CatalogMGR catalogoMGR, InventarioMGR invMGR, Suscripcion sus) {
		
		List<CompraInventarioBean> inventarioComprasBD = (ArrayList<CompraInventarioBean>) 
			invMGR.getCantidadPrecioFechaEntradaXCadaInventario(sus.getProducto(), catalogoMGR, 1);
	
		//Set<InventarioActual> inventarioActualizadoASuscripcion = new HashSet<InventarioActual>();
		
		InventarioActual elementoBD = null;
		InventarioActual elementoActual = null;
		
		if (sus.getNumeroMesesSuscripcionRestante() > 0 && inventarioComprasBD.size() > 0) { 
			
			int cantidadInicial = 1;
			
			for (int i = 0; i <= inventarioComprasBD.size() && cantidadInicial > 0; i++) {
				
				boolean entraSus = false;
				
				CompraInventarioBean bean = inventarioComprasBD.get(i);
				elementoBD = new InventarioActual();
				elementoBD.setInventarioId(bean.getInventarioId());
				elementoBD = (InventarioActual) catalogoMGR.getObjectData(elementoBD);
			
				//caso de 2 sopas				
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
					elementoActual.setPrecioProducto(sus.getMontoFactura() / sus.getNumeroMesesSuscripcion());
					elementoActual.setDefectuoso(NO_VALUE);

					//para saber que compra actualizar posteriormente
					//tomando como referencia el id de la remision
					elementoActual.setControlComprasId(bean.getComprasId());
															
					cantidadInicial = 0;
					
					//para saber que compra actualizar se agrega un nuevo inventario
					elementoActual.setFechaSalida(Utilerias.getDateToday(Utilerias.FORMAT_DATE));
					elementoActual.setEstatus("S");
					
					//inventarioActualizadoASuscripcion.add(elementoActual);
					sus.getListaInventario().add(elementoActual);
					
					entraSus = true;
					
				} else if (cantidadInicial == elementoBD.getCantidad()) { //cantidades iguales 
					
					//se actualizara el inventario con salida
					elementoActual = elementoBD;
					
					elementoActual.setPrecioProducto(sus.getMontoFactura() / sus.getNumeroMesesSuscripcion());
					elementoActual.setDefectuoso(NO_VALUE);
					
					cantidadInicial = 0;
					
					//para saber que compra actualizar se agrega un nuevo inventario
					elementoActual.setFechaSalida(Utilerias.getDateToday(Utilerias.FORMAT_DATE));
					elementoActual.setEstatus("S");
					
					//inventarioActualizadoASuscripcion.add(elementoActual);
					sus.getListaInventario().add(elementoActual);
					
					entraSus = true; 
					
				} else { 
					Logs.debug(RemisionesMGR.class, "Problema fuerte en suscripciones, favor de validar mauro");
				}
			
				if (entraSus) { 
					sus.setNumeroMesesSuscripcionRestante(sus.getNumeroMesesSuscripcionRestante() - 1);
					
					//se regresa el id de la suscripcion
					//idSuscripcion = catalogoMGR.dmlOperations(3, sus);
				}				
			} //del for
		} else {
			Logs.debug(RemisionesMGR.class, "Problema fuerte en suscripciones, ya se agotaron o no hay inventario");
		}
		
		//return inventarioActualizadoASuscripcion;
	}
	
	@SuppressWarnings("unchecked")
	public boolean validarExistenciaProductoParaSuscripcion(CatalogMGR catalogoMGR, Suscripcion sus, int cantidadRequerida) {
		
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
			    new Object[]{ sus.getProducto().getProductoId() });
		
		for (int i=0; i < lista.size(); i++) {  
			Object[] elementos = (Object[]) lista.get(i);
			long cantidad = (Long) elementos[0];
			if (cantidadRequerida <= cantidad) { 
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public List<Suscripcion> getSuscripcionesValidas(CatalogMGR catalogoMGR) {
		
		List<Object[]> lista = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"   select sus.suscripcionId, 1 " + 
			    "     from Suscripcion sus " +
			    "    where sus.numeroMesesSuscripcionRestante >= 0 " + 
			    "      and sus.activo = ? ",
			    new Object[]{ 
								Utilerias.YES_VALUE
							 });					
			
		List<Suscripcion> listaSuscripcion = new ArrayList<Suscripcion>();
		
		for (int i=0; i < lista.size(); i++) {  
			Object[] elementos = (Object[]) lista.get(i);
			
			Suscripcion suscripcion = new Suscripcion();
			suscripcion.setSuscripcionId((Integer) elementos[0]);
			suscripcion = (Suscripcion) catalogoMGR.getObjectData(suscripcion);
			
			listaSuscripcion.add(suscripcion);
		}
		
		return listaSuscripcion;
				
	}
	
	@SuppressWarnings("unchecked")
	public List<Suscripcion> getSuscripcionesValidasPorFecha(CatalogMGR catalogoMGR, String fechaParametroInicial, String fechaParametroFinal) {
		
		List<Object[]> lista = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"   select sus.suscripcionId, 1 " + 
			    "     from Suscripcion sus " +
			    "    where sus.numeroMesesSuscripcionRestante >= 0 " + 
			    "      and sus.activo = ? " +  
			    "      and sus.fechaInicialSuscripcion >= ? " + 
			    "      and sus.fechaInicialSuscripcion <= ? ",
			    new Object[]{ 
								Utilerias.YES_VALUE,
								Utilerias.transformStringToDate(Utilerias.startDate(fechaParametroInicial), Utilerias.FORMAT_DATE_WITH_HOUR),
								Utilerias.transformStringToDate(Utilerias.endDate(fechaParametroFinal), Utilerias.FORMAT_DATE_WITH_HOUR)
							 });					
			
		List<Suscripcion> listaSuscripcion = new ArrayList<Suscripcion>();
		
		for (int i=0; i < lista.size(); i++) {  
			Object[] elementos = (Object[]) lista.get(i);
			
			Suscripcion suscripcion = new Suscripcion();
			suscripcion.setSuscripcionId((Integer) elementos[0]);
			suscripcion = (Suscripcion) catalogoMGR.getObjectData(suscripcion);
			
			listaSuscripcion.add(suscripcion);
		}
		
		return listaSuscripcion;
				
	}
}
