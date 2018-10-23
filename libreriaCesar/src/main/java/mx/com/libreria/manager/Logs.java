package mx.com.libreria.manager;

import org.apache.log4j.Logger;

public class Logs {
		
	public static void info(Class<?> clazz, String message) {
		Logger.getLogger(clazz).info("Clase info: " + clazz.getName() + " " + message);
	}
	
	public static void debug(Class<?> clazz, String message) { 
		Logger.getLogger(clazz).debug("Clase debug: " + clazz.getName() + " " + message);		
	}
		
	public static void error(Class<?> clazz, String message) { 
		Logger.getLogger(clazz).error("Clase error: " + clazz.getName() + " " + message);		
	}
}
