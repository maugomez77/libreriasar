package mx.com.libreria.timer;

import java.text.SimpleDateFormat;

import java.util.Date;

import mx.com.libreria.factory.ObjectFactory;

import mx.com.libreria.manager.CatalogMGR;
import mx.com.libreria.manager.InventarioHistoricoMGR;
import mx.com.libreria.manager.Logs;
import mx.com.libreria.manager.Utilerias;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Job;

public class ExampleJob implements Job {

	private SimpleDateFormat sdf = new SimpleDateFormat(Utilerias.FORMAT_DATE_WITH_HOUR);
	
	private int timeout;

	/**
	 * Setter called after the ExampleJob is instantiated with the value from
	 * the JobDetailBean (5)
	 */
	public void setTimeout(int pTimeout) {
		this.timeout = pTimeout;
		
		Logs.debug(ExampleJob.class, "Variable del timeout capturada mauro -> " + timeout);
	}

	public void execute(JobExecutionContext ctx) throws JobExecutionException {		
		// do the actual work
		TestScheduler();			
	}

	private String printDate() { 
		Date curDate = new Date();		
		return sdf.format(curDate);
	}
	
	public void TestScheduler() {
		
		Logs.debug(ExampleJob.class, "Iniciando traspaso de inventarios a las: " + printDate());
		
		InventarioHistoricoMGR hist = (InventarioHistoricoMGR) ObjectFactory.getBean("inventarioHistoricoMGR");
		CatalogMGR catalogoMGR = (CatalogMGR) ObjectFactory.getBean("catalogoMGR");
		
    	Logs.debug(ExampleJob.class, "Resultado Del Movimiento de Inventario: " + hist.moverInventarioActualAHistorico(catalogoMGR));
		
    	Logs.debug(ExampleJob.class, "Empieza eliminación de registro no necesarios en inventarios");
    	
    	Logs.debug(ExampleJob.class, "Resultado de eliminación de registro no necesarios en inventarios: " + hist.eliminarSnapShotNoNecesarios(catalogoMGR));
    	    	    	
    	Logs.debug(ExampleJob.class, "Termino el proceso de inventarios: " + printDate());
	}
	
	public static void main(String[] args) { 
		new ExampleJob().TestScheduler();
	}
}