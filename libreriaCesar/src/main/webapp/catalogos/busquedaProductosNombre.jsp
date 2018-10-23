<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR" %>
<%@ page import="mx.com.libreria.manager.ProductMGR"%>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

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
      				
      					<% 
      						CatalogMGR catalogoMGR = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");
      						ProductMGR productMGR = (ProductMGR) ObjectFactory.getBean("productMGR");
						%>
    					
    					<p><h2>B&uacute;squeda de Productos</h2></p>
    					
    					<%  
    						Proveedor prov = new Proveedor();
    						List<Proveedor> listProv = (ArrayList<Proveedor>) catalogoMGR.getList(prov); 
    					%>
    					<form action="busquedaProductosNombre.jsp?sessionid=<%= session.getId() %>" method="post">
	    					<p>Introducir Caracteres a Buscar: 
    							<input type="text" name="comodin" value="">
    							<input type="submit" value="Buscar">	    						
	    					</p>
	    				</form>	
    					
    					<% if (!Utilerias.isNullOrUndefined(request.getParameter("comodin"))) { %>
    					
    					<table border="1" class="tablas">
	    					<tr bgcolor="silver">
    							<td>Clave del Producto</td>
    							<td>Nombre del Producto</td>
    							<td>Descripcion</td>    	
    							<td>Aplica Suscripci&oacute;n</td>
    							<td>Proveedor</td>						
    						</tr>
                        	            
                        	<% 
								                    
                        	    Producto prod = new Producto();
	    						ArrayList<Producto> lista = (ArrayList<Producto>) productMGR.getAllNameArticulosPorComodin(request.getParameter("comodin"));
								
                        		for (int i=0; i < lista.size(); i++) { 
                        		    prod = lista.get(i); 
                        		    %> 
                        			
                        			<tr>
        				    			<td><%= prod.getProductoId() %></td>
      									<td><%= prod.getNombreProducto() %></td>
      									<td><%= prod.getDescProducto() %></td>
      									<td><%= Utilerias.getMessageValue(Utilerias.TIPO_PRODUCTO_KEY, prod.getTipoProducto()) %></td>
      									<td><%= prod.getProveedor().getNombreProveedor() %></td>      									
      								</tr>		
                        		<% } %>                        	                        	    				
    					</table>
    			
    					<% if (request.getParameter("result") != null) { %>
    						<script>alert("<%= request.getParameter("result") %>");</script>;    						
    						<%= request.getParameter("result") %>
    					<% } %>
    					
    					<br><b><font color="black" size="2" face="verdana">Tienes <%= lista.size() %> producto(s) en el sistema.</font></b>
    			    	
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