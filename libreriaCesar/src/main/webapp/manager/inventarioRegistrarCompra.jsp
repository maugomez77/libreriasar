<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR"%>
<%@ page import="mx.com.libreria.manager.InventarioMGR"%>
<%@ page import="mx.com.libreria.manager.ProductMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.Compras"%>
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
      					   CatalogMGR catalogMGR = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");
      					   InventarioMGR inventarioMGR = (InventarioMGR) ObjectFactory.getBean("inventarioMGR");
      					   Compras compras = null;
						
						   //proviene de inventarios.
						   List<InventarioActual> lista = (ArrayList<InventarioActual>) inventarioMGR.getInventarioActualSesion(request);
						   
						   if (inventarioMGR.getObjetoCompraSesion(request) == null) { 
						        compras = new Compras();	   
						       		
						       	//compras.setComprasId();
						       	compras.setFechaCompra(Utilerias.getDateToday(Utilerias.FORMAT_DATE));
						       		
						       	InventarioActual inventarioTemporal = new InventarioActual();						       		
						       	compras.setListaInventario((Set<InventarioActual>) Utilerias.convertListToSet(lista, inventarioTemporal));
						       
						       	Proveedor prov = new Proveedor();
						       	prov.setProveedorId(Utilerias.strToInt(inventarioMGR.getCompraProveedorSesion(request).toString()));
						       	prov = (Proveedor) catalogMGR.getObjectData(prov);						       
						       	compras.setProveedor(prov);
						       
						       	compras.setMontoFactura(inventarioMGR.getMontoFactura(lista));						       		
						       	//compras.setEstatusPagoCredito("C"); //opcion
						       	//compras.setPorPagar(compras.getMontoFactura()); //si es credito, se tiene que pagar totalmente la factura.
						       	//compras.setMontoAbonado(0);
						       
						       	inventarioMGR.crearObjectoCompraSesion(request, compras);
						   } else { 
							   compras = (Compras) inventarioMGR.getObjetoCompraSesion(request);
						   }
						   
						   compras.setEstatusPagoCredito(request.getParameter("estatusPagoCredito"));
						   compras.setMontoInicial(Utilerias.strToFloat(request.getParameter("montoInicial")));
						   compras.setActivo(Utilerias.YES_VALUE);
						   
						   String result = inventarioMGR.validaMontoAbonadoConFacturado(compras, false, "0");
						   
						   if (!result.equals("")) { %>						      
							<form action="inventarioRegistrarCompra.jsp?sessionid=<%= session.getId() %>" method="post">
		  					<p>Compra Realizada el dia <%= Utilerias.getDate(compras.getFechaCompra(), Utilerias.FORMAT_DATE) %> al proveedor <%= compras.getProveedor().getNombreProveedor() %></p>
		  					<p>Costo total de la Factura: <%= compras.getMontoFactura() %></p>
		  					<p>Forma de Pago: 
		  						<select id="estatusPagoCredito" name="estatusPagoCredito" onchange="desplegarCantidad();">
		  							<option value="P">Contado</option>
  							    	<option value="C">Cr&eacute;dito</option>
  							    	<% if (session.getAttribute("rolId").toString().equals(Utilerias.ROL_ADMON)) {  %>
  							    		<option value="I">Carga de Inventario</option>
  							    	<% } %>	 
    							</select>  									
		  					</p>
		  					
		  					<div id="montoAbonadoDiv">				
		  						<p>Monto Inicial:
		  							<input type="text" name="montoInicial" value="<%= Utilerias.mostrarCamposFront(compras.getMontoInicial()) %>"> 
		  						</p>		  							
		  					</div>
		  					
		  					<script type="text/javascript">
		  						function desplegarCantidad() { 
		  							var mylist=document.getElementById("estatusPagoCredito");
		  							//alert(mylist.selectedIndex);
		  							if (mylist.selectedIndex == 1) { 
		  								//alert("Credito");
		  								document.getElementById("montoAbonadoDiv").style.visibility = "visible";		  												  										
		  							} else if (mylist.selectedIndex == 0 || mylist.selectedIndex == 2){
		  								//alert("Contado o Carga Inventario");		  										
		  								document.getElementById("montoAbonadoDiv").style.visibility = "hidden";		  								
		  							}		  									
		  						}			  								
		  						desplegarCantidad();		  								
		  					</script>
		  							
		  					<p>
		  					  	<input type="hidden" name="registrarCompraFinal" value="Y">
		  						<input type="submit" name="enviar" value="Registrar Compra Final">
		  					</p>
		  					
		  					<% if (request.getParameter("inventario") == null) { %>
		  						<script type="text/javascript">alert("<%= result %>");</script>
		  					<% } %>
		  				</form>
		  				 
						<% } %> 
													
						<% if (request.getParameter("registrarCompraFinal") != null && result.equals("")) { 								
							   result = "";							   
							   
							   //actualizar campos
							   inventarioMGR.crearObjectoCompraSesion(request, compras);
							   
							   int opera = inventarioMGR.agregarInventarioSistema(request, response, catalogMGR);
							   if (opera == -1) { 
								   result = "Hubo problemas al realizar la operacion"; %>
								   <p>Hubo problemas al realizar la operacion, intente de nuevo.</p>
							   <% } else { 
								   result = "Se guardo exitosamente el inventario con los tipos de pago.";%>
								   <p>Se genero exitosamente la compra <%= compras.getComprasId() %> con fecha <%= Utilerias.getDate(compras.getFechaCompra(), Utilerias.FORMAT_DATE) %></p>								   
								   <% response.sendRedirect(request.getContextPath() + "/reportes/entradaInventarioReportes.jsp?sessionid=" + session.getId() + "&comprasId=" + opera ); %>
							   <% } %>							   							   
							   <script type="text/javascript">alert("<%= result %>");</script>
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