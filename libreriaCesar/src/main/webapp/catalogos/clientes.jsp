<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.Cliente" %>


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
      				
    					<p><h2>Administraci&oacute;n de Clientes</h2></p>
    					<form action="modclientes.jsp?sessionid=<%= session.getId() %>" method="post">
    						<input type="submit" name="" value="Administrar Clientes"><br>
    					</form>
    			
    					<table border="1" class="tablas">
	    					<tr bgcolor="silver">
    							<td>Clave del Cliente</td>
    							<td>Nombre</td>
    							<td>Direcci&oacute;n</td>
    							<td>Telefono 1</td>
    							<td>Telefono 2</td>
    						</tr>
                        	            
                        	<% 
								                    
                        	    Cliente prod = new Cliente();
	    						CatalogMGR catalogoMGR = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");
                        		ArrayList<Cliente> lista = (ArrayList<Cliente>)catalogoMGR.getList(prod);
								
                        		for (int i=0; i < lista.size(); i++) { 
                        		    prod = lista.get(i); 
                        		    %> 
                        			
                        			<tr>
        				    			<td><%= prod.getClienteId() %></td>
      									<td><%= prod.getNombreCliente() %></td>
      									<td><%= prod.getDireccion() %></td>
      									<td><%= prod.getTelefono_1() %></td>
      									<td><%= prod.getTelefono_2() %></td>
      									<td>
      										<form action="modclientes.jsp?sessionid=<%= session.getId() %>" method="post">
      											<input type="hidden" name="clienteId" value="<%= prod.getClienteId() %>">	
      						    				<input type="submit" name="Modificar" value="Modificar">
      						    			</form>
      									</td>
      									<td>
      										<form action="modclientes.jsp?sessionid=<%= session.getId() %>" method="post">
      											<input type="hidden" name="clienteId" value="<%= prod.getClienteId() %>">	
      						    				<input type="submit" name="Eliminar" value="Eliminar">
      						    			</form>
      									</td>    
      								</tr>		
                        		<% } %>                        	                        	    				
    					</table>
    			
    					<br><b><font color="black" size="2" face="verdana">Tienes <%= lista.size() %> cliente(s) en el sistema.</font></b>

						<% if (request.getParameter("result") != null) { %>
    						<script>alert("<%= request.getParameter("result") %>");</script>;
    						<%= request.getParameter("result") %>
    					<% } %>
    					    			    		        	
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