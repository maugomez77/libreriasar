package mx.com.libreria.beans;

import java.io.Serializable;

public class EstadoResultados implements Serializable {
	
	/**
	 *  
	 */
	private static final long serialVersionUID = -4215874003679087967L;
		
	private float ventas;	
	
	private float costoDeVentas;
	
	private float inventarioInicial;
	private float compras;
	private float inventarioFinal;	
	
	private float utilidadBruta;
	
	private float gastosOperacion;
	
	private float gastosVenta;
	private float gastosAdmon;		
	private float gastosIndirectos;
	
	private float utilidadOperacion;	
	
	private float otrosGastos;
	
	private float utilidadNeta;

	public float getVentas() {
		return ventas;
	}

	public void setVentas(float ventas) {
		this.ventas = ventas;
	}

	public float getCostoDeVentas() {
		return costoDeVentas;
	}

	public void setCostoDeVentas(float costoDeVentas) {
		this.costoDeVentas = costoDeVentas;
	}

	public float getInventarioInicial() {
		return inventarioInicial;
	}

	public void setInventarioInicial(float inventarioInicial) {
		this.inventarioInicial = inventarioInicial;
	}

	public float getCompras() {
		return compras;
	}

	public void setCompras(float compras) {
		this.compras = compras;
	}

	public float getInventarioFinal() {
		return inventarioFinal;
	}

	public void setInventarioFinal(float inventarioFinal) {
		this.inventarioFinal = inventarioFinal;
	}

	public float getUtilidadBruta() {
		return utilidadBruta;
	}

	public void setUtilidadBruta(float utilidadBruta) {
		this.utilidadBruta = utilidadBruta;
	}

	public float getGastosOperacion() {
		return gastosOperacion;
	}

	public void setGastosOperacion(float gastosOperacion) {
		this.gastosOperacion = gastosOperacion;
	}

	public float getGastosVenta() {
		return gastosVenta;
	}

	public void setGastosVenta(float gastosVenta) {
		this.gastosVenta = gastosVenta;
	}

	public float getGastosAdmon() {
		return gastosAdmon;
	}

	public void setGastosAdmon(float gastosAdmon) {
		this.gastosAdmon = gastosAdmon;
	}

	public float getGastosIndirectos() {
		return gastosIndirectos;
	}

	public void setGastosIndirectos(float gastosIndirectos) {
		this.gastosIndirectos = gastosIndirectos;
	}

	public float getUtilidadOperacion() {
		return utilidadOperacion;
	}

	public void setUtilidadOperacion(float utilidadOperacion) {
		this.utilidadOperacion = utilidadOperacion;
	}

	public float getOtrosGastos() {
		return otrosGastos;
	}

	public void setOtrosGastos(float otrosGastos) {
		this.otrosGastos = otrosGastos;
	}

	public float getUtilidadNeta() {
		return utilidadNeta;
	}

	public void setUtilidadNeta(float utilidadNeta) {
		this.utilidadNeta = utilidadNeta;
	}	
}
