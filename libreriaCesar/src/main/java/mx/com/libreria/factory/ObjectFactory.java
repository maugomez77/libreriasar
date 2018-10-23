package mx.com.libreria.factory;

import mx.com.libreria.interfases.servicios.ServiciosBO;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Clase para acceder a los beans definidos en archivos de
 * configuración para el acceso a datos y otras beans de funciones multiples.
 */
public final class ObjectFactory {
    /**
     * Variable del applicationContext.
     */
    private static ApplicationContext applicationContext;

	private static String[] configLocationFiles = new String[]{
            "persistence-context.xml",
            "service-context.xml", 
            "Timer.xml"
    };

    /**
     * Constructor por default privado.
     */
    private ObjectFactory() {
    }

    /**
     * Metodo para acceder a un applicationContext.
     * @return ApplicationContext
     */
    private static ApplicationContext getInstance() {
        if (ObjectFactory.applicationContext == null) {
            //applicationContext = 
            //new FileSystemXmlApplicationContext
            //("/src/com/hsbc/hbmx/resources/ApplicationContext.xml");
            //applicationContext = new XmlWebApplicationContext();
            
        	//ObjectFactory.applicationContext = 
            //    new ClassPathXmlApplicationContext(
            //        "/mx/com/rosti/resources/ApplicationContext.xml");
        	
        	ObjectFactory.applicationContext = new ClassPathXmlApplicationContext(configLocationFiles);
        }

        return ObjectFactory.applicationContext;
    }

    /**
     * Metodo para traer un bean de los archivos de configuración.
     * @param  pBeanId   Nombre del Bean
     * @return Object    Objeto tomado de los archivos de configuración
     */
    public static Object getBean(final String pBeanId) {
        return getInstance().getBean(pBeanId);        
    }
    
    public static void main(String args[]) { 
    	ServiciosBO servicios = (ServiciosBO) ObjectFactory.getBean("servicioBO");
    	servicios.getClass();    	
    }
}
