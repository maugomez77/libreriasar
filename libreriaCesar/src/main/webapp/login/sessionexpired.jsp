<%@ include file="../admin/Header.jsp" %>

<% response.setHeader("Refresh", "3; URL=Logout.jsp"); %>

<h1>Tu sesion ha expirado, te redireccionara en 3 segundos</h1>

<a href="Logout.jsp"><font color="black" size="5">Login</font></a>

<%@ include file="../admin/Footer.jsp" %>

</body></html>