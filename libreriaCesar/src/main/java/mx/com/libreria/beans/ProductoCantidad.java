package mx.com.libreria.beans;

import java.io.Serializable;

import mx.com.libreria.model.Producto;

public class ProductoCantidad implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6077865808787562238L;
	
	private Producto producto;
	private int cantidad;
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	
}
