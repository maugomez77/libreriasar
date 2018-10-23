package mx.com.libreria.manager;

import java.util.ArrayList;
import java.util.List;

import mx.com.libreria.beans.ProductoCantidad;
import mx.com.libreria.factory.ObjectFactory;
import mx.com.libreria.interfases.persistencia.dao.BaseDao;

import mx.com.libreria.model.Producto;
import mx.com.libreria.model.Proveedor;

public class ProductMGR {

	BaseDao baseDao;
	
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;		
	}
	 
	@SuppressWarnings("unchecked")
	public List<Producto> getAllNameArticulosSel(Object obj) { 
		List<Producto> lista = new ArrayList<Producto>();
		lista = (List<Producto>) baseDao.find(" from Producto as pr where pr.productoId = ? ", new Object[]{obj});
		for (int i=0; i < lista.size(); i++) {
			Producto prod = (Producto) lista.get(i);
			Logs.debug(ProductMGR.class, "informacion: " + prod.getNombreProducto());
		}
		return lista;		
	}
	
	@SuppressWarnings("unchecked")
	public List<ProductoCantidad> getAllNameArticulosFromInventory() { 
		List<ProductoCantidad> listaProductosCantidad = new ArrayList<ProductoCantidad>();
		List<Object[]> rawList = (ArrayList<Object[]>) 
								  baseDao.find(" select sum(inv.cantidad), inv.producto.productoId, inv.producto.nombreProducto " + 
				                               "   from Compras compras " +
				            			       "   join compras.listaInventario inv " + 
				            				   "  where compras.activo = 'Y' " +
				                               "    and inv.estatus = ? " + 
				                               "  group by inv.producto.productoId, inv.producto.nombreProducto " +
				                               " having sum(inv.cantidad) > 0 " + 
				                               "  order by inv.producto.nombreProducto desc ", 
				                               new Object[]{ "E" });
		int cantidad = -1;
		for (int i=0; i < rawList.size(); i++) {
			Object[] row = (Object[]) rawList.get(i);
			Producto prod = new Producto();
			cantidad = Utilerias.strToInt(row[0].toString());
			prod.setProductoId(Utilerias.strToInt(row[1].toString()));
			prod.setNombreProducto(row[2].toString());
			
			ProductoCantidad pCant = new ProductoCantidad();
			pCant.setCantidad(cantidad);
			pCant.setProducto(prod);
			
			listaProductosCantidad.add(pCant);
		}
		
		return listaProductosCantidad;		
	}
	
	@SuppressWarnings("unchecked")
	public List<Producto> getAllNameArticulos() {
		List<Producto> lista = new ArrayList<Producto>();
		Producto prod = new Producto();
		lista = (List<Producto>) baseDao.find(prod);
		for (int i=0; i < lista.size(); i++) {
			prod = (Producto) lista.get(i);
			Logs.debug(ProductMGR.class, "Data " + prod.getNombreProducto());
		}
		return lista;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Producto> getAllNameArticulosPorProveedor(int idProveedor) {
		List<Producto> lista = new ArrayList<Producto>();				             
		
		List<Object> listaObjectos = (ArrayList) baseDao.find(
				  " select p.productoId, p.nombreProducto, p.descProducto, p.tipoProducto, p.proveedor " + 
			      "   from Producto p " + 
			      "  where p.proveedor.proveedorId = ? ", new Object[]{ idProveedor });
		
		for (int i=0; i < listaObjectos.size(); i++) {
			Object[] values = (Object[]) listaObjectos.get(i);
			
			Producto p = new Producto();
			p.setProductoId((Integer) values[0]);
			p.setNombreProducto((String) values[1]);
			p.setDescProducto((String) values[2]);
			p.setTipoProducto((String) values[3]);
			p.setProveedor((Proveedor) values[4]);
			
			lista.add(p);
		}		
		
		return lista;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Producto> getAllNameArticulosPorComodin(String comodin) {
		List<Producto> lista = new ArrayList<Producto>();				             
		
		comodin = "%" + comodin + "%";
		
		List<Object> listaObjectos = (ArrayList) baseDao.find(
				  " select p.productoId, p.nombreProducto, p.descProducto, p.tipoProducto, p.proveedor " + 
			      "   from Producto p " + 
			      "  where UPPER(p.nombreProducto) like ? ", new Object[]{ comodin });
		
		for (int i=0; i < listaObjectos.size(); i++) {
			Object[] values = (Object[]) listaObjectos.get(i);
			
			Producto p = new Producto();
			p.setProductoId((Integer) values[0]);
			p.setNombreProducto((String) values[1]);
			p.setDescProducto((String) values[2]);
			p.setTipoProducto((String) values[3]);
			p.setProveedor((Proveedor) values[4]);
			
			lista.add(p);
		}		
		
		return lista;
	}
	public static void main(String[] args) { 
		ProductMGR mgr = (ProductMGR) ObjectFactory.getBean("productMGR");
		mgr.getAllNameArticulos();
		//mgr.getAllNameArticulosSel();
		//mgr.getAllNameArticulos("P");
		
	}
}
