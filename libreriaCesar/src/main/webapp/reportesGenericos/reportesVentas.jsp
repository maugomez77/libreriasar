<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR" %>
<%@ page import="mx.com.libreria.manager.InventarioMGR" %>
<%@ page import="mx.com.libreria.manager.ReportesMGR" %>
<%@ page import="mx.com.libreria.manager.RemisionesMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.Cliente" %>
<%@ page import="mx.com.libreria.model.Remision" %>
<%@ page import="mx.com.libreria.model.InventarioActual" %>


				<% if(request.getParameter("sessionid") != null 
					  && session.getId().equals(request.getParameter("sessionid"))
					  && session.getAttribute("uname") != null){  %>
      				
      					<% CatalogMGR catalogMGR = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");
      					   ReportesMGR reportesMGR = (ReportesMGR) ObjectFactory.getBean("reportesMGR");
      					   RemisionesMGR remisionesMGR = (RemisionesMGR) ObjectFactory.getBean("remisionesMGR");
      					   
      					   String start = request.getParameter("start");
      					   String end = request.getParameter("end");
      					   
      					   if (Utilerias.isNullOrUndefined(start)) { 
      						   start = "";
      					   }
      					   if (Utilerias.isNullOrUndefined(end)) { 
      						   end = "";
      					   }
      					   
      					   if (!Utilerias.isNullOrUndefined(start) && !Utilerias.isNullOrUndefined(end)) {

								List<Remision> listVentas = remisionesMGR.getVentasEntreFechas(start, end, catalogMGR); 
								
								if (listVentas.size() > 0) { %>
									
									<p><h3>Reporte de Ventas del Periodo <%= start %> al periodo <%= end %></h3></p>
									<table border="1" class="tablas" title="Ventas">
	    								<tr>
	    									<td>Activo</td>
											<td>Cliente</td>
											<td>Remisi&oacute;n</td>
											<td>Fecha de Venta</td>
											<td>Forma de Pago</td>
											<td>Importe</td>
											<td>Por Pagar</td>
											<td>Abonado</td>
											<td>Detalle Venta</td>										
										</tr>
										
										<% 
										   float montoTotal = 0;
										   for (int i=0; i < listVentas.size(); i++) { 
										   Remision elemento = listVentas.get(i); %>
										   <tr>
										   		<td><%= Utilerias.getMessageValue(Utilerias.YES_NO_KEY, elemento.getActivo()) %></td>
										   		<td><%= elemento.getCliente().getNombreCliente() %></td>
										   		<td><%= elemento.getRemisionId() %></td>
										   		<td><%= Utilerias.getDate(elemento.getFechaRemision(), Utilerias.FORMAT_DATE) %></td>
										   		<td><%= Utilerias.getDescripcionesBD(Utilerias.ESTATUS_PAGO_VENTA, elemento.getEstatusPagoCredito()) %></td>
										   		<td><%= Utilerias.getCurrencyFormat(elemento.getMontoFactura()) %></td>
										   		<td><%= Utilerias.getCurrencyFormat(elemento.getPorPagar()) %></td>
										   		<td><%= Utilerias.getCurrencyFormat(elemento.getMontoInicial()) %></td>
										   		<td>
										   		<%
										   			List<InventarioActual> listInv = catalogMGR.getInventarioAgrupadoCantidad(Utilerias.VENTAS_VALUE, elemento.getRemisionId());
										   			for (int j=0; j < listInv.size(); j++) { 
										   				InventarioActual act = listInv.get(j);
										   				out.println(act.getProducto().getProductoId() + " " + act.getProducto().getNombreProducto() + " " + act.getCantidad() + "<br/>");
										   			}
										   		%>
										   		</td>
										   		<% montoTotal += elemento.getMontoInicial(); %>
										   </tr>
										<% } %> 	
									</table>																	
									<br><b><font color="black" size="2" face="verdana">Ingreso de efectivo <%= Utilerias.getCurrencyFormat(montoTotal) %></font></b>
								<% } %>
      					   <% } %>
      					   
      					   <% if (request.getParameter("preview") == null) { %>
      					   <p><h2>Reporte de Ventas</h2></p>    					    	    													
      					    <form action="<%out.println(request.getContextPath());%>/reportes/ventasReportes.jsp?sessionid=<%= session.getId() %>" method="post">
      					    <table class="tablecontent">
      					    	<tr>
      					    		<td>
		    							Fecha Inicial
		    						</td>
		    						<td>
		    							<input type="text" name="start" id="start" maxlength="10" size="10" value="<%= start %>" readonly="true">
    									<a href="javascript:NewCal('start','ddmmyyyy')">
    										<img src="../images/cal.gif" width="16" height="16" border="0" alt="Selecciona una fecha">
    									</a>
    								</td>
    							</tr>				    					
		    					<tr>
		    						<td>
		    							Fecha Final
		    						</td>
		    						<td>
		    							<input type="text" name="end" id="end" maxlength="10" size="10" value="<%= end %>" readonly="true">
    									<a href="javascript:NewCal('end','ddmmyyyy')">
    										<img src="../images/cal.gif" width="16" height="16" border="0" alt="Selecciona una fecha">
    									</a>
    								</td>
    							</tr>
    							<tr>
    								<td>
    									<input type="submit" value="Enviar" name="enviar">
    								</td>
    							</tr>
    						</table>
		    				</form>							    			 		
    			    		<% } %>
    			    		
    			    		<% if (!Utilerias.isNullOrUndefined(start) && !Utilerias.isNullOrUndefined(end)) { %>
		    					<input type="button" value="Vista Preeliminar" onclick="javascript:window.open('../reportesGenericos/previewPage.jsp?sessionid=<%= session.getId()%>&preview=Y&reporte=ventas&start=<%= start %>&end=<%= end %>', 'Vista Preeliminar');">
		    				<% } %>	
    			    			
      			<% } else { 
  					session.invalidate();    
  					response.sendRedirect(request.getContextPath() + Utilerias.LOGIN_INDEX);		  							
				} %>												