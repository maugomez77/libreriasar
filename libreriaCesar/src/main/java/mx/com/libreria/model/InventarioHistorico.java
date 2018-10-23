package mx.com.libreria.model;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import mx.com.libreria.marcador.Marker;

@Entity
@Table(name = "INVENTARIO_HISTORICO")
public class InventarioHistorico implements Serializable, Marker {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4118094585284451582L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="generatorName")
	@TableGenerator(name="generatorName", table="secuencias",  
	                pkColumnName="tablename", // TableID.TableName (value = table_name, test_table, etc.)  
	                valueColumnName="id", // TableID.ID (value = 1,2,3,etc.)  
	                allocationSize=1 // flush every 1 insert  
				   )
	@Column(name = "INVENTARIO_ID_HISTORICO")
	private int inventarioIdHist;

	@Column(name = "FECHA_INVENTARIO_HISTORICO")
	private Date fechaInventarioHistorico;
	
	@Column(name = "INVENTARIO_ID")
	private int inventarioId;
	
	/**
	 * Para este caso el precio y costo del producto se toman directamente de la tabla
	 * de productos.
	 * 
	 * Se crea una nueva clase para que se eliminen los inventarios historicos del snapshot que no sirven.
	 * esto para dejar solo un snapshot y no crezca considerablemente la base de datos.
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)	
    private Producto producto;
	
	@Column(name = "FECHA_ENTRADA")
	private Date fechaEntrada;
	
	@Column(name = "FECHA_SALIDA")
	private Date fechaSalida;
	
	@Column(name = "COSTO_PRODUCTO", nullable=false)
	private float costoProducto;
	
	@Column(name = "PRECIO_PRODUCTO", nullable=false)
	private float precioProducto;
	
	@Column(name = "IVA", nullable=false)
	private float iva;
	
	@Column(name = "CANTIDAD", nullable=false)
	private int cantidad;
		
	/**
	 * E = entrada
	 * S = salida
	 */
	@Column(name = "ESTATUS", nullable=false, length=1)
	private String estatus;

	@Column(name = "CONTROL_COMPRAS_ID")
	private int controlComprasId;

	@Column(name = "DEFECTUOSO", nullable=false, length=1)
	private String defectuoso;

	public int getInventarioIdHist() {
		return inventarioIdHist;
	}

	public void setInventarioIdHist(int inventarioIdHist) {
		this.inventarioIdHist = inventarioIdHist;
	}

	public Date getFechaInventarioHistorico() {
		return fechaInventarioHistorico;
	}

	public void setFechaInventarioHistorico(Date fechaInventarioHistorico) {
		this.fechaInventarioHistorico = fechaInventarioHistorico;
	}

	public int getInventarioId() {
		return inventarioId;
	}

	public void setInventarioId(int inventarioId) {
		this.inventarioId = inventarioId;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Date getFechaEntrada() {
		return fechaEntrada;
	}

	public void setFechaEntrada(Date fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}

	public Date getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public float getCostoProducto() {
		return costoProducto;
	}

	public void setCostoProducto(float costoProducto) {
		this.costoProducto = costoProducto;
	}

	public float getPrecioProducto() {
		return precioProducto;
	}

	public void setPrecioProducto(float precioProducto) {
		this.precioProducto = precioProducto;
	}

	public float getIva() {
		return iva;
	}

	public void setIva(float iva) {
		this.iva = iva;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public int getControlComprasId() {
		return controlComprasId;
	}

	public void setControlComprasId(int controlComprasId) {
		this.controlComprasId = controlComprasId;
	}

	public String getDefectuoso() {
		return defectuoso;
	}

	public void setDefectuoso(String defectuoso) {
		this.defectuoso = defectuoso;
	}

	
}
