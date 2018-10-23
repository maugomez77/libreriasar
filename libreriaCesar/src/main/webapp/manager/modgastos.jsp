<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.Gastos" %>
<%@ page import="mx.com.libreria.model.TipoGasto"%>
  
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
	
						Gastos prod = new Gastos();
						
						//solo en actualizar
						prod.setGastosId(Utilerias.strToInt(request.getParameter("gastosId")));
						
						String start = Utilerias.mostrarCamposFront(request.getParameter("start"));
						
						if (!Utilerias.isNullOrUndefined(start)) {
							prod.setFechaAplicacion(Utilerias.transformStringToDate(start, Utilerias.FORMAT_DATE));
						}
																	
						prod.setMontoGasto(Utilerias.strToFloat(request.getParameter("montoGasto")));
						
						TipoGasto tGasto = new TipoGasto();
						tGasto.setTipoGastoId(Utilerias.strToInt(request.getParameter("tipoGastoId")));
						tGasto = (TipoGasto) catalogoMGR.getObjectData(tGasto);
						prod.setTipoGasto(tGasto);
						prod.setActivo(Utilerias.YES_VALUE);
																								           
						boolean act = false;
						    		    		    		    		    		
            			if ( request.getParameter( "Modificar" ) != null ) {
                         				
                			if (prod.getGastosId() < 0) { %>
                    			<script>alert("Necesitas llenar el campo de clave del gasto");</script>
                			<% } else {             
             	     			readonly = "readonly";     		             
		             			act = true;
		             			prod = (Gastos) catalogoMGR.getObjectData(prod);		             			
	             			}
             			} else if ( request.getParameter( "Actualizar" ) != null ) {
               
                			readonly = "readonly";
                			result = catalogoMGR.validarCamposLlenos(prod);
    			 			                	  
                 			if (result.equals("")) {                				
                	
                				int opera = catalogoMGR.dmlOperations(1, prod);
                				if ( opera > -1 ) {
                 					prod = new Gastos();
                 				}
                 				result = "Gastos actualizado satisfactoriamente";
                 	
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
                 					prod = new Gastos();
                 					readonly = "";
                 					result = "Gastos insertados satisfactoriamente";
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
               					prod = new Gastos();
               					result = "Registro eliminado satisfactoriamente";    		  					
    		   				} else {               
               					result = "Problemas al eliminar el registro";
               				}                  
            				
            				response.sendRedirect(request.getContextPath() + "/manager/gastos.jsp?sessionid=" + session.getId() + "&result=" + result);
            				
          				} %>
    		    			    		
		    					    			
		    			<p><h2>Administraci&oacute;n de Gastos</h2></p>
		    
		    			<form action="modgastos.jsp?sessionid=<%= session.getId() %>" method="post">
		    				<% if (act) {  %>
		    				<p>Clave del Gasto:
		    					<input type="text" name="gastosId" value="<%= Utilerias.mostrarCamposFront(prod.getGastosId()) %>" <%= readonly %>>
		    				</p>
		    				<% } %>
		    				
		    				<p>Fecha Aplicaci&oacute;n del Gasto:
		    					<input type="text" name="start" id="start" maxlength="10" size="10" value="<%= start %>" readonly="true">
    							<a href="javascript:NewCal('start','ddmmyyyy')">
    								<img src="../images/cal.gif" width="16" height="16" border="0" alt="Selecciona una fecha">
    							</a>
		    				</p>
		    						    			
		    				<p>Monto del Gasto:
		    					<input type="text" name="montoGasto" value="<%= Utilerias.mostrarCamposFront(prod.getMontoGasto()) %>">
		    				</p>
		    				    
		    				<p>Tipo de Gasto:
								<select name="tipoGastoId">
  							    <% 
  							    	TipoGasto tipoGasto = new TipoGasto();
  							  		ArrayList<TipoGasto> listaGastos = (ArrayList<TipoGasto>) catalogoMGR.getList(tipoGasto);
									for (int i=0; i < listaGastos.size(); i++) { 
										tipoGasto = listaGastos.get(i); %>
										<option value="<%= tipoGasto.getTipoGastoId() %>" <% if (prod.getTipoGasto() != null && prod.getTipoGasto().getTipoGastoId() == tipoGasto.getTipoGastoId()) out.println("selected");%>><%= tipoGasto.getNombreGasto() + " " + tipoGasto.getTipoGastoEnum() %></option>  										
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