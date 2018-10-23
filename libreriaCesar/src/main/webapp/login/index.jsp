<%@ page import="mx.com.libreria.manager.Utilerias" %>

<jsp:useBean id = "dbObj"
			     scope = "page"
			     class = "mx.com.libreria.manager.DatabaseMGR" />

<html>  

	<%@ include file="../admin/Header.jsp" %>

	<body>
		<div class="wrapper1">
			<div class="wrapper">
				<%@ include file="../admin/MenuHeader.jsp" %>			
				<div class="content">
					<p>Bienvenido al Sistema de Administraci&oacute;n de Librerias.</p>
					
					<p>Aqui se pueden administrar diversos inventarios.</p>
										        	
    				<% if (!dbObj.isConnected()) { %>		
						<p>No puedes acceder debido a problemas tecnicos: El servidor de base de datos no esta corriendo. <br/>
						   Favor de verificarlo con el administrador.</p>
					<% } else if (!session.isNew()) { 
						response.setHeader("Refresh", "0; URL=" + request.getContextPath() + Utilerias.LOGIN_INDEX);
						session.invalidate();
					} else { %> 
																	
						<form action="<%out.println(request.getContextPath());%>/login/Welcome.jsp" method="post">
	    					<table align="center">
	  							<tbody>
	    							<tr>
	      								<td><font size="2" face="verdana">Usuario:</font></td>
	      								<td><input type="text" name="uname" size="15" maxlength="20"></td>	      										      				
	    							</tr>
	    							<tr>
	      								<td><font size="2" face="verdana">Contrase&ntilde;a:</font></td>
				      					<td>
	      									<input type="password" name="passwd" size="15" maxlength="20">	      										
	      								</td>	      					
	    							</tr>
	    							<tr>
		  		    					<td><input type="submit" value="Entrar"></td>
	      				</form>	      								
	      								<form action="<%out.println(request.getContextPath());%>/login/Recover.jsp" method="post">
	      									<td><input type="submit" name="" value="Olvidaste tu Contrase&ntilde;a"></td>
	      								</form>	      						      						      				
	    							</tr>
	  							</tbody>
							</table>				
						
						<center>
							<a href="<%out.println(request.getContextPath() + Utilerias.LOGIN_REGISTER_NEW);%>"><img src="<%out.println(request.getContextPath());%>/images/crearUsuario.gif" width="150" height="30" alt="Registrar usuario"/></a>
						</center>						
					<% } %>											
				
				    		<%@ include file="../admin/Footer.jsp" %>    
  			</div> <% //se cierra content %>   		
  		</div><% //se cierra wrapper %>
  	</div><% //se cierra wrapper1 %>
	</body>
</html>