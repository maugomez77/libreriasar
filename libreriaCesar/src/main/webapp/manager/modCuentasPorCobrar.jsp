<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR"%>
<%@ page import="mx.com.libreria.manager.InventarioMGR"%>
<%@ page import="mx.com.libreria.manager.RemisionesMGR"%>
<%@ page import="mx.com.libreria.manager.ProductMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.Abono" %>
<%@ page import="mx.com.libreria.model.Cliente" %>
<%@ page import="mx.com.libreria.model.InventarioActual"%>
<%@ page import="mx.com.libreria.model.Producto" %>
<%@ page import="mx.com.libreria.model.Cliente" %>
<%@ page import="mx.com.libreria.model.Remision" %>

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
      					   InventarioMGR inventarioMGR = (InventarioMGR) ObjectFactory.getBean("inventarioMGR");
      					         					   
      					    InventarioActual inventario = new InventarioActual();
      					    
      					    String readonly = "";
							String result = "";
	
							Remision prod = new Remision();
						
							//entrara en modificar la primera vez y actualizara estos campos, 
							//posteriormente los que mande la misma forma.
							prod.setRemisionId(Utilerias.strToInt(request.getParameter("remisionId")));
							prod.setMontoInicial(Utilerias.strToFloat(request.getParameter("montoInicial")));
							prod.setMontoFactura(Utilerias.strToFloat(request.getParameter("montoFactura")));
							//prod.setEstatusPagoCredito(request.getParameter("estatusPagoCredito"));							
							
							boolean act = Utilerias.strToBoolean(request.getParameter("act"));
						    		    		    		    		    		
            				if (request.getParameter( "Modificar" ) != null ) {
                         				
                				if (prod.getRemisionId() < 0) { %>
                    				<script>alert("Necesitas llenar el campo de clave de venta");</script>
                				<% } else {             
             	     				readonly = "readonly";     		             
		             				act = true;
		             				prod = (Remision) catalogoMGR.getObjectData(prod);		             			
	             				}
             				
            				} else if ( request.getParameter( "Actualizar" ) != null ) {
               
                				readonly = "readonly";
                				prod = (Remision) catalogoMGR.getObjectData(prod);
                				
                			    remisionMGR.agregarAbonoListaVentas(prod, Utilerias.strToFloat(request.getParameter("abono")));
                			    result = remisionMGR.validaMontoAbonadoConFacturado(prod, true, request.getParameter("abono"));                				
    			 			    
                 				if (result.equals("")) {                				                					
                					int opera = remisionMGR.actualizarVentaInventario(prod, catalogoMGR);                					
                 					result = "Remisiones actualizadas satisfactoriamente";                 					
                 				} else {
                 					prod = (Remision) catalogoMGR.getObjectData(prod);
                 					result = Utilerias.DATOS_FALTANTES + result;
                 					act = true;
                 				} %>
                                  
                 				<script>alert("<%= result %>");</script>
       
         					<% } %>
    		    			    		
		    					    			
		    				<p><h2>Administraci&oacute;n de Cuentas Por Cobrar</h2></p>
		    
		    				<form action="modCuentasPorCobrar.jsp?sessionid=<%= session.getId() %>" method="post">
		    				
		    				<p>Remisi&oacute;n:
		    					<input type="text" name="remisionId" value="<%= Utilerias.mostrarCamposFront(prod.getRemisionId()) %>" <%= readonly %>>
		    				</p>
		    				
		    				<p>Articulos Vendidos en esta orden</p>
		    				<table border="1" class="tablas">
	    						<tr bgcolor="silver">
    								<td>Clave</td>
    								<td>Producto</td>
    								<td>Fecha de Venta</td>
    								<td>Cantidad</td>
    								<td>Precio</td>
    								<td><%= Utilerias.IVA_DESC %></td>
    								<td>Total</td>
    							</tr>
                        	    
                        	        <%
                        	    	List<InventarioActual> listInv = remisionMGR.getInventarioXOrdenVenta(prod, catalogoMGR);
                        	    	for (int i=0; i < listInv.size(); i++) { 
                        	    		InventarioActual ele = listInv.get(i); %>
                        	    		<tr>
	                        	    		<td><%= ele.getInventarioId() %></td>
	                        	    		<td><%= ele.getProducto().getNombreProducto() %></td>
	                        	    		<td><%= Utilerias.getDate(ele.getFechaSalida(), Utilerias.FORMAT_DATE) %></td>
	                        	    		<td><%= ele.getCantidad() %></td>
	                        	    		<td><%= Utilerias.getCurrencyFormat(ele.getPrecioProducto()) %></td>
	                        	    		<td><%= Utilerias.getCurrencyFormat(ele.getIva()) %></td>
	                        	    		<td><%= Utilerias.getCurrencyFormat(ele.getCantidad() * ele.getPrecioProducto()) %></td>
	                        	    	</tr>	
                        	    	<% } %>                        	                            	    
		    				</table>
		    				<br/>
		    				
		    				<p>Fecha de Venta: <%= Utilerias.mostrarCamposFront(Utilerias.getDate(prod.getFechaRemision(), Utilerias.FORMAT_DATE)) %></p>
		    				
		    				<p>Forma de Pago:  <%= Utilerias.getDescripcionesBD(Utilerias.ESTATUS_PAGO_VENTA, prod.getEstatusPagoCredito()) %></p>
		  							    						    						    					    				
		  					<script type="text/javascript">
		  						function desplegarCantidad() { 
		  							var mylist=document.getElementById("estatusPagoCredito");
		  							if (mylist.selectedIndex == 0) { 
		  								//alert("Credito");
		  								document.getElementById("montoAbonadoDiv").style.visibility = "visible";		  												  										
		  							} else if (mylist.selectedIndex  == 1){
		  								//alert("Contado");		  										
		  								document.getElementById("montoAbonadoDiv").style.visibility = "hidden";
		  							}		  									
		  						}			  								
		  						desplegarCantidad();		  								
		  					</script>
		  							  						
		  					<div id="montoAbonadoDiv">				
		  						<p>Monto Inicial: <%= Utilerias.mostrarCamposFront(prod.getMontoInicial()) %></p>		  							
		  					</div>
		    				
		    				<p>Detalle de Abonos:
		    					<table border="1" class="tablas">
		    						<tr bgcolor="silver">	    							
		    							<td>Fecha</td>
		    							<td>Monto</td>
		    						</tr>
		    						
		    						<%
		    							List<Abono> listAbono = (ArrayList<Abono>) Utilerias.convertSetToList(prod.getListaAbonos(), new Abono());
		    						    float montosAbonos = 0;
		    							for (int i=0; i < listAbono.size(); i++) { 
		    								Abono elemento = listAbono.get(i); %>
		    								<tr>
		    									<td><%= Utilerias.getDate(elemento.getFechaAbono(), Utilerias.FORMAT_DATE) %></td>
		    									<td><%= Utilerias.getCurrencyFormat(elemento.getMonto()) %></td>
		    								</tr>
		    								<% montosAbonos += elemento.getMonto(); %>
		    						<% } %>		    								    						
		    					</table>
		    				</p>
		    				
		    				<p>Monto Total Abonado: <%= Utilerias.getCurrencyFormat(montosAbonos) %></p>
		    				
		    				<p>
		    					Agregar Abono: 
		    					<input type="text" name="abono" value="">		    					
		    				</p>
		    				
		    				
		    				<p>Monto de la Remisi&oacute;n: <%= Utilerias.mostrarCamposFront(prod.getMontoFactura()) %></p>
		    				
		    				<p>Monto Por Pagar: <%= Utilerias.mostrarCamposFront(prod.getPorPagar()) %></p>
		    						    						    					    						    						  							    				      			    						
						    <p align="center">		    					
		    					<% if (act) {  %>		    						
		    						<input type="hidden" name="act" value="<%= act %>">
		    						<input type="submit" name="Actualizar" value="Actualizar">
		    					<% } %>		    				    							    					    		    
							</p>        
						</form>	        	        
								    
		    			<%= Utilerias.imprimePaginaHTML(result) %>

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