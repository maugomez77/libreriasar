package mx.com.libreria.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "PROVEEDOR")
public class Proveedor implements Serializable, Marker {

	/**
	 * 
	 */
	private static final long serialVersionUID = -691333246295126918L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="generatorName")
	@TableGenerator(name="generatorName", table="secuencias",  
	                pkColumnName="tablename", // TableID.TableName (value = table_name, test_table, etc.)  
	                valueColumnName="id", // TableID.ID (value = 1,2,3,etc.)  
	                allocationSize=1 // flush every 1 insert  
				   )
	@Column(name = "PROVEEDOR_ID")
	private int proveedorId;
	
	@Column(name = "NOMBRE_PROVEEDOR", nullable = false, length = 100)    
    private String nombreProveedor;
    
	@Column(name = "DIRECCION", length = 100)    
    private String direccion;
    
	@Column(name = "TELEFONO_1", nullable = true, length=15)    
    private String telefono_1;
    
	@Column(name = "TELEFONO_2", nullable = true, length=15)    
    private String telefono_2;

	public int getProveedorId() {
		return proveedorId;
	}

	public void setProveedorId(int proveedorId) {
		this.proveedorId = proveedorId;
	}

	public String getNombreProveedor() {
		return nombreProveedor;
	}

	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono_1() {
		return telefono_1;
	}

	public void setTelefono_1(String telefono_1) {
		this.telefono_1 = telefono_1;
	}

	public String getTelefono_2() {
		return telefono_2;
	}

	public void setTelefono_2(String telefono_2) {
		this.telefono_2 = telefono_2;
	}

	
	
}