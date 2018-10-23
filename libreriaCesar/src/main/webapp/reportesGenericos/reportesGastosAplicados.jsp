<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR" %>
<%@ page import="mx.com.libreria.manager.InventarioMGR" %>
<%@ page import="mx.com.libreria.manager.ReportesMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.Gastos"%>


				<% if(request.getParameter("sessionid") != null 
					  && session.getId().equals(request.getParameter("sessionid"))
					  && session.getAttribute("uname") != null){  %>
      				
      					<% CatalogMGR catalogMGR = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");
      					   ReportesMGR reportesMGR = (ReportesMGR) ObjectFactory.getBean("reportesMGR");
      					   InventarioMGR invMGR = (InventarioMGR) ObjectFactory.getBean("inventarioMGR");
      					   
      					   String start = request.getParameter("start");
      					   String end = request.getParameter("end");
      					   
      					   if (Utilerias.isNullOrUndefined(start)) { 
      						   start = "";
      					   }
      					   if (Utilerias.isNullOrUndefined(end)) { 
      						   end = "";
      					   }
      					   
      					   if (!Utilerias.isNullOrUndefined(start) && !Utilerias.isNullOrUndefined(end)) {

								List<Gastos> listGastos = reportesMGR.getGastosRangoFechas(start, end, catalogMGR); 
								
								if (listGastos.size() > 0) { %>
									
									<p><h3>Reporte de Gastos del Periodo <%= start %> al periodo <%= end %></h3></p>
									<table border="1" class="tablas" title="Gastos">
	    								<tr>
	    									<td>Activo</td>
											<td>Identificador Gasto</td>
											<td>Fecha Aplicaci&oacute;n</td>
											<td>Monto del Gasto</td>
											<td>Tipo de Gasto</td>
											<td>Descripcion del Gasto</td>																				
										</tr>
										
										<% 
										   float montoTotal = 0;
										   for (int i=0; i < listGastos.size(); i++) { 
										   Gastos elemento = listGastos.get(i); %>
										   <tr>
										   		<td><%= Utilerias.getMessageValue(Utilerias.YES_NO_KEY, elemento.getActivo()) %></td>
										   		<td><%= elemento.getGastosId() %></td>
										   		<td><%= Utilerias.getDate(elemento.getFechaAplicacion(), Utilerias.FORMAT_DATE) %></td>
										   		<td><%= Utilerias.getCurrencyFormat(elemento.getMontoGasto()) %></td>
										   		<td><%= elemento.getTipoGasto().getNombreGasto() %></td>
										   		<td><%= elemento.getTipoGasto().getDescGasto() %></td>
										   		<%
										   			montoTotal += elemento.getMontoGasto();
										   		%>
										   </tr>
										<% } %> 	
									</table>								
									<br><b><font color="black" size="2" face="verdana">Total de Gastos <%= Utilerias.getCurrencyFormat(montoTotal) %></font></b>
								<% } %>
      					   <% } %>
      					   
      					   <% if (request.getParameter("preview") == null) { %>
      					   <p><h2>Reporte de Gastos</h2></p>    					    	    													
      					    <form action="<%out.println(request.getContextPath());%>/reportes/gastosAplicadosReportes.jsp?sessionid=<%= session.getId() %>" method="post">
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
		    					<input type="button" value="Vista Preeliminar" onclick="javascript:window.open('../reportesGenericos/previewPage.jsp?sessionid=<%= session.getId()%>&preview=Y&reporte=gastosAplicados&start=<%= start %>&end=<%= end %>', 'Vista Preeliminar');">
		    				<% } %>	
    			    			
      			<% } else { 
  					session.invalidate();    
  					response.sendRedirect(request.getContextPath() + Utilerias.LOGIN_INDEX);		  							
				} %>												