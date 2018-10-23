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
@Table(name = "CLIENTE")
public class Cliente implements Serializable, Marker {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1461023182404312748L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="generatorName")
	@TableGenerator(name="generatorName", table="secuencias",  
	                pkColumnName="tablename", // TableID.TableName (value = table_name, test_table, etc.)  
	                valueColumnName="id", // TableID.ID (value = 1,2,3,etc.)  
	                allocationSize=1 // flush every 1 insert  
				   )
	@Column(name = "CLIENTE_ID")
	private int clienteId;
	
	@Column(name = "NOMBRE_CLIENTE", nullable = false, length = 100)    
    private String nombreCliente;
    
	@Column(name = "DIRECCION", length = 100)    
    private String direccion;
    
	@Column(name = "TELEFONO_1", nullable = true, length=15)    
    private String telefono_1;
    
	@Column(name = "TELEFONO_2", nullable = true, length=15)    
    private String telefono_2;

	public int getClienteId() {
		return clienteId;
	}

	public void setClienteId(int clienteId) {
		this.clienteId = clienteId;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
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