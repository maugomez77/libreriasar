<%@ include file="../admin/Secure.jsp" %>

<%@ page import="mx.com.libreria.manager.Utilerias" %>

	
				<% if(request.getParameter("sessionid") != null 
					  && session.getId().equals(request.getParameter("sessionid"))
					  && session.getAttribute("uname") != null){  %>
      				      					  
      					<% if (request.getParameter("reporte") != null && request.getParameter("reporte").equals("estadoResultados")) { %>
    						<jsp:include page="reportesEstadoResultados.jsp"/>
						<%  } else if (request.getParameter("reporte") != null && request.getParameter("reporte").equals("flujoEfectivo")) { %>
    						<jsp:include page="reportesFlujoEfectivo.jsp"/>    						
						<%  } else if (request.getParameter("reporte") != null && request.getParameter("reporte").equals("inventarios")) { %>
    						<jsp:include page="reportesInventarios.jsp"/>
						<%  } else if (request.getParameter("reporte") != null && request.getParameter("reporte").equals("ventas")) { %>
    						<jsp:include page="reportesVentas.jsp"/>
						<%  } else if (request.getParameter("reporte") != null && request.getParameter("reporte").equals("compras")) { %>
    						<jsp:include page="reportesCompras.jsp"/>
						<%  } else if (request.getParameter("reporte") != null && request.getParameter("reporte").equals("gastosAplicados")) { %>
    						<jsp:include page="reportesGastosAplicados.jsp"/>
						<%  } else if (request.getParameter("reporte") != null && request.getParameter("reporte").equals("entradaInventario")) { %>
    						<jsp:include page="reportesEntradaInventario.jsp"/>
						<%  } else if (request.getParameter("reporte") != null && request.getParameter("reporte").equals("entradaRemision")) { %>
    						<jsp:include page="reportesEntradaRemision.jsp"/>
						<%  } else if (request.getParameter("reporte") != null && request.getParameter("reporte").equals("suscripcion")) { %>
							<jsp:include page="reportesSuscripcion.jsp"/>
						<%  } else if (request.getParameter("reporte") != null && request.getParameter("reporte").equals("ventasEspeciales")) { %>
							<jsp:include page="reportesVentasEspeciales.jsp"/>
						<%  }  %>	

						<br/>
			    		<input type="submit" value="Imprimir" onclick="javascript:window.print();">	    	
	    				<input type="submit" value="Cerrar Pagina" onclick="javascript:window.close();">
    					   

      			<% } else { 
  					session.invalidate();    
  					response.sendRedirect(request.getContextPath() + Utilerias.LOGIN_INDEX);		  							
				} %>				
									