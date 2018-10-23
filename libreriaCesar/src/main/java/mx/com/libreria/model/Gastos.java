package mx.com.libreria.model;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import mx.com.libreria.marcador.Marker;

@Entity
@Table(name = "GASTOS")
public class Gastos implements Serializable, Marker {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7873877227285295035L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="generatorName")
	@TableGenerator(name="generatorName", table="secuencias",  
	                pkColumnName="tablename", // TableID.TableName (value = table_name, test_table, etc.)  
	                valueColumnName="id", // TableID.ID (value = 1,2,3,etc.)  
	                allocationSize=1 // flush every 1 insert  
				   )
	@Column(name = "GASTOS_ID")
	private int gastosId;
		
	@Column(name = "FECHA_APLICACION", nullable=false)
	private Date fechaAplicacion;
			
	@Column(name = "MONTO_GASTO")
	private float montoGasto;
			
	@OneToOne(cascade = CascadeType.ALL)
    private TipoGasto tipoGasto;

	@Column(name = "ACTIVO", length=1, nullable=false)
	private String activo;

	public int getGastosId() {
		return gastosId;
	}

	public void setGastosId(int gastosId) {
		this.gastosId = gastosId;
	}

	public Date getFechaAplicacion() {
		return fechaAplicacion;
	}

	public void setFechaAplicacion(Date fechaAplicacion) {
		this.fechaAplicacion = fechaAplicacion;
	}

	public float getMontoGasto() {
		return montoGasto;
	}

	public void setMontoGasto(float montoGasto) {
		this.montoGasto = montoGasto;
	}

	public TipoGasto getTipoGasto() {
		return tipoGasto;
	}

	public void setTipoGasto(TipoGasto tipoGasto) {
		this.tipoGasto = tipoGasto;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	
}
