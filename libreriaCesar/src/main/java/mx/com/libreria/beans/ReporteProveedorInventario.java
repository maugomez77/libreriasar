package mx.com.libreria.beans;

import java.io.Serializable;

import mx.com.libreria.model.Producto;
import mx.com.libreria.model.Proveedor;

public class ReporteProveedorInventario implements Serializable {

	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = -5589678423765328488L;

	private Proveedor proveedor;
	private Producto producto;
	private int cantidad;
	
	public Proveedor getProveedor() {
		return proveedor;
	}
	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}
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
