package mx.com.libreria.model;

import java.io.Serializable;

import mx.com.libreria.manager.Constants;

public enum TipoGastoEnum implements Serializable, Constants {
	
	Venta(2), Indirectos(3), Administrativos(4), OtrosGastos(5); 
		
	private int id;
	
	private TipoGastoEnum(int id) { 
		this.id = id;
	}
	
	public int getId() { 
		return id;
	}	
}