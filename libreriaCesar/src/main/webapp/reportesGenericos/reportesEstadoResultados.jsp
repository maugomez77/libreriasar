<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.beans.EstadoResultados" %>

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

								EstadoResultados estadoResultados = reportesMGR.getEstadoResultados(start, end, catalogMGR, invHisMGR); %>
								
								<p><h3>Estado de Resultados del Periodo <%= start %> al periodo <%= end %></h3></p>
								<table border="1" class="tablas" title="Estado de Resultados">
    								<tr>
										<td>Ventas</td>
										<td><%= Utilerias.getCurrencyFormat(estadoResultados.getVentas()) %></td>
									</tr>
									<tr>
										<td>- Costo de Ventas</td>
										<td><%= Utilerias.getCurrencyFormat(estadoResultados.getCostoDeVentas()) %></td>
									</tr>	
									<tr>
										<td>----- + Inventario Inicial </td>
										<td><%= Utilerias.getCurrencyFormat(estadoResultados.getInventarioInicial()) %></td>
									</tr>	
									
									<tr>
										<td>----- + Compras </td>
										<td><%= Utilerias.getCurrencyFormat(estadoResultados.getCompras()) %></td>
									</tr>
									
									<tr>
										<td>----- - Inventario Final </td>
										<td><%= Utilerias.getCurrencyFormat(estadoResultados.getInventarioFinal()) %></td>
									</tr>
									
									<tr>
										<td>Utilidad Bruta</td>
										<td><%= Utilerias.getCurrencyFormat(estadoResultados.getUtilidadBruta()) %></td>
									</tr>
									<tr>
										<td>- Gastos de Operacion</td>
										<td><%= Utilerias.getCurrencyFormat(estadoResultados.getGastosOperacion()) %></td>
									</tr>
									<tr>
										<td>-------- - Gastos de Admon</td>
										<td><%= Utilerias.getCurrencyFormat(estadoResultados.getGastosAdmon()) %></td>
									</tr>
									<tr>
										<td>-------- - Gastos de Venta</td>
										<td><%= Utilerias.getCurrencyFormat(estadoResultados.getGastosVenta()) %></td>
									</tr>
									<tr>
										<td>-------- - Gastos Indirectos</td>
										<td><%= Utilerias.getCurrencyFormat(estadoResultados.getGastosIndirectos()) %></td>
									</tr>
									<tr>
										<td>Utilidad de Operacion</td>
										<td><%= Utilerias.getCurrencyFormat(estadoResultados.getUtilidadOperacion()) %></td>
									</tr>
									<tr>
										<td>- Otros Gastos</td>
										<td><%= Utilerias.getCurrencyFormat(estadoResultados.getOtrosGastos()) %></td>
									</tr>
									<tr>
										<td>Utilidad Neta</td>
										<td><%= Utilerias.getCurrencyFormat(estadoResultados.getUtilidadNeta()) %></td>
									</tr>
								</table>
      					   <% } %>
      					   
      					   <% if (request.getParameter("preview") == null) {  %>
      					    <p><h2>Reporte de Estado de Resultados</h2></p>
      					    <p>Ejecutar Dicho Reporte Una Vez Actualizado el Inventario</p>    					    	    													
      					    <form action="<%out.println(request.getContextPath());%>/reportes/estadoResultados.jsp?sessionid=<%= session.getId() %>" method="post">
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
		    					<input type="button" value="Vista Preeliminar" onclick="javascript:window.open('../reportesGenericos/previewPage.jsp?sessionid=<%= session.getId()%>&preview=Y&reporte=estadoResultados&start=<%= start %>&end=<%= end %>', 'Vista Preeliminar');">
		    				<% } %>	
    			    			
      			<% } else { 
  					session.invalidate();    
  					response.sendRedirect(request.getContextPath() + Utilerias.LOGIN_INDEX);		  							
				} %>												