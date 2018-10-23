package mx.com.libreria.manager;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.com.libreria.beans.CompraInventarioBean;
import mx.com.libreria.interfases.persistencia.dao.BaseDao;

import mx.com.libreria.model.Abono;
import mx.com.libreria.model.Cliente;
import mx.com.libreria.model.Devolucion;
import mx.com.libreria.model.InventarioActual;
import mx.com.libreria.model.Producto;
import mx.com.libreria.model.Remision;

public class RemisionesMGR implements Constants {

	SessionMGR sessionMGR;

	BaseDao baseDao;
	
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;		
	}

	public void setSessionMGR(SessionMGR sessionMGR) {
		this.sessionMGR = sessionMGR;		
	}
	
	private void agregarRemisionSesion(final List<InventarioActual> lista, final InventarioActual inv, final HttpServletRequest request) { 
		//agregar nuevo elemento a la lista actual
		//y actualizar sesion
		lista.add(inv);
		sessionMGR.setAttributeSession(request, REMISION_ACTUAL_SESION, lista);
	}
	
	@SuppressWarnings("rawtypes")
	public long getExistenciaInventario(Producto prod, CatalogMGR catalogoMGR) {
		
		List lista = catalogoMGR.getListParameters(
			"   select sum(inv.cantidad) as cantidad " + 
		    "     from Compras compras " +
		    "     join compras.listaInventario inv " +		    
	        "    where compras.activo = 'Y' " +
	        "      and inv.producto.productoId = ? " +
	        "      and inv.estatus = 'E' ",
		    //" group by inv.precioProducto " + 
		    //" order by inv.precioProducto desc ", 
	        new Object[]{ prod.getProductoId() });
		
		long existencia = 0;
		for (int i=0; i < lista.size(); i++) { 
			existencia += (Long) lista.get(i);
			//existencia += (Long) elementos[0];
		}
		
		return existencia;
	}
		
	@SuppressWarnings("rawtypes")
	public List getPreciosXProducto(Producto prod, CatalogMGR catalogoMGR) {
		
		List lista = catalogoMGR.getListParameters(
				"   select sum(inv.cantidad) as cantidad, inv.precioProducto, inv.iva " + 
			    "     from Compras compras " +
			    "     join compras.listaInventario inv " +
		        "    where compras.activo = 'Y' " +
		        "      and inv.producto.productoId = ? " +
		        "      and inv.estatus = 'E' " +
			    " group by inv.precioProducto, inv.iva " + 
			    " order by inv.precioProducto desc ", 
		        new Object[]{ prod.getProductoId() });
			
		return lista;
	}
	
	public String validarUnProductoAgregarARemisionSesionActual(InventarioActual inv, CatalogMGR catalogo) {
		StringBuilder sb = new StringBuilder();
		long montoExistenciaProductoSistema = getExistenciaInventario(inv.getProducto(), catalogo);
		if (inv.getCantidad() > montoExistenciaProductoSistema) { 
			sb.append("- La cantidad que quieres vender es mayor al inventario en el sistema -");
		}
		return sb.toString();
	}
	
	public String validarUnProductoAgregarAREmisionChecandoListaSesionActual(HttpServletRequest request, InventarioActual inv, CatalogMGR catalogo) { 		
		StringBuilder sb = new StringBuilder();
		List<InventarioActual> lista = checarListaSesion(request);
		for (int i=0; i < lista.size(); i++) { 
			InventarioActual elemento = lista.get(i);
			if (elemento.getProducto().getProductoId() == inv.getProducto().getProductoId()) {
				sb.append("- Este producto ya se encuentra en la venta actual de la sesion -");
				break;				
			}
		}
		return sb.toString();
	}
	
	public void agregarRemisionSesion(HttpServletRequest request, HttpServletResponse  response, InventarioActual inv) 
		throws IOException {
	
		if (!sessionMGR.validarSesion(request, response)) { 
			response.sendRedirect(request.getContextPath() + LOGIN_INDEX);
		}
	
		List<InventarioActual> lista = checarListaSesion(request);
		inv.setDefectuoso(NO_VALUE);
		
		// regla de productos defectuosos
		if (inv.getPrecioProducto() == 0) { 
			inv.setDefectuoso(YES_VALUE);
		}
		
		agregarRemisionSesion(lista, inv, request);				
	}

	@SuppressWarnings("unchecked")
	private List<InventarioActual> checarListaSesion(final HttpServletRequest request) { 
		
		//traer la lista actual
		List<InventarioActual> lista = null;
		if (sessionMGR.getAttributeSession(request, REMISION_ACTUAL_SESION) != null) { 
			lista = (List<InventarioActual>) sessionMGR.getAttributeSession(request, REMISION_ACTUAL_SESION);
		} else { 
			lista = new ArrayList<InventarioActual>();
		}
		return lista;
	}
	
	public float getMontoFactura(List<InventarioActual> lista) {
		float montoTotalCompra = 0;
		InventarioActual inventario = null;		
		for (int i=0; i < lista.size(); i++) {
			inventario = lista.get(i);
			montoTotalCompra += inventario.getPrecioProducto() * inventario.getCantidad(); 
		}		
		return montoTotalCompra;
	}
		
	public String checarInventarioSesionSinMovimientos(final HttpServletRequest request, CatalogMGR catalogo) {
		StringBuilder sb = new StringBuilder();		
		List<InventarioActual> lista = checarListaSesion(request);
		for (int i=0; i < lista.size(); i++) { 
			InventarioActual elemento = lista.get(i);
			elemento.getCantidad();
			if (elemento.getCantidad() > getExistenciaInventario(elemento.getProducto(), catalogo)) { 
				sb.append("- El producto " + elemento.getProducto().getNombreProducto() + " ha cambiado su existencia, eliminarlo para corroborar la existencia de este -");
				break;
			}
		}
		return sb.toString();
	}
	
	public void agregarAbonoListaVentas(Remision prod, float abono) {
		
		Set<Abono> listAbono = prod.getListaAbonos();
		
		Abono nuevo = new Abono();
		nuevo.setAplicaCompraOVenta("V");
		nuevo.setFechaAbono(Utilerias.getDateToday(Utilerias.FORMAT_DATE));
		nuevo.setMonto(abono);
		listAbono.add(nuevo);
			
		prod.setListaAbonos(listAbono);
			
	}
	
	public static float getAbonosTotales(Remision remision) { 		
		Iterator<Abono> it = remision.getListaAbonos().iterator();
		float totalAbonos = 0;
		while (it.hasNext()) { 
			Abono abono = it.next();
			totalAbonos += abono.getMonto();
		}
		return totalAbonos;
	}

	public String validaMontoAbonadoConFacturado(Remision remision, boolean validaAbono, String abono) {	
		
		String result = "";
		if (remision.getEstatusPagoCredito() == null) { 
			result = "- Favor de capturar la forma de pago, ya sea a credito o contado. -";
			return result;
		}
		
		Logs.debug(InventarioMGR.class, "objeto remision: " 
				+ remision.getMontoInicial() + " " 
				+ remision.getEstatusPagoCredito() + " " 
				+ remision.getMontoFactura());
		
		if (remision.getMontoFactura() > 0 && remision.getEstatusPagoCredito().equals("C")) { 
			if (remision.getMontoInicial() < 0) {
				result = "-  Favor de capturar un valor positivo o cero para el monto abonado -";
			}
			
			if (remision.getMontoInicial() > remision.getMontoFactura()) { 
				result += "- Favor de capturar un valor positivo para el monto inicial que sea menor al valor de la factura -";
			}
			
			if (validaAbono) { 
				float abonoAInsertar = Utilerias.strToFloat(abono);
				if (abonoAInsertar <= 0) { 
					result += "- Favor de capturar un valor positivo para el abono a insertar. -";
				}
					
				if (result.equals("")) { 
					 
					 float montoTotalAbono = getAbonosTotales(remision);
					 if (remision.getMontoInicial() + montoTotalAbono <= remision.getMontoFactura()) { 
						 remision.setPorPagar(remision.getMontoFactura() - remision.getMontoInicial() - montoTotalAbono);					 
					 } else { 
						 result += "- El monto inicial y sus abonos rebasan el monto de la factura, favor de verificar. -";
					 }
				}
			} else { 
				remision.setPorPagar(remision.getMontoFactura() - remision.getMontoInicial());
			}
			
		} else if (remision.getEstatusPagoCredito().equals("P")) { 
			remision.setMontoInicial(remision.getMontoFactura());
			remision.setPorPagar(0);
		}
		
		return result;
	}

	public int agregarSalidaInventarioSistema(HttpServletRequest request, 
			HttpServletResponse response, CatalogMGR catalogo, InventarioMGR invMGR) throws IOException {
				
		if (!sessionMGR.validarSesion(request, response)) { 
			response.sendRedirect(request.getContextPath() + LOGIN_INDEX);
    	}
    	
		int operacionExitosa = -1;		
		
		Remision remision = (Remision) getObjetoVentaSesion(request);
		
		//este es el metodo interesante
		actualizarRemisionInventario(remision, catalogo, invMGR);
				
		remision.setEmpleadoId(Utilerias.strToInt(getVentaEmpleadoSesion(request).toString()));
		operacionExitosa = catalogo.dmlOperations(3, remision);
		
		Logs.debug(RemisionesMGR.class, "Esperemos lo logre en RemisionesMGR: " + remision.getRemisionId());
		
		//si todo fue exitoso
		if (operacionExitosa > 0) { 
			//actualizar si hubo algunos cortes de compra
			List<CompraInventarioBean> listaComprasCortadas = getCortesCompraDeRemision(operacionExitosa, catalogo);
			
			//este metodo es el complementario del corte, para actualizar las compras si se movieron algunos
			//inventarios a algunas remisiones, y se queden actualizadas correctamente las compras
			if (listaComprasCortadas.size() > 0) { 
				Logs.debug(RemisionesMGR.class, "Entro al ciclo referente a que se tienen que actualizar las compras - mauro");
				invMGR.procesarCortesActualizarInventarioParaCompra(listaComprasCortadas, catalogo);
			}
			limpiarCompraSesion(request);
		}
		
		return operacionExitosa;
	}
	
	
	
	@SuppressWarnings("unchecked")
	private List<CompraInventarioBean> getCortesCompraDeRemision(int idRemision, CatalogMGR catalogoMGR) {
		
		List<Object[]> lista = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"   select inv.inventarioId, inv.controlComprasId " + 
			    "     from Remision remision " +
			    "     join remision.listaInventario inv " +
			    "    where remision.remisionId = ? " +
			    "      and remision.activo = 'Y' " + 
			    "      and inv.controlComprasId is not null " + 
			    "      and inv.controlComprasId <> 0 ",			    
		        new Object[]{ idRemision });
		
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
	
	public int actualizarVentaInventario(Remision remision, CatalogMGR catalogo) {
		
		//Remision temporal = (Remision) catalogo.getObjectData(remision);
		
		//actualizar los campos del usuario
		//temporal.setEstatusPagoCredito(remision.getEstatusPagoCredito());
		//temporal.setMontoInicial(remision.getMontoInicial());
		//temporal.setMontoFactura(remision.getMontoFactura());
		//temporal.setPorPagar(remision.getPorPagar());
		
		float montoTotalAbono = getAbonosTotales(remision);
		remision.setPorPagar(remision.getMontoFactura() - remision.getMontoInicial() - montoTotalAbono);
			 
		//todo lo demas se queda como esta en la bd.		
		return catalogo.dmlOperations(3, remision);				
	}

	public void actualizarRemisionInventario(Remision remision, CatalogMGR catalogo, InventarioMGR invMGR) { 
		
		Set<InventarioActual> inventarioTemporal = remision.getListaInventario();
		Set<InventarioActual> newInventory = new HashSet<InventarioActual>();
		
		Iterator<InventarioActual> it = inventarioTemporal.iterator();
		InventarioActual elementoActual = null;
		InventarioActual elementoBD = null;
		
		while (it.hasNext()) { 
			elementoActual = it.next();
						
			int cantidadInicial = elementoActual.getCantidad();
			float precioActualizar = elementoActual.getPrecioProducto();
			
			//Producto productoTemporal = elementoActual.getProducto();
			
			//things to maintain
			List<CompraInventarioBean> inventarioComprasBD = (ArrayList<CompraInventarioBean>) 
				invMGR.getCantidadPrecioFechaEntradaXCadaInventario(elementoActual.getProducto(), catalogo, 0);
			
			for (int i = 0; i <= inventarioComprasBD.size() && cantidadInicial > 0; i++) {
				CompraInventarioBean bean = inventarioComprasBD.get(i);
				elementoBD = new InventarioActual();
				elementoBD.setInventarioId(bean.getInventarioId());
				elementoBD = (InventarioActual) catalogo.getObjectData(elementoBD);
				
				//caso de 3 sopas				
				if (cantidadInicial < elementoBD.getCantidad()) { //cantidad requerida es suficiente con este elemento de inventario
					
					//descuento al de bd la cantidad que voy a tomar
					elementoBD.setCantidad(elementoBD.getCantidad() - cantidadInicial);
					//para el caso del cero, no se pierdan los precios anteriores
					//elementoBD.setPrecioProducto(precioActualizar);
					
					//actualizo a bd el inventario de bd
					catalogo.dmlOperations(1, elementoBD);
										
					//se creara un nuevo inventario
					//elementoActual.setInventarioId(-1);
					//member to be inserted
					elementoActual = new InventarioActual();					
					elementoActual.setCantidad(cantidadInicial);
					
					//member to recover from database element
					elementoActual.setCostoProducto(elementoBD.getCostoProducto());
					elementoActual.setFechaEntrada(elementoBD.getFechaEntrada());
					elementoActual.setProducto(elementoBD.getProducto());
					
					//para saber que compra actualizar posteriormente
					//tomando como referencia el id de la remision
					elementoActual.setControlComprasId(bean.getComprasId());
															
					cantidadInicial = 0;
					
					elementoActual.setPrecioProducto(precioActualizar);
					elementoActual.setDefectuoso(NO_VALUE);
					if (precioActualizar == 0) { 
						elementoActual.setDefectuoso(YES_VALUE);
					}
					elementoActual.setFechaSalida(Utilerias.getDateToday(Utilerias.FORMAT_DATE));
					elementoActual.setEstatus("S");
					
					//se agrega un nuevo inventario
					newInventory.add(elementoActual);
					
				} else if (cantidadInicial == elementoBD.getCantidad()) { //cantidades iguales 
					
					//se actualizara el inventario con salida
					elementoActual = elementoBD;
					
					//member to be update, not necesary but to remember
					//elementoActual.setCantidad(cantidadInicial);
					
					cantidadInicial = 0;
					
					elementoActual.setPrecioProducto(precioActualizar);
					elementoActual.setDefectuoso(NO_VALUE);
					if (precioActualizar == 0) { 
						elementoActual.setDefectuoso(YES_VALUE);
					}
					elementoActual.setFechaSalida(Utilerias.getDateToday(Utilerias.FORMAT_DATE));
					elementoActual.setEstatus("S");
					
					//se actualiza el inventario y se relaciona a la remision
					//en este caso se sigue manteniendo la misma compra con su inventario
					newInventory.add(elementoActual);
					
					
				} else if (cantidadInicial > elementoBD.getCantidad()) { //cantidad requerida es mayor a este elemento de bd de inventario
					
					//se actualizara el inventario con salida
					elementoActual = elementoBD;
										
					cantidadInicial = cantidadInicial - elementoBD.getCantidad();

					elementoActual.setPrecioProducto(precioActualizar);
					elementoActual.setDefectuoso(NO_VALUE);
					if (precioActualizar == 0) { 
						elementoActual.setDefectuoso(YES_VALUE);
					}
					elementoActual.setFechaSalida(Utilerias.getDateToday(Utilerias.FORMAT_DATE));
					elementoActual.setEstatus("S");
					
					//se actualiza el inventario y relaciona a remision
					newInventory.add(elementoActual);
					
				} else { 
					Logs.debug(RemisionesMGR.class, "Problema fuerte en remisiones, favor de validar mauro");
				}
			}
		}
		
		remision.setListaInventario(newInventory);
	}
	
	public Object getRemisionActualSesion(HttpServletRequest request) {
		return sessionMGR.getAttributeSession(request, REMISION_ACTUAL_SESION);
	}
	
	public void eliminarUnProductoDeRemisionActualSesion(HttpServletRequest request, int indice) {
		List<InventarioActual> lista = checarListaSesion(request);
		lista.remove(indice);
		if (lista.size() == 0) { 
			limpiarCompraSesion(request);
		} else {
			sessionMGR.setAttributeSession(request, INVENTARIO_ACTUAL_SESION, lista);
		}
	}
	
	
	public Object getVentaClienteSesion(HttpServletRequest request) { 
		return sessionMGR.getAttributeSession(request, COMPRA_CLIENTE);
	}
	
	public Object getVentaEmpleadoSesion(HttpServletRequest request) {
		return sessionMGR.getAttributeSession(request, COMPRA_EMPLEADO);
	}
	
	public void crearIdVentaSesion(HttpServletRequest request) {		
		sessionMGR.setAttributeSession(request, COMPRA_CLIENTE, request.getParameter(CLIENTE_ID_INVENTARIO));
		sessionMGR.setAttributeSession(request, COMPRA_EMPLEADO, request.getParameter(EMPLEADO_ID_INVENTARIO));
	}
	
	public void crearObjectoVentaSesion(HttpServletRequest request, Remision remision) {		
		sessionMGR.setAttributeSession(request, OBJETO_VENTA, remision);
	}
		
	public Object getObjetoVentaSesion(HttpServletRequest request) { 
		return sessionMGR.getAttributeSession(request, OBJETO_VENTA);
	}
	
	public void limpiarCompraSesion(HttpServletRequest request) {		
		sessionMGR.setAttributeSession(request, COMPRA_CLIENTE, null);
		sessionMGR.setAttributeSession(request, COMPRA_EMPLEADO, null);
		sessionMGR.setAttributeSession(request, REMISION_ACTUAL_SESION, null);
		sessionMGR.setAttributeSession(request, OBJETO_VENTA, null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Remision> getCuentasPorCobrarRangoFechas(String start, String end, Cliente cli, CatalogMGR catalogoMGR) { 
		
		List<Object[]> lista = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				   " select r.remisionId, r.fechaRemision, r.cliente, " +
                   "        r.estatusPagoCredito, r.montoFactura, r.porPagar, r.montoInicial " + 
		           "   from Remision as r " + 
	               "  where r.fechaRemision >= ? " + 
	               "    and r.fechaRemision <= ? " + 
	               "    and r.cliente.clienteId = ? ", 
	               new Object[]{ Utilerias.transformStringToDate(Utilerias.startDate(start), Utilerias.FORMAT_DATE_WITH_HOUR), 
					             Utilerias.transformStringToDate(Utilerias.endDate(end), Utilerias.FORMAT_DATE_WITH_HOUR), 
					             cli.getClienteId() });
		
		List<Remision> listaCompras = new ArrayList<Remision>();
		
		//transpasar a la otra lista de inventarios
		for (int i=0; i < lista.size(); i++) {
			
			Object[] row = lista.get(i);
			
			Remision remision = new Remision();
			
			remision.setRemisionId(Utilerias.strToInt(row[0].toString()));
			remision.setFechaRemision((Date) row[1]);
			remision.setCliente((Cliente) row[2]);
			remision.setEstatusPagoCredito(row[3].toString());
			remision.setMontoFactura(Utilerias.strToFloat(row[4].toString()));
			remision.setPorPagar(Utilerias.strToFloat(row[5].toString()));
			remision.setMontoInicial(Utilerias.strToFloat(row[6].toString()));
			
			listaCompras.add(remision);
		}
		
		return listaCompras;
	}
	
	/**
	 * Referencias: 
     * - http://www.sergiy.ca/how-to-write-many-to-many-search-queries-in-mysql-and-hibernate/
     */	
	@SuppressWarnings("unchecked")
	public List<InventarioActual> getInventarioXOrdenVenta(Remision remision, CatalogMGR catalogoMGR) {
		
		List<Object[]> lista = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"   select inv.inventarioId, inv.producto, inv.fechaEntrada, inv.fechaSalida, " + 
				"          inv.costoProducto, inv.precioProducto, inv.cantidad, inv.estatus, " + 
				"          inv.controlComprasId, inv.iva " + 
			    "     from Remision r " +
			    "     join r.listaInventario inv " +
			    "    where r.remisionId = ? " +
			    "      and r.activo = 'Y' " + 
		        " order by inv.producto.nombreProducto desc ", 
		        new Object[]{ remision.getRemisionId() });
			
		List<InventarioActual> listaInv = new ArrayList<InventarioActual>();
		
		//transpasar a la otra lista de inventarios
		for (int i=0; i < lista.size(); i++) {
			
			Object[] row = lista.get(i);
			
			InventarioActual elemento = new InventarioActual();
			
			elemento.setInventarioId((Integer) row[0]);
			elemento.setProducto((Producto) row[1]);
			elemento.setFechaEntrada((Date) row[2]);
			elemento.setFechaSalida((Date) row[3]);
			elemento.setCostoProducto((Float) row[4]);
			elemento.setPrecioProducto((Float) row[5]);
			elemento.setCantidad((Integer) row[6]);
			elemento.setEstatus((String) row[7]);
			elemento.setControlComprasId((Integer) row[8]);
			elemento.setIva((Float) row[9]);
			
			listaInv.add(elemento);
		}
		return listaInv;			
	}
	
	/**
	 * Referencias: 
     * - http://www.sergiy.ca/how-to-write-many-to-many-search-queries-in-mysql-and-hibernate/
     */	
	@SuppressWarnings("unchecked")
	public List<Remision> getVentasEntreFechas(String fechaInicial, String fechaFinal, CatalogMGR catalogoMGR) {
		
		List<Object[]> lista = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"   select r.remisionId, 1 " +
			    "     from Remision r " +
			    "    where r.fechaRemision >= ? " +
			    "      and r.fechaRemision <= ? " + 
			    " order by r.remisionId desc, r.fechaRemision desc ", 
		        new Object[]{ Utilerias.transformStringToDate(Utilerias.startDate(fechaInicial), Utilerias.FORMAT_DATE_WITH_HOUR), 
						      Utilerias.transformStringToDate(Utilerias.endDate(fechaFinal), Utilerias.FORMAT_DATE_WITH_HOUR)});
			
		List<Remision> listaVentas = new ArrayList<Remision>();
		
		//transpasar a la otra lista de inventarios
		for (int i=0; i < lista.size(); i++) {
			Object[] row = lista.get(i);
			
			Remision elemento = new Remision();
			elemento.setRemisionId(Utilerias.strToInt(row[0].toString()));
			elemento = (Remision) catalogoMGR.getObjectData(elemento);
						
			listaVentas.add(elemento);
		}
		return listaVentas;				
	}
	
	@SuppressWarnings("unchecked")
	@Deprecated
	public static int procesarDevolucion(HttpServletRequest request, HttpServletResponse response, CatalogMGR catalogMGR) {
		
		Remision prod = new Remision();
		prod.setRemisionId(Utilerias.strToInt(request.getParameter("remisionId")));
		prod = (Remision) catalogMGR.getObjectData(prod);
		
		List<InventarioActual> listInventario = 
			(ArrayList<InventarioActual>) Utilerias.convertSetToList(prod.getListaInventario(), new InventarioActual());
				
		List<InventarioActual> listInventarioActualizarEntrada = new ArrayList<InventarioActual>();
			
		for (int i = 0; i < listInventario.size(); i++) { 
			InventarioActual ele = (InventarioActual) listInventario.get(i);
			if (!Utilerias.isNullOrUndefined(request.getParameter("devCheck" + ele.getInventarioId())) 
				&& request.getParameter("devCheck" + ele.getInventarioId()).equals("on")) {
				listInventarioActualizarEntrada.add(ele);
				listInventario.remove(ele);
			}			
		}
				
		prod.setListaInventario((Set<InventarioActual>) Utilerias.convertListToSet(listInventario, new InventarioActual()));
		
		//actualizado ya una vez el set de inventarios.
		actualizarMontosTotalesRemision(prod);
				
		for (int i=0; i < listInventarioActualizarEntrada.size(); i++) {
			//se accesa a los objetos con su apuntador correspondiente
			InventarioActual ele = listInventarioActualizarEntrada.get(i);
			ele.setFechaSalida(null);
			ele.setControlComprasId(0);			       
			ele.setEstatus("E");						
		}
						
		Devolucion dev = new Devolucion();
		dev.setCliente(prod.getCliente());
		dev.setFechaDevolucion(Utilerias.getDateToday(Utilerias.FORMAT_DATE));
		dev.setListaInventario((Set<InventarioActual>) Utilerias.convertListToSet(listInventarioActualizarEntrada, new InventarioActual()));
		
		int operacionActualizadaRemision = catalogMGR.dmlOperations(2, prod);
		catalogMGR.dmlOperations(2, dev);		
		
		return operacionActualizadaRemision;
	}
	
	@SuppressWarnings("unchecked")
	@Deprecated
	public static void actualizarMontosTotalesRemision(Remision prod) {
		List<InventarioActual> listInv = (ArrayList<InventarioActual>) 
			Utilerias.convertSetToList(prod.getListaInventario(), new InventarioActual());
		
		float montoTotalRemision = 0;
		for (int i=0; i < listInv.size(); i++) { 
			InventarioActual ele = listInv.get(i);
			montoTotalRemision += ele.getPrecioProducto() * ele.getCantidad();
		}
		
		//que reglas aplicar para cuando es devolucion, 
		//y como se deben actualizar los montos de la remision
		prod.setMontoFactura(montoTotalRemision);

		float abonosTotales = getAbonosTotales(prod);
		prod.setPorPagar(prod.getMontoFactura() - prod.getMontoInicial() - abonosTotales);
									
	}
}
