<%@page import="mx.com.libreria.model.VentaEspecial"%>
<%@page import="mx.com.libreria.manager.VentaEspecialMGR"%>
<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR"%>
<%@ page import="mx.com.libreria.manager.GastosMGR"%>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.Gastos"%>
<%@ page import="mx.com.libreria.model.TipoGasto"%>

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
      						VentaEspecialMGR ventaEspecialMGR = (VentaEspecialMGR) ObjectFactory.getBean("ventaEspecialMGR");
      					         					   
      					    VentaEspecial vE = new VentaEspecial();
							String result = "";							
      					%>
      					
    					<p><h2>Administraci&oacute;n de Ventas Especiales</h2></p>    					    	    													
							
							<form action="modventasEspeciales.jsp?sessionid=<%= session.getId() %>" method="post">
    							<input type="submit" name="" value="Administrar Ventas Especiales"><br>
    						</form>							
							
						    <table border="1" class="tablas">
	    						<tr bgcolor="silver">
	    							<td>Clave del Venta Especial</td>
    								<td>Fecha Inicial</td>
    								<td>Fecha Final</td>
    								<td>Cliente</td>
    								<td>Producto</td>    											
    							</tr>
    							
    							<%
    								List<VentaEspecial> listaVE = ventaEspecialMGR.getVentaEspecial(catalogoMGR);
    								for (int i=0; i < listaVE.size(); i++) { 
    									VentaEspecial elemento = listaVE.get(i); %>
    									<tr>
	    									<td><%= elemento.getVentaEspecialId() %></td>
	    									<td><%= Utilerias.getDate(elemento.getFechaInicialVentaEspecial(), Utilerias.FORMAT_DATE) %></td>
	    									<td><%= Utilerias.getDate(elemento.getFechaFinalVentaEspecial(), Utilerias.FORMAT_DATE) %></td>
	    									<td><%= elemento.getCliente().getNombreCliente() %></td>
	    									<td><%= elemento.getProducto().getNombreProducto() %></td>
	    								</tr>	
    								<% } 
    							%>    							                        			                        													
							</table>
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