<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.beans.EstadoResultados" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR" %>
<%@ page import="mx.com.libreria.manager.InventarioMGR" %>
<%@ page import="mx.com.libreria.manager.ReportesMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.Compras"%>
<%@ page import="mx.com.libreria.model.Empleado"%>
<%@ page import="mx.com.libreria.model.InventarioActual"%>
<%@ page import="mx.com.libreria.model.Remision"%>


				<% if(request.getParameter("sessionid") != null 
					  && session.getId().equals(request.getParameter("sessionid"))
					  && session.getAttribute("uname") != null){  %>
      				
      					<% CatalogMGR catalogMGR = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");
      					   ReportesMGR reportesMGR = (ReportesMGR) ObjectFactory.getBean("reportesMGR");
      					   InventarioMGR invMGR = (InventarioMGR) ObjectFactory.getBean("inventarioMGR"); %>
      					        
      					        <% Remision remision = new Remision();
      					   		   remision.setRemisionId(Utilerias.strToInt(request.getParameter("remisionId")));
      					   		   remision = (Remision) catalogMGR.getObjectData(remision); %>
								
								<font size=3 face=arial>"LIBR SAN JUAN DIEGO"</font>
								<br/>					   				
      					   		<font size=2 face=arial>Remisi&oacute;n: <%= remision.getRemisionId() %></font>
      					   		<br/>      					   		
      					   		<font size=2 face=arial>Cliente:</font>
      					   		<br/>
      					   			<textarea rows="2" cols="25" name="cliente"><%= remision.getCliente().getNombreCliente() %></textarea>
		  						<br/>		  						
      					   		<font size=2 face=arial>Fecha de Venta: <%= Utilerias.getDate(remision.getFechaRemision(), Utilerias.FORMAT_DATE) %></font>		  						
		  						<br/>		  						
      					   		<font size=2 face=arial>Forma de Pago: <%= Utilerias.getDescripcionesBD(Utilerias.ESTATUS_PAGO_VENTA, remision.getEstatusPagoCredito()) %></font>
      					   		<br/>
		  						<font size=2 face=arial>Monto Facturado: <%= Utilerias.getCurrencyFormat(remision.getMontoFactura()) %></font>
		  						<br/>
		  						<font size=2 face=arial>Monto Inicial:  <%= Utilerias.getCurrencyFormat(remision.getMontoInicial()) %></font>
		  						<br/>
		  						<font size=2 face=arial>Monto Por Pagar: <%= Utilerias.getCurrencyFormat(remision.getPorPagar()) %></font>
		  						<br/>
		  						
		  						<table>
		  							<tr>		  							
		  								<td>		  											    								
		    									<% List<InventarioActual> list = (ArrayList<InventarioActual>) 
		    											Utilerias.convertSetToList(remision.getListaInventario(), new InventarioActual());
		    									
		    	      					   		   if (list.size() > 0) { %>
		    	    					   				<table border="1" class="tablas" title="Detalle de la Venta">
		    	    										<tr>
		    													<td>Producto</td>
		    													<td>Cantidad</td>										
		    													<td>Precio</td>
		    													<td>Total</td>																			
		    												</tr>
		    																					
		    												<%  float montoTotal = 0;
		    												    for (int i=0; i < list.size(); i++) { 
		    													InventarioActual elemento = list.get(i); %>
		    													<tr>
		    														<td><%= elemento.getProducto().getNombreProducto() %></td>
		    														<td align="center"><%= elemento.getCantidad() %></td>
		    														<td><%= Utilerias.getCurrencyFormat(elemento.getPrecioProducto()) %></td>
		    														<td><%= Utilerias.getCurrencyFormat(elemento.getCantidad() * elemento.getPrecioProducto()) %></td>
		    														<% montoTotal += elemento.getPrecioProducto() * elemento.getCantidad(); %>
		    													</tr>	
		    												<% } %>
		    											</table>		    													    		
		    										<% }  %>											    										    											    						
		  								</td>						
		  							</tr>
		  						   </table>	      					   		         					   		   		  						   																				      					 
								  <font size=2 face=arial>Atendido por: 
								  	<% Empleado e = new Empleado();
								  	   e.setEmpleadoId(remision.getEmpleadoId());
								  	   e = (Empleado) catalogMGR.getObjectData(e);
								  	   out.println(e.getNombre()); %>
								  </font>
								  <br/>
								  <font size=2 face=arial>Despu&eacute;s de entregado la mercanc&iacute;a,</font>
								  <br/>
								  <font size=2 face=arial>no se aceptan devoluciones</font>
								  <br/>
									      					   
    			    			  <input type="button" value="Vista Preeliminar" onclick="javascript:window.open('../reportesGenericos/previewPage.jsp?sessionid=<%= session.getId()%>&reporte=entradaRemision&remisionId=<%=request.getParameter("remisionId")%>', 'Vista Preeliminar');">
    			    			
      			<% } else { 
  					session.invalidate();    
  					response.sendRedirect(request.getContextPath() + Utilerias.LOGIN_INDEX);		  							
				} %>												