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
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import mx.com.libreria.marcador.Marker;

@Entity
@Table(name = "SNAP_SHOT_INVENTARIO_HISTORICO")
public class SnapShotInventarioHistorico implements Serializable, Marker {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4234799072529875264L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="generatorName")
	@TableGenerator(name="generatorName", table="secuencias",  
	                pkColumnName="tablename", // TableID.TableName (value = table_name, test_table, etc.)  
	                valueColumnName="id", // TableID.ID (value = 1,2,3,etc.)  
	                allocationSize=1 // flush every 1 insert  
				   )
	@Column(name = "SNAP_SHOT_ID")
	private int snapShotId;
		
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name = "SNAP_SHOT_INVENTARIO_HISTORICO_WITH_INVENTARIO_HISTORICO", joinColumns = { @JoinColumn(name = "SNAP_SHOT_ID") }, inverseJoinColumns = { @JoinColumn(name = "INVENTARIO_ID_HISTORICO")})
    Set<InventarioHistorico> listaInventario;
	
	@Column(name = "FECHA_SNAPSHOT", nullable=false)
	private Date fechaSnapShot;

	public int getSnapShotId() {
		return snapShotId;
	}

	public void setSnapShotId(int snapShotId) {
		this.snapShotId = snapShotId;
	}

	public Set<InventarioHistorico> getListaInventario() {
		return listaInventario;
	}

	public void setListaInventario(Set<InventarioHistorico> listaInventario) {
		this.listaInventario = listaInventario;
	}

	public Date getFechaSnapShot() {
		return fechaSnapShot;
	}

	public void setFechaSnapShot(Date fechaSnapShot) {
		this.fechaSnapShot = fechaSnapShot;
	}			
}
