package mx.com.libreria.mail.context.interfases;

/**
 * Interfaz de conexión local para el dataSource con acceso a factories.
 */
public interface ContextConstant {
    String INITIAL_CONTEXT_FACTORY = 
        "org.apache.naming.java.javaURLContextFactory";
    /**
     * Initial Factory
     */
    String URL_PKGS = "org.apache.naming";
    
    //String URL_MYSQL = "jdbc:mysql://127.0.0.1/rosti";
    //String BIND_MYSQL = "java:/comp/env/jdbc/RostiDB";    
    
    String ENV_JAVA = "java:/comp/env";
    
    String EMAIL_SESSION = "mail/Session";
    
    String sSMTPServer = "smtp.gmail.com";    
    
    String USER_AUTH_EMAIL = "admoncorreocesar@gmail.com";
    
    String PASSWORD_USER_AUTH_EMAIL = "admoncorreo";
    
    String USER_ADMIN = "admoncorreocesar@gmail.com";
    
	String SUBJECT_SISTEMA = "Envio de Cuenta de LibreNet";
	
	String MESSAGE_BODY = "Que tal <usuarioId>. <br/><br/>" +
			"Esta es tu contrase&ntilde;a en el sistema: <contrasena> <br/><br/>" +
			"Recibe un cordial Saludo.<br/><br/>" + 
			"Ing. Mauricio G&oacute;mez. <br/><br/>" + 
			"Correo electr&oacute;nico: mauricio.gomez.77@gmail.com";
}
