package mx.com.libreria.beans;

import java.io.Serializable;

public class CompraInventarioBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5233665582781024386L;

	private int comprasId;
	private int inventarioId;
	
	public int getComprasId() {
		return comprasId;
	}
	public void setComprasId(int comprasId) {
		this.comprasId = comprasId;
	}
	public int getInventarioId() {
		return inventarioId;
	}
	public void setInventarioId(int inventarioId) {
		this.inventarioId = inventarioId;
	}
	
	
}
