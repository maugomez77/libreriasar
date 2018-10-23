<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>
<%@ page import="mx.com.libreria.manager.CatalogMGR" %>

<%@ page import="mx.com.libreria.model.login.Usuario" %>
<%@ page import="mx.com.libreria.model.login.Rol" %>

<%@ page import="mx.com.libreria.manager.Utilerias" %>
  
<html>
	<%@ include file="../admin/Header.jsp" %>

	<body>
		<div class="wrapper1">
			<div class="wrapper">
				<%@ include file="../admin/MenuHeader.jsp" %>			
				<div class="content">

				<% if(request.getParameter("sessionid") != null 
					  && session.getId().equals(request.getParameter("sessionid"))
					  && session.getAttribute("uname") != null){ 
      							
						CatalogMGR catalogoMGR = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");
						
						String readonly = "";
						String result = "";
	
						Usuario prod = new Usuario();
						
						prod.setUsuarioId(request.getParameter("usuarioId"));
						prod.setNombreUsuario(request.getParameter("nombreUsuario"));
						prod.setCorreoElectronico(request.getParameter("correoElectronico"));
						prod.setContrasena(request.getParameter("contrasena"));
						prod.setContrasenaConf(request.getParameter("contrasenaConf"));
						prod.setTelefono(request.getParameter("telefono"));
						
						Rol rolUsuario = new Rol();
						
						//para cuando viene de actualizar
						try  { 
							rolUsuario.setRolId(Utilerias.strToInt(request.getParameter("rolId")));
							rolUsuario = (Rol) catalogoMGR.getObjectData(rolUsuario);						
							prod.setRolUsuario(rolUsuario);
						} catch (Exception e) { 
							prod.setRolUsuario(null);
						}
						
						boolean act = false;
						    		    		    		    		    		
            			if ( request.getParameter( "Modificar" ) != null ) {
                         				
                			if (prod.getUsuarioId().equals("")) { %>
                    			<script>alert("Necesitas llenar el campo de clave del usuario");</script>
                			<% } else {             
             	     			readonly = "readonly";     		             
		             			act = true;
		             			prod = (Usuario) catalogoMGR.getObjectData(prod);		             			
	             			}
             			} else if ( request.getParameter( "Actualizar" ) != null ) {
               
                			readonly = "readonly";
                			result = catalogoMGR.validarCamposLlenos(prod);
    			 			                	  
                 			if (result.equals("")) {                				
                	
                				int opera = catalogoMGR.dmlOperations(1, prod);
                				if ( opera > -1 ) {
                 					prod = new Usuario();
                 				}
                 				result = "Usuario actualizado satisfactoriamente";
                 	
                 			} else {
                 				result = Utilerias.DATOS_FALTANTES + result;
                 				act = true;
                 			} %>
                                  
                 			<script>alert("<%= result %>");</script>
       
         				<% } else if ( request.getParameter( "Agregar" ) != null ) {
      
      						result = catalogoMGR.validarCamposLlenos(prod);
                				                  
                 			if (result.equals("")) {                				
                	
                				int opera = catalogoMGR.dmlOperations(0, prod);
                				if (opera > -1) {
                 					prod = new Usuario();
                 					readonly = "";
                 					result = "Usuario insertado satisfactoriamente";
                 				} else {
                 					result = "Problemas al insertar";
                 				}                 	                 	
                 			} else {
                 				result = Utilerias.DATOS_FALTANTES + result;
                 			} %>
                                  
                 			<script>alert("<%= result %>");</script>
      
          				<% } else if ( request.getParameter("Eliminar") != null ) {
  		
  			   				int opera = catalogoMGR.dmlOperations(-1, prod);
               				if (opera > -1) {
               					prod = new Usuario();
               					result = "Registro eliminado satisfactoriamente";    		  					
    		   				} else {               
               					result = "Problemas al eliminar el registro";
               				}                  
            					
            				response.sendRedirect(request.getContextPath() + "/catalogos/usuarios.jsp?sessionid=" + session.getId() + "&result=" + result);

          				} %>
    		    			    		
		    					    			
		    			<p><h2>Administraci&oacute;n de Usuarios</h2></p>
		    
		    			<form action="modusuarios.jsp?sessionid=<%= session.getId() %>" method="post">
		    				<p>Clave del Usuario:
		    					<input type="text" name="usuarioId" value="<%= Utilerias.mostrarCamposFront(prod.getUsuarioId()) %>" <%= readonly %>>
		    				</p>
		    				
		    				<p>Nombre del Usuario:
		    					<input type="text" name="nombreUsuario" value="<%= Utilerias.mostrarCamposFront(prod.getNombreUsuario()) %>">
		    				</p>
		    				
		    				<p>Contrase&ntilde;a:
		    					<input type="text" name="contrasena" value="<%= Utilerias.mostrarCamposFront(prod.getContrasena()) %>">
		    				</p>
		    				
		    				<p>Confirmaci&oacute;n de Contrase&ntilde;a:
		    					<input type="text" name="contrasenaConf" value="<%= Utilerias.mostrarCamposFront(prod.getContrasenaConf()) %>">
		    				</p>
		    				    
		    				<p>Email:
		    					<input type="text" name="correoElectronico" value="<%= Utilerias.mostrarCamposFront(prod.getCorreoElectronico()) %>">
		    				</p>
		    				    
		    				<p>Tel&eacute;fono:
		    					<input type="text" name="telefono" value="<%= Utilerias.mostrarCamposFront(prod.getTelefono()) %>">
		    				</p>  			    										
		    					
		    				<p>Rol del Usuario: 
  							    <select name="rolId">
  							    <% 
  							    	Rol roles = new Rol();
  							  		ArrayList<Rol> listaRoles = (ArrayList<Rol>) catalogoMGR.getList(roles);
									for (int i=0; i < listaRoles.size(); i++) { 
  									    roles = (Rol) listaRoles.get(i);
  									%>
      									<option value="<%= roles.getRolId() %>" <% if (prod.getRolUsuario() != null && prod.getRolUsuario().getRolId() == roles.getRolId()) out.println("selected"); %>><%= roles.getNombreRol() %></option>
    							   <% } %>
    							</select>
  							</p>
		    				
						    <p align="center">
		    					<input type="submit" name="Agregar" value="Agregar" <% if (act) out.println("disabled"); %>>		    		
		    					
		    					<% if (act) {  %>
		    						<input type="submit" name="Actualizar" value="Actualizar">
		    					<% } %>		    		
		    							    					    		    	
							</p>        
						</form>	        	        
								    
		    			<%= Utilerias.imprimePaginaHTML(result) %>

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