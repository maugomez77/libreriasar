<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR" %>
<%@ page import="mx.com.libreria.manager.InventarioMGR" %>
<%@ page import="mx.com.libreria.manager.ReportesMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>
<%@ page import="mx.com.libreria.manager.VentaEspecialMGR"%>

<%@ page import="mx.com.libreria.model.Compras" %>
<%@ page import="mx.com.libreria.model.InventarioActual" %>
<%@ page import="mx.com.libreria.model.VentaEspecial"%>

				<% if(request.getParameter("sessionid") != null 
					  && session.getId().equals(request.getParameter("sessionid"))
					  && session.getAttribute("uname") != null){  %>
      				
      					<% CatalogMGR catalogMGR = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");
      					   VentaEspecialMGR venMGR = (VentaEspecialMGR) ObjectFactory.getBean("ventaEspecialMGR");
      					   
      					   String start = request.getParameter("start");
      					   String end = request.getParameter("end");
      					   
      					   if (Utilerias.isNullOrUndefined(start)) { 
      						   start = "";
      					   }
      					   if (Utilerias.isNullOrUndefined(end)) { 
      						   end = "";
      					   }
      					   
      					   if (!Utilerias.isNullOrUndefined(start) && !Utilerias.isNullOrUndefined(end)) {

								List<VentaEspecial> list = venMGR.getVentaEspecial(catalogMGR, start, end); 
								
								if (list.size() > 0) { %>
									
									<p><h3>Reporte de Ventas Especiales del Periodo <%= start %> al periodo <%= end %></h3></p>
									<table border="1" class="tablas" title="Compras">
	    								<tr>
											<td>Activo</td>
											<td>Cliente</td>
											<td>Venta Especial</td>
											<td>Fecha de Venta Especial</td>
											<td>Importe</td>
											<td>Por Pagar</td>
											<td>Abonado</td>
											<td>Detalle Venta Especial</td>																				
										</tr>
										
										<% 
										   float montoTotal = 0;
										   for (int i=0; i < list.size(); i++) { 
										   VentaEspecial elemento = list.get(i); %>
										   <tr>
										   		<td><%= Utilerias.getMessageValue(Utilerias.YES_NO_KEY, elemento.getActivo()) %></td>
										   		<td><%= elemento.getCliente().getNombreCliente() %></td>
										   		<td><%= elemento.getVentaEspecialId() %></td>
										   		<td><%= Utilerias.getDate(elemento.getFechaInicialVentaEspecial(), Utilerias.FORMAT_DATE) %></td>
										   		<td><%= Utilerias.getCurrencyFormat(elemento.getMontoFactura()) %></td>
										   		<td><%= Utilerias.getCurrencyFormat(elemento.getPorPagar()) %></td>
										   		<td><%= Utilerias.getCurrencyFormat(catalogMGR.getAbonosTotales(elemento.getListaAbonos())) %></td>
										   		<td>
										   		<%
										   			List<InventarioActual> listInv = catalogMGR.getInventarioAgrupadoCantidad(Utilerias.VENTAS_ESPECIALES_VALUE, elemento.getVentaEspecialId());
										   			for (int j=0; j < listInv.size(); j++) { 
										   				InventarioActual act = listInv.get(j);
										   				out.println(act.getProducto().getProductoId() + " " + act.getProducto().getNombreProducto() + " " + act.getCantidad() + "<br/>");
										   			}
										   		%>
										   		</td>
										   		<% montoTotal += elemento.getMontoInicial() + catalogMGR.getAbonosTotales(elemento.getListaAbonos()); %>
										   </tr>
										<% } %> 	
									</table>
									
									<br><b><font color="black" size="2" face="verdana">Ingreso de efectivo <%= Utilerias.getCurrencyFormat(montoTotal) %></font></b>
								<% } %>
      					   <% } %>
      					   
      					   <% if (request.getParameter("preview") == null) { %>
      					   <p><h2>Reporte de Ventas Especiales</h2></p>    					    	    													
      					    <form action="<%out.println(request.getContextPath());%>/reportes/ventasEspecialesReportes.jsp?sessionid=<%= session.getId() %>" method="post">
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
		    					<input type="button" value="Vista Preeliminar" onclick="javascript:window.open('../reportesGenericos/previewPage.jsp?sessionid=<%= session.getId()%>&preview=Y&reporte=ventasEspeciales&start=<%= start %>&end=<%= end %>', 'Vista Preeliminar');">
		    				<% } %>	
    			    			
      			<% } else { 
  					session.invalidate();    
  					response.sendRedirect(request.getContextPath() + Utilerias.LOGIN_INDEX);		  							
				} %>												