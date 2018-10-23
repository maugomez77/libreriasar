<%@ page import="mx.com.libreria.manager.SuscripcionMGR"%>
<%@ page import="mx.com.libreria.model.Suscripcion"%>
<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR" %>
<%@ page import="mx.com.libreria.manager.InventarioMGR" %>
<%@ page import="mx.com.libreria.manager.ReportesMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.Compras" %>

				<% if(request.getParameter("sessionid") != null 
					  && session.getId().equals(request.getParameter("sessionid"))
					  && session.getAttribute("uname") != null){  %>
      				
      					<% CatalogMGR catalogMGR = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");
      					   SuscripcionMGR susMGR = (SuscripcionMGR) ObjectFactory.getBean("suscripcionMGR");
      					   
      					   String start = request.getParameter("start");
      					   String end = request.getParameter("end");
      					   
      					   if (Utilerias.isNullOrUndefined(start)) { 
      						   start = "";
      					   }
      					   if (Utilerias.isNullOrUndefined(end)) { 
      						   end = "";
      					   }
      					   
      					   if (!Utilerias.isNullOrUndefined(start) && !Utilerias.isNullOrUndefined(end)) {

								List<Suscripcion> listSus = susMGR.getSuscripciones(catalogMGR, start, end); 
								
								if (listSus.size() > 0) { %>
									
									<p><h3>Reporte de Suscripciones del Periodo <%= start %> al periodo <%= end %></h3></p>
									<table border="1" class="tablas" title="Compras">
	    								<tr>
	    									<td>Activo</td>
											<td>Clave de Suscripcion</td>
											<td>Fecha de Suscripcion</td>
											<td>Numero de Meses Restantes</td>
											<td>Cliente</td>
											<td>Producto</td>
											<td>Monto Suscripcion</td>										
										</tr>
										
										<% 
										   float montoTotal = 0;
										   for (int i=0; i < listSus.size(); i++) { 
										   Suscripcion elemento = listSus.get(i); %>
										   <tr>
										   		<td><%= Utilerias.getMessageValue(Utilerias.YES_NO_KEY, elemento.getActivo()) %></td>
										   		<td><%= elemento.getSuscripcionId() %></td>
										   		<td><%= Utilerias.getDate(elemento.getFechaInicialSuscripcion(), Utilerias.FORMAT_DATE) %></td>
										   		<td><%= elemento.getNumeroMesesSuscripcionRestante() %></td>
										   		<td><%= elemento.getCliente().getNombreCliente() %></td>
										   		<td><%= elemento.getProducto().getNombreProducto() %></td>
										   		<td><%= Utilerias.getCurrencyFormat(elemento.getMontoInicial()) %></td>
										   		<% montoTotal += elemento.getMontoInicial(); %>
										   </tr>
										<% } %> 	
									</table>
									
									<br><b><font color="black" size="2" face="verdana">Ingreso de efectivo <%= Utilerias.getCurrencyFormat(montoTotal) %></font></b>								
								<% } %>
      					   <% } %>
      					   
      					   <% if (request.getParameter("preview") == null) { %>
      					   <p><h2>Reporte de Suscripcion</h2></p>    					    	    													
      					    <form action="<%out.println(request.getContextPath());%>/reportes/suscripcionReportes.jsp?sessionid=<%= session.getId() %>" method="post">
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
		    					<input type="button" value="Vista Preeliminar" onclick="javascript:window.open('../reportesGenericos/previewPage.jsp?sessionid=<%= session.getId()%>&preview=Y&reporte=suscripcion&start=<%= start %>&end=<%= end %>', 'Vista Preeliminar');">
		    				<% } %>	
    			    			
      			<% } else { 
  					session.invalidate();    
  					response.sendRedirect(request.getContextPath() + Utilerias.LOGIN_INDEX);		  							
				} %>												