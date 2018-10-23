package mx.com.libreria.manager;

import java.sql.Connection;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import mx.com.libreria.factory.ObjectFactory;

public class DatabaseMGR {

	public boolean isConnected() {
		
		SessionFactory sessionFactory = null;
		Session session = null;
		boolean connected = false;
		try {
			sessionFactory = (SessionFactory) ObjectFactory.getBean("sessionFactory");
			session = sessionFactory.openSession();
			Connection conn = session.connection();
			connected = conn.isValid(0);
			session.close();
			sessionFactory.close();
			
		} catch (Exception e) { 
			Logs.debug(DatabaseMGR.class, "Ha sucedido una excepcion: " + e.toString());
		}
		return connected;		
	}
	
	public static void main(String args[]) { 
		DatabaseMGR db = new DatabaseMGR();
		Logs.debug(DatabaseMGR.class, "Conectado: " + db.isConnected());
		 
	}
}
