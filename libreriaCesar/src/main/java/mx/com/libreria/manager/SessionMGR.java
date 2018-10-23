package mx.com.libreria.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionMGR {

	public boolean validarSesion(final HttpServletRequest request, final HttpServletResponse  response) {
		
		HttpSession session = request.getSession();
		
		boolean valida = true;
		if (!session.getId().equals(request.getParameter("sessionid"))) {
    		Logs.debug(InventarioMGR.class,  
    			"Sesiones invalidas: " + 
    			"sesion creada del usuario: " + session.getId() + "    " +  
    			"sesion del request particular: " + request.getParameter("sessionid") + 
    			" difieren.");
    		session.invalidate();
    		valida = false;
		}
		return valida;    	
	}
	
	public Object getAttributeSession(final HttpServletRequest request, final String nameAttribute) {
		HttpSession session = request.getSession();
		return session.getAttribute(nameAttribute);
	}
	
	public void setAttributeSession(final HttpServletRequest request, final String nameAttribute, final Object val) {
		HttpSession session = request.getSession();
		session.setAttribute(nameAttribute, val);
	}

}
