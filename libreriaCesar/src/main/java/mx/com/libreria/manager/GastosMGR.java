package mx.com.libreria.manager;

import java.util.ArrayList;
import java.util.List;

import mx.com.libreria.interfases.persistencia.dao.BaseDao;

import mx.com.libreria.model.Gastos;

public class GastosMGR implements Constants {

	SessionMGR sessionMGR;

	BaseDao baseDao;
	
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;		
	}

	public void setSessionMGR(SessionMGR sessionMGR) {
		this.sessionMGR = sessionMGR;		
	}
	
	@SuppressWarnings("rawtypes")
	public List<Gastos> getGastosFechaParametros(String fechaParametroFinal, CatalogMGR catalogoMGR) {
		
		List lista = catalogoMGR.getListParameters(
			"   select gastos.gastosId, 1 " + 
		    "     from Gastos as gastos " + 
	        "    where gastos.fechaAplicacion >= ? " +
	        "      and gastos.fechaAplicacion <= ? " +
		    " order by gastos.tipoGasto desc ", 
	        new Object[]{ Utilerias.transformStringToDate(Utilerias.startDate(fechaParametroFinal), Utilerias.FORMAT_DATE_WITH_HOUR), 
					      Utilerias.transformStringToDate(Utilerias.endDate(fechaParametroFinal), Utilerias.FORMAT_DATE_WITH_HOUR)
					    });
		
		List<Gastos> listaGastos = new ArrayList<Gastos>();
			
		for (int i=0; i < lista.size(); i++) {  
			Object[] elementos = (Object[]) lista.get(i);
			
			Gastos gasto = new Gastos();
			gasto.setGastosId((Integer) elementos[0]);
			gasto = (Gastos) catalogoMGR.getObjectData(gasto);
			listaGastos.add(gasto);
		}
		
		return listaGastos;
	}	
}
