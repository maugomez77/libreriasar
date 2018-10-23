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
<%@ page import="mx.com.libreria.model.InventarioActual"%>

				<% if(request.getParameter("sessionid") != null 
					  && session.getId().equals(request.getParameter("sessionid"))
					  && session.getAttribute("uname") != null){  %>
      				
      					<% CatalogMGR catalogMGR = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");
      					   ReportesMGR reportesMGR = (ReportesMGR) ObjectFactory.getBean("reportesMGR");
      					   InventarioMGR invMGR = (InventarioMGR) ObjectFactory.getBean("inventarioMGR"); %>
      					         					   				
      					   		<p><h2>Registro de Compra - Folio No. <%= request.getParameter("comprasId") %></h2></p>
      					   		
      					   		<% Compras compras = new Compras();
      					   		   compras.setComprasId(Utilerias.strToInt(request.getParameter("comprasId")));
      					   		   compras = (Compras) catalogMGR.getObjectData(compras); %>
      					   		   
      					   		   <table class="tablecontent">
		  							<tr>
		  								<td>	
		  									Folio: 
		  								</td>	
		  								<td>
		  									<%= compras.getComprasId() %>
		  								</td>
		  							</tr>
		  							<tr>
		  								<td>
		  									Forma de Pago:
		  								</td>
		  								<td>
		  									<%= Utilerias.getDescripcionesBD(Utilerias.ESTATUS_PAGO_COMPRA, compras.getEstatusPagoCredito()) %>
		  								</td>
		  							</tr>
		  							<tr>
		  								<td>Fecha de Compra:</td>
		  								<td><%= compras.getFechaCompra() %></td>
		  							</tr>
		  							<tr>
		  								<td>Proveedor:</td>
		  								<td><%= compras.getProveedor().getNombreProveedor() %></td>
		  							</tr>
		  							<tr>
		  								<td>Monto Inicial:</td>
		  								<td><%= compras.getMontoInicial() %></td>
		  							</tr>
		  							<tr>
		  								<td>Monto Facturado:</td>
		  								<td><%= compras.getMontoFactura() %></td>
		  							</tr>
		  							<tr>
		  								<td>Monto Por Pagar:</td>
		  								<td><%= compras.getPorPagar() %></td>
		  							</tr>
		  							<tr>
		  								<td>Detalle de Productos:</td>
		  								<td>		  											    								
		    									<% List<InventarioActual> list = (ArrayList<InventarioActual>) 
		    											Utilerias.convertSetToList(compras.getListaInventario(), new InventarioActual());
		    									
		    	      					   		   if (list.size() > 0) { %>
		    	    					   				<table border="1" class="tablas" title="Detalle de la Compra">
		    	    										<tr>
		    													<td>Producto</td>
		    													<td>Fecha de Compra</td>
		    													<td>Costo</td>
		    													<td><%= Utilerias.IVA_DESC %></td>
		    													<td>Cantidad</td>										
		    													<td>Precio P&uacute;blico</td>
		    													<td>Total</td>
		    												</tr>
		    																					
		    												<%  float montoTotal = 0;
		    												    for (int i=0; i < list.size(); i++) { 
		    													InventarioActual elemento = list.get(i); %>
		    													<tr>
		    														<td><%= elemento.getProducto().getNombreProducto() %></td>
		    														<td><%= Utilerias.getDate(elemento.getFechaEntrada(), Utilerias.FORMAT_DATE) %></td>
		    														<td><%= elemento.getCostoProducto() %></td>
		    														<td><%= elemento.getIva() %></td>
		    														<td><%= elemento.getCantidad() %></td>
		    														<td><%= elemento.getPrecioProducto() %></td>
		    														<td><%= elemento.getCantidad() * elemento.getCostoProducto() %></td>
		    														<% montoTotal += elemento.getCostoProducto() * elemento.getCantidad(); %>
		    													</tr>	
		    												<% } %>
		    											</table>		    			
		    										<% }  %>											    										    											    						
		  								</td>						
		  							</tr>
		  						   </table>	      					   		         					   		   		  						   																				      					 
									      					   
    			    			  <input type="button" value="Vista Preeliminar" onclick="javascript:window.open('../reportesGenericos/previewPage.jsp?sessionid=<%= session.getId()%>&reporte=entradaInventario&comprasId=<%=request.getParameter("comprasId")%>', 'Vista Preeliminar');">		    				
    			    			
      			<% } else { 
  					session.invalidate();    
  					response.sendRedirect(request.getContextPath() + Utilerias.LOGIN_INDEX);		  							
				} %>												