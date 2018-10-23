<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.LoginMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.login.Usuario" %>

<jsp:useBean id = "infoUser"
			     scope = "page"
			     class = "mx.com.libreria.model.login.Usuario" />

<%                     
	/** SE MUEVE ESTA LOGICA PORQUE COMO ES LA PANTALLA INICIAL, PARA SABER SI PONER O NO LOS MENUS LA PRIMERA VEZ **/
	
	LoginMGR loginMGR = (LoginMGR) ObjectFactory.getBean("loginMGR");
		  
    infoUser.setUsuarioId(request.getParameter( "uname" ));
    infoUser.setContrasena(request.getParameter( "passwd" ));
        
    int escenario = -1;
    
    if (session.getAttribute("uname") != null && request.getParameter("home") == null) {
    	
        escenario = 0;
        
    } else if (loginMGR.existUserWithPass(infoUser)) {
    	
    	escenario = 1;
        
    	//se escriben las credenciales del usuario
    	infoUser = (Usuario) loginMGR.getDataUser(infoUser);  
  	  
		session.setAttribute("uname", infoUser.getUsuarioId());
		//session.setAttribute("passwd", infoUser.getContrasena());
        session.setAttribute("name", infoUser.getNombreUsuario());
        session.setAttribute("rolId", infoUser.getRolUsuario().getRolId());
        	    
    } else if (!loginMGR.existUser(infoUser)) {
        
        escenario = 2;
        		                    
    } else { 
    	
    	escenario = 3;
          	          	          	          	
    } %>
                
<html>
	<%@ include file="../admin/Header.jsp" %>

	<body>
		<div class="wrapper1">
			<div class="wrapper">
				<%@ include file="../admin/MenuHeader.jsp" %>			
				<div class="content">

					<% if (escenario == 0) { %>
						
						<% response.setHeader("Refresh", "0; URL=" + Utilerias.LOGIN_INDEX); %>
			
						<p>Solo puedes tener una sesion abierta por Cliente<br/>
			   			   Las conexiones que tenias abiertas se cerraran, espera para redireccionarte. . .
			   			</p>			
						<p>Puedes tener a dos usuarios conectados siempre y cuando sean navegadores diferentes: <br/>
						   Ejemplo: Firefox y Explorer
						</p>
						<p>Pero es preferible tener solo un usuario conectado por cliente</p>
			
						<% session.invalidate(); %>
												
					<% } else if (escenario == 1) { %>
                                           
        				<h1>Sistema de Venta para Libreria</h1>
            			<h2>Bienvenido(a) <%= infoUser.getNombreUsuario() %></h2>
            			<p>El sistema te permite administrar lo siguiente: </p>
            			<ul>
            				<li>Inventarios</li>
            				<li>Usuarios</li>
            				<li>Proveedores</li>
            				<li>Reportes</li>
            			</ul>	            	            		
            			<p>Comentarios favor de hacerlos llegar al siguiente correo: mauricio.gomez.77@gmail.com</p>
            									
    				<% } else if (escenario == 2) { %>
    					
    					<h2>Acceso Denegado debido a que este usuario no existe, intente de nuevo</h2>
        				<br/><br/>
        				<a href="<%out.println(request.getContextPath() + Utilerias.LOGIN_INDEX);%>">
        					<img src="<%out.println(request.getContextPath());%>/images/btnRegresar.png" alt="Regresar" />
        				</a>
        				
        			<% } else { %>
        				
        				<h2>Acceso Denegado debido a contraseña o usuario incorrecto, intente de nuevo</h2>
          				<br/><br/>
          				<a href="<%out.println(request.getContextPath() + Utilerias.LOGIN_INDEX);%>">
          					<img src="<%out.println(request.getContextPath());%>/images/btnRegresar.png" alt="Regresar" />
          				</a>
          				
        			<% } %>	
    			<%@ include file="../admin/Footer.jsp" %>    
  				</div> <% //se cierra content %>   		
  			</div><% //se cierra wrapper %>
  		</div><% //se cierra wrapper1 %>  	
	</body>
</html>