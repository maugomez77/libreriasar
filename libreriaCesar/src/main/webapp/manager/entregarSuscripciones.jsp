<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR"%>
<%@ page import="mx.com.libreria.manager.InventarioMGR"%>
<%@ page import="mx.com.libreria.manager.SuscripcionMGR"%>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.Cliente"%>
<%@ page import="mx.com.libreria.model.Suscripcion"%>
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
      					    SuscripcionMGR suscripcionMGR = (SuscripcionMGR) ObjectFactory.getBean("suscripcionMGR");
      					    InventarioMGR invMGR = (InventarioMGR) ObjectFactory.getBean("inventarioMGR");
      					    
      					   	String start = request.getParameter("start");
      					   	if (Utilerias.isNullOrUndefined(start)) { 
      						   start = "";
      					   	}
      					   	
      					   	String end = request.getParameter("end");
      					   	if (Utilerias.isNullOrUndefined(end)) { 
      						   end = "";
      					   	}
      					   	
      					   	Suscripcion suscripcion = new Suscripcion();
      					    							
							String result = "";							
      					%>
      					
    					<p><h2>Entregar Suscripciones</h2></p>    					    	    													
							
							<form action="entregarSuscripciones.jsp?sessionid=<%= session.getId() %>" method="post">												
								<p>Fecha Suscripcion Inicial:
		    						<input type="text" name="start" id="start" maxlength="10" size="10" value="<%= start %>" readonly="true">
    									<a href="javascript:NewCal('start','ddmmyyyy')">
    										<img src="../images/cal.gif" width="16" height="16" border="0" alt="Selecciona una fecha">
    									</a>
    							</p>
    							
    							<p>Fecha Suscripcion Final:
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
							
							<%	
								suscripcion.setSuscripcionId(Utilerias.strToInt(request.getParameter("suscripcionId")));
								suscripcion = (Suscripcion) catalogoMGR.getObjectData(suscripcion);
								if (suscripcion.getNumeroMesesSuscripcionRestante() <= 0) { %>
									<script>alert("Ya agoto las suscripciones");</script>
								<% } else if (suscripcionMGR.validarExistenciaProductoParaSuscripcion(catalogoMGR, suscripcion, 1)) {
									int res = suscripcionMGR.agregarSalidaInventarioSistema(catalogoMGR, invMGR, suscripcion);
									if (res > 0) { %>
										<script>alert("Se ha actualizado la suscripcion");</script>
									<% } else { %>
										<script>alert("Hubo problemas al actualizar la suscripcion");</script>	 
									<% } %>
								<% } else { %>
									<script>alert("Favor de checar existencias sobre este producto");</script>	
								<% } %>
							
							<% } %> 
							
							
							<% if (!Utilerias.isNullOrUndefined(request.getParameter("Cancelar"))) { %>
								<%
									suscripcion.setSuscripcionId(Utilerias.strToInt(request.getParameter("suscripcionId")));
									result = catalogoMGR.desactivarNegocios(Utilerias.SUSCRIPCION_VALUE, suscripcion.getSuscripcionId());
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
	    								<td>Ejemplares Restantes</td>
	    								<td>Producto</td>
	    								<td>Entregas</td>
	    								<td>Monto Suscripci&oacute;n</td>
	    								<td></td>
	    								<td></td>				
	    							</tr>
	    							
	    							<%
	    								List<Suscripcion> lista = suscripcionMGR.getSuscripcionesValidasPorFecha(catalogoMGR, start, end);
	    								for (int i=0; i < lista.size(); i++) { 
	    									Suscripcion elemento = lista.get(i); %>
	    									<tr>
		    									<td><%= elemento.getSuscripcionId() %></td>
		    									<td><%= Utilerias.getDate(elemento.getFechaInicialSuscripcion(), Utilerias.FORMAT_DATE) %></td>
		    									<td><%= elemento.getNumeroMesesSuscripcionRestante() %></td>
		    									<td><%= elemento.getProducto().getNombreProducto() %></td>
		    									<td>
		    										<% 
		    											Set<InventarioActual> listInv = elemento.getListaInventario();	
		    											Iterator<InventarioActual> it = listInv.iterator();
		    											while (it.hasNext()) {
		    												InventarioActual ele = it.next();
		    												out.println(Utilerias.getDate(ele.getFechaSalida(), Utilerias.FORMAT_DATE_WITH_HOUR) + "<br/>"); 
		    											}
		    										 %>
		    									</td>
		    									<td><%= elemento.getMontoInicial() %></td>
		    									<td>
      												<form action="entregarSuscripciones.jsp?sessionid=<%= session.getId() %>" method="post">
      													<input type="hidden" name="start" value="<%= request.getParameter("start") %>">
      													<input type="hidden" name="end" value="<%= request.getParameter("end") %>">
      													<input type="hidden" name="clienteId" value="<%= request.getParameter("clienteId") %>">
      													<input type="hidden" name="suscripcionId" value="<%= elemento.getSuscripcionId() %>">	
      						    						<input type="submit" name="Entregar" value="Entregar">
      						    					</form>
      											</td>
      											<td>
      												<form action="entregarSuscripciones.jsp?sessionid=<%= session.getId() %>" method="post">
      													<input type="hidden" name="start" value="<%= request.getParameter("start") %>">
      													<input type="hidden" name="end" value="<%= request.getParameter("end") %>">
      													<input type="hidden" name="clienteId" value="<%= request.getParameter("clienteId") %>">
      													<input type="hidden" name="suscripcionId" value="<%= elemento.getSuscripcionId() %>">	
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