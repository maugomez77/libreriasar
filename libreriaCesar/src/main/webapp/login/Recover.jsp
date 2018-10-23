<%@ page import="mx.com.libreria.factory.ObjectFactory" %>

<%@ page import="mx.com.libreria.manager.LoginMGR" %>
<%@ page import="mx.com.libreria.manager.Utilerias" %>

<%@ page import="mx.com.libreria.model.login.Usuario" %>

<jsp:useBean id = "dbObj"
			     scope = "page"
			     class = "mx.com.libreria.manager.DatabaseMGR" />

<jsp:useBean id = "infoUser"
			     scope = "page"
			     class = "mx.com.libreria.model.login.Usuario" />

<html>
	<%@ include file="../admin/Header.jsp" %>

	<body>
		<div class="wrapper1">
			<div class="wrapper">
				<%@ include file="../admin/MenuHeader.jsp" %>			
				<div class="content">
    	
    	<%  LoginMGR loginMGR = (LoginMGR) ObjectFactory.getBean("loginMGR"); %>
    	
    	<% if (!dbObj.isConnected()) { %>
		
			<p>No puedes acceder debido a problemas tecnicos: El servidor de base de datos no esta corriendo. <br/>
			   Favor de verificarlo con el administrador.</p>

		<% } else { 
				
		       if (request.getParameter("Enviar") != null) {				
		           
		    	   infoUser.setUsuarioId(request.getParameter( "uname" ));		           					
				   String result = loginMGR.validarIdUsuario(infoUser);						
				   
				   if (result.equals("") && !loginMGR.existUser(infoUser)) { 
				       result += "- Este usuario no existe en la Base de Datos -";
		    	   } 
		    			   
		    	   //si no hubo problemas
		    	   if (result.equals("")) {
		               if (loginMGR.sendEmail(infoUser) ){ 
		    		       result += "Tu cuenta ha sido enviada a tu correo electronico";
		    		   } else {
		    			   result += "No se pudo enviar tu password a tu cuenta de correo electronico";								                   	
		               }														
		    	   } 
		    	   
		    	   %> <script>alert("<%= result %>");</script>
						
			<% } %>
		
			
			<form action="<%out.println(request.getContextPath());%>/login/Recover.jsp" method="post">
				
				<table align="center">
					<tbody>
				    	<tr>
				      		<td>
				      			<font size="2" face="verdana">Id de Usuario:</font>
				      		</td>
				      		<td>
				      			<input type="text" name="uname" size="15" maxlength="20">				      			
				      		</td>
				    	</tr>
				    	<tr>
				      		<td>
				      			<input type="submit" name = "Enviar" value="Enviar">
				      		</td>
				      		<td>
				      			<a href="<%out.println(request.getContextPath() + Utilerias.LOGIN_INDEX);%>">
          							<img src="<%out.println(request.getContextPath());%>/images/btnRegresar.png" alt="Regresar" />
          						</a>
				      		</td>
			</form>	      					
				      		
				  	</tbody>
				</table>					
				
		<% } %>
		
    		<%@ include file="../admin/Footer.jsp" %>    
  			</div> <% //se cierra content %>   		
  		</div><% //se cierra wrapper %>
  	</div><% //se cierra wrapper1 %>
  	
</body>
</html>