<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.beans.ProductoCantidad"%>

<%@ page import="mx.com.libreria.manager.CatalogMGR"%>
<%@ page import="mx.com.libreria.manager.ProductMGR" %>
<%@ page import="mx.com.libreria.manager.RemisionesMGR"%>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.Cliente" %>
<%@ page import="mx.com.libreria.model.Empleado" %>
<%@ page import="mx.com.libreria.model.InventarioActual"%>
<%@ page import="mx.com.libreria.model.Producto" %>

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
      					   RemisionesMGR remisionMGR = (RemisionesMGR) ObjectFactory.getBean("remisionesMGR");
      					         					   
      					    InventarioActual inventario = new InventarioActual();
      					    Producto prod = new Producto();
      					        					 
      					  	if (request.getParameter("producto") != null) {
      					  		
  								prod.setProductoId(Utilerias.strToInt(request.getParameter("producto")));
  								prod = (Producto) catalogoMGR.getObjectData(prod);
  								  							
  								inventario.setProducto(prod);
      					  	}
      					  
							//inventario.setFechaEntrada(Utilerias.getDateToday(Utilerias.FORMAT_DATE));
						    inventario.setFechaSalida(Utilerias.getDateToday(Utilerias.FORMAT_DATE));
							//inventario.setCostoProducto(Utilerias.strToFloat(request.getParameter("costoProducto")));
							inventario.setPrecioProducto(Utilerias.strToFloat(request.getParameter("precioProducto")));
							inventario.setCantidad(Utilerias.strToInt(request.getParameter("cantidad")));
							//inventario.setEstatus("S");
							
							String result = "";
							boolean entraInventario = false;
      					%>
      					
    					<p><h2>Administraci&oacute;n de Remisiones</h2></p>    					    	    													
							
						    <% if (request.getParameter("resultRemisionRegistrarVenta") != null) { %>
						    	<script>alert("<%= result %>");</script>
						    <% } %>
						    
							<% if (request.getParameter("agregar") != null) { 
		  						
		  						result = catalogoMGR.validarCamposLlenos(inventario);
		  						
		  						//validacion de cantidades para sacar de inventario
		  						if (result.equals("")) { 
		  							result = remisionMGR.validarUnProductoAgregarARemisionSesionActual(inventario, catalogoMGR);
		  						}
		  						
		  						if (result.equals("")) {
		  							result = remisionMGR.validarUnProductoAgregarAREmisionChecandoListaSesionActual(request, inventario, catalogoMGR);
		  						}
		  						
		  						if (result.equals("")) {
		  							remisionMGR.agregarRemisionSesion(request, response, inventario);
		  							result = "Inventario agregado a la sesi&oacute;n satisfactoriamente";
		  							entraInventario = true;		  						    
		  						}
		  						
		  						if (!entraInventario) { %>
		  							<script>alert("<%= result %>");</script>
		  						<% } %>
		  					
		  					<% } %>
		  					
		  					<% if (request.getParameter("eliminar") != null) { 		  							  						
		  						remisionMGR.eliminarUnProductoDeRemisionActualSesion(request, Utilerias.strToInt(request.getParameter("indice")));
		  					} %>
		  					
		  					<% if (remisionMGR.getRemisionActualSesion(request) != null) { %> 
								
								<table border="1" class="tablas">
	    							<tr bgcolor="silver">
	    								<td>Producto</td>
	    								<td>Defectuoso</td>
    									<td>Fecha Salida</td>
    									<td>Precio</td>
    									<td>Cantidad</td>
    									<td></td>    									
    								</tr>
                        			                        
								<% 
								
								List<InventarioActual> lista = (ArrayList<InventarioActual>) remisionMGR.getRemisionActualSesion(request);
								
								for (int i=0; i < lista.size(); i++) { %> 
									<% InventarioActual elemento = lista.get(i); %>
									<tr>
										<td><%= elemento.getProducto().getNombreProducto() %></td>
										<td><%= elemento.getDefectuoso() %></td>
										<td><%= Utilerias.getDate(elemento.getFechaSalida(), Utilerias.FORMAT_DATE) %></td>
										<td><%= elemento.getPrecioProducto() %></td>
										<td><%= elemento.getCantidad() %></td>
										<td>
											<form action="remisiones.jsp?sessionid=<%= session.getId() %>" method="post">
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
		  						
		  						<form action="remisiones.jsp?sessionid=<%= session.getId() %>" method="post" name="forma">

								<table class="tablecontent">
		  							<tr>
		  								<td>
		  									Producto
		  								</td>
		  								<td>
		  									<%= prod.getProductoId() + " " + prod.getNombreProducto() %>
		  								</td>
		  							</tr>
		  								
		  							<tr>
		  								<td>
		  									Existencia del Producto en Inventarios
		  								</td>
		  								<td>
		  									<%= remisionMGR.getExistenciaInventario(prod, catalogoMGR) %>	
		  								</td>	
		  							</tr>
		  								
		  							<tr>
		  								<td>
		  									Existencia del Producto x Precios
		  								</td>
		  								<td>
		  									<table border="1" class="tablas">
	    										<tr bgcolor="silver">
	    											<td>Cantidad Existencia</td>
    												<td>Precios</td>
    												<td><%= Utilerias.IVA_DESC %></td>
    											</tr>
    											
		  										<% 
			  										List listaProd = remisionMGR.getPreciosXProducto(prod, catalogoMGR);
		  											for (int i=0; i < listaProd.size(); i++) {  
		  												Object[] elementos = (Object[]) listaProd.get(i); 
		  												if (i == 0) { //para tomar el precio más alto de los producto que trae la lista 
		  													inventario.setPrecioProducto(Utilerias.strToFloat(elementos[1].toString()));
		  												} %>
		  												<tr>
		  													<td><%= elementos[0] %></td>
		  													<td><%= elementos[1] %></td>
		  													<td><%= Utilerias.getDecimalFormat((Float)elementos[2]) %></td>
		  												</tr>		  												
		  											<% } %>	
		  									</table>			
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
		  									<input type="hidden" name="producto" value="<%= prod.getProductoId() %>">
		  						    		<input type="hidden" name="agregar" value="Y"> <!-- Buscar esta opcion en el request -->
		  									<input type="submit" name="enviar" value="Agregar Productos">
		  								</td>
		  							</tr>
		  						</table>			
		  						</form>
		  						
		  					<% } %> 
		  					
		  					<% if (remisionMGR.getRemisionActualSesion(request) != null) { %>
		  						
		  						<form action="remisionRegistrarVenta.jsp?sessionid=<%= session.getId() %>" method="post">
		  						<table class="tablecontent">
		  							<tr>
		  								<td>
		  									<input type="submit" name="registrarSistema" value="Registrar en Sistema">
		  								</td>
		  							</tr>
		  						</table>			
		  						</form>
		  						
		  					<% } %>
		  					
		  							  					
		  					<% 
		  						/**
		  						 * AQUI TIENE QUE ENTAR POR PRIMERA VEZ
		  						 */		  						
		  					%>
		  						
		  					<% if (!Utilerias.isNullOrUndefined(request.getParameter("registrarCompraCliente")) 
		  						   && !Utilerias.isNullOrUndefined(request.getParameter("clienteIdInventario"))
		  						   && !Utilerias.isNullOrUndefined(request.getParameter("empleadoId"))) { 
		  						   remisionMGR.crearIdVentaSesion(request);
		  					} %>
		  							  							  					
		  					<% if (request.getParameter("deshacer") != null) { 		  						
		  						   remisionMGR.limpiarCompraSesion(request);		  						   
		  						   response.sendRedirect(request.getContextPath() + "/manager/remisiones.jsp?sessionid=" + session.getId());		  					    
		  					 } %>
		  							  					
		  					<% if (remisionMGR.getVentaClienteSesion(request) == null) { %>
		  						
		  						<form action="remisiones.jsp?sessionid=<%= session.getId() %>" method="post">
		  						<table class="tablecontent">
		  							<tr>
		  								<td>
		  									Cliente:
		  								</td>
		  								<td>
		  									<select name="clienteIdInventario">
  							    				<% 
  							    				Cliente cli = new Cliente();
  							  					ArrayList<Cliente> listaCli = (ArrayList<Cliente>) catalogoMGR.getList(cli);
												for (int i=0; i < listaCli.size(); i++) { 
													cli = (Cliente) listaCli.get(i); %>
      												<option value="<%= cli.getClienteId() %>"><%= cli.getNombreCliente() %></option>
    							        		<% } %>
    										</select>    										
    									</td>
    								</tr>
    								<tr>	
    									<td>Empleado:</td>
    									<td>
    										<select name="empleadoId">
    											<% 
  							    				Empleado e = new Empleado();
  							  					ArrayList<Empleado> list = (ArrayList<Empleado>) catalogoMGR.getList(e);
												for (int i=0; i < list.size(); i++) { 
													e = (Empleado) list.get(i); %>
      												<option value="<%= e.getEmpleadoId() %>"><%= e.getNombre() %></option>
    							        		<% } %>
    										</select>
    									</td>
    								</tr>
    								<tr>
    									<td>
    										<input type="submit" name="registrarCompraCliente" value="Seleccionar Productos">
    									</td>
    								</tr>				  							
    							</table>
		  						</form>
		  					
		  					<% } %>
		  					
							<% if (remisionMGR.getVentaClienteSesion(request) != null) { %>  					
		  						
		  						<form action="remisiones.jsp?sessionid=<%= session.getId() %>" method="post">
		  						<table class="tablecontent">
		  							<tr>
		  								<td>
		  									<input type="submit" name="deshacer" value="Deshacer Venta">
		  								</td>
		  							</tr>
		  						</table>					  								  						
		  						</form>
		  								  						
		  						<form action="remisiones.jsp?sessionid=<%= session.getId() %>" method="post" name="forma" id="forma">
		  						<table class="tablecontent">
		  							<tr>
		  								<td>
		  									Selecciona un producto
		  								</td>
		  								<td>
		  									<div id="producto" ></div>
		  								</td>		  										  							
		  							</tr>
		  						</table>
		  							
		  						<%  
		  							List<ProductoCantidad> listaProductos = productMGR.getAllNameArticulosFromInventory();
		  		  				%>
		  			  	    								  			
				  					<script>
				  				
				  				    	//el primer nombre esta en el div, el segundo es como se recibe en el request
				  				    	//por convencion se dejan los mismos en ambos.			  				   
			      	  					var combo2 = new dhtmlXCombo("producto","producto",200);
										combo2.enableFilteringMode(true);
							
										<% 			
											for (int i=0; i < listaProductos.size(); i++) {
												ProductoCantidad elemento  = (ProductoCantidad) listaProductos.get(i);
												out.println("combo2.addOption(" + elemento.getProducto().getProductoId() + ", '" + elemento.getProducto().getNombreProducto() + " " + elemento.getCantidad() + "');");	
											}				 			
										%>			
									
										combo2.attachEvent("onKeyPressed", 							
											function(keyCode){ 
												if (keyCode != 13) { 
													return;
												}
												
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