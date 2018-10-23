package mx.com.libreria.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import mx.com.libreria.marcador.Marker;

@Entity
@Table(name = "ABONO")
public class Abono implements Serializable, Marker {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5628155507969679887L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="generatorName")
	@TableGenerator(name="generatorName", table="secuencias",  
	                pkColumnName="tablename", // TableID.TableName (value = table_name, test_table, etc.)  
	                valueColumnName="id", // TableID.ID (value = 1,2,3,etc.)  
	                allocationSize=1 // flush every 1 insert  
				   )
	@Column(name = "ABONO_ID")
	private int abonoId;
	
	@Column(name = "FECHA_ABONO", nullable=false)
	private Date fechaAbono;
		
	/**
	 * C = compra
	 * V = venta
	 */
	@Column(name = "APLICA_COMPRA_O_VENTA", length = 1)    
    private String aplicaCompraOVenta;

	@Column(name = "MONTO")    
    private float monto;

	public int getAbonoId() {
		return abonoId;
	}

	public void setAbonoId(int abonoId) {
		this.abonoId = abonoId;
	}

	public Date getFechaAbono() {
		return fechaAbono;
	}

	public void setFechaAbono(Date fechaAbono) {
		this.fechaAbono = fechaAbono;
	}

	public String getAplicaCompraOVenta() {
		return aplicaCompraOVenta;
	}

	public void setAplicaCompraOVenta(String aplicaCompraOVenta) {
		this.aplicaCompraOVenta = aplicaCompraOVenta;
	}

	public float getMonto() {
		return monto;
	}

	public void setMonto(float monto) {
		this.monto = monto;
	}
	
	
}