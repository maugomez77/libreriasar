<%@ include file="../admin/Secure.jsp" %>

<%@ page import="mx.com.libreria.manager.Utilerias" %>

<% 		
		response.setContentType("text/html");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control","no-store");  		
  		response.setDateHeader("Expires", 0);  		
  		
%>

	<% if(!session.getId().equals(request.getParameter("sessionid"))){//then trying to log out w/o having logged in.

		session.invalidate();
		response.sendRedirect(request.getContextPath() + Utilerias.LOGIN_INDEX);
		
	} else {//person is logged in, so let person log out.
		
		session.removeAttribute("user");
		session.removeAttribute("name");	
		session.removeAttribute("passwd");
		session.removeAttribute("rolId");		
		
		session.invalidate();				
		
		response.setContentType("text/html");
		response.sendRedirect(request.getContextPath() + Utilerias.LOGIN_INDEX);
		
		%>

		<HTML>
		<BODY BGCOLOR=lightgreen>
		<BR>
		<CENTER>
		<FONT SIZE=5>Te has deslogeado exitosamente.</FONT><BR><BR>
		<a href="<% out.println(request.getContextPath() + Utilerias.LOGIN_INDEX); %>">Log In</a>
		</CENTER>
		</BODY>
		</HTML>
		
	<% } %>