<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR"%>
<%@ page import="mx.com.libreria.manager.VentaEspecialMGR"%>
<%@ page import="mx.com.libreria.manager.InventarioMGR"%>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.Cliente"%>
<%@ page import="mx.com.libreria.model.VentaEspecial"%>
<%@ page import="mx.com.libreria.model.InventarioActual"%>

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
      					    VentaEspecialMGR venMGR = (VentaEspecialMGR) ObjectFactory.getBean("ventaEspecialMGR");
      					    InventarioMGR invMGR = (InventarioMGR) ObjectFactory.getBean("inventarioMGR");
      					    
      					    VentaEspecial ven = new VentaEspecial();
      					    					
      					    String start = request.getParameter("start");
      					   	if (Utilerias.isNullOrUndefined(start)) { 
      						   start = "";
      					   	}
      					   	
      					   	String end = request.getParameter("end");
      					   	if (Utilerias.isNullOrUndefined(end)) { 
      						   end = "";
      					   	}
      					   								
							String result = "";							
      					%>
      					
    					<p><h2>Entregar / Abonar : Venta Especial</h2></p>    					    	    													
							
							<form action="abonoEntVE.jsp?sessionid=<%= session.getId() %>" method="post">
								<p>Fecha de Venta Especial Inicial:
		    						<input type="text" name="start" id="start" maxlength="10" size="10" value="<%= start %>" readonly="true">
    									<a href="javascript:NewCal('start','ddmmyyyy')">
    										<img src="../images/cal.gif" width="16" height="16" border="0" alt="Selecciona una fecha">
    									</a>
    							</p>
    								
    							<p>Fecha de Venta Especial Final:
		    						<input type="text" name="end" id="end" maxlength="10" size="10" value="<%= end %>" readonly="true">
    									<a href="javascript:NewCal('end','ddmmyyyy')">
    										<img src="../images/cal.gif" width="16" height="16" border="0" alt="Selecciona una fecha">
    									</a>
    							</p>
    																			
								<p>Cliente:
									<% 	
										Cliente cli = new Cliente();
										List<Cliente> listCliente = (ArrayList<Cliente>) catalogoMGR.getList(cli); 
									%>
									<select name="clienteId">
									<% for (int i=0; i < listCliente.size(); i++) { %>
										<option value="<%= listCliente.get(i).getClienteId()%>"><%= listCliente.get(i).getNombreCliente() %></option>
									<% } %>
									</select>
									<input type="submit" name="enviar" value="Enviar">
								</p>
							</form>
							
							<% if (!Utilerias.isNullOrUndefined(request.getParameter("Entregar"))) { %>
							
							<%	ven.setVentaEspecialId(Utilerias.strToInt(request.getParameter("ventaEspecialId")));
								ven = (VentaEspecial) catalogoMGR.getObjectData(ven);
								if (ven.getListaInventario().size() <= 0) {
									if (venMGR.validarExistenciaProductoParaVE(catalogoMGR, ven, Utilerias.strToInt(request.getParameter("cantidad" + ven.getVentaEspecialId())))) {
										int res = venMGR.agregarSalidaInventarioSistema(catalogoMGR, invMGR, ven, Utilerias.strToInt(request.getParameter("cantidad" + ven.getVentaEspecialId())));
										if (res > 0) { %>
											<script>alert("Se ha actualizado la venta especial");</script>
										<% } else { %>
											<script>alert("Hubo problemas al actualizar la venta especial");</script>	 
										<% } %>
									<% } else { %>
										<script>alert("Favor de verificar que la cantidad sea numerica y que haya existencias sobre este producto");</script>	
									<% } %>	
								<% } else { %>
									<script>alert("Ya se tiene asignado inventario para esta venta especial");</script>
								<% } %> 																		
							<% } %> 
							
							<% if (!Utilerias.isNullOrUndefined(request.getParameter("Abonar"))) { %>
							
								<%
									ven.setVentaEspecialId(Utilerias.strToInt(request.getParameter("ventaEspecialId")));
									ven = (VentaEspecial) catalogoMGR.getObjectData(ven);
									result = venMGR.abonarVentaEspecial(catalogoMGR, ven, request.getParameter("abono" + ven.getVentaEspecialId()));
								%>		
								
								<script type="text/javascript">
									alert("<%= result %>");
								</script>					
							<% } %>
							
							<% if (!Utilerias.isNullOrUndefined(request.getParameter("Cancelar"))) { %>
								
								<%
									ven.setVentaEspecialId(Utilerias.strToInt(request.getParameter("ventaEspecialId")));
									result = catalogoMGR.desactivarNegocios(Utilerias.VENTAS_ESPECIALES_VALUE, ven.getVentaEspecialId());									
								%>		
								
								<script type="text/javascript">
									alert("<%= result %>");
								</script>
							
							<% } %>
							
							<% if (!Utilerias.isNullOrUndefined(request.getParameter("clienteId")) 
								   && !start.equals(Utilerias.STRING_VACIO)
								   && !end.equals(Utilerias.STRING_VACIO)) { %>
							
							    <table border="1" class="tablas">
		    						<tr bgcolor="silver">
		    							<td>Clave</td>
	    								<td>Fecha Inicial</td>
	    								<td>Inventario</td>
	    								<td>Monto Inicial</td>
	    								<td>Saldo en Abonos</td>
	    								<td></td>
	    								<td></td>
	    								<td></td>
	    							</tr>
	    							
	    							<%
	    								List<VentaEspecial> lista = venMGR.getVentaEspecialPorFecha(catalogoMGR, start, end);
	    								for (int i=0; i < lista.size(); i++) { 
	    									VentaEspecial elemento = lista.get(i); %>
	    									<tr>
		    									<td><%= elemento.getVentaEspecialId() %></td>
		    									<td><%= Utilerias.getDate(elemento.getFechaInicialVentaEspecial(), Utilerias.FORMAT_DATE) %></td>
		    									<td>
											   		<%
											   			List<InventarioActual> listInv = catalogoMGR.getInventarioAgrupadoCantidad(Utilerias.VENTAS_ESPECIALES_VALUE, elemento.getVentaEspecialId());
											   			for (int j=0; j < listInv.size(); j++) { 
											   				InventarioActual act = listInv.get(j);
											   				out.println(act.getProducto().getProductoId() + " " + act.getProducto().getNombreProducto() + " " + act.getCantidad() + "<br/>");
											   			}
											   		%>
										   		</td>
										   		<td><%= elemento.getMontoInicial() %></td>
		    									<td><%= venMGR.getAbonosVE(catalogoMGR, elemento) %></td>
		    									<td>
		    										<% if (Utilerias.isNullOrUndefined(elemento.getListaInventario()) || elemento.getListaInventario().size() == 0) { %>
			    										<form action="abonoEntVE.jsp?sessionid=<%= session.getId() %>" method="post">
			    											<input type="hidden" name="start" value="<%= start %>">
			    											<input type="hidden" name="end" value="<%= end %>">
			    											<input type="text"   name="abono<%= elemento.getVentaEspecialId() %>" value="<%= Utilerias.mostrarCamposFront(request.getParameter("abono" + elemento.getVentaEspecialId())) %>" size="5">
			    											<input type="hidden" name="clienteId" value="<%= request.getParameter("clienteId") %>">
	      													<input type="hidden" name="ventaEspecialId" value="<%= elemento.getVentaEspecialId() %>">	
	      						    						<input type="submit" name="Abonar" value="Abonar">
	      						    					</form>
      						    					<% } else { %>
      						    						Ya se entrego el producto, por lo que no se puede abonar.
      						    					<% }  %>
		    									</td>
		    									<td>
		    										<% if (Utilerias.isNullOrUndefined(elemento.getListaInventario()) || elemento.getListaInventario().size() == 0) { %>
	      												<form action="abonoEntVE.jsp?sessionid=<%= session.getId() %>" method="post">
	      													<input type="hidden" name="start" value="<%= start %>">
			    											<input type="hidden" name="end" value="<%= end %>">
			    											<input type="text"   name="cantidad<%= elemento.getVentaEspecialId() %>" value="<%= Utilerias.mostrarCamposFront(request.getParameter("cantidad" + elemento.getVentaEspecialId())) %>" size="5" >
	      													<input type="hidden" name="clienteId" value="<%= request.getParameter("clienteId") %>">
	      													<input type="hidden" name="ventaEspecialId" value="<%= elemento.getVentaEspecialId() %>">	
	      						    						<input type="submit" name="Entregar" value="Entregar">
	      						    					</form>
      						    					<% } else { %>
      						    						Ya se entrego el producto, por lo que no se puede volver a entregar
      						    					<% }  %>
      											</td>
      											<td>
      												<form action="abonoEntVE.jsp?sessionid=<%= session.getId() %>" method="post">
      													<input type="hidden" name="start" value="<%= start %>">
		    											<input type="hidden" name="end" value="<%= end %>">
		    											<input type="hidden" name="clienteId" value="<%= request.getParameter("clienteId") %>">
      													<input type="hidden" name="ventaEspecialId" value="<%= elemento.getVentaEspecialId() %>">	
      						    						<input type="submit" name="Cancelar" value="Cancelar">
      						    					</form>
      											</td>
      										</tr>	
	    								<% } 
	    							%>    							                        			                        													
								</table>
							<% } %>	
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