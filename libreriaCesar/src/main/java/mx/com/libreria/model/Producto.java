package mx.com.libreria.model;

import java.io.Serializable;

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


/**
 * http://www.vaannila.com/hibernate/hibernate-example/hibernate-mapping-component-using-annotations-1.html
 * 
 * @author mdgomez
 *
 */
@Entity
@Table(name = "PRODUCTO")
public class Producto implements Serializable, Marker {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7153546692667155277L;
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="generatorName")
	@TableGenerator(name="generatorName", table="secuencias",  
	                pkColumnName="tablename", // TableID.TableName (value = table_name, test_table, etc.)  
	                valueColumnName="id", // TableID.ID (value = 1,2,3,etc.)  
	                allocationSize=1 // flush every 1 insert  
				   )
	@Column(name = "PRODUCTO_ID")
	private int productoId;
	
	@Column(name = "NOMBRE_PRODUCTO", nullable = false, length = 100)    
    private String nombreProducto;
    
	@Column(name = "DESC_PRODUCTO", length = 100)    
    private String descProducto;
    
	@Column(name = "TIPO_PRODUCTO", length = 3, nullable=false)    
    private String tipoProducto;

	@OneToOne(cascade = CascadeType.ALL)
    private Proveedor proveedor;

	public int getProductoId() {
		return productoId;
	}

	public void setProductoId(int productoId) {
		this.productoId = productoId;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public String getDescProducto() {
		return descProducto;
	}

	public void setDescProducto(String descProducto) {
		this.descProducto = descProducto;
	}

	public String getTipoProducto() {
		return tipoProducto;
	}

	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	
}