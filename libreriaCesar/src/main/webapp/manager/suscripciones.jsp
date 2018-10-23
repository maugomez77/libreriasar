<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR"%>
<%@ page import="mx.com.libreria.manager.SuscripcionMGR"%>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.Suscripcion"%>

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
      					         					   
      					    Suscripcion suscripcion = new Suscripcion();
      					    							
							String result = "";							
      					%>
      					
    					<p><h2>Administraci&oacute;n de Suscripciones</h2></p>    					    	    													
							
							<form action="modsuscripcion.jsp?sessionid=<%= session.getId() %>" method="post">
    							<input type="submit" name="" value="Administrar Suscripciones"><br>
    						</form>							
							
						    <table border="1" class="tablas">
	    						<tr bgcolor="silver">
	    							<td>Clave de la Suscripci&oacute;n</td>
    								<td>Fecha Inicial</td>
    								<td>Numero Meses Restantes</td>
    								<td>Cliente</td>
    								<td>Producto</td>
    								<td>Monto Suscripci&oacute;n</td>				
    							</tr>
    							
    							<%
    								List<Suscripcion> lista = suscripcionMGR.getSuscripcionesValidas(catalogoMGR);
    								for (int i=0; i < lista.size(); i++) { 
    									Suscripcion elemento = lista.get(i); %>
    									<tr>
	    									<td><%= elemento.getSuscripcionId() %></td>
	    									<td><%= Utilerias.getDate(elemento.getFechaInicialSuscripcion(), Utilerias.FORMAT_DATE) %></td>
	    									<td><%= elemento.getNumeroMesesSuscripcionRestante() %></td>
	    									<td><%= elemento.getCliente().getNombreCliente() %></td>
	    									<td><%= elemento.getProducto().getNombreProducto() %></td>
	    									<td><%= elemento.getMontoInicial() %></td>
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