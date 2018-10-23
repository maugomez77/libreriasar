package mx.com.libreria.manager;

import java.util.ArrayList;
import java.util.List;

import mx.com.libreria.beans.EstadoResultados;
import mx.com.libreria.beans.FlujoDeEfectivo;

import mx.com.libreria.factory.ObjectFactory;

import mx.com.libreria.interfases.persistencia.dao.BaseDao;

import mx.com.libreria.model.Gastos;
import mx.com.libreria.model.TipoGastoEnum;

public class ReportesMGR implements Constants {

	SessionMGR sessionMGR;

	BaseDao baseDao;
	
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;		
	}

	public void setSessionMGR(SessionMGR sessionMGR) {
		this.sessionMGR = sessionMGR;		
	}
	
	@SuppressWarnings("unchecked")
	public List<Gastos> getGastosRangoFechas(String fechaInicial, String fechaFinal, CatalogMGR catalogoMGR) {
		
		List<Object[]> listaGenerica = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"   select g.gastosId, 1 " + 
				"     from Gastos as g " + 
		        "    where g.fechaAplicacion >= ? " +
		        "      and g.fechaAplicacion <= ? " + 
		        "      and g.activo in (?, ?)", //ambos		         
		        new Object[]{ Utilerias.transformStringToDate(Utilerias.startDate(fechaInicial), FORMAT_DATE_WITH_HOUR),
							  Utilerias.transformStringToDate(Utilerias.endDate(fechaFinal), FORMAT_DATE_WITH_HOUR), 
							  YES_VALUE, 
							  NO_VALUE
							});
		List<Gastos> listGastos = new ArrayList<Gastos>();
		
		for (int i=0; i < listaGenerica.size(); i++) { 
			Object[] elemento = listaGenerica.get(i);
			
			Gastos g = new Gastos();
			g.setGastosId(Utilerias.strToInt(elemento[0].toString()));
			g = (Gastos) catalogoMGR.getObjectData(g);
			listGastos.add(g);			
		}
		
		return listGastos;
	}
	
	@SuppressWarnings("unchecked")
	public FlujoDeEfectivo getFlujoEfectivo(String fechaParametroInicial, String fechaParametroFinal, 
			CatalogMGR catalogoMGR, InventarioHistoricoMGR invHisMGR) {
		
		FlujoDeEfectivo flujoEfectivo = new FlujoDeEfectivo();
		
		//ventas normales
		List<Object[]> listaGenerica = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"    select sum(remision.montoInicial) as montoInicial, sum(remision.montoFactura) as montoFacturado, " +
				"           sum(remision.porPagar) as montoPorPagar, " +
				"           (select sum(abono.monto) as abonos " +
				"              from Remision remSub " +
				"              left join remSub.listaAbonos abono " +
				"             where remSub.remisionId = remision.remisionId " +
				"               and abono.fechaAbono >= ? " +
				"               and abono.fechaAbono <= ? )" + 
				"      from Remision as remision " +
				"     where remision.fechaRemision >= ? " +
		        "       and remision.fechaRemision <= ? " + 
		        "       and remision.activo = ? ",			  
		        new Object[]{ 
							  Utilerias.transformStringToDate(Utilerias.startDate(fechaParametroInicial), FORMAT_DATE_WITH_HOUR),
							  Utilerias.transformStringToDate(Utilerias.endDate(fechaParametroFinal), FORMAT_DATE_WITH_HOUR),
							  
							  Utilerias.transformStringToDate(Utilerias.startDate(fechaParametroInicial), FORMAT_DATE_WITH_HOUR),
							  Utilerias.transformStringToDate(Utilerias.endDate(fechaParametroFinal), FORMAT_DATE_WITH_HOUR),
							  
							  YES_VALUE
							});
		
		float montoInicial = 0;
		//float montoVentaFacturado = 0;
		//float montoVentaPorPagar = 0;
		//en teoria solo debe de entrar una vez.
		for (int i=0; i < listaGenerica.size();  i++) {
			Object[] values = listaGenerica.get(i);
			if (Utilerias.isNullOrUndefined(values[0])) { 
				values[0] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[1])) { 
				values[1] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[2])) { 
				values[2] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[3])) { 
				values[3] = "0.0";
			}
				
			montoInicial += Utilerias.strToFloat(values[0].toString()) + Utilerias.strToFloat(values[3].toString());
				
			//para el caso de las devoluciones
			if (Utilerias.strToFloat(values[2].toString()) < 0) { 
				montoInicial += Utilerias.strToFloat(values[2].toString());
			}
				
			//montoVentaFacturado += Utilerias.strToFloat(values[1].toString());
			//montoVentaPorPagar += Utilerias.strToFloat(values[2].toString());
		}
			
		flujoEfectivo.setVentasConAbono(montoInicial);
			
		//ventas por las suscripciones
		listaGenerica = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
						"    select sum(sus.montoInicial) as montoInicial, sum(sus.montoFactura) as montoFacturado, " +
						"           sum(sus.porPagar) as montoPorPagar, " +
						"           (select sum(abono.monto) as abonos " +
						"              from Suscripcion susSub " +
						"              left join susSub.listaAbonos abono " +
						"             where susSub.suscripcionId = sus.suscripcionId " +
						"               and abono.fechaAbono >= ? " +
						"               and abono.fechaAbono <= ? )" + 
						"      from Suscripcion as sus " +
				        "     where sus.fechaInicialSuscripcion >= ? " +
				        "       and sus.fechaInicialSuscripcion <= ? " +
				        "       and sus.activo = ? ",			  
				        new Object[]{ 
								Utilerias.transformStringToDate(Utilerias.startDate(fechaParametroInicial), FORMAT_DATE_WITH_HOUR),
								Utilerias.transformStringToDate(Utilerias.endDate(fechaParametroFinal), FORMAT_DATE_WITH_HOUR),
								
								Utilerias.transformStringToDate(Utilerias.startDate(fechaParametroInicial), FORMAT_DATE_WITH_HOUR),
								Utilerias.transformStringToDate(Utilerias.endDate(fechaParametroFinal), FORMAT_DATE_WITH_HOUR),
								
								YES_VALUE
							});
		
		montoInicial = 0;
		//float montoVentaFacturado = 0;
		//float montoVentaPorPagar = 0;
		//en teoria solo debe de entrar una vez.
		for (int i=0; i < listaGenerica.size();  i++) {
			Object[] values = listaGenerica.get(i);
			if (Utilerias.isNullOrUndefined(values[0])) { 
				values[0] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[1])) { 
				values[1] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[2])) { 
				values[2] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[3])) { 
				values[3] = "0.0";
			}
				
			montoInicial += Utilerias.strToFloat(values[0].toString()) + Utilerias.strToFloat(values[3].toString());
				
			//para el caso de las devoluciones
			if (Utilerias.strToFloat(values[2].toString()) < 0) { 
				montoInicial += Utilerias.strToFloat(values[2].toString());
			}
				
			//montoVentaFacturado += Utilerias.strToFloat(values[1].toString());
			//montoVentaPorPagar += Utilerias.strToFloat(values[2].toString());
		}
			
		flujoEfectivo.setVentasConAbono(flujoEfectivo.getVentasConAbono() + montoInicial);
	
		//http://www.thinkplexx.com/learn/article/db/quer/joinwhere
			
		//ventas por las ventas especiales
		listaGenerica = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
						"    select sum(ve.montoInicial) as montoInicial, sum(ve.montoFactura) as montoFacturado, " +
						"           sum(ve.porPagar) as montoPorPagar, " + 
						"           (select sum(abono.monto) as abonos " +
						"              from VentaEspecial as veSub " +
						"              left join veSub.listaAbonos abono " +
						"             where veSub.ventaEspecialId = ve.ventaEspecialId " +
						"               and abono.fechaAbono >= ? " +
						"               and abono.fechaAbono <= ? )" + 
						"      from VentaEspecial as ve " +
						"     where ve.fechaInicialVentaEspecial >= ? " +
				        "       and ve.fechaInicialVentaEspecial <= ? " +
				        "       and ve.activo = ? ",			  
				        new Object[]{ 
										Utilerias.transformStringToDate(Utilerias.startDate(fechaParametroInicial), FORMAT_DATE_WITH_HOUR),
										Utilerias.transformStringToDate(Utilerias.endDate(fechaParametroFinal), FORMAT_DATE_WITH_HOUR),
										
										Utilerias.transformStringToDate(Utilerias.startDate(fechaParametroInicial), FORMAT_DATE_WITH_HOUR),
										Utilerias.transformStringToDate(Utilerias.endDate(fechaParametroFinal), FORMAT_DATE_WITH_HOUR),
										
										YES_VALUE
									});
		
		montoInicial = 0;
		//float montoVentaFacturado = 0;
		//float montoVentaPorPagar = 0;
		//en teoria solo debe de entrar una vez.
		for (int i=0; i < listaGenerica.size();  i++) {
			Object[] values = listaGenerica.get(i);
			if (Utilerias.isNullOrUndefined(values[0])) { 
				values[0] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[1])) { 
				values[1] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[2])) { 
				values[2] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[3])) { 
				values[3] = "0.0";
			}
				
			montoInicial += Utilerias.strToFloat(values[0].toString()) + Utilerias.strToFloat(values[3].toString());
				
			//para el caso de las devoluciones
			if (Utilerias.strToFloat(values[2].toString()) < 0) { 
				montoInicial += Utilerias.strToFloat(values[2].toString());
			}
				
			//montoVentaFacturado += Utilerias.strToFloat(values[1].toString());
			//montoVentaPorPagar += Utilerias.strToFloat(values[2].toString());
		}
			
		flujoEfectivo.setVentasConAbono(flujoEfectivo.getVentasConAbono() + montoInicial);
	
		
		//compras normales de inventario
		listaGenerica = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
					"    select sum(compras.montoInicial) as montoInicial, sum(compras.montoFactura) as montoFacturado, " +
					"           sum(compras.porPagar) as montoPorPagar, " +
					"           (select sum(abono.monto) as abonos " +
					"              from Compras comSub " +
					"              left join comSub.listaAbonos abono " +
					"             where comSub.comprasId = compras.comprasId " +
					"               and abono.fechaAbono >= ? " +
					"               and abono.fechaAbono <= ? )" + 
					"      from Compras as compras " + 
					"     where compras.fechaCompra >= ? " +
					"       and compras.fechaCompra <= ? " + 
					"       and compras.activo = ? ",
					new Object[]{ 
									Utilerias.transformStringToDate(Utilerias.startDate(fechaParametroInicial), FORMAT_DATE_WITH_HOUR),
									Utilerias.transformStringToDate(Utilerias.endDate(fechaParametroFinal), FORMAT_DATE_WITH_HOUR), 
									
									Utilerias.transformStringToDate(Utilerias.startDate(fechaParametroInicial), FORMAT_DATE_WITH_HOUR),
									Utilerias.transformStringToDate(Utilerias.endDate(fechaParametroFinal), FORMAT_DATE_WITH_HOUR), 
									
									YES_VALUE
								});
		
		montoInicial = 0;
		//montoVentaFacturado = 0;
		//montoVentaPorPagar = 0;
			
		//en teoria solo debe de entrar una vez.
		//para compras
		for (int i=0; i < listaGenerica.size();  i++) {
			Object[] values = listaGenerica.get(i);
			if (Utilerias.isNullOrUndefined(values[0])) { 
				values[0] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[1])) { 
				values[1] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[2])) { 
				values[2] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[3])) { 
				values[3] = "0.0";
			}
				
			//version sar
			//montoInicial += Utilerias.strToFloat(values[1].toString());
					
			//version mau
			montoInicial += Utilerias.strToFloat(values[0].toString()) + Utilerias.strToFloat(values[3].toString());					
					
		}
			
		flujoEfectivo.setComprasConAbono(montoInicial);
		
		flujoEfectivo.setGastoAdmon(invHisMGR.getTipoGastosMontoTotal(fechaParametroInicial, fechaParametroFinal, TipoGastoEnum.Administrativos, catalogoMGR));
		flujoEfectivo.setGastoVenta(invHisMGR.getTipoGastosMontoTotal(fechaParametroInicial, fechaParametroFinal, TipoGastoEnum.Venta, catalogoMGR));
		flujoEfectivo.setGastoIndirecto(invHisMGR.getTipoGastosMontoTotal(fechaParametroInicial, fechaParametroFinal, TipoGastoEnum.Indirectos, catalogoMGR));
		flujoEfectivo.setGastoOtros(invHisMGR.getTipoGastosMontoTotal(fechaParametroInicial, fechaParametroFinal, TipoGastoEnum.OtrosGastos, catalogoMGR));
		
		flujoEfectivo.setIngresos(flujoEfectivo.getVentasConAbono());
		
		flujoEfectivo.setEgresos(flujoEfectivo.getComprasConAbono() 
				+ flujoEfectivo.getGastoAdmon() 
				+ flujoEfectivo.getGastoIndirecto() 
				+ flujoEfectivo.getGastoOtros()
				+ flujoEfectivo.getGastoVenta());
		
		return flujoEfectivo;
	}
	
	@SuppressWarnings("unchecked")
	public EstadoResultados getEstadoResultados(String fechaParametroInicial, String fechaParametroFinal, 
			CatalogMGR catalogoMGR, InventarioHistoricoMGR invHisMGR) {
		
		EstadoResultados estadoResultado = new EstadoResultados();
		
		List<Object[]> listaGenerica = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"    select sum(remision.montoInicial) as montoInicial, sum(remision.montoFactura) as montoFacturado, " +
				"           sum(remision.porPagar) as montoPorPagar, " +
				"           (select sum(abono.monto) as abonos " +
				"              from Remision remSub " +
				"              left join remSub.listaAbonos abono " +
				"             where remSub.remisionId = remision.remisionId " +
				"               and abono.fechaAbono >= ? " +
				"               and abono.fechaAbono <= ? )" + 				
				"      from Remision as remision " +
				"     where remision.fechaRemision >= ? " +
		        "       and remision.fechaRemision <= ? " + 
		        "       and remision.activo = ? ",
		        new Object[]{ 
								Utilerias.transformStringToDate(Utilerias.startDate(fechaParametroInicial), FORMAT_DATE_WITH_HOUR),
								Utilerias.transformStringToDate(Utilerias.endDate(fechaParametroFinal), FORMAT_DATE_WITH_HOUR),
								
								Utilerias.transformStringToDate(Utilerias.startDate(fechaParametroInicial), FORMAT_DATE_WITH_HOUR),
								Utilerias.transformStringToDate(Utilerias.endDate(fechaParametroFinal), FORMAT_DATE_WITH_HOUR), 
								
								YES_VALUE
							});
		
		float montoInicial = 0;
		//float montoVentaFacturado = 0;
		//float montoVentaPorPagar = 0;
		//en teoria solo debe de entrar una vez.
		for (int i=0; i < listaGenerica.size();  i++) {
			Object[] values = listaGenerica.get(i);
			if (Utilerias.isNullOrUndefined(values[0])) { 
				values[0] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[1])) { 
				values[1] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[2])) { 
				values[2] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[3])) { 
				values[3] = "0.0";
			}
			
			//montoInicial += Utilerias.strToFloat(values[0].toString()) + Utilerias.strToFloat(values[3].toString());
			montoInicial += Utilerias.strToFloat(values[1].toString());
			
			//para el caso de las devoluciones
			//TODO verificar si aplica, por lo pronto comentarlo
			//if (Utilerias.strToFloat(values[2].toString()) < 0) { 
			//	montoInicial += Utilerias.strToFloat(values[2].toString());
			//}
			
			//montoVentaFacturado += Utilerias.strToFloat(values[1].toString());
			//montoVentaPorPagar += Utilerias.strToFloat(values[2].toString());
		}
		
		estadoResultado.setVentas(montoInicial);
		
		//se añaden los montos de venta				
		//lista.add((Object) montoVentaAbonado);
		//lista.add((Object) montoVentaFacturado);
		//lista.add((Object) montoVentaPorPagar);
		
		//Montos sobre las suscripciones
		listaGenerica = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"    select sum(sus.montoInicial) as montoInicial, sum(sus.montoFactura) as montoFacturado, " +
				"           sum(sus.porPagar) as montoPorPagar, " +
				"           (select sum(abono.monto) as abonos " +
				"              from Suscripcion susSub " +
				"              left join susSub.listaAbonos abono " +
				"             where susSub.suscripcionId = sus.suscripcionId " +
				"               and abono.fechaAbono >= ? " +
				"               and abono.fechaAbono <= ? )" + 
				"      from Suscripcion as sus " +
				"     where sus.fechaInicialSuscripcion >= ? " +
		        "       and sus.fechaInicialSuscripcion <= ? " +
		        "       and sus.activo = ? ",			  
		        new Object[]{ 
								Utilerias.transformStringToDate(Utilerias.startDate(fechaParametroInicial), FORMAT_DATE_WITH_HOUR),
								Utilerias.transformStringToDate(Utilerias.endDate(fechaParametroFinal), FORMAT_DATE_WITH_HOUR),
								
								Utilerias.transformStringToDate(Utilerias.startDate(fechaParametroInicial), FORMAT_DATE_WITH_HOUR),
								Utilerias.transformStringToDate(Utilerias.endDate(fechaParametroFinal), FORMAT_DATE_WITH_HOUR),
								
								YES_VALUE
							});
							
		montoInicial = 0;
		//float montoVentaFacturado = 0;
		//float montoVentaPorPagar = 0;
		//en teoria solo debe de entrar una vez.
		for (int i=0; i < listaGenerica.size();  i++) {
			Object[] values = listaGenerica.get(i);
			if (Utilerias.isNullOrUndefined(values[0])) { 
				values[0] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[1])) { 
				values[1] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[2])) {
				values[2] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[3])) { 
				values[3] = "0.0";
			}
			montoInicial += Utilerias.strToFloat(values[1].toString());
		}
		
		//las ventas que ya se tienen previamente de ventas mas el monto de las suscripciones
		estadoResultado.setVentas(estadoResultado.getVentas() + montoInicial);
		
		
		//Montos sobre las ventas especiales
		listaGenerica = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
				"    select sum(ve.montoInicial) as montoInicial, sum(ve.montoFactura) as montoFacturado, " +
				"           sum(ve.porPagar) as montoPorPagar, " +
				"           (select sum(abono.monto) as abonos " +
				"              from VentaEspecial venSub " +
				"              left join venSub.listaAbonos abono " +
				"             where venSub.ventaEspecialId = ve.ventaEspecialId " +
				"               and abono.fechaAbono >= ? " +
				"               and abono.fechaAbono <= ? )" + 
				"      from VentaEspecial as ve " +
				"     where ve.fechaInicialVentaEspecial >= ? " +
		        "       and ve.fechaInicialVentaEspecial <= ? " + 
		        "       and ve.activo = ? ",			  
		        new Object[]{ 
								Utilerias.transformStringToDate(Utilerias.startDate(fechaParametroInicial), FORMAT_DATE_WITH_HOUR),
								Utilerias.transformStringToDate(Utilerias.endDate(fechaParametroFinal), FORMAT_DATE_WITH_HOUR),
								
								Utilerias.transformStringToDate(Utilerias.startDate(fechaParametroInicial), FORMAT_DATE_WITH_HOUR),
								Utilerias.transformStringToDate(Utilerias.endDate(fechaParametroFinal), FORMAT_DATE_WITH_HOUR),
								
								YES_VALUE
							});
							
		montoInicial = 0;
		//float montoVentaFacturado = 0;
		//float montoVentaPorPagar = 0;
		//en teoria solo debe de entrar una vez.
		for (int i=0; i < listaGenerica.size();  i++) {
			Object[] values = listaGenerica.get(i);
			if (Utilerias.isNullOrUndefined(values[0])) { 
				values[0] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[1])) { 
				values[1] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[2])) {
				values[2] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[3])) { 
				values[3] = "0.0";
			}
			montoInicial += Utilerias.strToFloat(values[1].toString());
		}
		
		//las ventas que ya se tienen previamente mas el monto de las ventas especiales
		estadoResultado.setVentas(estadoResultado.getVentas() + montoInicial);
		
		
		//monto sobre compras
		listaGenerica = 
			(ArrayList<Object[]>)
				catalogoMGR.getListParameters(
					"    select sum(compras.montoInicial) as montoInicial, sum(compras.montoFactura) as montoFacturado, " +
					"           sum(compras.porPagar) as montoPorPagar, " +
					"           (select sum(abono.monto) as abonos " +
					"              from Compras comSub " +
					"              left join comSub.listaAbonos abono " +
					"             where comSub.comprasId = compras.comprasId " +
					"               and abono.fechaAbono >= ? " +
					"               and abono.fechaAbono <= ? )" + 
					"      from Compras as compras " + 
					"     where compras.fechaCompra >= ? " +
					"       and compras.fechaCompra <= ? " + 
					"       and compras.activo = ? ",			  
					new Object[]{ 
									Utilerias.transformStringToDate(Utilerias.startDate(fechaParametroInicial), FORMAT_DATE_WITH_HOUR),
									Utilerias.transformStringToDate(Utilerias.endDate(fechaParametroFinal), FORMAT_DATE_WITH_HOUR), 
									
									Utilerias.transformStringToDate(Utilerias.startDate(fechaParametroInicial), FORMAT_DATE_WITH_HOUR),
									Utilerias.transformStringToDate(Utilerias.endDate(fechaParametroFinal), FORMAT_DATE_WITH_HOUR), 
									
									YES_VALUE
								});
		
		montoInicial = 0;
		//montoVentaFacturado = 0;
		//montoVentaPorPagar = 0;
		
		//en teoria solo debe de entrar una vez.
		//para compras
		for (int i=0; i < listaGenerica.size();  i++) {
			Object[] values = listaGenerica.get(i);
			if (Utilerias.isNullOrUndefined(values[0])) { 
				values[0] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[1])) { 
				values[1] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[2])) { 
				values[2] = "0.0";
			}
			if (Utilerias.isNullOrUndefined(values[3])) { 
				values[3] = "0.0";
			}
			
			//version sar
			//montoInicial += Utilerias.strToFloat(values[1].toString());
			
			//version mau
			//montoInicial += Utilerias.strToFloat(values[0].toString()) + Utilerias.strToFloat(values[3].toString());
			montoInicial += Utilerias.strToFloat(values[1].toString());
			
		}
		
		
		//se añaden los montos de compra
		//lista.add((Object) montoVentaAbonado);
		//lista.add((Object) montoVentaFacturado);
		//lista.add((Object) montoVentaPorPagar);
				
		float inventarioInicial = invHisMGR.getInventarioHistoricoDeUnDia(fechaParametroInicial, catalogoMGR);
		float inventarioFinal = invHisMGR.getInventarioHistoricoDeUnDia(fechaParametroFinal, catalogoMGR);
		
		estadoResultado.setInventarioInicial(inventarioInicial);
		estadoResultado.setCompras(montoInicial);
		estadoResultado.setInventarioFinal(inventarioFinal);
		
		estadoResultado.setCostoDeVentas(estadoResultado.getInventarioInicial() + estadoResultado.getCompras() - estadoResultado.getInventarioFinal());
		
		//lista.add(inventarioInicial);
		//lista.add(inventarioFinal);
		
		estadoResultado.setUtilidadBruta(estadoResultado.getVentas() - estadoResultado.getCostoDeVentas());
		
		estadoResultado.setGastosAdmon(invHisMGR.getTipoGastosMontoTotal(fechaParametroInicial, fechaParametroFinal, TipoGastoEnum.Administrativos, catalogoMGR));
		estadoResultado.setGastosVenta(invHisMGR.getTipoGastosMontoTotal(fechaParametroInicial, fechaParametroFinal, TipoGastoEnum.Venta, catalogoMGR));
		estadoResultado.setGastosIndirectos(invHisMGR.getTipoGastosMontoTotal(fechaParametroInicial, fechaParametroFinal, TipoGastoEnum.Indirectos, catalogoMGR));
		
		estadoResultado.setGastosOperacion(estadoResultado.getGastosAdmon() + estadoResultado.getGastosVenta() + estadoResultado.getGastosIndirectos());
		
		estadoResultado.setUtilidadOperacion(estadoResultado.getUtilidadBruta() - estadoResultado.getGastosOperacion());
		
		estadoResultado.setOtrosGastos(invHisMGR.getTipoGastosMontoTotal(fechaParametroInicial, fechaParametroFinal, TipoGastoEnum.OtrosGastos, catalogoMGR));
		
		estadoResultado.setUtilidadNeta(estadoResultado.getUtilidadOperacion() - estadoResultado.getOtrosGastos());
		
		return estadoResultado;
	}
	
	public static void main(String[] args) {

		CatalogMGR catalogo = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");
		ReportesMGR reportes = (ReportesMGR) ObjectFactory.getBean("reportesMGR");
		InventarioHistoricoMGR inventarioHistoricoMGR = (InventarioHistoricoMGR) ObjectFactory.getBean("inventarioHistoricoMGR");
				
		reportes.getEstadoResultados("2011-9-11", "2011-9-11", catalogo, inventarioHistoricoMGR);
		
	}
}
