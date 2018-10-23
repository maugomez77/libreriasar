<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR"%>
<%@ page import="mx.com.libreria.manager.InventarioMGR"%>
<%@ page import="mx.com.libreria.manager.ProductMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.Cliente" %>
<%@ page import="mx.com.libreria.model.InventarioActual"%>
<%@ page import="mx.com.libreria.model.Producto" %>
<%@ page import="mx.com.libreria.model.Proveedor" %>

<html>
	<%@ include file="../admin/Header.jsp" %>

	<body>
		<div class="wrapper1">
			<div class="wrapper">
				<%@ include file="../admin/MenuHeader.jsp" %>			
				<div class="content">

				<% if(request.getParameter("sessionid") != null 
					  && session.getId().equals(request.getParameter("sessionid"))
					  && session.getAttribute("uname") != null){  %>
      				
      					<% ProductMGR productMGR = (ProductMGR) ObjectFactory.getBean("productMGR"); 
      					   CatalogMGR catalogoMGR = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");
      					   InventarioMGR inventarioMGR = (InventarioMGR) ObjectFactory.getBean("inventarioMGR");
      					         					   
      					    InventarioActual inventario = new InventarioActual();
      					    Producto prod = new Producto();
      					        					 
      					  	if (request.getParameter("producto") != null) {
      					  		
  								prod.setProductoId(Utilerias.strToInt(request.getParameter("producto")));
  								prod = (Producto) catalogoMGR.getObjectData(prod);
  								  							
  								inventario.setProducto(prod);  							
      					  	}
      					  
							inventario.setFechaEntrada(Utilerias.getDateToday(Utilerias.FORMAT_DATE));
							inventario.setCostoProducto(Utilerias.strToFloat(request.getParameter("costoProducto")));
							inventario.setPrecioProducto(Utilerias.strToFloat(request.getParameter("precioProducto")));
							inventario.setCantidad(Utilerias.strToInt(request.getParameter("cantidad")));
							if (!Utilerias.isNullOrUndefined(request.getParameter("iva")) && request.getParameter("iva").equals(Utilerias.YES_VALUE)) {
								inventario.setIva(inventario.getPrecioProducto() * Utilerias.IVA_PORCENTAJE); 
							} else { 
								inventario.setIva(0);
							}
							inventario.setEstatus("E");
							
							String result = "";
							boolean entraInventario = false;
      					%>
      					
    					<p><h2>Compras</h2></p>    					    	    													
							
							<% if (request.getParameter("agregar") != null) { 
		  						
		  						result = catalogoMGR.validarCamposLlenos(inventario);
		  						if (result.equals("")) { 
		  							result = inventarioMGR.validarInventarioDuplicadoSesion(request, response, inventario);
		  						}
		  						if (result.equals("")) { 
		  							inventarioMGR.agregarInventarioSesion(request, response, inventario);
		  							result = "Compra agregada a la sesion satisfactoriamente";
		  							entraInventario = true;		  						    
		  						} 
		  						
		  						if (!entraInventario) { %>
		  							<script>alert("<%= result %>");</script>
		  						<% } %>			  							
		  					<% } %>
		  					
		  					
		  					
		  					<% if (request.getParameter("eliminar") != null) { 		  							  						
		  						inventarioMGR.eliminarUnProductoDeInventarioActualSesion(request, Utilerias.strToInt(request.getParameter("indice")));
		  					} %>
		  					
		  					<% if (inventarioMGR.getInventarioActualSesion(request) != null) { %> 
								
								<table border="1" class="tablas">
	    							<tr bgcolor="silver">
	    								<td>Producto</td>
    									<td>Fecha Entrada</td>
    									<td>Costo</td>
    									<td>Precio</td>
    									<td>Cantidad</td>
    									<td>IVA</td>    					
    									<td></td>				
    								</tr>
                        			                        
								<% 
								
								List<InventarioActual> lista = (ArrayList<InventarioActual>) inventarioMGR.getInventarioActualSesion(request);
								
								for (int i=0; i < lista.size(); i++) { %> 
									<% InventarioActual elemento = lista.get(i); %>
									<tr>
										<td><%= elemento.getProducto().getNombreProducto() %></td>
										<td><%= Utilerias.getDate(elemento.getFechaEntrada(), Utilerias.FORMAT_DATE) %></td>
										<td><%= elemento.getCostoProducto() %></td>
										<td><%= elemento.getPrecioProducto() %></td>
										<td><%= elemento.getCantidad() %></td>
										<td><%= Utilerias.getDecimalFormat(elemento.getIva()) %></td>
										<td>
											<form action="inventarios.jsp?sessionid=<%= session.getId() %>" method="post">
												<input type="hidden" name="indice" value="<%= i %>">
												<input type="submit" name="eliminar" value="Eliminar">
											</form>
										</td>
									</tr>	
								<% } %>
								
								</table>
								<br/>
								
							<% } %>
							
							
		  					<% if (request.getParameter("producto") != null && !entraInventario) { %> 
		  						
		  						<form action="inventarios.jsp?sessionid=<%= session.getId() %>" method="post" name="forma">
		  						<table class="tablecontent">
		  							<tr>
		  								<td>
		  									Producto Inventariado
		  								</td>
		  								<td>
		  									<%= prod.getProductoId() + " " + prod.getNombreProducto() %>
		  								</td>		
		  							</tr>	
		  							<tr>
		  								<td>
		  									Costo del producto
		  								</td>
		  								<td>	 
		  						   			<input type="text" name="costoProducto" value="<%= Utilerias.mostrarCamposFront(inventario.getCostoProducto()) %>">
		  						   		</td>
		  						   	</tr>		  						   	
		  							<tr>
		  								<td>
		  									Precio del producto
		  								</td>
		  								<td>
		  									<input type="text" name="precioProducto" value="<%= Utilerias.mostrarCamposFront(inventario.getPrecioProducto()) %>">
		  								</td>
		  							</tr>		
		  							<tr>
		  								<td>
		  									Cantidad del producto
		  								</td>
		  								<td>	 
		  						   			<input type="text" name="cantidad" value="<%= Utilerias.mostrarCamposFront(inventario.getCantidad()) %>">
		  						   		</td>
		  						   	</tr>
		  						   	<tr>
		  						   		<td>
		  						   			Aplica IVA 16 %:
		  						   		</td>
		  						   		<td>
		  						   			<select name="iva">
		  						   				<option value="<%= Utilerias.NO_VALUE %>"  <% if (!Utilerias.isNullOrUndefined(request.getParameter("iva")) && request.getParameter("iva").equals(Utilerias.NO_VALUE)) out.println("selected");%>><%= Utilerias.NO_DESC %></option>
		  						   				<option value="<%= Utilerias.YES_VALUE %>" <% if (!Utilerias.isNullOrUndefined(request.getParameter("iva")) && request.getParameter("iva").equals(Utilerias.YES_VALUE)) out.println("selected"); %>><%= Utilerias.YES_DESC %></option>		  						   				
		  						   			</select>
		  						   		</td>
		  						   	</tr>
		  						   	
		  						   	<tr>
		  						   		<td>
		  						   			<input type="hidden" name="producto" value="<%= prod.getProductoId() %>">
		  						    		<input type="hidden" name="agregar" value="Y">
		  									<input type="submit" name="enviar" value="Agregar Inventario">
		  								</td>
		  							</tr>		
		  						</table>
		  						</form>
		  						
		  					<% } %> 
		  					
		  					<% if (inventarioMGR.getInventarioActualSesion(request) != null) { %>
		  						
		  						<form action="inventarioRegistrarCompra.jsp?sessionid=<%= session.getId() %>" method="post">
		  						<table class="tablecontent">
		  							<tr>
		  								<td>
		  									<input type="hidden" name="inventario" value="YES">
		  									<input type="submit" name="enviar" value="Registrar en Sistema">	
		  								</td>
		  							</tr>		  							    
		  						</table>
		  						</form>
		  						
		  					<% } %>
		  							  					
		  							  					
		  					<% 
		  						/**
		  						 * AQUI TIENE QUE ENTRAR POR PRIMERA VEZ
		  						 */		  						
		  					%>
		  						
		  					<% if (request.getParameter("registrarCompraProveedor") != null) { 
		  						   inventarioMGR.crearIdCompraSesion(request);
		  					} %>
		  							  							  					
		  					<% if (request.getParameter("deshacer") != null) { 		  						
		  						   inventarioMGR.limpiarCompraSesion(request);		  						   
		  						   response.sendRedirect(request.getContextPath() + "/manager/inventarios.jsp?sessionid=" + session.getId());		  					    
		  					 } %>
		  							  					
		  					<% if (inventarioMGR.getCompraProveedorSesion(request) == null) { %>
		  						
		  						<form action="inventarios.jsp?sessionid=<%= session.getId() %>" method="post">
		  						<table class="tablecontent">
		  							<tr>
		  								<td>
		  									Proveedor
		  								</td>
		  								<td>			  						
		  									<select name="proveedorIdInventario">
  							    			<% 
  							    				Proveedor prov = new Proveedor();
  							  					ArrayList<Proveedor> listaProv = (ArrayList<Proveedor>) catalogoMGR.getList(prov);
												for (int i=0; i < listaProv.size(); i++) { 
													prov = (Proveedor) listaProv.get(i);
  												%>
      											<option value="<%= prov.getProveedorId() %>"><%= prov.getNombreProveedor() %></option>
    							        		<% } %>
    										</select>
    										<input type="submit" name="registrarCompraProveedor" value="Seleccionar Productos">
    									</td>
    								</tr>
    							</table>			
		  						</form>
		  					
		  					<% } %>
		  					
							<% if (inventarioMGR.getCompraProveedorSesion(request) != null) { %>  					
		  						
		  						<form action="inventarios.jsp?sessionid=<%= session.getId() %>" method="post">
		  						<table class="tablecontent">
		  							<tr>
		  								<td>		  						
		  									<input type="submit" name="deshacer" value="Deshacer Compra">
		  								</td>
		  							</tr>
		  						</table>					  								  						
		  						</form>
		  								  						
		  						<form action="inventarios.jsp?sessionid=<%= session.getId() %>" method="post" name="forma" id="forma">
		  						<table class="tablecontent">
		  							<tr>
		  								<td>
		  									Selecciona un producto:
		  								</td>
		  								<td>
		  									<div id="producto" ></div>
		  								</td>
		  							</tr>
			    	  			</table>
			    	  			
		  								<% List<Producto> listaProductos = productMGR.getAllNameArticulosPorProveedor(inventarioMGR.getNumeroProveedor(request)); %>
		  			  	    								  			
				  						<script>
				  				
				  				    		//el primer nombre esta en el div, el segundo es como se recibe en el request
				  				    		//por convencion se dejan los mismos en ambos.			  				   
			      	  						var combo2 = new dhtmlXCombo("producto","producto",200);
											combo2.enableFilteringMode(true);
							
											<% for (int i=0; i < listaProductos.size(); i++) {
												Producto elemento  = (Producto) listaProductos.get(i);
												out.println("combo2.addOption(" + elemento.getProductoId() + ", '" + elemento.getNombreProducto() + "');");	
											} %>			
									
											combo2.attachEvent("onKeyPressed", 							
												function(keyCode){ 
													if (keyCode != 13) return; 
													if (combo2.getSelectedValue() != null) { 
														document.forms.forma.submit();	
													}											
													//alert("1: " + combo2.getSelectedValue()); 
													//alert("2: " + combo2.getSelectedText()); 
													//alert("3: " + combo2.getComboText()); 																					
											});  							
			    	  					</script>
			    	  			</form>
    			    			
    			    		<% } %>	
  					<% } else { 
  							session.invalidate();    
  							response.sendRedirect(request.getContextPath() + Utilerias.LOGIN_INDEX);		  							
					} %>				
									
				<%@ include file="../admin/Footer.jsp" %>    
  			</div> <% //se cierra content %>   		
  		</div><% //se cierra wrapper %>
  	</div><% //se cierra wrapper1 %>
  	
</body>
</html>