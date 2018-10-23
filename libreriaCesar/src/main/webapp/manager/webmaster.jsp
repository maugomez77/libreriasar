<%@ include file="../admin/Secure.jsp" %>

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
					  && session.getAttribute("uname") != null){  %>
		
						<p><h1>Bienvenido. </h1></p>
						
						<p><h2>Los datos del webmaster para cualquier duda o comentario:</h2></p>
						<p>Correo electronico: mauricio.gomez.77@gmail.com</p>
						<p>Facebook: mauriciogomez77</p>
						<p>Telefono celular: 722 5702847</p>						
						<p>Cualquier duda estamos a sus ordenes.</p>

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
