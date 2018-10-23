package mx.com.libreria.implementacion.servicios;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;

public class BaseServiceTestCase extends TestCase {

    ApplicationContext context;
    
    protected void setUp() throws Exception {
    	
    	super.setUp();
        
        String[] configLocationFiles = new String[]{
                "persistence-context.xml",
                "service-context.xml"
        };
        
        context = new ClassPathXmlApplicationContext(configLocationFiles);
              		                     
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * JUnit pide al menos un m&eacute;todo de prueba
     * @throws Exception
     */
    public void testDefault() throws Exception {
        assertTrue(true);
    }
    
    public static void main(String args[]) throws Exception {
    	BaseServiceTestCase bla = new BaseServiceTestCase();
    	bla.setUp();
    	//bla.context.getBean(arg0)
    }
}
