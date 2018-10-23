package mx.com.libreria.model.login;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import mx.com.libreria.marcador.Marker;

/**
 * http://www.vaannila.com/hibernate/hibernate-example/hibernate-mapping-component-using-annotations-1.html
 * 
 * @author Mauricio D Gómez T
 *
 */
@Entity
@Table(name = "ROL")
public class Rol implements Serializable, Marker {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1537203581293441786L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="generatorName")
	@TableGenerator(name="generatorName", table="secuencias",  
	                pkColumnName="tablename", // TableID.TableName (value = table_name, test_table, etc.)  
	                valueColumnName="id", // TableID.ID (value = 1,2,3,etc.)  
	                allocationSize=1 // flush every 1 insert  
				   )
	@Column(name = "ROL_ID")
	private int rolId;
	
	@Column(name = "NOMBRE_ROL", nullable = false, length = 100)
	private String nombreRol;

	public int getRolId() {
		return rolId;
	}

	public void setRolId(int rolId) {
		this.rolId = rolId;
	}

	public String getNombreRol() {
		return nombreRol;
	}

	public void setNombreRol(String nombreRol) {
		this.nombreRol = nombreRol;
	}
	
	
}
