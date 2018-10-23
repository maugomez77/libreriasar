<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.login.Usuario" %>

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
      				
    					<p><h2>Administraci&oacute;n de Usuarios</h2></p>
    					<form action="modusuarios.jsp?sessionid=<%= session.getId() %>" method="post">
    						<input type="submit" name="" value="Administrar Usuarios"><br>
    					</form>
    			
    					<table border="1" class="tablas">
	    					<tr bgcolor="silver">
    							<td>Clave del Usuario</td>
    							<td>Nombre</td>
    							<td>Contrase&ntilde;a</td>
    							<td>Correo Electronico</td>
    							<td>Telefono</td>
    							<td>Rol</td>
    						</tr>
                        	            
                        	<% 
								                    
                        	    Usuario prod = new Usuario();
	    						CatalogMGR catalogoMGR = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");
                        		ArrayList<Usuario> lista = (ArrayList<Usuario>)catalogoMGR.getList(prod);
								
                        		for (int i=0; i < lista.size(); i++) { 
                        		    prod = lista.get(i); 
                        		    %> 
                        			
                        			<tr>
        				    			<td><%= prod.getUsuarioId() %></td>
      									<td><%= prod.getNombreUsuario() %></td>
      									<td><%= prod.getContrasena() %></td>
      									<td><%= prod.getCorreoElectronico() %></td>
      									<td><%= prod.getTelefono() %></td>
      									<td><%= prod.getRolUsuario().getNombreRol() %></td>
      									<td>
      										<form action="modusuarios.jsp?sessionid=<%= session.getId() %>" method="post">
      											<input type="hidden" name="usuarioId" value="<%= prod.getUsuarioId() %>">
      											<input type="hidden" name="rolId" value="<%= prod.getRolUsuario().getRolId() %>">
      						    				<input type="submit" name="Modificar" value="Modificar">
      						    			</form>
      									</td>
      									<td>
      										<form action="modusuarios.jsp?sessionid=<%= session.getId() %>" method="post">
      											<input type="hidden" name="usuarioId" value="<%= prod.getUsuarioId() %>">
      											<input type="hidden" name="rolId" value="<%= prod.getRolUsuario().getRolId() %>">
      						    				<input type="submit" name="Eliminar" value="Eliminar">
      						    			</form>
      									</td>    
      								</tr>		
                        		<% } %>                        	                        	    				
    					</table>
    			
    					<% if (request.getParameter("result") != null) { %>
    						<script>alert("<%= request.getParameter("result") %>");</script>;    						
    						<%= request.getParameter("result") %>
    					<% } %>
    	
    					<br><b><font color="black" size="2" face="verdana">Tienes <%= lista.size() %> usuario(s) en el sistema.</font></b>
    			    		            			    	
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