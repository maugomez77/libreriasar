<% if (!request.isSecure()) {

	String serverString = request.getServerName() + ":8443"; //servidor
	//String serverString = request.getLocalAddr() + ":8443"; //servidor
	String pathString = request.getContextPath();
	String pageLoaded = request.getServletPath();

	//response.sendRedirect("https://" + serverString + pathString + pageLoaded);

} %>