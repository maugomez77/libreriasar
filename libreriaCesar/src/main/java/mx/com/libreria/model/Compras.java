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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import mx.com.libreria.marcador.Marker;

@Entity
@Table(name = "COMPRAS")
public class Compras implements Serializable, Marker {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1734607026793302052L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="generatorName")
	@TableGenerator(name="generatorName", table="secuencias",  
	                pkColumnName="tablename", // TableID.TableName (value = table_name, test_table, etc.)  
	                valueColumnName="id", // TableID.ID (value = 1,2,3,etc.)  
	                allocationSize=1 // flush every 1 insert  
				   )
	@Column(name = "COMPRAS_ID")
	private int comprasId;
	
	
	@Column(name = "FECHA_COMPRA", nullable=false)
	private Date fechaCompra;
	
	/**
	 * El inventario me dice el costo de cada producto, cantidades y precios.
	 */
	@ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name = "COMPRAS_INVENTARIOS", joinColumns = { @JoinColumn(name = "COMPRAS_ID") }, inverseJoinColumns = { @JoinColumn(name = "INVENTARIO_ID") })
    private Set<InventarioActual> listaInventario;		
	
	@OneToOne(cascade = CascadeType.ALL)
    private Proveedor proveedor;           
	
	/**
	 * P = pagado
	 * C = credito
	 * I = carga inicial de inventarios
	 */
	@Column(name = "ESTATUS_PAGO_CREDITO", length=1)
	private String estatusPagoCredito;
	
	@Column(name = "POR_PAGAR")
	private float porPagar;
	
	@Column(name = "MONTO_FACTURA")
	private float montoFactura;

	@Column(name = "MONTO_INICIAL")
	private float montoInicial;

	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name = "COMPRAS_WITH_ABONOS", joinColumns = { @JoinColumn(name = "COMPRAS_ID") }, inverseJoinColumns = { @JoinColumn(name = "ABONO_ID")})
    Set<Abono> listaAbonos;


	@Column(name = "ACTIVO", length=1, nullable=false)
	private String activo;


	public int getComprasId() {
		return comprasId;
	}


	public void setComprasId(int comprasId) {
		this.comprasId = comprasId;
	}


	public Date getFechaCompra() {
		return fechaCompra;
	}


	public void setFechaCompra(Date fechaCompra) {
		this.fechaCompra = fechaCompra;
	}


	public Set<InventarioActual> getListaInventario() {
		return listaInventario;
	}


	public void setListaInventario(Set<InventarioActual> listaInventario) {
		this.listaInventario = listaInventario;
	}


	public Proveedor getProveedor() {
		return proveedor;
	}


	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}


	public String getEstatusPagoCredito() {
		return estatusPagoCredito;
	}


	public void setEstatusPagoCredito(String estatusPagoCredito) {
		this.estatusPagoCredito = estatusPagoCredito;
	}


	public float getPorPagar() {
		return porPagar;
	}


	public void setPorPagar(float porPagar) {
		this.porPagar = porPagar;
	}


	public float getMontoFactura() {
		return montoFactura;
	}


	public void setMontoFactura(float montoFactura) {
		this.montoFactura = montoFactura;
	}


	public float getMontoInicial() {
		return montoInicial;
	}


	public void setMontoInicial(float montoInicial) {
		this.montoInicial = montoInicial;
	}


	public Set<Abono> getListaAbonos() {
		return listaAbonos;
	}


	public void setListaAbonos(Set<Abono> listaAbonos) {
		this.listaAbonos = listaAbonos;
	}


	public String getActivo() {
		return activo;
	}


	public void setActivo(String activo) {
		this.activo = activo;
	}


	
}
