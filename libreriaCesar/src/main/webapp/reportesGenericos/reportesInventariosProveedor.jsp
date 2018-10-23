<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.beans.EstadoResultados" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR" %>
<%@ page import="mx.com.libreria.manager.InventarioMGR" %>
<%@ page import="mx.com.libreria.manager.ReportesMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.beans.ReporteProveedorInventario"%>

				<% if(request.getParameter("sessionid") != null 
					  && session.getId().equals(request.getParameter("sessionid"))
					  && session.getAttribute("uname") != null){  %>
      				
      					<% CatalogMGR catalogMGR = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");
      					   ReportesMGR reportesMGR = (ReportesMGR) ObjectFactory.getBean("reportesMGR");
      					   InventarioMGR invMGR = (InventarioMGR) ObjectFactory.getBean("inventarioMGR"); %>
      					         					   				
      					   		<p><h2>Reporte de Inventarios al Dia Por Proveedor</h2></p>
      					   		
      					   		<% List<ReporteProveedorInventario> list = invMGR.getInventarioAlDiaProveedor(catalogMGR);
      					   		   if (list.size() > 0) { %>
    					   				<table border="1" class="tablas" title="Estado de Resultados">
    										<tr>
    										    <td>Proveedor</td>
												<td>Producto</td>
												<td>Cantidad</td>																																								
											</tr>
																				
											<%  for (int i=0; i < list.size(); i++) { 
												ReporteProveedorInventario elemento = list.get(i); %>
												<tr>
													<td><%= elemento.getProveedor().getNombreProveedor() %></td>
													<td><%= elemento.getProducto().getNombreProducto() %></td>
													<td><%= elemento.getCantidad() %></td>													
												</tr>	
											<% } %>
										</table>
										<br/>										
										
									<% } %>	      					   
									      					   
    			    			<input type="button" value="Vista Preeliminar" onclick="javascript:window.open('../reportesGenericos/previewPage.jsp?sessionid=<%= session.getId()%>&reporte=inventarios', 'Vista Preeliminar');">		    				
    			    			
      			<% } else { 
  					session.invalidate();    
  					response.sendRedirect(request.getContextPath() + Utilerias.LOGIN_INDEX);		  							
				} %>												