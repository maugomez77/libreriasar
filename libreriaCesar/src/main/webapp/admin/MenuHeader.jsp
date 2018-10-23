  	<div class="nav-wrapper">
			<div class="nav-left"></div>
			
			<div class="nav">
				<ul id="navigation">
			   		<li class="active">
						<a href="#">
							<span class="menu-left"></span>
							<span class="menu-mid">Home</span>
							<span class="menu-right"></span>
						</a>
					</li>
					
					<li class="">
						<a href="#">
							<span class="menu-left"></span>
							<span class="menu-mid">Admon Sis</span>
							<span class="menu-right"></span>
						</a>
						
						<% if (session.getAttribute("uname") != null && !session.getAttribute("uname").toString().equals("")) { %>			   		
	            	   	<div class="sub">
			   				<ul>
			   					<li>
									<a href="<%out.println(request.getContextPath());%>/catalogos/usuarios.jsp?sessionid=<%= session.getId() %>">Usuarios</a>
								</li>
								<li>
									<a href="<%out.println(request.getContextPath());%>/catalogos/roles.jsp?sessionid=<%= session.getId() %>">Roles</a>
								</li>																			   			
			   				</ul>
			   				<div class="btm-bg"></div>
			   			</div>			   			
			   			<% } %>
			   						   		
					</li>
					
					<li class="">
						<a href="#">
							<span class="menu-left"></span>
							<span class="menu-mid">Cat&aacute;logos</span>
							<span class="menu-right"></span>
						</a>
						
						<% if (session.getAttribute("uname") != null && !session.getAttribute("uname").toString().equals("")) { %>			   		
	            	   	<div class="sub">
			   				<ul>
			   					<li>
									<a href="<%out.println(request.getContextPath());%>/catalogos/productos.jsp?sessionid=<%= session.getId() %>">Productos</a>
								</li>
								<li>
									<a href="<%out.println(request.getContextPath());%>/catalogos/busquedaProductos.jsp?sessionid=<%= session.getId() %>">Busqueda Productos Por Proveedor</a>
								</li>
								<li>
									<a href="<%out.println(request.getContextPath());%>/catalogos/busquedaProductosNombre.jsp?sessionid=<%= session.getId() %>">Busqueda Productos Por Nombre</a>
								</li>								
			   					<li>
									<a href="<%out.println(request.getContextPath());%>/catalogos/clientes.jsp?sessionid=<%= session.getId() %>">Clientes</a>
								</li>
								<li>
									<a href="<%out.println(request.getContextPath());%>/catalogos/proveedores.jsp?sessionid=<%= session.getId() %>">Proveedores</a>
								</li>
								<li>
									<a href="<%out.println(request.getContextPath());%>/catalogos/tipogasto.jsp?sessionid=<%= session.getId() %>">Tipo Gasto</a>
								</li>
								<li>
									<a href="<%out.println(request.getContextPath());%>/catalogos/empleados.jsp?sessionid=<%= session.getId() %>">Empleados</a>
								</li>																											   										
			   				</ul>
			   				<div class="btm-bg"></div>
			   			</div>			   			
			   			<% } %>
			   						   		
					</li>
										
			   		<li class="">
						<a href="#">
							<span class="menu-left"></span>
							<span class="menu-mid">Compra / Venta</span>
							<span class="menu-right"></span>
						</a>
						<% if (session.getAttribute("uname") != null && !session.getAttribute("uname").toString().equals("")) { %>						
			   			<div class="sub">
			   				<ul>
			   					<li>
									<a href="<%out.println(request.getContextPath());%>/manager/inventarios.jsp?sessionid=<%= session.getId() %>">Compras</a>									
								</li>								
			   					<li>
									<a href="<%out.println(request.getContextPath());%>/manager/remisiones.jsp?sessionid=<%= session.getId() %>">Ventas</a>																	
								</li>								
			   					<li>
									<a href="<%out.println(request.getContextPath());%>/manager/cuentasPorPagar.jsp?sessionid=<%= session.getId() %>">Cuentas por Pagar</a>									
								</li>			   				
								<li>
									<a href="<%out.println(request.getContextPath());%>/manager/cuentasPorCobrar.jsp?sessionid=<%= session.getId() %>">Cuentas por Cobrar</a>									
								</li>			   																				
			   				</ul>
			   				<div class="btm-bg"></div>
			   			</div>
			   			<% } %>
			   		</li>
			   		
			   		<li class="">
						<a href="#">
							<span class="menu-left"></span>
							<span class="menu-mid">Especiales</span>
							<span class="menu-right"></span>
						</a>
						<% if (session.getAttribute("uname") != null && !session.getAttribute("uname").toString().equals("")) { %>						
			   			<div class="sub">
			   				<ul>
			   					<li>
									<a href="<%out.println(request.getContextPath());%>/manager/suscripciones.jsp?sessionid=<%= session.getId() %>">Registrar Suscripciones</a>									
								</li>			   												
								<li>
									<a href="<%out.println(request.getContextPath());%>/manager/entregarSuscripciones.jsp?sessionid=<%= session.getId() %>">Entregar Suscripcion</a>									
								</li>			   																				
								<li>
									<a href="<%out.println(request.getContextPath());%>/manager/ventasEspeciales.jsp?sessionid=<%= session.getId() %>">Registrar Ventas Especiales</a>									
								</li>
								<li>
									<a href="<%out.println(request.getContextPath());%>/manager/abonoEntVE.jsp?sessionid=<%= session.getId() %>">Admon Ventas Especiales</a>									
								</li>								
								<li>
									<a href="<%out.println(request.getContextPath());%>/manager/gastos.jsp?sessionid=<%= session.getId() %>">Registrar Gastos</a>																	
								</li>
								<li>
									<a href="<%out.println(request.getContextPath());%>/manager/desactivar.jsp?sessionid=<%= session.getId() %>">Desactivar Compras / Ventas / Suscripciones / Ventas Especiales / Gastos</a>									
								</li>
			   				</ul>
			   				<div class="btm-bg"></div>
			   			</div>
			   			<% } %>
			   		</li>
			   			
			   		<li class="">
						<a href="#">
							<span class="menu-left"></span>
							<span class="menu-mid">Reportes</span>
							<span class="menu-right"></span>
						</a>
						<% if (session.getAttribute("uname") != null && !session.getAttribute("uname").toString().equals("")) { %>						
			   			<div class="sub">
			   				<ul>
			   					<li>
			   						<a href="<%out.println(request.getContextPath());%>/reportes/estadoResultados.jsp?sessionid=<%= session.getId() %>">Estado de Resultados</a>																	
								</li>
								<li>
			   						<a href="<%out.println(request.getContextPath());%>/reportes/flujoEfectivo.jsp?sessionid=<%= session.getId() %>">Flujo de Efectivo</a>																	
								</li>			   					
								<li>
									<a href="<%out.println(request.getContextPath());%>/reportes/ventasReportes.jsp?sessionid=<%= session.getId() %>">Reporte de Ventas</a>
								</li>
								<li>
									<a href="<%out.println(request.getContextPath());%>/reportes/suscripcionReportes.jsp?sessionid=<%= session.getId() %>">Reporte de Suscripciones</a>
								</li>
								<li>
									<a href="<%out.println(request.getContextPath());%>/reportes/ventasEspecialesReportes.jsp?sessionid=<%= session.getId() %>">Reporte de Ventas Especiales</a>
								</li>								
								<li>
									<a href="<%out.println(request.getContextPath());%>/reportes/comprasReportes.jsp?sessionid=<%= session.getId() %>">Reporte de Compras</a>
								</li>
								<li>
									<a href="<%out.println(request.getContextPath());%>/reportes/inventariosReportes.jsp?sessionid=<%= session.getId() %>">Reporte de Inventarios</a>									
								</li>
								<li>
									<a href="<%out.println(request.getContextPath());%>/reportes/inventariosReportesProveedor.jsp?sessionid=<%= session.getId() %>">Reporte de Inventarios Por Proveedor</a>									
								</li>								
								<li>
									<a href="<%out.println(request.getContextPath());%>/reportes/gastosAplicadosReportes.jsp?sessionid=<%= session.getId() %>">Reporte de Gastos Aplicados</a>									
								</li>								
			   				</ul>
			   				<div class="btm-bg"></div>
			   			</div>
			   			<% } %>
			   		</li>								
			   			   					   	
			   		<li class="last">
						<a href="">
							<span class="menu-left"></span>
							<span class="menu-mid">Creditos</span>
							<span class="menu-right"></span>
						</a>
						<% if (session.getAttribute("uname") != null && !session.getAttribute("uname").toString().equals("")) { %>
						<div class="sub">
			   				<ul>
			   					<li>
									<a href="<%out.println(request.getContextPath());%>/manager/webmaster.jsp?sessionid=<%= session.getId() %>">Contacte a WebMaster</a>																	
								</li>
			   					<li>
									<a href="../login/Logout.jsp">Logout</a>
								</li>			   					
			   				</ul>
			   				<div class="btm-bg"></div>
			   			</div>
			   			<% } %>
			   		</li>			   										   					   					   				
			   	</ul>
			</div>
			<div class="nav-right"></div>
		</div>  	  		
  	