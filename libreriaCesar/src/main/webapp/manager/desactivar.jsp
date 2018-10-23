<%@page import="mx.com.libreria.model.Gastos"%>
<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR"%>
<%@ page import="mx.com.libreria.manager.InventarioMGR"%>
<%@ page import="mx.com.libreria.manager.RemisionesMGR"%>
<%@ page import="mx.com.libreria.manager.SuscripcionMGR"%>
<%@ page import="mx.com.libreria.manager.VentaEspecialMGR" %>

<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.Abono"%>
<%@ page import="mx.com.libreria.model.Cliente"%>
<%@ page import="mx.com.libreria.model.Compras"%>
<%@ page import="mx.com.libreria.model.InventarioActual"%>
<%@ page import="mx.com.libreria.model.Remision"%>
<%@ page import="mx.com.libreria.model.Suscripcion"%>
<%@ page import="mx.com.libreria.model.VentaEspecial"%>


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
      				
      					<%  CatalogMGR catalogoMGR = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");
      						String result = "";							
      					%>
      					
    					<p><h2>Desactivar Compras / Ventas / Suscripciones / Ventas Especiales / Gastos</h2></p>    					    	    													
							
							<form action="desactivar.jsp?sessionid=<%= session.getId() %>" method="post">
								<p>Tipo: 
									<select name="tipo">
										<option value="<%= Utilerias.COMPRAS_VALUE %>" <% if (!Utilerias.isNullOrUndefined(request.getParameter("tipo")) && request.getParameter("tipo").equals(Utilerias.COMPRAS_VALUE)) out.println("selected");%>><%= Utilerias.COMPRAS_DESC %></option>
										<option value="<%= Utilerias.VENTAS_VALUE %>" <% if (!Utilerias.isNullOrUndefined(request.getParameter("tipo")) && request.getParameter("tipo").equals(Utilerias.VENTAS_VALUE)) out.println("selected");%>><%= Utilerias.VENTAS_DESC %></option>
										<option value="<%= Utilerias.SUSCRIPCION_VALUE %>" <% if (!Utilerias.isNullOrUndefined(request.getParameter("tipo")) && request.getParameter("tipo").equals(Utilerias.SUSCRIPCION_VALUE)) out.println("selected"); %>><%= Utilerias.SUSCRIPCION_DESC %></option>
										<option value="<%= Utilerias.VENTAS_ESPECIALES_VALUE %>" <% if (!Utilerias.isNullOrUndefined(request.getParameter("tipo")) && request.getParameter("tipo").equals(Utilerias.VENTAS_ESPECIALES_VALUE)) out.println("selected"); %>><%= Utilerias.VENTAS_ESPECIALES_DESC %></option>
										<option value="<%= Utilerias.GASTO_VALUE %>" <% if (!Utilerias.isNullOrUndefined(request.getParameter("tipo")) && request.getParameter("tipo").equals(Utilerias.GASTO_VALUE)) out.println("selected"); %>><%= Utilerias.GASTO_DESC %></option>
									</select>
								</p>												
								<p>Folio:
									<input type="text" name="campo" value="<%= Utilerias.mostrarCamposFront(request.getParameter("campo")) %>">
									<input type="submit" name="enviar" value="Enviar">
								</p>
							</form>
							
							<% if (!Utilerias.isNullOrUndefined(request.getParameter("desactivar"))
								   && !Utilerias.isNullOrUndefined(request.getParameter("tipo"))
								   && !Utilerias.isNullOrUndefined(request.getParameter("campo"))) { %> 
								
								<%
									result = catalogoMGR.desactivarNegocios(request.getParameter("tipo"), Utilerias.strToInt(request.getParameter("campo")));
								%>
								<script type="text/javascript">
									alert("<%= result %>");									
								</script>								
							<% } %> 
							
														
							<% if (!Utilerias.isNullOrUndefined(request.getParameter("tipo")) && !Utilerias.isNullOrUndefined("campo")) { %>
							
							    <table border="1" class="tablas">
		    						<tr bgcolor="silver">
		    							<td>Folio</td>
		    							<td>Activo</td>
		    							<td>Fecha</td>
		    							<td>Detalle Inv</td>
		    							<td>Monto Inicial</td>
		    							<td>Monto Abonos</td>
		    							<td>Monto por Pagar</td>
		    							<td></td>	    											
	    							</tr>
	    							
	    							<%  
	    							    
	    							    if (request.getParameter("tipo").equals(Utilerias.COMPRAS_VALUE)) { 
	    									
	    									Compras com = new Compras();
	    									com.setComprasId(Utilerias.strToInt(request.getParameter("campo")));
	    									com = (Compras) catalogoMGR.getObjectData(com);
	    									if (com != null) { %>
	    										<tr>
	    											<td><%= com.getComprasId() %></td>
			    									<td><%= Utilerias.getMessageValue(Utilerias.YES_NO_KEY, com.getActivo()) %></td>
			    									<td><%= Utilerias.getDate(com.getFechaCompra(), Utilerias.FORMAT_DATE) %></td>
											   		<td>
												   		<%
												   			List<InventarioActual> listInv = catalogoMGR.getInventarioAgrupadoCantidad(Utilerias.COMPRAS_VALUE, com.getComprasId());
												   			for (int j=0; j < listInv.size(); j++) { 
												   				InventarioActual act = listInv.get(j);
												   				out.println(act.getProducto().getProductoId() + " " + act.getProducto().getNombreProducto() + " " + act.getCantidad() + "<br/>");
												   			}
												   		%>
											   		</td>

			    									<td><%= com.getMontoInicial() %></td>
			    									<td>
			    										<%
			    											float montoAbono = 0;
			    											if (com.getListaAbonos() != null) {
			    												Iterator<Abono> it2 = com.getListaAbonos().iterator();
			    												while (it2.hasNext()) {
			    													Abono abono = it2.next();
			    													montoAbono += abono.getMonto(); 
			    												}	 			    												
			    											}
			    											
			    											out.println(Utilerias.getCurrencyFormat(montoAbono));
			    										 %>
			    									</td>
			    									<td>
			    										<%= com.getPorPagar() %>
			    									</td>
			    									<td>
	      												<form action="desactivar.jsp?sessionid=<%= session.getId() %>" method="post">
	      													<input type="hidden" name="campo" value=<%= com.getComprasId() %>>
	      													<input type="hidden" name="tipo" value="<%= request.getParameter("tipo") %>">
	      													<input type="submit" name="desactivar" value="Desactivar">
	      						    					</form>
	      											</td>
	    										</tr>
	    									<% } // para el caso de la compra no sea nulla 
	    									%> 
	    								<% } else if (request.getParameter("tipo").equals(Utilerias.VENTAS_VALUE)) { 
	    								
	    									Remision rem = new Remision();
	    									rem.setRemisionId(Utilerias.strToInt(request.getParameter("campo")));
	    									rem = (Remision) catalogoMGR.getObjectData(rem);
	    									if (rem != null) { %>
	    										<tr>
	    											<td><%= rem.getRemisionId() %></td>
			    									<td><%= Utilerias.getMessageValue(Utilerias.YES_NO_KEY, rem.getActivo()) %></td>
			    									<td><%= Utilerias.getDate(rem.getFechaRemision(), Utilerias.FORMAT_DATE) %></td>
											   		<td>
												   		<%
												   			List<InventarioActual> listInv = catalogoMGR.getInventarioAgrupadoCantidad(Utilerias.VENTAS_VALUE, rem.getRemisionId());
												   			for (int j=0; j < listInv.size(); j++) { 
												   				InventarioActual act = listInv.get(j);
												   				out.println(act.getProducto().getProductoId() + " " + act.getProducto().getNombreProducto() + " " + act.getCantidad() + "<br/>");
												   			}
												   		%>
											   		</td>
			    									<td><%= rem.getMontoInicial() %></td>
			    									<td>
			    										<%
			    											float montoAbono = 0;
			    											if (rem.getListaAbonos() != null) {
			    												Iterator<Abono> it2 = rem.getListaAbonos().iterator();
			    												while (it2.hasNext()) {
			    													Abono abono = it2.next();
			    													montoAbono += abono.getMonto(); 
			    												}	 			    												
			    											}
			    											
			    											out.println(Utilerias.getCurrencyFormat(montoAbono));
			    										 %>
			    									</td>
			    									<td>
			    										<%= rem.getPorPagar() %>
			    									</td>
			    									<td>
	      												<form action="desactivar.jsp?sessionid=<%= session.getId() %>" method="post">
	      													<input type="hidden" name="campo" value=<%= rem.getRemisionId() %>>
	      													<input type="hidden" name="tipo" value="<%= request.getParameter("tipo") %>">
	      													<input type="submit" name="desactivar" value="Desactivar">
	      						    					</form>
	      											</td>
	    										</tr>
	    									<% } // para el caso de la venta no se nulla
	    									
	    								} else if (request.getParameter("tipo").equals(Utilerias.SUSCRIPCION_VALUE)) { 
	    								
	    									Suscripcion sus = new Suscripcion();
	    									sus.setSuscripcionId(Utilerias.strToInt(request.getParameter("campo")));
		    								sus = (Suscripcion) catalogoMGR.getObjectData(sus);
		    								if (sus != null) { %> 
		    									<tr>
			    									<td><%= sus.getSuscripcionId() %></td>
			    									<td><%= Utilerias.getMessageValue(Utilerias.YES_NO_KEY, sus.getActivo()) %></td>
			    									<td><%= Utilerias.getDate(sus.getFechaInicialSuscripcion(), Utilerias.FORMAT_DATE) %></td>
											   		<td>
												   		<%
												   			List<InventarioActual> listInv = catalogoMGR.getInventarioAgrupadoCantidad(Utilerias.SUSCRIPCION_VALUE, sus.getSuscripcionId());
												   			for (int j=0; j < listInv.size(); j++) { 
												   				InventarioActual act = listInv.get(j);
												   				out.println(act.getProducto().getProductoId() + " " + act.getProducto().getNombreProducto() + " " + act.getCantidad() + "<br/>");
												   			}
												   		%>
											   		</td>
			    									<td><%= sus.getMontoInicial() %></td>
			    									<td>
			    										<%
			    											float montoAbono = 0;
			    											if (sus.getListaAbonos() != null) {
			    												Iterator<Abono> it2 = sus.getListaAbonos().iterator();
			    												while (it2.hasNext()) {
			    													Abono abono = it2.next();
			    													montoAbono += abono.getMonto(); 
			    												}	 			    												
			    											}
			    											
			    											out.println(Utilerias.getCurrencyFormat(montoAbono));
			    										 %>
			    									</td>
			    									<td>
			    										<%= sus.getPorPagar() %>
			    									</td>
			    									<td>
	      												<form action="desactivar.jsp?sessionid=<%= session.getId() %>" method="post">
	      													<input type="hidden" name="campo" value=<%= sus.getSuscripcionId() %>>
	      													<input type="hidden" name="tipo" value="<%= request.getParameter("tipo") %>">
	      													<input type="submit" name="desactivar" value="Desactivar">
	      						    					</form>
	      											</td>	      											
	      										</tr>	
		    								<% } //para el caso de la suscripcion no sea nulla
		    							} else if (request.getParameter("tipo").equals(Utilerias.VENTAS_ESPECIALES_VALUE)) { //para checar el tipo de elemento
		    								VentaEspecial ve = new VentaEspecial();
	    									ve.setVentaEspecialId(Utilerias.strToInt(request.getParameter("campo")));
		    								ve = (VentaEspecial) catalogoMGR.getObjectData(ve);
		    								if (ve != null) { %> 
		    									<tr>
			    									<td><%= ve.getVentaEspecialId() %></td>
			    									<td><%= Utilerias.getMessageValue(Utilerias.YES_NO_KEY, ve.getActivo()) %></td>
			    									<td><%= Utilerias.getDate(ve.getFechaFinalVentaEspecial(), Utilerias.FORMAT_DATE) %></td>
											   		<td>
												   		<%
												   			List<InventarioActual> listInv = catalogoMGR.getInventarioAgrupadoCantidad(Utilerias.VENTAS_ESPECIALES_VALUE, ve.getVentaEspecialId());
												   			for (int j=0; j < listInv.size(); j++) { 
												   				InventarioActual act = listInv.get(j);
												   				out.println(act.getProducto().getProductoId() + " " + act.getProducto().getNombreProducto() + " " + act.getCantidad() + "<br/>");
												   			}
												   		%>
											   		</td>
			    									<td><%= ve.getMontoInicial() %></td>
			    									<td>
			    										<%
			    											float montoAbono = 0;
			    											if (ve.getListaAbonos() != null) {
			    												Iterator<Abono> it2 = ve.getListaAbonos().iterator();
			    												while (it2.hasNext()) {
			    													Abono abono = it2.next();
			    													montoAbono += abono.getMonto(); 
			    												}	 			    												
			    											}
			    											
			    											out.println(Utilerias.getCurrencyFormat(montoAbono));
			    										 %>
			    									</td>
			    									<td>
			    										<%= ve.getPorPagar() %>
			    									</td>
			    									<td>
	      												<form action="desactivar.jsp?sessionid=<%= session.getId() %>" method="post">
	      													<input type="hidden" name="campo" value=<%= ve.getVentaEspecialId() %>>
	      													<input type="hidden" name="tipo" value="<%= request.getParameter("tipo") %>">
	      													<input type="submit" name="desactivar" value="Desactivar">
	      						    					</form>
	      											</td>	      											
	      										</tr>	
		    								<% } //para el caso de la venta especial no sea nulla		    						
		    							} else if (request.getParameter("tipo").equals(Utilerias.GASTO_VALUE)) { //para checar el tipo de elemento
		    								Gastos ve = new Gastos();
	    									ve.setGastosId(Utilerias.strToInt(request.getParameter("campo")));
		    								ve = (Gastos) catalogoMGR.getObjectData(ve);
		    								if (ve != null) { %> 
		    									<tr>
			    									<td><%= ve.getGastosId() %></td>
			    									<td><%= Utilerias.getMessageValue(Utilerias.YES_NO_KEY, ve.getActivo()) %></td>
			    									<td><%= Utilerias.getDate(ve.getFechaAplicacion(), Utilerias.FORMAT_DATE) %></td>
											   		<td>
											   			<!-- detalle productos vacio para el gasto -->												   		
											   		</td>
			    									<td><%= ve.getMontoGasto() %></td>
			    									<td>
			    										<!-- detalle abonos vacio para el gasto -->
			    									</td>
			    									<td>
			    										<!-- por pagar vacio para el gasto -->			    										
			    									</td>
			    									<td>
	      												<form action="desactivar.jsp?sessionid=<%= session.getId() %>" method="post">
	      													<input type="hidden" name="campo" value=<%= ve.getGastosId() %>>
	      													<input type="hidden" name="tipo" value="<%= request.getParameter("tipo") %>">
	      													<input type="submit" name="desactivar" value="Desactivar">
	      						    					</form>
	      											</td>	      											
	      										</tr>	
		    								<% } //para el caso del gasto no sea nulla
		    							} 
		    					%>     							                        			                        													
								</table>
							<% } //para el if que checa el tipo y campo que no vengan vacios 
							%>	
							<br/>													
								  							
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