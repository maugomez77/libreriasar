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
@Table(name = "VENTA_ESPECIAL")
public class VentaEspecial implements Serializable, Marker {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6450488618171926551L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="generatorName")
	@TableGenerator(name="generatorName", table="secuencias",  
	                pkColumnName="tablename", // TableID.TableName (value = table_name, test_table, etc.)  
	                valueColumnName="id", // TableID.ID (value = 1,2,3,etc.)  
	                allocationSize=1 // flush every 1 insert  
				   )
	@Column(name = "VENTA_ESPECIAL_ID")
	private int ventaEspecialId;
	
	/**
	 * Para este caso el precio y costo del producto se toman directamente de la tabla
	 * de productos.
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Producto producto;
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name = "VENTA_ESPECIAL_WITH_INVENTARIO_ACTUAL", joinColumns = { @JoinColumn(name = "VENTA_ESPECIAL_ID") }, inverseJoinColumns = { @JoinColumn(name = "INVENTARIO_ID")})
    Set<InventarioActual> listaInventario;
	
	@Column(name = "FECHA_INICIAL_VENTA_ESPECIAL", nullable=false)
	private Date fechaInicialVentaEspecial;
	
	@Column(name = "FECHA_FINAL_VENTA_ESPECIAL", nullable=false)
	private Date fechaFinalVentaEspecial;
		
	@OneToOne(cascade = CascadeType.ALL)
    private Cliente cliente;

	/**
	 * P = pagado
	 * C = credito
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
    @JoinTable(name = "VENTA_ESPECIAL_WITH_ABONOS", joinColumns = { @JoinColumn(name = "VENTA_ESPECIAL_ID") }, inverseJoinColumns = { @JoinColumn(name = "ABONO_ID")})
    Set<Abono> listaAbonos;

	@Column(name = "ACTIVO", length=1, nullable=false)
	private String activo;

	public int getVentaEspecialId() {
		return ventaEspecialId;
	}

	public void setVentaEspecialId(int ventaEspecialId) {
		this.ventaEspecialId = ventaEspecialId;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Set<InventarioActual> getListaInventario() {
		return listaInventario;
	}

	public void setListaInventario(Set<InventarioActual> listaInventario) {
		this.listaInventario = listaInventario;
	}

	public Date getFechaInicialVentaEspecial() {
		return fechaInicialVentaEspecial;
	}

	public void setFechaInicialVentaEspecial(Date fechaInicialVentaEspecial) {
		this.fechaInicialVentaEspecial = fechaInicialVentaEspecial;
	}

	public Date getFechaFinalVentaEspecial() {
		return fechaFinalVentaEspecial;
	}

	public void setFechaFinalVentaEspecial(Date fechaFinalVentaEspecial) {
		this.fechaFinalVentaEspecial = fechaFinalVentaEspecial;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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
