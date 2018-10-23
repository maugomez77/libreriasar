package mx.com.libreria.mail.context;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Session;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import mx.com.libreria.mail.context.interfases.ContextConstant;

public final class ContextEmail implements ContextConstant {
    
	private static Context initCtx;
	private static Context envCtx;
    
	private ContextEmail() { 
		
	}
	
	@SuppressWarnings("unused")
	private static Context getContext() throws NamingException { 
		if (initCtx == null) {
			Properties prop = new Properties();
			prop.setProperty(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
			prop.setProperty(Context.URL_PKG_PREFIXES, URL_PKGS);
			initCtx = new InitialContext(prop);
		    envCtx = (Context) initCtx.lookup(ENV_JAVA);
		    
		}  
		return envCtx;
	}
	/*
	public static Session getSessionEmail() throws NamingException {
		Object o = getContext().lookup(EMAIL_SESSION);
		Utilerias.debug(ContextEmail.class, "This before cast : " + o.getClass());
		
		javax.mail.Session ses = (javax.mail.Session) o;
		
		Utilerias.debug(ContextEmail.class, "This: " + o.getClass());
		return ses;
	}*/
	
	public static Session getSessionEmail(boolean authFlag, boolean bDebug) { 

		Session session = null;
		
		Properties properties = System.getProperties(); // Gets the System properties
	    properties.put("mail.smtp.host", sSMTPServer);  // Puts the SMTP server name to properties object
		
	    if (authFlag) {
	    	properties.put("mail.smtp.auth", "true");
	    	/* Get the default Session using Properties Object */
	        Authenticator auth = new MyPasswordAuthenticator(USER_AUTH_EMAIL, PASSWORD_USER_AUTH_EMAIL);	
		    session = Session.getDefaultInstance(properties, auth);
		    
	    } else {
	    	session = Session.getDefaultInstance(properties);
	    }

	    //Session session = Session.getDefaultInstance(properties, null);
	    session.setDebug(bDebug);
	    
	    return session;
	
	}
}
