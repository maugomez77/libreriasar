<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

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
      				
    					<p><h2>Administraci&oacute;n de Proveedores</h2></p>
    					<form action="modproveedores.jsp?sessionid=<%= session.getId() %>" method="post">
    						<input type="submit" name="" value="Administrar Proveedores"><br>
    					</form>
    			
    					<table border="1" class="tablas">
	    					<tr bgcolor="silver">
    							<td>Clave del Proveedor</td>
    							<td>Nombre</td>
    							<td>Direcci&oacute;n</td>
    							<td>Telefono 1</td>
    							<td>Telefono 2</td>
    						</tr>
                        	            
                        	<% 
								                    
                        	    Proveedor prod = new Proveedor();
	    						CatalogMGR catalogoMGR = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");
                        		ArrayList<Proveedor> lista = (ArrayList<Proveedor>)catalogoMGR.getList(prod);
								
                        		for (int i=0; i < lista.size(); i++) { 
                        		    prod = lista.get(i); 
                        		    %> 
                        			
                        			<tr>
        				    			<td><%= prod.getProveedorId() %></td>
      									<td><%= prod.getNombreProveedor() %></td>
      									<td><%= prod.getDireccion() %></td>
      									<td><%= prod.getTelefono_1() %></td>
      									<td><%= prod.getTelefono_2() %></td>
      									<td>
      										<form action="modproveedores.jsp?sessionid=<%= session.getId() %>" method="post">
      											<input type="hidden" name="proveedorId" value="<%= prod.getProveedorId() %>">	
      						    				<input type="submit" name="Modificar" value="Modificar">
      						    			</form>
      									</td>
      									<td>
      										<form action="modproveedores.jsp?sessionid=<%= session.getId() %>" method="post">
      											<input type="hidden" name="proveedorId" value="<%= prod.getProveedorId() %>">	
      						    				<input type="submit" name="Eliminar" value="Eliminar">
      						    			</form>
      									</td>    
      								</tr>		
                        		<% } %>                        	                        	    				
    					</table>
    			
    					<br><b><font color="black" size="2" face="verdana">Tienes <%= lista.size() %> proveedor(es) en el sistema.</font></b>

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