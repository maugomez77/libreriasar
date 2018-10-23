<%@ include file="../admin/Secure.jsp" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR" %>
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
					  && session.getAttribute("uname") != null){ 
      							
						CatalogMGR catalogoMGR = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");
						
						String readonly = "";
						String result = "";
	
						Producto prod = new Producto();
						
						//por si se recibe para la eliminacion
					    prod.setProductoId(Utilerias.strToInt(request.getParameter("productoId")));
						
						prod.setNombreProducto(request.getParameter("nombreProducto"));
						prod.setDescProducto(request.getParameter("descProducto"));
						
						prod.setTipoProducto(Utilerias.mostrarCamposFront(request.getParameter("tipoProducto")));
						
						if (!Utilerias.isNullOrUndefined(request.getParameter("proveedorId"))) {
							Proveedor prov = new Proveedor();
							prov.setProveedorId(Utilerias.strToInt(request.getParameter("proveedorId")));
							prov = (Proveedor) catalogoMGR.getObjectData(prov);
							prod.setProveedor(prov);
						}
						
						//prod.setCostoProducto(Utilerias.strToFloat(request.getParameter("costoProducto")));
						//prod.setPrecioProducto(Utilerias.strToFloat(request.getParameter("precioProducto")));
																			           
						boolean act = false;
						    		    		    		    		    		
            			if ( request.getParameter( "Modificar" ) != null ) {
                         				
                			if (prod.getProductoId() < 0) { %>
                    			<script>alert("Necesitas llenar el campo de clave del producto");</script>
                			<% } else {             
             	     			readonly = "readonly";     		             
		             			act = true;
		             			prod = (Producto) catalogoMGR.getObjectData(prod);		             			
	             			}
             			} else if ( request.getParameter( "Actualizar" ) != null ) {
               
                			readonly = "readonly";
                			result = catalogoMGR.validarCamposLlenos(prod);
    			 			                	  
                 			if (result.equals("")) {                				
                	
                				int opera = catalogoMGR.dmlOperations(1, prod);
                				if ( opera > -1 ) {
                 					prod = new Producto();
                 				}
                 				result = "Producto actualizado satisfactoriamente";
                 	
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
                 					prod = new Producto();
                 					readonly = "";
                 					result = "Producto insertado satisfactoriamente";
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
               					prod = new Producto();
               					result = "Registro eliminado satisfactoriamente";    		  					
    		   				} else {               
               					result = "Problemas al eliminar el registro";
               				}                  
            				            				
            				response.sendRedirect(request.getContextPath() + "/catalogos/productos.jsp?sessionid=" + session.getId() + "&result=" + result);
            				
          				   } %>
    		    			    				    					    		
		    			<p><h2>Administraci&oacute;n de Productos</h2></p>
		    
		    			<form action="modproductos.jsp?sessionid=<%= session.getId() %>" method="post">
		    				<% if (act) {  %> 
		    				<p>Clave del Producto:
		    					<input type="text" name="productoId" value="<%= Utilerias.mostrarCamposFront(prod.getProductoId()) %>" <%= readonly %>>
		    				</p>
		    				<% } %>
		    				<p>Nombre del Producto:
		    					<input type="text" name="nombreProducto" value="<%= Utilerias.mostrarCamposFront(prod.getNombreProducto()) %>">
		    				</p>
		    				
		    				<p>Descripcion del Producto:
		    					<input type="text" name="descProducto" value="<%= Utilerias.mostrarCamposFront(prod.getDescProducto()) %>">
		    				</p>
		    				
		    				<p>Nombre del Proveedor:
		    					<% 
		    						Proveedor prov = new Proveedor();
		    					    List<Proveedor> listProv = (ArrayList<Proveedor>) catalogoMGR.getList(prov); 
		    					%>
		    					<select name="proveedorId">
		    						<% for (int i=0; i < listProv.size(); i++) { %>
		    							<option value="<%= listProv.get(i).getProveedorId()%>" <% if (prod.getProveedor() != null && listProv.get(i).getProveedorId() == prod.getProveedor().getProveedorId()) out.println("selected"); %>><%= listProv.get(i).getNombreProveedor() %></option>
		    						<% } %>	
		    					</select>
		    				</p>
		    				<p>Tipo de Producto:
		    					<input type="radio" name="tipoProducto" value="<%= Utilerias.TIPO_PRODUCTO_NORMAL_VALUE %>" <% if (Utilerias.isNullOrUndefined(prod.getTipoProducto()) || prod.getTipoProducto().equals(Utilerias.TIPO_PRODUCTO_NORMAL_VALUE)) out.println("checked"); %>><%= Utilerias.TIPO_PRODUCTO_NORMAL_DESC %>
		    					<input type="radio" name="tipoProducto" value="<%= Utilerias.TIPO_PRODUCTO_SUSCRIPCION_VALUE %>" <% if (!Utilerias.isNullOrUndefined(prod.getTipoProducto()) &&  prod.getTipoProducto().equals(Utilerias.TIPO_PRODUCTO_SUSCRIPCION_VALUE)) out.println("checked"); %>><%= Utilerias.TIPO_PRODUCTO_SUSCRIPCION_DESC %>
		    					<input type="radio" name="tipoProducto" value="<%= Utilerias.TIPO_PRODUCTO_VENTA_ESPECIAL_VALUE %>" <% if (!Utilerias.isNullOrUndefined(prod.getTipoProducto()) && prod.getTipoProducto().equals(Utilerias.TIPO_PRODUCTO_VENTA_ESPECIAL_VALUE)) out.println("checked"); %>><%= Utilerias.TIPO_PRODUCTO_VENTA_ESPECIAL_DESC %>
		    				</p>
		    				
		    				<!-- 
		    				<p>Costo del Producto:
		    					<input type="text" name="costoProducto" value="Utilerias.mostrarCamposFront(prod.getCostoProducto())">
		    				</p>
		    				    
		    				<p>Precio del Producto:
		    					<input type="text" name="precioProducto" value="Utilerias.mostrarCamposFront(prod.getPrecioProducto())">
		    				</p>
		    				-->      			    										
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