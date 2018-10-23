package mx.com.libreria.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import mx.com.libreria.marcador.Marker;

@Entity
@Table(name = "TIPO_GASTO")
public class TipoGasto implements Serializable, Marker {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 2964349196254834052L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="generatorName")
	@TableGenerator(name="generatorName", table="secuencias",  
	                pkColumnName="tablename", // TableID.TableName (value = table_name, test_table, etc.)  
	                valueColumnName="id", // TableID.ID (value = 1,2,3,etc.)  
	                allocationSize=1 // flush every 1 insert  
				   )
	@Column(name = "TIPO_GASTO_ID")	
	private int tipoGastoId;
	
	@Column(name = "NOMBRE_GASTO")
	private String nombreGasto;
	
	/**
	 * Hibernate maneja otra especie de indexacion, contando desde el 0 para guardarlos en la bd, 
	 * pero el mapeo con los objetos java es el correcto.
	 */
	@Enumerated(EnumType.ORDINAL)
	private TipoGastoEnum tipoGastoEnum;
	
	@Column(name = "DESC_GASTO")
	private String descGasto;

	public int getTipoGastoId() {
		return tipoGastoId;
	}

	public void setTipoGastoId(int tipoGastoId) {
		this.tipoGastoId = tipoGastoId;
	}

	public String getNombreGasto() {
		return nombreGasto;
	}

	public void setNombreGasto(String nombreGasto) {
		this.nombreGasto = nombreGasto;
	}

	public TipoGastoEnum getTipoGastoEnum() {
		return tipoGastoEnum;
	}

	public void setTipoGastoEnum(TipoGastoEnum tipoGastoEnum) {
		this.tipoGastoEnum = tipoGastoEnum;
	}

	public String getDescGasto() {
		return descGasto;
	}

	public void setDescGasto(String descGasto) {
		this.descGasto = descGasto;
	}		
}