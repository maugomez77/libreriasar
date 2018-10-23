package mx.com.libreria.manager;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.com.libreria.beans.CompraInventarioBean;
import mx.com.libreria.beans.ReporteProveedorInventario;

import mx.com.libreria.factory.ObjectFactory;

import mx.com.libreria.interfases.persistencia.dao.BaseDao;

import mx.com.libreria.model.Abono;
import mx.com.libreria.model.Compras;
import mx.com.libreria.model.InventarioActual;
import mx.com.libreria.model.Producto;
import mx.com.libreria.model.Proveedor;

public class InventarioMGR implements Constants {

	SessionMGR sessionMGR;
	
	BaseDao baseDao;
	
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;		
	}
	 
	public void setSessionMGR(SessionMGR sessionMGR) {
		this.sessionMGR = sessionMGR;		
	}
	
	public float getAbonosTotales(Compras compras) { 		
		Iterator<Abono> it = compras.getListaAbonos().iterator();
		float totalAbonos = 0;
		while (it.hasNext()) { 
			Abono abono = it.next();
			totalAbonos += abono.getMonto();
		}
		return totalAbonos;
	}
	
	public String validaMontoAbonadoConFacturado(Compras compras, boolean validaAbono, String abono) {
		String result = "";
		if (compras.getEstatusPagoCredito() == null) { 
			result = "- Favor de capturar la forma de pago, ya sea a credito o contado. -";
			return result;
		}
		
		Logs.debug(InventarioMGR.class, "objeto compras: " 
				+ compras.getMontoInicial() + " " 
				+ compras.getEstatusPagoCredito() + " " 
				+ compras.getMontoFactura());
		
		
		
		if (compras.getMontoFactura() > 0 && compras.getEstatusPagoCredito().equals("C")) { 
			if (compras.getMontoInicial() < 0) {
				result = "- Favor de capturar un valor positivo o cero para el monto abonado -";
			}
			
			if (compras.getMontoInicial() > compras.getMontoFactura()) { 
				result += "- Favor de capturar un valor positivo para el monto inicial que sea menor al valor de la factura -";
			}
			
			if (validaAbono) { 
				float abonoAInsertar = Utilerias.strToFloat(abono);
				if (abonoAInsertar <= 0) { 
					result += "- Favor de capturar un valor positivo para el abono a insertar. -";
				}
					
				if (result.equals("")) { 
					 
					 float montoTotalAbono = getAbonosTotales(compras);
					 if (compras.getMontoInicial() + montoTotalAbono <= compras.getMontoFactura()) { 
						 compras.setPorPagar(compras.getMontoFactura() - compras.getMontoInicial() - montoTotalAbono);					 
					 } else { 
						 result += "- El monto inicial y sus abonos rebasan el monto de la factura, favor de verificar. -";
					 }
				}
			} else { 
				compras.setPorPagar(compras.getMontoFactura() - compras.getMontoInicial());
			}
			
		} else if (compras.getEstatusPagoCredito().equals("P")) { 
			compras.setMontoInicial(compras.getMontoFactura());
			compras.setPorPagar(0);
		}
		
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	private List<InventarioActual> checarListaSesion(final HttpServletRequest request) { 
		
		//traer la lista actual
		List<InventarioActual> lista = null;
		if (sessionMGR.getAttributeSession(request, INVENTARIO_ACTUAL_SESION) != null) { 
			lista = (List<InventarioActual>) sessionMGR.getAttributeSession(request, INVENTARIO_ACTUAL_SESION);
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
			montoTotalCompra += inventario.getCostoProducto() * inventario.getCantidad(); 
		}		
		return montoTotalCompra;
	}
	
	public void crearIdCompraSesion(HttpServletRequest request) {		
		sessionMGR.setAttributeSession(request, COMPRA_PROVEEDOR, request.getParameter(PROVEEDOR_ID_INVENTARIO));
		sessionMGR.setAttributeSession(request, NUMERO_PROVEEDOR, request.getParameter(REQUEST_PROVEEDOR_SESION));
	}
	
	public void crearObjectoCompraSesion(HttpServletRequest request, Compras compras) {		
		sessionMGR.setAttributeSession(request, OBJETO_COMPRA, compras);
	}
		
	public Object getObjetoCompraSesion(HttpServletRequest request) { 
		return sessionMGR.getAttributeSession(request, OBJETO_COMPRA);
	}
	
	public Object getCompraProveedorSesion(HttpServletRequest request) { 
		return sessionMGR.getAttributeSession(request, COMPRA_PROVEEDOR);
	}
	
	public int getNumeroProveedor(HttpServletRequest request) {
		return Utilerias.strToInt((String) sessionMGR.getAttributeSession(request, NUMERO_PROVEEDOR));
	}
	
	public Object getInventarioActualSesion(HttpServletRequest request) {
		return sessionMGR.getAttributeSession(request, INVENTARIO_ACTUAL_SESION);
	}
	
	public void eliminarUnProductoDeInventarioActualSesion(HttpServletRequest request, int indice) {
		List<InventarioActual> lista = checarListaSesion(request);
		lista.remove(indice);
		if (lista.size() == 0) { 
			limpiarCompraSesion(request);
		} else {
			sessionMGR.setAttributeSession(request, INVENTARIO_ACTUAL_SESION, lista);
		}
	}
	
	public void limpiarCompraSesion(HttpServletRequest request) {		
		sessionMGR.setAttributeSession(request, COMPRA_PROVEEDOR, null);
		sessionMGR.setAttributeSession(request, INVENTARIO_ACTUAL_SESION, null);
		sessionMGR.setAttributeSession(request, OBJETO_COMPRA, null);
		sessionMGR.setAttributeSession(request, NUMERO_PROVEEDOR, null);
	}
	
	private void agregarInventarioSesionElemento(final List<InventarioActual> lista, final InventarioActual inv, final HttpServletRequest request) { 
		//agregar nuevo elemento a la lista actual
		//y actualizar sesion
		lista.add(inv);
		sessionMGR.setAttributeSession(request, INVENTARIO_ACTUAL_SESION, lista);
	}
	
	
	public String validarInventarioDuplicadoSesion(HttpServletRequest request, HttpServletResponse  response, InventarioActual inv) 
		throws IOException {
		
		if (!sessionMGR.validarSesion(request, response)) { 
			response.sendRedirect(request.getContextPath() + LOGIN_INDEX);
    	}
    	
		StringBuilder sb = new StringBuilder();		
		List<InventarioActual> lista = checarListaSesion(request);
		for (int i=0; i < lista.size(); i++) { 
			InventarioActual temporal = lista.get(i);
			if (temporal.getProducto().getProductoId() == inv.getProducto().getProductoId()) { 
				sb.append("- Este producto ya se encuentra dado de alta en la sesion -");
			}
		}
		return sb.toString();
		
	}
	
	public void agregarAbonoListaCompra(Compras prod, float abono) {
		
		Set<Abono> listAbono = prod.getListaAbonos();
		
		Abono nuevo = new Abono();
		nuevo.setAplicaCompraOVenta("C");
		nuevo.setFechaAbono(Utilerias.getDateToday(Utilerias.FORMAT_DATE));
		nuevo.setMonto(abono);
		listAbono.add(nuevo);
			
		prod.setListaAbonos(listAbono);
			
	}
	
	public int actualizarCompraInventario(Compras compras, CatalogMGR catalogo) {
		
		//Compras temporal = (Compras) catalogo.getObjectData(compras);
		
		//actualizar los campos del usuario
		//temporal.setEstatusPagoCredito(compras.getEstatusPagoCredito());
		//temporal.setMontoInicial(compras.getMontoInicial());
		//temporal.setMontoFactura(compras.getMontoFactura());
		//temporal.setPorPagar(compras.getPorPagar());
		
		float montoTotalAbono = getAbonosTotales(compras);
		compras.setPorPagar(compras.getMontoFactura() - compras.getMontoInicial() - montoTotalAbono);
			 
		//todo lo demas se queda como esta en la bd.		
		return catalogo.dmlOperations(3, compras);		
	}
	
	public void agregarInventarioSesion(HttpServletRequest request, HttpServletResponse  response, InventarioActual inv) 
		throws IOException {
			
		if (!sessionMGR.validarSesion(request, response)) { 
			response.sendRedirect(request.getContextPath() + LOGIN_INDEX);
		}
	
		List<InventarioActual> lista = checarListaSesion(request);
		
		//agregar la regla del producto defectuoso, siempre se comprarian asumiendo que estos no son defectuosos
		inv.setDefectuoso(Utilerias.NO_VALUE);
		
		agregarInventarioSesionElemento(lista, inv, request);		
					
	}

	public int agregarInventarioSistema(HttpServletRequest request, 
			HttpServletResponse response, CatalogMGR catalogo) throws IOException {
				
		if (!sessionMGR.validarSesion(request, response)) { 
			response.sendRedirect(request.getContextPath() + LOGIN_INDEX);
    	}
    	
		int operacionExitosa = -1;		
		
		Compras compras = (Compras) getObjetoCompraSesion(request);
		
		if (compras.getEstatusPagoCredito().equals(PAGO_INVENTARIO)) { 
			compras.setMontoFactura(0);
			compras.setPorPagar(0);
			compras.setMontoInicial(0);
		}
		
		operacionExitosa = catalogo.dmlOperations(3, compras);		
		
		//si todo fue exitoso
		if (operacionExitosa >= 0) { 
			limpiarCompraSesion(request);
		}
		
		return operacionExitosa;
	}	
		
	/**
	 * Referencias: 
     * - http://www.sergiy.ca/how-to-write-many-to-many-search-queries-in-mysql-and-hibernate/
     */	
	@SuppressWarnings("unchecked")
	public List<InventarioActual> getInventarioXOrdenCompra(Compras compra, CatalogMGR catalogoMGR) {
		
		List<Object[]> lista = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"   select inv.inventarioId, inv.producto, inv.fechaEntrada, inv.fechaSalida, " + 
				"          inv.costoProducto, inv.precioProducto, inv.cantidad, inv.estatus, " + 
				"          inv.controlComprasId, inv.iva " + 
			    "     from Compras compras " +
			    "     join compras.listaInventario inv " +
			    "    where compras.comprasId = ? " +
			    "      and compras.activo = 'Y' " + 
		        " order by inv.producto.nombreProducto desc ", 
		        new Object[]{ compra.getComprasId() });
			
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
	public List<Compras> getComprasEntreFechas(String fechaInicial, String fechaFinal, CatalogMGR catalogoMGR) {
		
		List<Object[]> lista = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"   select compras.comprasId, 1 " +
			    "     from Compras compras " +
			    "    where compras.fechaCompra >= ? " +
			    "      and compras.fechaCompra <= ? " + 
			    " order by compras.comprasId desc, compras.fechaCompra desc ", 
		        new Object[]{ Utilerias.transformStringToDate(Utilerias.startDate(fechaInicial), Utilerias.FORMAT_DATE_WITH_HOUR), 
						      Utilerias.transformStringToDate(Utilerias.endDate(fechaFinal), Utilerias.FORMAT_DATE_WITH_HOUR)});
			
		List<Compras> listaCompras = new ArrayList<Compras>();
		
		//transpasar a la otra lista de inventarios
		for (int i=0; i < lista.size(); i++) {
			Object[] row = lista.get(i);
			
			Compras elemento = new Compras();
			elemento.setComprasId(Utilerias.strToInt(row[0].toString()));
			elemento = (Compras) catalogoMGR.getObjectData(elemento);
			
			listaCompras.add(elemento);
		}
		return listaCompras;				
	}

	@SuppressWarnings("unchecked")
	public List<Compras> getCuentasPorPagarRangoFechas(String start, String end, Proveedor prov, CatalogMGR catalogoMGR) { 
		
		List<Object[]> lista = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				   " select c.comprasId, c.fechaCompra, c.proveedor, " +
                   "        c.estatusPagoCredito, c.montoFactura, c.porPagar " + 
		           "   from Compras as c " + 
	               "  where c.fechaCompra >= ? " + 
	               "    and c.fechaCompra <= ? " + 
	               "    and c.proveedor.proveedorId = ? ", 
	               new Object[]{ Utilerias.transformStringToDate(Utilerias.startDate(start), Utilerias.FORMAT_DATE_WITH_HOUR), 
					             Utilerias.transformStringToDate(Utilerias.endDate(end), Utilerias.FORMAT_DATE_WITH_HOUR), 
					             prov.getProveedorId() });
		
		List<Compras> listaCompras = new ArrayList<Compras>();
		
		//transpasar a la otra lista de inventarios
		for (int i=0; i < lista.size(); i++) {
			
			Object[] row = lista.get(i);
			
			Compras elemento = new Compras();
			
			elemento.setComprasId(Utilerias.strToInt(row[0].toString()));
			elemento.setFechaCompra((Date) row[1]);
			elemento.setProveedor((Proveedor) row[2]);
			elemento.setEstatusPagoCredito(row[3].toString());
			elemento.setMontoFactura(Utilerias.strToFloat(row[4].toString()));
			elemento.setPorPagar(Utilerias.strToFloat(row[5].toString()));
						
			listaCompras.add(elemento);
		}
		
		return listaCompras;
	}
	
	/**
	 * Referencias: 
     * - http://www.sergiy.ca/how-to-write-many-to-many-search-queries-in-mysql-and-hibernate/
     */	
	@SuppressWarnings("unchecked")
	public List<ReporteProveedorInventario> getInventarioAlDiaProveedor(CatalogMGR catalogoMGR) {
		
		List<Object[]> lista = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"   select sum(inv.cantidad), compras.proveedor, inv.producto " + 
			    "     from Compras as compras " +
			    "     join compras.listaInventario inv " +
			    "    where inv.estatus = ? " +
			    "      and compras.activo = 'Y' " + 
			    "    group by compras.proveedor, inv.producto " + 
		        "    order by compras.proveedor asc, inv.producto desc ", 
		        new Object[]{ "E" });
			
		List<ReporteProveedorInventario> listaInv = new ArrayList<ReporteProveedorInventario>();
		
		//transpasar a la otra lista de inventarios
		for (int i=0; i < lista.size(); i++) {
			
			Object[] row = lista.get(i);
			
			ReporteProveedorInventario elemento = new ReporteProveedorInventario();
			
			elemento.setCantidad(Utilerias.strToInt(row[0].toString()));
			elemento.setProveedor((Proveedor) row[1]);
			elemento.setProducto((Producto) row[2]);
			
			listaInv.add(elemento);
		}
		return listaInv;
	}
	
	/**
	 * Referencias: 
     * - http://www.sergiy.ca/how-to-write-many-to-many-search-queries-in-mysql-and-hibernate/
     */	
	@SuppressWarnings("unchecked")
	public List<InventarioActual> getInventarioAlDia(CatalogMGR catalogoMGR) {
		
		List<Object[]> lista = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"   select sum(inv.cantidad), inv.producto " + 
				"     from Compras compras " +
			    "     join compras.listaInventario inv " + 
				"    where compras.activo = 'Y' " + 
			    "      and inv.estatus = ? " +
			    " group by inv.producto " + 
		        " order by inv.producto desc ", 
		        new Object[]{ "E" });
			
		List<InventarioActual> listaInv = new ArrayList<InventarioActual>();
		
		//transpasar a la otra lista de inventarios
		for (int i=0; i < lista.size(); i++) {
			
			Object[] row = lista.get(i);
			
			InventarioActual elemento = new InventarioActual();
			
			//elemento.setInventarioId((Integer) row[0]);
			//elemento.setFechaSalida((Date) row[3]);
			//elemento.setCostoProducto((Float) row[4]);
			//elemento.setPrecioProducto((Float) row[5]);
			
			elemento.setCantidad(Utilerias.strToInt(row[0].toString()));
			//elemento.setPrecioProducto(Utilerias.strToFloat(row[1].toString()));
			elemento.setProducto((Producto) row[1]);
			//elemento.setFechaEntrada((Date) row[2]);
			
			//elemento.setEstatus((String) row[7]);
			//elemento.setControlComprasId((Integer) row[8]);
			
			listaInv.add(elemento);
		}
		return listaInv;
				
	}
	
	
	/**
	 * Referencias: 
     * - http://www.sergiy.ca/how-to-write-many-to-many-search-queries-in-mysql-and-hibernate/
     */	
	@SuppressWarnings("unchecked")
	public List<CompraInventarioBean> getCantidadPrecioFechaEntradaXCadaInventario(Producto prod, CatalogMGR catalogoMGR, int orderBy) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("   select compras.comprasId, inv.inventarioId " + 
				   "     from Compras compras " +
				   "     join compras.listaInventario inv " +
				   "    where inv.producto.productoId = ? " +
			       "      and inv.estatus = 'E' " + 
				   "      and compras.activo = 'Y' ");
		
		if (orderBy == 0) { 
		    sql.append(" order by inv.cantidad desc ");
		} else if (orderBy == 1) { 
			sql.append(" order by inv.fechaEntrada asc ");
		}
		
		List<Object[]> lista = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(sql.toString(), new Object[]{ prod.getProductoId() });
		
		List<CompraInventarioBean> listCompra = new ArrayList<CompraInventarioBean>();
		for (int i=0; i < lista.size(); i++) { 
			Object[] values = lista.get(i);
			CompraInventarioBean bean = new CompraInventarioBean();
			bean.setComprasId(Utilerias.strToInt(values[0].toString()));
			bean.setInventarioId(Utilerias.strToInt(values[1].toString()));
			listCompra.add(bean);
		}
		return listCompra;
	}
	
	public void procesarCortesActualizarInventarioParaCompra(List<CompraInventarioBean> listaComprasCortadas, CatalogMGR catalogo) { 		
		
		for (int i=0; i < listaComprasCortadas.size(); i++) { 
			
			CompraInventarioBean bean = listaComprasCortadas.get(i);
			
			//inventarioId elementos[0];
			//controlComprasId elementos[1];
			
			//seteo el inventario del query a actualizar en compras
			InventarioActual invTemp = new InventarioActual();
			Logs.debug(RemisionesMGR.class, "Elemento inventario id mauro: " + bean.getInventarioId());
			invTemp.setInventarioId(bean.getInventarioId());
			invTemp = (InventarioActual) catalogo.getObjectData(invTemp);
			Logs.debug(RemisionesMGR.class, "pasa round 1 mauro: " + bean.getInventarioId());
			
			//seteo la compra para traerme el objeto
			Compras compras = new Compras();
			Logs.debug(RemisionesMGR.class, "elemento compra id mauro: " + bean.getComprasId());
			compras.setComprasId(bean.getComprasId());
			compras = (Compras) catalogo.getObjectData(compras);
			Logs.debug(RemisionesMGR.class, "pasa round 2 mauro: " + bean.getComprasId());
			
			//asigno el nuevo inventario en la compra para actualizar el inventario en base de datos
			Set<InventarioActual> listaInvCompra = compras.getListaInventario();
			listaInvCompra.add(invTemp);
			Logs.debug(RemisionesMGR.class, "acaba de asignar el set mauro: " + bean.getInventarioId() + " " + bean.getComprasId());
			
			/*
			Iterator it = listaInvCompra.iterator();
			while (it.hasNext()) { 
				InventarioActual elemento = (InventarioActual) it.next();
				Logging.debug(RemisionesMGR.class, "inventario bd mauro " + elemento.getInventarioId() + "    " + elemento.getControlComprasId());
			}*/
			
			//realizo operacion en base de datos
			catalogo.dmlOperations(3, compras);
			
			Logs.debug(RemisionesMGR.class, "finalizado mauro actualizacion de una compra");
			
		}
	}

	public static void main(String[] args) { 
		InventarioMGR mgr = (InventarioMGR) ObjectFactory.getBean("productMGR");
		mgr.getClass();
	}
}
