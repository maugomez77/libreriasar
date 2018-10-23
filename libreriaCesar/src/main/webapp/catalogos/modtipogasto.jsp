<%@ include file="../admin/Secure.jsp" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.TipoGasto" %>
<%@ page import="mx.com.libreria.model.TipoGastoEnum"%>
  
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
	
						TipoGasto prod = new TipoGasto();
						
						//solo en actualizar
						prod.setTipoGastoId(Utilerias.strToInt(request.getParameter("tipoGastoId")));
						
						prod.setNombreGasto(request.getParameter("nombreGasto"));
						prod.setDescGasto(request.getParameter("descGasto"));
						
						prod.setTipoGastoEnum((TipoGastoEnum) Utilerias.getEnumValue(1, Utilerias.strToInt(request.getParameter("tipoGastoEnumId"))));
						
						boolean act = false;
						    		    		    		    		    		
            			if ( request.getParameter( "Modificar" ) != null ) {
                         				
                			if (prod.getTipoGastoId() < 0) { %>
                    			<script>alert("Necesitas llenar el campo de clave del tipo gasto.");</script>
                			<% } else {             
             	     			readonly = "readonly";     		             
		             			act = true;
		             			prod = (TipoGasto) catalogoMGR.getObjectData(prod);		             			
	             			}
             			} else if ( request.getParameter( "Actualizar" ) != null ) {
               
                			readonly = "readonly";
                			result = catalogoMGR.validarCamposLlenos(prod);
    			 			                	  
                 			if (result.equals("")) {                				
                	
                				int opera = catalogoMGR.dmlOperations(1, prod);
                				if ( opera > -1 ) {
                 					prod = new TipoGasto();
                 				}
                 				result = "Tipo de gasto actualizado satisfactoriamente";
                 	
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
                 					prod = new TipoGasto();
                 					readonly = "";
                 					result = "Tipo de gasto insertado satisfactoriamente";
                 				} else {
                 					result = "Problemas al insertar";
                 				}                 	                 	
                 			} else {
                 				result = Utilerias.DATOS_FALTANTES + result;
                 			} %>
                                  
                 			<script>alert("<%= result %>");</script>
      
          				<% } else if ( request.getParameter("Eliminar") != null ) {
  		
  			   				int opera = catalogoMGR.dmlOperations(-1, prod);
               				if (opera >= -1) {
               					prod = new TipoGasto();
               					result = "Registro eliminado satisfactoriamente";    		  					
    		   				} else {               
               					result = "Problemas al eliminar el registro";
               				}                  
            				
            				response.sendRedirect(request.getContextPath() + "/catalogos/tipogasto.jsp?sessionid=" + session.getId() + "&result=" + result);
            				
          				} %>
    		    			    		
		    					    			
		    			<p><h2>Administraci&oacute;n de Tipos de Gastos</h2></p>
		    
		    			<form action="modtipogasto.jsp?sessionid=<%= session.getId() %>" method="post">
		    				<% if (act) {  %>
		    				<p>Clave del Tipo de Gasto:
		    					<input type="text" name="tipoGastoId" value="<%= Utilerias.mostrarCamposFront(prod.getTipoGastoId()) %>" <%= readonly %>>
		    				</p>
		    				<% } %>
		    				
		    				<p>Nombre del Gasto:
		    					<input type="text" name="nombreGasto" value="<%= Utilerias.mostrarCamposFront(prod.getNombreGasto()) %>" 
		    						   selectBoxOptions="<%= catalogoMGR.getEmpleadoNomina() %>">
		    				</p>
		    				
		    				<p>Descripcion del Gasto:
		    					<input type="text" name="descGasto" value="<%= Utilerias.mostrarCamposFront(prod.getDescGasto()) %>" 
		    							selectBoxOptions="<%= catalogoMGR.getEmpleadoNomina() %>">
		    				</p>
		    				
		    				<p>Tipo de Gasto:
		    					<select name="tipoGastoEnumId">
  							    <% 
  							    	TipoGastoEnum[] arreglo = TipoGastoEnum.class.getEnumConstants();
  							  		for (int i=0; i < arreglo.length; i++) { %>
      									<option value="<%= arreglo[i].getId() %>" <% if (prod.getTipoGastoEnum() != null && prod.getTipoGastoEnum().getId() == arreglo[i].getId()) out.println("selected");%>><%= arreglo[i] %></option>
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
						
						<script type="text/javascript">
							createEditableSelect(document.forms[0].nombreGasto);
							createEditableSelect(document.forms[0].descGasto);							
						</script>
						
								    
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