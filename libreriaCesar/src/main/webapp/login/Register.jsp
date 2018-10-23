<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>


<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR" %>
<%@ page import="mx.com.libreria.manager.LoginMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.login.Usuario" %>
<%@ page import="mx.com.libreria.model.login.Rol" %>

<jsp:useBean id = "infoUser"
			     scope = "page"
			     class = "mx.com.libreria.model.login.Usuario" />

<jsp:useBean id = "infoUserRol"
			     scope = "page"
			     class = "mx.com.libreria.model.login.Rol" />

<html>
	<%@ include file="../admin/Header.jsp" %>

	<body>
		<div class="wrapper1">
			<div class="wrapper">
				<%@ include file="../admin/MenuHeader.jsp" %>			
				<div class="content">


<% 
 			String readonly = ""; 
            String result = "";
            boolean  act = false;

            LoginMGR loginMGR = (LoginMGR) ObjectFactory.getBean("loginMGR");
            CatalogMGR catalogoMGR = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");        		
    		            
    		if ( request.getParameter ( "Agregar" ) != null ) {
            	            	
            	infoUser.setUsuarioId(request.getParameter("usuarioId"));
            	
            	if (loginMGR.existUserWithPass(infoUser)) {
            		result += "Este id de usuario ya esta registrado";	
            	}
            	
            	infoUser.setNombreUsuario(request.getParameter("nombreUsuario"));
            	
            	infoUserRol.setRolId(Utilerias.strToInt(request.getParameter("rolId")));
            	infoUserRol = (Rol) loginMGR.getObjectData(infoUserRol);
            	infoUser.setRolUsuario(infoUserRol);
            	            	
            	infoUser.setContrasena(request.getParameter("contrasena"));
            	infoUser.setContrasenaConf(request.getParameter("contrasenaConf"));
            	infoUser.setCorreoElectronico(request.getParameter("correoElectronico"));
            	infoUser.setTelefono(request.getParameter("telefono"));
            	
        	            	    
            	result += loginMGR.checkContrasenas(infoUser);            	            		            	
            	result += loginMGR.validarCamposUsuario(infoUser);                 	

                 if (result.equals("")) {
                 	                	 
                	int opera = loginMGR.dmlOperations(0, infoUser);                	
                 	if (opera > -1) {
                 		infoUser = new Usuario();
                 		result = "Usuario agregado satisfactoriamente";
                 	} else {
                 		result = "Problemas al insertar el registro";
                 	}                 	                 	                 	
                 } else {                 
                 	result = Utilerias.DATOS_FALTANTES + result;
                 }
                 
                 %>                 	                 	                                                    
                                  
                 <script>alert("<%= result %>");</script>
                 
             <% } %>
    		    			    		            	
            	<p><h2>Alta de Usuarios</h2></p>
  
					<form name="form1" method="post" action="Register.jsp">
  					
  					<table width="90%">
  						<tr>
  							<td>Clave de Usuario:</td> 
  							<td><input type="text" name="usuarioId" size="15" maxlength="20" value="<%= Utilerias.mostrarCamposFront(infoUser.getUsuarioId()) %>" <%= readonly %>></td>
  						</tr>
  						<tr>
  							<td>Nombre: </td>
  							<td><input type="text" name="nombreUsuario" size="15" maxlength="15" value="<%= Utilerias.mostrarCamposFront(infoUser.getNombreUsuario()) %>"></td>
  						</tr>
  						<tr>
  							<td>Contrase&ntilde;a: </td>
  							<td><input type="password" name="contrasena" size="20" maxlength="30" value="<%= Utilerias.mostrarCamposFront(infoUser.getContrasena()) %>"></td>
  						</tr>
  						<tr>
  							<td>Confirmar Contrase&ntilde;a: </td>
  							<td><input type="password" name="contrasenaConf" size="20" maxlength="30" value="<%= Utilerias.mostrarCamposFront(infoUser.getContrasenaConf()) %>"></td>
  						</tr>
  						
  						<tr>
  							<td>Email: </td>
  							<td><input type="text" name="correoElectronico" size="30" maxlength="50" value="<%= Utilerias.mostrarCamposFront(infoUser.getCorreoElectronico()) %>"></td>
  						</tr>
  						<tr>
  							<td>Telefono:</td>
  							<td><input type="text" name="telefono" size="15" maxlength="20" value="<%= Utilerias.mostrarCamposFront(infoUser.getTelefono()) %>"></td>
  						</tr>
  						<tr>
  							<td>Rol del Usuario: </td>
  							<td><select name="rolId">
  							    <% 
  							    	Rol roles = new Rol();
  							  		ArrayList<Rol> listaRoles = (ArrayList<Rol>) catalogoMGR.getList(roles);
									for (int i=0; i < listaRoles.size(); i++) { 
  									    roles = (Rol) listaRoles.get(i);
  									%>
      									<option value="<%= roles.getRolId() %>" <% if (infoUser.getRolUsuario() != null && infoUser.getRolUsuario().getRolId() == roles.getRolId()) out.println("selected"); %>><%= roles.getNombreRol() %></option>
    							   <% } %>
    							</select>
  							</td>
  						</tr>
  					</table>

					<center>
    					<input type="submit" name="Agregar" value="Agregar">    							
    					<input type="reset" name="Reset" value="Limpiar" <% if (act) out.println("disabled"); %>>
    					</form>
    					<a href="<%out.println(request.getContextPath() + Utilerias.LOGIN_INDEX);%>">
    						<img src="<%out.println(request.getContextPath());%>/images/btnRegresar.png" alt="Regresar" />
    					</a>    					
    				</center>    				    				
              			        			
    				<%= Utilerias.imprimePaginaHTML(result) %>

    		<%@ include file="../admin/Footer.jsp" %>    
  			</div> <% //se cierra content %>   		
  		</div><% //se cierra wrapper %>
  	</div><% //se cierra wrapper1 %>
  	
</body>
</html>        