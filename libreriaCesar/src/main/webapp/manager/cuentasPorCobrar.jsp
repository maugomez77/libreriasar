<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR"%>
<%@ page import="mx.com.libreria.manager.RemisionesMGR"%>
<%@ page import="mx.com.libreria.manager.ProductMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.Cliente" %>
<%@ page import="mx.com.libreria.model.InventarioActual"%>
<%@ page import="mx.com.libreria.model.Producto" %>
<%@ page import="mx.com.libreria.model.Proveedor" %>
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
      					   RemisionesMGR remisionMGR = (RemisionesMGR) ObjectFactory.getBean("remisionesMGR");
      					   CatalogMGR catalogoMGR = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");
      					         					   
      					    Producto prod = new Producto();
      					        					       					  	
							String start = Utilerias.mostrarCamposFront(request.getParameter("start"));
							String end = Utilerias.mostrarCamposFront(request.getParameter("end"));
							String idCliente = Utilerias.mostrarCamposFront(request.getParameter("clienteIdInventario"));													
							
      					%>
      					
    					<p><h2>Administraci&oacute;n de Cuentas por Cobrar</h2></p>    					    	    													
							
							<% if (request.getParameter("verOrdenesVenta") != null) { 
		  								  						
		  						if (!Utilerias.isNullOrUndefined(start) && !Utilerias.isNullOrUndefined(end)) {
		  							
		  							Cliente cli = new Cliente();
		  							cli.setClienteId(Utilerias.strToInt(idCliente));
		  							
		  							List<Remision> lista = remisionMGR.getCuentasPorCobrarRangoFechas(start, end, cli, catalogoMGR); %>
		  							
		  							<% if (lista.size() > 0) { %>
			  							<table border="1" class="tablas">
		    								<tr bgcolor="silver">
	    										<td>Remisi&oacute;n</td>
			  									<td>Fecha de Venta</td>
			  									<td>Cliente</td>
			  									<td>Forma de Pago</td>
			  									<td>Monto de Remisi&oacute;n</td>
			  									<td>Por Pagar</td>			  									
			  								</tr>
			  							
			  							<%
			  							for (int i=0; i < lista.size(); i++) { 
			  								Remision elemento = lista.get(i); %>
			  								<tr>
			  									<td><%= elemento.getRemisionId() %></td>
			  									<td><%= Utilerias.getDate((java.util.Date) elemento.getFechaRemision(), Utilerias.FORMAT_DATE) %></td>
			  									<td><%= elemento.getCliente().getNombreCliente() %></td>
			  									<td><%= Utilerias.getDescripcionesBD(Utilerias.ESTATUS_PAGO_COMPRA, elemento.getEstatusPagoCredito()) %></td>
			  									<td><%= Utilerias.getCurrencyFormat(elemento.getMontoFactura()) %></td>
			  									<td><%= Utilerias.getCurrencyFormat(elemento.getPorPagar()) %></td>
			  									<td>
			  										<% if (!Utilerias.getDescripcionesBD(Utilerias.ESTATUS_PAGO_COMPRA, elemento.getEstatusPagoCredito()).equals("Contado")) {  %>
      												<form action="modCuentasPorCobrar.jsp?sessionid=<%= session.getId() %>" method="post">
      													<input type="hidden" name="remisionId" value="<%= elemento.getRemisionId() %>">	
      						    						<input type="submit" name="Modificar" value="Modificar">
      						    					</form>
      						    					<% } %>	
      											</td>      												  								
			  								</tr>
			  							<% } %>
			  							
			  							</table>
			  							<br/>
			  						<% } %>			  							
		  					<% } else { %>
		  							<script>alert("Favor de capturar ambas fechas.");</script>
		  						<% } %>			  							
		  					<% } %>
		  							  		
		  							  						
		  					<form action="cuentasPorCobrar.jsp?sessionid=<%= session.getId() %>" method="post">
		  					
		  						<table class="tablecontent">
		  							<tr>
		  								<td>	
		  									Clientes
		  								</td>	
		  								<td>
		  									<select name="clienteIdInventario">
	  							    		<% 
  								    			Cliente cli = new Cliente();
  								  				ArrayList<Cliente> listaCli = (ArrayList<Cliente>) catalogoMGR.getList(cli);
												for (int i=0; i < listaCli.size(); i++) { 
													cli = (Cliente) listaCli.get(i);
  												%>
      											<option value="<%= cli.getClienteId() %>"><%= cli.getNombreCliente() %></option>
    									        <% } %>
    										</select>
    									</td>
    								</tr>			  							  
    								  										  							
		  							<tr>
		  								<td>
		  									Fecha Inicial de Venta:
		  								</td>
		  								<td>	 		  						
    										<input type="text" name="start" id="start" maxlength="10" size="10" value="<%= start %>" readonly="true">
    										<a href="javascript:NewCal('start','ddmmyyyy')">
    											<img src="../images/cal.gif" width="16" height="16" border="0" alt="Selecciona una fecha">
    										</a>
    									</td>	    							
    								</tr>	
		  						
		  							<tr>
		  								<td>
		  									Fecha Final de Venta:
		  								</td>	 
		  						   		<td>
		  						   			<input type="text" name="end" id="end" maxlength="10" size="10" value="<%= end %>" readonly="true">
    										<a href="javascript:NewCal('end','ddmmyyyy')">
    											<img src="../images/cal.gif" width="16" height="16" border="0" alt="Selecciona una fecha">
    										</a>
    									</td>
    								</tr>		
		  						
		  						    <tr>
		  						    	<td>
		  						    		<input type="submit" name="verOrdenesVenta" value="Ver Ordenes de Venta">
		  						    	</td>
		  						    </tr>				  						   
		  						</table>
		  					</form>
		  								  					
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