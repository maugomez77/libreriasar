<%@ include file="../admin/Secure.jsp" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.Empleado" %>
  
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
	
						Empleado prod = new Empleado();
						
						//solo en actualizar
						prod.setEmpleadoId(Utilerias.strToInt(request.getParameter("empleadoId")));
						
						prod.setNombre(request.getParameter("nombre"));
						prod.setDireccion(request.getParameter("direccion"));
						prod.setTelefono_1(request.getParameter("telefono_1"));
						prod.setTelefono_2(request.getParameter("telefono_2"));
																			           
						boolean act = false;
						    		    		    		    		    		
            			if ( request.getParameter( "Modificar" ) != null ) {
                         				
                			if (prod.getEmpleadoId() < 0) { %>
                    			<script>alert("Necesitas llenar el campo de clave del empleado");</script>
                			<% } else {             
             	     			readonly = "readonly";     		             
		             			act = true;
		             			prod = (Empleado) catalogoMGR.getObjectData(prod);
	             			}
             			} else if ( request.getParameter( "Actualizar" ) != null ) {
               
                			readonly = "readonly";
                			result = catalogoMGR.validarCamposLlenos(prod);
    			 			                	  
                 			if (result.equals("")) {                				
                	
                				int opera = catalogoMGR.dmlOperations(1, prod);
                				if ( opera > -1 ) {
                 					prod = new Empleado();
                 				}
                 				result = "Empleado actualizado satisfactoriamente";
                 	
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
                 					prod = new Empleado();
                 					readonly = "";
                 					result = "Empleado insertado satisfactoriamente";
                 				} else {
                 					result = "Problemas al insertar";
                 				}                 	                 	
                 			} else {
                 				result = Utilerias.DATOS_FALTANTES + result;
                 			} %>
                                  
                 			<script>alert("<%= result %>");</script>
      
          				<% } else if ( request.getParameter("Eliminar") != null ) {
  		
  			   				int opera = catalogoMGR.dmlOperations(-1, prod);
               				if (opera == -1) {
               					prod = new Empleado();
               					result = "Registro eliminado satisfactoriamente";    		  					
    		   				} else {               
               					result = "Problemas al eliminar el registro";
               				}                  
            				
            				response.sendRedirect(request.getContextPath() + "/catalogos/empleados.jsp?sessionid=" + session.getId() + "&result=" + result);
            				
          				} %>
    		    			    		
		    					    			
		    			<p><h2>Administraci&oacute;n de Empleados</h2></p>
		    
		    			<form action="modempleados.jsp?sessionid=<%= session.getId() %>" method="post">
		    				<% if (act) {  %>
		    				<p>Clave del Empleado:
		    					<input type="text" name="empleadoId" value="<%= Utilerias.mostrarCamposFront(prod.getEmpleadoId()) %>" <%= readonly %>>
		    				</p>
		    				<% } %>
		    				
		    				<p>Nombre:
		    					<input type="text" name="nombre" value="<%= Utilerias.mostrarCamposFront(prod.getNombre()) %>">
		    				</p>
		    				
		    				<p>Direcci&oacute;n:
		    					<input type="text" name="direccion" value="<%= Utilerias.mostrarCamposFront(prod.getDireccion()) %>">
		    				</p>
		    				
		    				<p>Telefono 1:
		    					<input type="text" name="telefono_1" value="<%= Utilerias.mostrarCamposFront(prod.getTelefono_1()) %>">
		    				</p>
		    				    
		    				<p>Telefono 2:
		    					<input type="text" name="telefono_2" value="<%= Utilerias.mostrarCamposFront(prod.getTelefono_2()) %>">
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