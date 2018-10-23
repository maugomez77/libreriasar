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
      						GastosMGR gastosMGR = (GastosMGR) ObjectFactory.getBean("gastosMGR");
      					         					   
      					    Gastos gastos = new Gastos();      					          					   
							String result = "";							
      					%>
      					
    					<p><h2>Administraci&oacute;n de Gastos</h2></p>    					    	    													
							
							<form action="modgastos.jsp?sessionid=<%= session.getId() %>" method="post">
    							<input type="submit" name="" value="Administrar Gastos"><br>
    						</form>							
							
						    <table border="1" class="tablas">
	    						<tr bgcolor="silver">
	    							<td>Clave del Gasto</td>
    								<td>Fecha de Aplicacion</td>
    								<td>Monto de Gasto</td>
    								<td>Tipo de Gasto</td>				
    							</tr>
    							
    							<%
    								List<Gastos> listaGastos = gastosMGR.getGastosFechaParametros(Utilerias.getDateStringToday(Utilerias.FORMAT_DATE), catalogoMGR);
    								for (int i=0; i < listaGastos.size(); i++) { 
    									Gastos elemento = listaGastos.get(i); %>
    									<tr>
	    									<td><%= elemento.getGastosId() %></td>
	    									<td><%= Utilerias.getDate(elemento.getFechaAplicacion(), Utilerias.FORMAT_DATE) %></td>
	    									<td><%= elemento.getMontoGasto() %></td>
	    									<td><%= elemento.getTipoGasto().getTipoGastoEnum() %></td>
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