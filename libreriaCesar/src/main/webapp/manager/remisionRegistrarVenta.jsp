<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR"%>
<%@ page import="mx.com.libreria.manager.InventarioMGR"%>
<%@ page import="mx.com.libreria.manager.RemisionesMGR"%>
<%@ page import="mx.com.libreria.manager.ProductMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.Cliente" %>
<%@ page import="mx.com.libreria.model.InventarioActual"%>
<%@ page import="mx.com.libreria.model.Producto" %>
<%@ page import="mx.com.libreria.model.Remision"%>

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
      					   RemisionesMGR remisionMGR = (RemisionesMGR) ObjectFactory.getBean("remisionesMGR");
      					   InventarioMGR invMGR = (InventarioMGR) ObjectFactory.getBean("inventarioMGR");
      					   
      					   Remision remision = null;
						
						   List<InventarioActual> lista = (ArrayList<InventarioActual>) remisionMGR.getRemisionActualSesion(request);
						   
						   if (remisionMGR.getObjetoVentaSesion(request) == null) { 
							    remision = new Remision();	   
						       		
							    //remision.setComprasId();
						       	remision.setFechaRemision(Utilerias.getDateToday(Utilerias.FORMAT_DATE));						       	
						       		
						       	InventarioActual inventarioTemporal = new InventarioActual();
						       	remision.setListaInventario((Set<InventarioActual>) Utilerias.convertListToSet(lista, inventarioTemporal));
						       
						       	Cliente cli = new Cliente();
						       	cli.setClienteId(Utilerias.strToInt(remisionMGR.getVentaClienteSesion(request).toString()));
						       	cli = (Cliente) catalogMGR.getObjectData(cli);						       
						       	remision.setCliente(cli);
						       
						       	remision.setMontoFactura(remisionMGR.getMontoFactura(lista));						       		
						       	//remision.setEstatusPagoCredito("C"); //opcion
						       	//remision.setPorPagar(remision.getMontoFactura()); //si es credito, se tiene que pagar totalmente la factura.
						       	//remision.setMontoAbonado(0);
						       
						       	remisionMGR.crearObjectoVentaSesion(request, remision);
						       	
						   } else { 
							   remision = (Remision) remisionMGR.getObjetoVentaSesion(request);
						   }
						   
						   remision.setEstatusPagoCredito(request.getParameter("estatusPagoCredito"));
						   remision.setMontoInicial(Utilerias.strToFloat(request.getParameter("montoInicial")));
						   remision.setActivo(Utilerias.YES_VALUE);
						   
						   String result = remisionMGR.validaMontoAbonadoConFacturado(remision, false, "0");
						   
						   if (!result.equals("")) { %>						      
						   
						    <% 
						        /*** REALIZAR VALIDACION SI EL INVENTARIO NO SE HA MOVIDO ***/ 						    
						    	result = remisionMGR.checarInventarioSesionSinMovimientos(request, catalogMGR);
						    	if (!result.equals("")) { 
						    		response.sendRedirect(request.getContextPath() 
						    			+ "/manager/remisiones.jsp?sessionid=" + session.getId() + "&resultRemisionRegistrarVenta=" + result);
						    	}
						    %>
							<form action="remisionRegistrarVenta.jsp?sessionid=<%= session.getId() %>" method="post">
		  					<p>Venta Realizada el dia <%= Utilerias.getDate(remision.getFechaRemision(), Utilerias.FORMAT_DATE) %> por el cliente <%= remision.getCliente().getNombreCliente() %></p>
		  					<p>Importe de la Remisi&oacute;n: <%= remision.getMontoFactura() %></p>
		  					<p>Forma de Pago: 
		  						<select id="estatusPagoCredito" name="estatusPagoCredito" onchange="desplegarCantidad();">
		  							<option value="P">Contado</option>
		  							<% if (remision.getMontoFactura() > 0) { %>
		  								<option value="C">Cr&eacute;dito</option>
		  							<% } %>	    							   
    							</select>  									
		  					</p>
		  					
		  					<div id="montoAbonadoDiv">				
		  						<p>Monto Inicial:
		  							<input type="text" name="montoInicial" value="<%= Utilerias.mostrarCamposFront(remision.getMontoInicial()) %>"> 
		  						</p>		  							
		  					</div>
		  					
		  					<script type="text/javascript">
		  						function desplegarCantidad() { 
		  							var mylist=document.getElementById("estatusPagoCredito");
		  							if (mylist.selectedIndex == 1) { 
		  								//alert("Credito");
		  								document.getElementById("montoAbonadoDiv").style.visibility = "visible";		  												  										
		  							} else if (mylist.selectedIndex  == 0){
		  								//alert("Contado");		  										
		  								document.getElementById("montoAbonadoDiv").style.visibility = "hidden";
		  							}		  									
		  						}			  								
		  						desplegarCantidad();		  								
		  					</script>
		  							
		  					<p>
		  					  	<input type="hidden" name="registrarCompraFinal" value="Y">
		  						<input type="submit" name="enviar" value="Registrar Venta Final">
		  					</p>
		  					
		  					<% if (request.getParameter("registrarSistema") == null) { %>
		  						<script type="text/javascript">alert("<%= result %>");</script>
		  					<% } %>
		  				</form>
		  				 
						<% } %> 
													
						<% if (request.getParameter("registrarCompraFinal") != null && result.equals("")) { 								
							   result = "";
							   int opera = remisionMGR.agregarSalidaInventarioSistema(request, response, catalogMGR, invMGR);
							   if (opera == -1) { 
								   result = "Hubo problemas al realizar la operacion, intente de nuevo."; %>
								   <p><%= Utilerias.imprimePaginaHTML(result) %></p>
							   <% } else {
								   remision.setRemisionId(opera);
								   remision = (Remision) catalogMGR.getObjectData(remision);
								   result = "Se guardo exitosamente la venta del producto con los tipos de pago.";%>
								   <p>Se genero exitosamente la venta <%= remision.getRemisionId() %> con fecha <%= Utilerias.getDate(remision.getFechaRemision(), Utilerias.FORMAT_DATE) %></p>
								   
								   <% response.sendRedirect(request.getContextPath() + "/reportes/entradaRemisionReportes.jsp?sessionid=" + session.getId() + "&remisionId=" + remision.getRemisionId()); %>
								   								   
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