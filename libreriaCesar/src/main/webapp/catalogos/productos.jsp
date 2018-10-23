<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.Producto" %>

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
      				
    					<p><h2>Administraci&oacute;n de Productos</h2></p>
    					<form action="modproductos.jsp?sessionid=<%= session.getId() %>" method="post">
    						<input type="submit" name="" value="Administrar Productos"><br>
    					</form>
    			
    					<table border="1" class="tablas">
	    					<tr bgcolor="silver">
    							<td>Clave del Producto</td>
    							<td>Nombre del Producto</td>
    							<td>Descripcion</td>    	
    							<td>Tipo de Producto</td>
    							<td>Proveedor</td>						
    						</tr>
                        	            
                        	<% 
								                    
                        	    Producto prod = new Producto();
	    						CatalogMGR catalogoMGR = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");
                        		ArrayList<Producto> lista = (ArrayList<Producto>)catalogoMGR.getList(prod);
								
                        		for (int i=0; i < lista.size(); i++) { 
                        		    prod = lista.get(i); 
                        		    %> 
                        			
                        			<tr>
        				    			<td><%= prod.getProductoId() %></td>
      									<td><%= prod.getNombreProducto() %></td>
      									<td><%= prod.getDescProducto() %></td>
      									<td><%= Utilerias.getMessageValue(Utilerias.TIPO_PRODUCTO_KEY, prod.getTipoProducto()) %></td>
      									<td><%= prod.getProveedor().getNombreProveedor() %></td>
      									<td>
      										<form action="modproductos.jsp?sessionid=<%= session.getId() %>" method="post">
      											<input type="hidden" name="productoId" value="<%= prod.getProductoId() %>">	
      						    				<input type="submit" name="Modificar" value="Modificar">
      						    			</form>
      									</td>    
      									<td>
      										<form action="modproductos.jsp?sessionid=<%= session.getId() %>" method="post">
      											<input type="hidden" name="productoId" value="<%= prod.getProductoId() %>">	
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
    					
    					<br><b><font color="black" size="2" face="verdana">Tienes <%= lista.size() %> producto(s) en el sistema.</font></b>
    			    		        	
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