<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR"%>
<%@ page import="mx.com.libreria.manager.InventarioMGR"%>
<%@ page import="mx.com.libreria.manager.ProductMGR" %>
<%@ page import="mx.com.libreria.manager.RemisionesMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.Producto" %>
<%@ page import="mx.com.libreria.model.InventarioActual"%>
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
      					   InventarioMGR inventarioMGR = (InventarioMGR) ObjectFactory.getBean("inventarioMGR");
      					   RemisionesMGR remisionMGR = (RemisionesMGR) ObjectFactory.getBean("remisionesMGR");
      					   
      					   String result = ""; %>

						<p><h2>Administraci&oacute;n de Devoluciones</h2></p>
						  
						<% if (request.getParameter("remisionId") != null && request.getParameter("enviarDevolucion") != null) {
							int operacion = remisionMGR.procesarDevolucion(request, response, catalogMGR);
							result = "La remision se ha actualizado correctamente";%>
							<% if (!result.equals("")) { %>
								<script>alert("<%= result %>");</script>
							<% } %>
							
						<% } %>
						
						<% if (request.getParameter("remisionId") != null && request.getParameter("enviarDevolucion") == null) {
							Remision prod = new Remision();
							prod.setRemisionId(Utilerias.strToInt(request.getParameter("remisionId")));
							prod = (Remision) catalogMGR.getObjectData(prod);
							if (prod != null && prod.getRemisionId() > 0) { %>
								
								<p>Listado de la Remision</p>
								<form action="devolucion.jsp?sessionid=<%= session.getId() %>" method="post">
									<table border="1" class="tablas">
	    								<tr bgcolor="silver">
	    									<td>Producto</td>
	    									<td>Cantidad</td>
	    									<td>Precio</td>
	    									<td>Total</td>
	    									<td>Acci&oacute;n</td>
	    								</tr>
	    								
	    								<%  List listInventario = Utilerias.convertSetToList(prod.getListaInventario(), new InventarioActual());	
	    									for (int i=0; i < listInventario.size(); i++) { 
	    									    InventarioActual ele = (InventarioActual) listInventario.get(i); %>
	    										<tr>
	    											<td><%= ele.getProducto().getNombreProducto()%></td>
	    											<td><%= ele.getCantidad() %></td>
	    											<td><%= Utilerias.getCurrencyFormat(ele.getPrecioProducto()) %></td>
	    											<td><%= Utilerias.getCurrencyFormat(ele.getCantidad() * ele.getPrecioProducto()) %></td>
	    											<td>
	    												<input type="checkbox" name="devCheck<%= ele.getInventarioId() %>">Devoluci&oacute;n 
	    											</td>
	    										</tr>
	    									<% } %>
	    							</table>
	    							
	    							<table class="tablecontent">
    									<tr>
    										<td>
    										    <input type="hidden" name="remisionId" value="<%= prod.getRemisionId() %>">
    										    <input type="submit" name="enviarDevolucion" value="Aplicar Devoluciones">    											
    										</td>    										    										
    									</tr>
	    						</form>										
								
							<% } else { 
								result = "No se encontro dicha remision.";
							} %>
						
							<% if (!result.equals("")) { %>
								<script>alert("<%= result %>");</script>
							<% } %>	
							
						<% } %>
						
						<br/>
    					<form action="devolucion.jsp?sessionid=<%= session.getId() %>" method="post">
    						<table class="tablecontent">
    							<tr>
    								<td>
    									N&uacute;mero de Remisi&oacute;n:
    								</td>
    								<td>
    									<input type="text" name="remisionId">
    								</td>    								
    							</tr>    
    							<tr>
    								<td>
    									<input type="submit" value="Enviar">
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