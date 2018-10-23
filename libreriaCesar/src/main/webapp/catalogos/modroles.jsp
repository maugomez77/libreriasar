<%@ include file="../admin/Secure.jsp" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.login.Rol" %>
  
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
	
						Rol prod = new Rol();
						
						//solo en actualizar
						prod.setRolId(Utilerias.strToInt(request.getParameter("rolId")));
						
						prod.setNombreRol(request.getParameter("nombreRol"));
																			           
						boolean act = false;
						    		    		    		    		    		
            			if ( request.getParameter( "Modificar" ) != null ) {
                         				
                			if (prod.getRolId() < 0) { %>
                    			<script>alert("Necesitas llenar el campo de clave del rol");</script>
                			<% } else {             
             	     			readonly = "readonly";     		             
		             			act = true;
		             			prod = (Rol) catalogoMGR.getObjectData(prod);		             			
	             			}
             			} else if ( request.getParameter( "Actualizar" ) != null ) {
               
                			readonly = "readonly";
                			result = catalogoMGR.validarCamposLlenos(prod);
    			 			                	  
                 			if (result.equals("")) {                				
                	
                				int opera = catalogoMGR.dmlOperations(1, prod);
                				if ( opera > -1 ) {
                 					prod = new Rol();
                 				}
                 				result = "Rol actualizado satisfactoriamente";
                 	
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
                 					prod = new Rol();
                 					readonly = "";
                 					result = "Rol insertado satisfactoriamente";
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
               					prod = new Rol();
               					result = "Registro eliminado satisfactoriamente";    		  					
    		   				} else {               
               					result = "Problemas al eliminar el registro";
               				}                  
            				
            				response.sendRedirect(request.getContextPath() + "/catalogos/roles.jsp?sessionid=" + session.getId() + "&result=" + result);
            				
          				} %>
    		    			    		
		    					    			
		    			<p><h2>Administraci&oacute;n de Roles</h2></p>
		    
		    			<form action="modroles.jsp?sessionid=<%= session.getId() %>" method="post">
		    				<% if (act) {  %>
		    				<p>Clave del Rol:
		    					<input type="text" name="rolId" value="<%= Utilerias.mostrarCamposFront(prod.getRolId()) %>" <%= readonly %>>
		    				</p>
		    				<% } %>
		    				
		    				<p>Nombre del Rol:
		    					<input type="text" name="nombreRol" value="<%= Utilerias.mostrarCamposFront(prod.getNombreRol()) %>">
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