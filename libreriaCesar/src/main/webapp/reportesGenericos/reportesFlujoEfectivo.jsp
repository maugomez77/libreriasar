<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.beans.FlujoDeEfectivo" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR" %>
<%@ page import="mx.com.libreria.manager.InventarioHistoricoMGR" %>
<%@ page import="mx.com.libreria.manager.ReportesMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

				<% if(request.getParameter("sessionid") != null 
					  && session.getId().equals(request.getParameter("sessionid"))
					  && session.getAttribute("uname") != null){  %>
      				
      					<% CatalogMGR catalogMGR = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");
      					   ReportesMGR reportesMGR = (ReportesMGR) ObjectFactory.getBean("reportesMGR");
      					   InventarioHistoricoMGR invHisMGR = (InventarioHistoricoMGR) ObjectFactory.getBean("inventarioHistoricoMGR");
      					   
      					   String start = request.getParameter("start");
      					   String end = request.getParameter("end");
      					   
      					   if (Utilerias.isNullOrUndefined(start)) { 
      						   start = "";
      					   }
      					   if (Utilerias.isNullOrUndefined(end)) { 
      						   end = "";
      					   }
      					   
      					   if (!Utilerias.isNullOrUndefined(start) && !Utilerias.isNullOrUndefined(end)) {

								FlujoDeEfectivo flujoEfectivo = reportesMGR.getFlujoEfectivo(start, end, catalogMGR, invHisMGR); %>
								
								<p><h3>Flujo de Efectivo del Periodo <%= start %> al periodo <%= end %></h3></p>
								<table border="1" class="tablas" title="Flujo de Efectivo">
    								<tr>
										<td>Ingresos</td>
										<td><%= Utilerias.getCurrencyFormat(flujoEfectivo.getIngresos()) %></td>
									</tr>
									<tr>
										<td>--------- + Ventas parte del Ingreso</td>
										<td><%= Utilerias.getCurrencyFormat(flujoEfectivo.getVentasConAbono()) %></td>
									</tr>	
									<tr>
										<td>Egresos</td>
										<td><%= Utilerias.getCurrencyFormat(flujoEfectivo.getEgresos()) %></td>
									</tr>	
									
									<tr>
										<td>----- - Compras </td>
										<td><%= Utilerias.getCurrencyFormat(flujoEfectivo.getComprasConAbono()) %></td>
									</tr>
									
									<tr>
										<td>----- - Gastos Administrativos </td>
										<td><%= Utilerias.getCurrencyFormat(flujoEfectivo.getGastoAdmon()) %></td>
									</tr>
									
									<tr>
										<td>----- - Gastos Indirectos </td>
										<td><%= Utilerias.getCurrencyFormat(flujoEfectivo.getGastoIndirecto()) %></td>
									</tr>
									<tr>
										<td>----- - Otros Gastos </td>
										<td><%= Utilerias.getCurrencyFormat(flujoEfectivo.getGastoOtros()) %></td>
									</tr>
									<tr>
										<td>----- - Gastos de Venta </td>
										<td><%= Utilerias.getCurrencyFormat(flujoEfectivo.getGastoVenta()) %></td>
									</tr>
									<tr>
										<td>Total = Ingresos - Egresos </td>
										<td><%= Utilerias.getCurrencyFormat(flujoEfectivo.getIngresos() - flujoEfectivo.getEgresos()) %></td>
									</tr>									
								</table>
      					   <% } %>
      					   
      					   <% if (request.getParameter("preview") == null) {  %>
      					    <p><h2>Reporte de Flujo de Efectivo</h2></p>    		
      					    <p>Ejecutar Dicho Reporte Una Vez Actualizado el Inventario</p>			    	    													
      					    <form action="<%out.println(request.getContextPath());%>/reportes/flujoEfectivo.jsp?sessionid=<%= session.getId() %>" method="post">
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
		    					<input type="button" value="Vista Preeliminar" onclick="javascript:window.open('../reportesGenericos/previewPage.jsp?sessionid=<%= session.getId()%>&preview=Y&reporte=flujoEfectivo&start=<%= start %>&end=<%= end %>', 'Vista Preeliminar');">
		    				<% } %>	
    			    			
      			<% } else { 
  					session.invalidate();    
  					response.sendRedirect(request.getContextPath() + Utilerias.LOGIN_INDEX);		  							
				} %>												