<%@ include file="../admin/Secure.jsp" %>

<%@ page import="java.util.ArrayList" %>

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.CatalogMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.VentaEspecial" %>
<%@ page import="mx.com.libreria.model.Cliente" %>
<%@ page import="mx.com.libreria.model.Producto"%>
  
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
	
						VentaEspecial prod = new VentaEspecial();
						
						//solo en actualizar
						prod.setVentaEspecialId(Utilerias.strToInt(request.getParameter("ventaEspecialId")));
						
						Cliente cl = new Cliente();
						cl.setClienteId(Utilerias.strToInt(request.getParameter("clienteId")));
						cl = (Cliente) catalogoMGR.getObjectData(cl);
						prod.setCliente(cl);
						
						Producto pl = new Producto();
						pl.setProductoId(Utilerias.strToInt(request.getParameter("productoVentaEspecialId")));
						pl = (Producto) catalogoMGR.getObjectData(pl);
						prod.setProducto(pl);
						
						prod.setEstatusPagoCredito(Utilerias.PAGO_CONTADO);
						
						String start = Utilerias.mostrarCamposFront(request.getParameter("start"));
						String end = Utilerias.mostrarCamposFront(request.getParameter("end"));
						
						if (!Utilerias.isNullOrUndefined(start)) {
							prod.setFechaInicialVentaEspecial(Utilerias.transformStringToDate(start, Utilerias.FORMAT_DATE));
						}
						
						if (!Utilerias.isNullOrUndefined(end)) {
							prod.setFechaFinalVentaEspecial(Utilerias.transformStringToDate(end, Utilerias.FORMAT_DATE));
						}
						
																
						prod.setMontoInicial(Utilerias.strToFloat(request.getParameter("monto")));
						prod.setMontoFactura(prod.getMontoInicial());				
						prod.setActivo(Utilerias.YES_VALUE);
						
						boolean act = false;
						    		    		    		    		    		
            			if ( request.getParameter( "Modificar" ) != null ) {
                         				
                			if (prod.getVentaEspecialId() < 0) { %>
                    			<script>alert("Necesitas llenar el campo de clave de venta especial");</script>
                			<% } else {             
             	     			readonly = "readonly";     		             
		             			act = true;
		             			prod = (VentaEspecial) catalogoMGR.getObjectData(prod);		             			
	             			}
             			} else if ( request.getParameter( "Actualizar" ) != null ) {
               
                			readonly = "readonly";
                			result = catalogoMGR.validarCamposLlenos(prod);
    			 			                	  
                 			if (result.equals("")) {                				
                	
                				int opera = catalogoMGR.dmlOperations(1, prod);
                				if ( opera > -1 ) {
                 					prod = new VentaEspecial();
                 				}
                 				result = "Venta Especial actualizada satisfactoriamente";
                 	
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
                 					prod = new VentaEspecial();
                 					readonly = "";
                 					result = "Venta Especial insertada satisfactoriamente";
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
               					prod = new VentaEspecial();
               					result = "Registro eliminado satisfactoriamente";    		  					
    		   				} else {               
               					result = "Problemas al eliminar el registro";
               				}                  
            				
            				response.sendRedirect(request.getContextPath() + "/manager/ventasEspeciales.jsp?sessionid=" + session.getId() + "&result=" + result);
            				
          				} %>
    		    			    		
		    					    			
		    			<p><h2>Administraci&oacute;n de Ventas Especiales</h2></p>
		    
		    			<form action="modventasEspeciales.jsp?sessionid=<%= session.getId() %>" method="post">
		    				<% if (act) {  %>
		    				<p>Clave de la Venta Especial:
		    					<input type="text" name="ventaEspecialId" value="<%= Utilerias.mostrarCamposFront(prod.getVentaEspecialId()) %>" <%= readonly %>>
		    				</p>
		    				<% } %>
		    				
		    				<p>Fecha Inicial:
		    					<input type="text" name="start" id="start" maxlength="10" size="10" value="<%= start %>" readonly="true">
    							<a href="javascript:NewCal('start','ddmmyyyy')">
    								<img src="../images/cal.gif" width="16" height="16" border="0" alt="Selecciona una fecha">
    							</a>
		    				</p>
		    				
		    				<p>Fecha Final:
		    					<input type="text" name="end" id="end" maxlength="10" size="10" value="<%= end %>" readonly="true">
    							<a href="javascript:NewCal('end','ddmmyyyy')">
    								<img src="../images/cal.gif" width="16" height="16" border="0" alt="Selecciona una fecha">
    							</a>
		    				</p>
		    				
		    						    				
		    				<p>Monto de la Venta Especial Estimado:
		    					<input type="text" name="monto" value="<%= Utilerias.mostrarCamposFront(prod.getMontoInicial()) %>">
		    				</p>
		    				
		    				<p>Cliente:
  							  <select name="clienteId">
  							    <% 
  							    	Cliente cli = new Cliente();
  							  		ArrayList<Cliente> lista = (ArrayList<Cliente>) catalogoMGR.getList(cli);
									for (int i=0; i < lista.size(); i++) { 
  									    cli = (Cliente) lista.get(i);
  									%>
      									<option value="<%= cli.getClienteId() %>" <% if (prod.getCliente() != null && cli.getClienteId() == prod.getCliente().getClienteId()) out.println("selected"); %>><%= cli.getNombreCliente() %></option>
    							   <% } %>
    							</select>
  							</p>
  							
  							<p>Producto de la Venta Especial:
  								<select name="productoVentaEspecialId">
  								    <% 
  							    	Producto pro = new Producto();
  							  		ArrayList<Producto> listaProd = (ArrayList<Producto>) catalogoMGR.getList(pro);
									for (int i=0; i < listaProd.size(); i++) { 
  									    pro = (Producto) listaProd.get(i);
  									    if (pro.getTipoProducto() != null && pro.getTipoProducto().equals(Utilerias.TIPO_PRODUCTO_VENTA_ESPECIAL_VALUE)) { %>
      										<option value="<%= pro.getProductoId() %>" <% if (prod.getProducto() != null && pro.getProductoId() == prod.getProducto().getProductoId()) out.println("selected"); %>><%= pro.getNombreProducto() %></option>
      									<% } %>	
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