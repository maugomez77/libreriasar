package mx.com.libreria.model;

import java.io.Serializable;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import mx.com.libreria.marcador.Marker;

@Entity
@Table(name = "DEVOLUCION")
public class Devolucion implements Serializable, Marker {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4379915787776122647L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="generatorName")
	@TableGenerator(name="generatorName", table="secuencias",  
	                pkColumnName="tablename", // TableID.TableName (value = table_name, test_table, etc.)  
	                valueColumnName="id", // TableID.ID (value = 1,2,3,etc.)  
	                allocationSize=1 // flush every 1 insert  
				   )
	@Column(name = "DEVOLUCION_ID")
	private int devolucionId;
		
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name = "DEVOLUCION_WITH_INVENTARIO_ACTUAL", joinColumns = { @JoinColumn(name = "DEVOLUCION_ID") }, inverseJoinColumns = { @JoinColumn(name = "INVENTARIO_ID")})
    Set<InventarioActual> listaInventario;
	
	@Column(name = "FECHA_DEVOLUCION", nullable=false)
	private Date fechaDevolucion;
		
	@OneToOne(cascade = CascadeType.ALL)
    private Cliente cliente;

	public int getDevolucionId() {
		return devolucionId;
	}

	public void setDevolucionId(int devolucionId) {
		this.devolucionId = devolucionId;
	}

	public Set<InventarioActual> getListaInventario() {
		return listaInventario;
	}

	public void setListaInventario(Set<InventarioActual> listaInventario) {
		this.listaInventario = listaInventario;
	}

	public Date getFechaDevolucion() {
		return fechaDevolucion;
	}

	public void setFechaDevolucion(Date fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
}
