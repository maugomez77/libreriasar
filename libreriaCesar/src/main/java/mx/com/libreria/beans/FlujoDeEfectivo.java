package mx.com.libreria.beans;

import java.io.Serializable;

public class FlujoDeEfectivo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6224756650873081440L;
	
	private float ventasConAbono;
	
	private float comprasConAbono;
	
	private float gastoVenta;
	
	private float gastoIndirecto;
	
	private float gastoAdmon;
	
	private float gastoOtros;
		
	private float ingresos;	
		
	private float egresos;

	public float getVentasConAbono() {
		return ventasConAbono;
	}

	public void setVentasConAbono(float ventasConAbono) {
		this.ventasConAbono = ventasConAbono;
	}

	public float getComprasConAbono() {
		return comprasConAbono;
	}

	public void setComprasConAbono(float comprasConAbono) {
		this.comprasConAbono = comprasConAbono;
	}

	public float getGastoVenta() {
		return gastoVenta;
	}

	public void setGastoVenta(float gastoVenta) {
		this.gastoVenta = gastoVenta;
	}

	public float getGastoIndirecto() {
		return gastoIndirecto;
	}

	public void setGastoIndirecto(float gastoIndirecto) {
		this.gastoIndirecto = gastoIndirecto;
	}

	public float getGastoAdmon() {
		return gastoAdmon;
	}

	public void setGastoAdmon(float gastoAdmon) {
		this.gastoAdmon = gastoAdmon;
	}

	public float getGastoOtros() {
		return gastoOtros;
	}

	public void setGastoOtros(float gastoOtros) {
		this.gastoOtros = gastoOtros;
	}

	public float getIngresos() {
		return ingresos;
	}

	public void setIngresos(float ingresos) {
		this.ingresos = ingresos;
	}

	public float getEgresos() {
		return egresos;
	}

	public void setEgresos(float egresos) {
		this.egresos = egresos;
	}

	
}
