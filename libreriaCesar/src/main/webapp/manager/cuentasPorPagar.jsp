<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR"%>
<%@ page import="mx.com.libreria.manager.InventarioMGR"%>
<%@ page import="mx.com.libreria.manager.ProductMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.Cliente" %>
<%@ page import="mx.com.libreria.model.Compras" %>
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
      					        					       					  	
							String start = Utilerias.mostrarCamposFront(request.getParameter("start"));
							String end = Utilerias.mostrarCamposFront(request.getParameter("end"));
							String idProveedor = Utilerias.mostrarCamposFront(request.getParameter("proveedorIdInventario"));													
							
      					%>
      					
    					<p><h2>Administraci&oacute;n de Cuentas por Pagar</h2></p>    					    	    													
							
							<% if (request.getParameter("verOrdenesCompra") != null) { 
		  								  						
		  						if (!Utilerias.isNullOrUndefined(start) && !Utilerias.isNullOrUndefined(end)) {
		  							
		  							Proveedor prov = new Proveedor();
		  							prov.setProveedorId(Utilerias.strToInt(idProveedor));
		  							List<Compras> lista = inventarioMGR.getCuentasPorPagarRangoFechas(start, end, prov, catalogoMGR);
		  							
		  							%>
		  							
		  							<% if (lista.size() > 0) { %>
			  							<table border="1" class="tablas">
		    								<tr bgcolor="silver">
	    										<td>Folio</td>
			  									<td>Fecha de Compra</td>
			  									<td>Proveedor</td>
			  									<td>Forma de Pago</td>
			  									<td>Monto de Factura</td>
			  									<td>Por Pagar</td>
			  									<td></td>
			  								</tr>
			  							
			  							<%
			  							for (int i=0; i < lista.size(); i++) { 
			  								Compras elemento = lista.get(i); %>
			  								<tr>
			  									<td><%= elemento.getComprasId() %></td>
			  									<td><%= Utilerias.getDate(elemento.getFechaCompra(), Utilerias.FORMAT_DATE) %></td>
			  									<td><%= elemento.getProveedor().getNombreProveedor() %></td>
			  									<td><%= Utilerias.getDescripcionesBD(Utilerias.ESTATUS_PAGO_COMPRA, elemento.getEstatusPagoCredito()) %></td>
			  									<td><%= Utilerias.getCurrencyFormat(elemento.getMontoFactura()) %></td>
			  									<td><%= Utilerias.getCurrencyFormat(elemento.getPorPagar()) %></td>
			  									<td>
			  										<% if (!Utilerias.getDescripcionesBD(Utilerias.ESTATUS_PAGO_COMPRA, elemento.getEstatusPagoCredito()).equals("Contado")) {  %>
      												<form action="modCuentasPorPagar.jsp?sessionid=<%= session.getId() %>" method="post">
      													<input type="hidden" name="comprasId" value="<%= elemento.getComprasId() %>">	
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
		  							  		
		  							  						
		  					<form action="cuentasPorPagar.jsp?sessionid=<%= session.getId() %>" method="post">
		  					
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
    									</td>
    								</tr>			  							  
    								  										  							
		  							<tr>
		  								<td>
		  									Fecha Inicial de Compra:
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
		  									Fecha Final de Compra:
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
		  						    		<input type="submit" name="verOrdenesCompra" value="Ver Ordenes Compra">
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