package mx.com.libreria.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import mx.com.libreria.factory.ObjectFactory;

import mx.com.libreria.interfases.persistencia.dao.BaseDao;

import mx.com.libreria.marcador.Marker;

import mx.com.libreria.model.Abono;
import mx.com.libreria.model.Compras;
import mx.com.libreria.model.Devolucion;
import mx.com.libreria.model.Empleado;
import mx.com.libreria.model.Gastos;
import mx.com.libreria.model.InventarioActual;
import mx.com.libreria.model.InventarioHistorico;
import mx.com.libreria.model.Producto;
import mx.com.libreria.model.Cliente;
import mx.com.libreria.model.Proveedor;
import mx.com.libreria.model.Remision;
import mx.com.libreria.model.SnapShotInventarioHistorico;
import mx.com.libreria.model.Suscripcion;
import mx.com.libreria.model.TipoGasto;
import mx.com.libreria.model.VentaEspecial;

import mx.com.libreria.model.login.Rol;
import mx.com.libreria.model.login.Usuario;

public class CatalogMGR implements Constants {

	BaseDao baseDao;
	
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;		
	}

	@SuppressWarnings("unchecked")
	public List<?> getList(Object obj) {
		List<?> listaObjectos = null;
		if (obj instanceof Producto) {			
			Producto prod = (Producto) obj;
			listaObjectos = (ArrayList<Producto>) baseDao.find(prod);
		} else if (obj instanceof Cliente) {
			Cliente prod = (Cliente) obj;
			listaObjectos = (ArrayList<Cliente>) baseDao.find(prod);
		} else if (obj instanceof Rol) {
			Rol prod = (Rol) obj;
			listaObjectos = (ArrayList<Rol>) baseDao.find(prod);
		} else if (obj instanceof Usuario) {
			Usuario prod = (Usuario) obj;
			listaObjectos = (ArrayList<Usuario>) baseDao.find(prod);
		} else if (obj instanceof Proveedor) {
			Proveedor prod = (Proveedor) obj;
			listaObjectos = (ArrayList<Proveedor>) baseDao.find(prod);
		} else if (obj instanceof TipoGasto) {
			TipoGasto prod = (TipoGasto) obj;
			listaObjectos = (ArrayList<TipoGasto>) baseDao.find(prod);
		} else if (obj instanceof Empleado) {
			Empleado prod = (Empleado) obj;
			listaObjectos = (ArrayList<Empleado>) baseDao.find(prod);
		} else {
			Logs.debug(CatalogMGR.class, "no hay mapping: getList");
		}
		
		return listaObjectos;
	}
	
	@SuppressWarnings("rawtypes")
	public List<?> getListParameters(String query, Object[] values) {
		List<?> listaObjectos = (ArrayList) baseDao.find(query, values);
		return listaObjectos;
	}
	
	public Object getObjectData(Object obj) { 		
		if (obj instanceof Producto) {
			Producto prod = (Producto) obj;			
			prod = (Producto) baseDao.getByPK(prod, prod.getProductoId());							
			return prod;
		} else if (obj instanceof Cliente) {
			Cliente prod = (Cliente) obj;			
			prod = (Cliente) baseDao.getByPK(prod, prod.getClienteId());							
			return prod;
		} else if (obj instanceof Usuario) {
			Usuario prod = (Usuario) obj;			
			prod = (Usuario) baseDao.getByPK(prod, prod.getUsuarioId());							
			return prod;
		} else if (obj instanceof Rol) {
			Rol prod = (Rol) obj;			
			prod = (Rol) baseDao.getByPK(prod, prod.getRolId());							
			return prod;
		} else if (obj instanceof Proveedor) {
			Proveedor prod = (Proveedor) obj;			
			prod = (Proveedor) baseDao.getByPK(prod, prod.getProveedorId());							
			return prod;
		} else if (obj instanceof InventarioActual) {
			InventarioActual prod = (InventarioActual) obj;			
			prod = (InventarioActual) baseDao.getByPK(prod, prod.getInventarioId());							
			return prod;
		} else if (obj instanceof InventarioHistorico) {
			InventarioHistorico prod = (InventarioHistorico) obj;			
			prod = (InventarioHistorico) baseDao.getByPK(prod, prod.getInventarioIdHist());							
			return prod;					
		} else if (obj instanceof Compras) {
			Compras prod = (Compras) obj;		
			prod = (Compras) baseDao.getByPK(prod, prod.getComprasId());							
			return prod;
		} else if (obj instanceof Remision) {
			Remision prod = (Remision) obj;		
			prod = (Remision) baseDao.getByPK(prod, prod.getRemisionId());							
			return prod;
		} else if (obj instanceof Gastos) {
			Gastos prod = (Gastos) obj;		
			prod = (Gastos) baseDao.getByPK(prod, prod.getGastosId());							
			return prod;
		} else if (obj instanceof TipoGasto) {
			TipoGasto prod = (TipoGasto) obj;		
			prod = (TipoGasto) baseDao.getByPK(prod, prod.getTipoGastoId());							
			return prod;
		} else if (obj instanceof SnapShotInventarioHistorico) {
			SnapShotInventarioHistorico prod = (SnapShotInventarioHistorico) obj;		
			prod = (SnapShotInventarioHistorico) baseDao.getByPK(prod, prod.getSnapShotId());							
			return prod;
		} else if (obj instanceof Devolucion) {
			Devolucion prod = (Devolucion) obj;		
			prod = (Devolucion) baseDao.getByPK(prod, prod.getDevolucionId());							
			return prod;
		} else if (obj instanceof Suscripcion) { 
			Suscripcion prod = (Suscripcion) obj;
			prod = (Suscripcion) baseDao.getByPK(prod, prod.getSuscripcionId());
			return prod;
		} else if (obj instanceof VentaEspecial) {
			VentaEspecial prod = (VentaEspecial) obj;
			prod = (VentaEspecial) baseDao.getByPK(prod, prod.getVentaEspecialId());
			return prod;
		} else if (obj instanceof Empleado) {
			Empleado prod = (Empleado) obj;
			prod = (Empleado) baseDao.getByPK(prod, prod.getEmpleadoId());
			return prod;
		} else {
			Logs.debug(CatalogMGR.class, "Existe un no mapeo en getObjectData");
		}		
		return null;			
	}
	/**
	 * 
	 * @param operacion   1 = update
	 *                    0 = insert
	 *                   -1 = delete
	 * @return
	 */
	public int dmlOperations(int operacion, Object obj) {
		int result = 0;
		Marker marker = null;
		try { 
			if (obj instanceof Producto) {
				Logs.debug(CatalogMGR.class, "dml operations: Producto " + operacion);
				Producto prod = (Producto) obj;
				if (operacion == -1) { 
					prod = (Producto) getObjectData(prod);
				}
				marker = prod;		
				
			} else if (obj instanceof Cliente) {
				Logs.debug(CatalogMGR.class, "dml operations: Cliente " + operacion);
				Cliente prod = (Cliente) obj;
				if (operacion == -1) { 
					prod = (Cliente) getObjectData(prod);
				}
				marker = prod;
				
			} else if (obj instanceof Usuario) {
				Logs.debug(CatalogMGR.class, "dml operations: Usuario " + operacion);
				Usuario prod = (Usuario) obj;
				if (operacion == -1) { 
					prod = (Usuario) getObjectData(prod);
				}
				marker = prod;
			} else if (obj instanceof Rol) {
				Logs.debug(CatalogMGR.class, "dml operations: Rol " + operacion);
			    Rol prod = (Rol) obj;
				if (operacion == -1) { 
					prod = (Rol) getObjectData(prod);
				}
				marker = prod;			
			} else if (obj instanceof Proveedor) {
				Logs.debug(CatalogMGR.class, "dml operations: Proveedor " + operacion);
				Proveedor prod = (Proveedor) obj;
				if (operacion == -1) {
					prod = (Proveedor) getObjectData(prod);
				}
				marker = prod;
			} else if (obj instanceof InventarioActual) {
				Logs.debug(CatalogMGR.class, "dml operations: InventarioActual " + operacion);
				InventarioActual prod = (InventarioActual) obj;
				if (operacion == -1) { 
					prod = (InventarioActual) getObjectData(prod);
				}								
				marker = prod;
			} else if (obj instanceof InventarioHistorico) {
				Logs.debug(CatalogMGR.class, "dml operations: InventarioHistorico " + operacion);
				InventarioHistorico prod = (InventarioHistorico) obj;
				if (operacion == -1) { 
					prod = (InventarioHistorico) getObjectData(prod);
				}								
				marker = prod;							
			}  else if (obj instanceof Compras) {
				Logs.debug(CatalogMGR.class, "dml operations: Compras " + operacion);
				Compras prod = (Compras) obj;
				if (operacion == -1) {
					prod = (Compras) getObjectData(prod);
				}
				marker = prod;
			} else if (obj instanceof Remision) { 
				Logs.debug(CatalogMGR.class, "dml operations: Remision " + operacion);
				Remision prod = (Remision) obj;
				if (operacion == -1) {
					prod = (Remision) getObjectData(prod);
				}
				marker = prod;
			} else if (obj instanceof Gastos) { 
				Logs.debug(CatalogMGR.class, "dml operations: Gastos " + operacion);
				Gastos prod = (Gastos) obj;
				if (operacion == -1) {
					prod = (Gastos) getObjectData(prod);
				}
				marker = prod;
			} else if (obj instanceof TipoGasto) { 
				Logs.debug(CatalogMGR.class, "dml operations: TipoGasto " + operacion);
				TipoGasto prod = (TipoGasto) obj;
				if (operacion == -1) {
					prod = (TipoGasto) getObjectData(prod);
				}
				marker = prod;			
			} else if (obj instanceof SnapShotInventarioHistorico) { 
				Logs.debug(CatalogMGR.class, "dml operations: SnapShotInventarioHistorico " + operacion);
				SnapShotInventarioHistorico prod = (SnapShotInventarioHistorico) obj;
				if (operacion == -1) {
					prod = (SnapShotInventarioHistorico) getObjectData(prod);
					Set<InventarioHistorico> setHist = prod.getListaInventario();
					Iterator<InventarioHistorico> it = setHist.iterator();
					while (it.hasNext()) { 
						InventarioHistorico ele = it.next();
						ele.setProducto(null);						
					}
				}
				marker = prod;			
			} else if (obj instanceof Devolucion) { 
				Logs.debug(CatalogMGR.class, "dml operations: Devolucion " + operacion);
				Devolucion prod = (Devolucion) obj;
				if (operacion == -1) {
					prod = (Devolucion) getObjectData(prod);
				}
				marker = prod;			
			} else if (obj instanceof Suscripcion) {
				Logs.debug(CatalogMGR.class, "dml operations: Suscripcion " + operacion);
				Suscripcion prod = (Suscripcion) obj;
				if (operacion == -1) { 
					prod = (Suscripcion) getObjectData(prod);
				}
				marker = prod;
			} else if (obj instanceof VentaEspecial) { 
				Logs.debug(CatalogMGR.class, "dml operations: VentaEspecial " + operacion);
				VentaEspecial prod = (VentaEspecial) obj;
				if (operacion == -1) { 
					prod = (VentaEspecial) getObjectData(prod);
				}
				marker = prod;
			} else if (obj instanceof Empleado) {
				Logs.debug(CatalogMGR.class, "dml operations: Empleado " + operacion);
				Empleado prod = (Empleado) obj;
				if (operacion == -1) { 
					prod = (Empleado) getObjectData(prod);
				}
				marker = prod;
			} else { 
				Logs.debug(CatalogMGR.class, "no hubo mapping para dml operation " + operacion);
				marker = null;
			}		
			
			if (marker != null) { 
				Logs.debug(CatalogMGR.class, "marker not null " + operacion);
				switch (operacion) { 
					case -1: baseDao.delete(marker);
					         result = -1;
							 break;		
			        case 0: baseDao.save(marker);
			        		result = 0;
							break;
					case 1: baseDao.update(marker);
							result = 1;
							break;
					case 2: baseDao.saveOrUpdate(marker);
							result = 2;
						    break;
					case 3: 
						    if (obj instanceof Remision) { 
						    	Remision prod = (Remision) baseDao.merge(marker);
						    	result = prod.getRemisionId();						    	
						    } else if (obj instanceof Compras){ 
						    	Compras prod = (Compras) baseDao.merge(marker);
						    	result = prod.getComprasId();
						    } else if (obj instanceof Devolucion) { 
						    	Devolucion prod = (Devolucion) baseDao.merge(marker);
						    	result = prod.getDevolucionId();
						    } else if (obj instanceof Suscripcion) {
						    	Suscripcion prod = (Suscripcion) baseDao.merge(marker);
						    	result = prod.getSuscripcionId();
						    } else if (obj instanceof VentaEspecial) { 
						    	VentaEspecial prod = (VentaEspecial) baseDao.merge(marker);
						    	result = prod.getVentaEspecialId();
						    }
						    break;
					default: break;		
				};
				
			}
				
		} catch (Exception e) {
			Logs.debug(CatalogMGR.class, " error en dmlOperations " + e.toString());
			result = Utilerias.INFINITY_VALUE_INTEGER;
		}
		
		return result;
	}
	
	public String validarCamposLlenos(Object obj) {
		StringBuilder result = new StringBuilder("");
		
		if (obj instanceof Producto) { 
			Producto prod = (Producto) obj;
			/*
			if (Utilerias.isNullOrUndefined(prod.getProductoId())) 
 				result.append("- Identificador del producto -" + BREAK_NEW_LINE_HTML);
 			*/	                 	
 			if (Utilerias.isNullOrUndefined(prod.getNombreProducto()))
 				result.append("- Nombre del producto -" + BREAK_NEW_LINE_HTML);
 
 			if (Utilerias.isNullOrUndefined(prod.getDescProducto()))
  				result.append("- Descripcion del producto -" + BREAK_NEW_LINE_HTML);
 			
 			if (Utilerias.isNullOrUndefined(prod.getProveedor()) || prod.getProveedor().getProveedorId() <= 0) 
 				result.append("- Nombre del proveedor -" + BREAK_NEW_LINE_HTML);
 			
 			if (Utilerias.isNullOrUndefined(prod.getTipoProducto()))
 				result.append("- Tipo del Producto -" + BREAK_NEW_LINE_HTML);
 			/*
  			if (Utilerias.isNullOrUndefined(prod.getCostoProducto()))
  				result.append("- Costo del producto -" + BREAK_NEW_LINE_HTML);
  
			if (Utilerias.isNullOrUndefined(prod.getPrecioProducto()))
				result.append("- Precio del producto -" + BREAK_NEW_LINE_HTML);
 			 */
		}
		
		if (obj instanceof Cliente) { 
			Cliente prod = (Cliente) obj;
			/*
			if (Utilerias.isNullOrUndefined(prod.getClienteId())) 
 				result.append("- Identificador del cliente -" + BREAK_NEW_LINE_HTML);
 			*/
			
 			if (Utilerias.isNullOrUndefined(prod.getNombreCliente()))
 				result.append("- Nombre del cliente -" + BREAK_NEW_LINE_HTML);
 
 			if (Utilerias.isNullOrUndefined(prod.getDireccion()))
  				result.append("- Direccion del producto -" + BREAK_NEW_LINE_HTML);
  
  			if (Utilerias.isNullOrUndefined(prod.getTelefono_1()))
  				result.append("- Telefono 1 -" + BREAK_NEW_LINE_HTML);

		}
		
		if (obj instanceof Rol) { 
			Rol prod = (Rol) obj;
			if (Utilerias.isNullOrUndefined(prod.getNombreRol())) 
 				result.append("- Nombre del rol del usuario -" + BREAK_NEW_LINE_HTML);
		}
		
		if (obj instanceof Usuario) { 
			Usuario prod = (Usuario) obj;
			if (Utilerias.isNullOrUndefined(prod.getUsuarioId())) 
 				result.append("- Identificador del usuario -" + BREAK_NEW_LINE_HTML);
 				                 	
 			if (Utilerias.isNullOrUndefined(prod.getNombreUsuario()))
 				result.append("- Nombre del usuario -" + BREAK_NEW_LINE_HTML);
 
 			if (Utilerias.isNullOrUndefined(prod.getContrasena()))
  				result.append("- Contrasena del usuario -" + BREAK_NEW_LINE_HTML);
  
 			if (Utilerias.isNullOrUndefined(prod.getContrasenaConf()))
  				result.append("- Contrasena de confirmacion del usuario -" + BREAK_NEW_LINE_HTML);
 			
 			if (!Utilerias.isNullOrUndefined(prod.getContrasena()) && !Utilerias.isNullOrUndefined(prod.getContrasenaConf())) { 
 				if (!prod.getContrasena().equals(prod.getContrasenaConf())) { 
 					result.append("- La contrasena de confirmacion no es la misma a la capturada inicialmente -" + BREAK_NEW_LINE_HTML);
 				}
 			}
  			if (Utilerias.isNullOrUndefined(prod.getCorreoElectronico()))
  				result.append("- Correo electronico del usuario -" + BREAK_NEW_LINE_HTML);
  			
  			if (Utilerias.isNullOrUndefined(prod.getTelefono()))
  				result.append("- Telefono del usuario -" + BREAK_NEW_LINE_HTML);

  			if (Utilerias.isNullOrUndefined(prod.getRolUsuario().getNombreRol()))
  				result.append("- Rol del usuario -" + BREAK_NEW_LINE_HTML);
		}
		
		if (obj instanceof InventarioActual) { 
			InventarioActual prod = (InventarioActual) obj;
			if (Utilerias.isNullOrUndefined(prod.getCostoProducto()) || prod.getCostoProducto() <= 0) {
				if (!Utilerias.isNullOrUndefined(prod.getEstatus()) && prod.getEstatus().equals("E")) { 
					result.append("- Costo del producto tiene que tener un valor positivo -" + BREAK_NEW_LINE_HTML);
				}			
			}
			
			if (Utilerias.isNullOrUndefined(prod.getPrecioProducto()) || prod.getPrecioProducto() < 0) 
				result.append("- Precio del producto tiene que tener un valor positivo o igual a cero si es defectuoso -" + BREAK_NEW_LINE_HTML);					
			
			if (Utilerias.isNullOrUndefined(prod.getCantidad()) || prod.getCantidad() <= 0) 
				result.append("- Cantidad del producto tiene que ser mayor a cero -" + BREAK_NEW_LINE_HTML);
			
			
		}
		
		if (obj instanceof Proveedor) { 
			Proveedor prod = (Proveedor) obj;
			
 			if (Utilerias.isNullOrUndefined(prod.getNombreProveedor()))
 				result.append("- Nombre del proveedor -" + BREAK_NEW_LINE_HTML);
 
 			if (Utilerias.isNullOrUndefined(prod.getDireccion()))
  				result.append("- Direccion del proveedor -" + BREAK_NEW_LINE_HTML);
  
  			if (Utilerias.isNullOrUndefined(prod.getTelefono_1()))
  				result.append("- Telefono 1 -" + BREAK_NEW_LINE_HTML);

		}
		
		if (obj instanceof Gastos) {
			Gastos prod = (Gastos) obj;
			if (Utilerias.isNullOrUndefined(Utilerias.getDate(prod.getFechaAplicacion(), Utilerias.FORMAT_DATE)))
 				result.append("- Fecha de aplicacion del gasto -" + BREAK_NEW_LINE_HTML);	
 			
			if (Utilerias.isNullOrUndefined(prod.getMontoGasto()) && prod.getMontoGasto() <= 0)
 				result.append("- El monto del gasto tiene que tener un valor positivo -" + BREAK_NEW_LINE_HTML);			
		}
		
		if (obj instanceof TipoGasto) { 
			TipoGasto prod = (TipoGasto) obj;
			
			if (Utilerias.isNullOrUndefined(prod.getNombreGasto()))
 				result.append("- Nombre del gasto -" + BREAK_NEW_LINE_HTML);
 
			if (Utilerias.isNullOrUndefined(prod.getDescGasto()))
				result.append("- Descripcion del gasto -" + BREAK_NEW_LINE_HTML);
		}
		
		if (obj instanceof Suscripcion) { 
			Suscripcion prod = (Suscripcion) obj;
			if (Utilerias.isNullOrUndefined(prod.getFechaInicialSuscripcion()))
				result.append("- La fecha inicial debe ser llenada -" + BREAK_NEW_LINE_HTML);
			
			//if (Utilerias.isNullOrUndefined(prod.getFechaFinalSuscripcion()))
			//	result.append("- La fecha final debe ser llenada -" + BREAK_NEW_LINE_HTML);
			
			if (Utilerias.isNullOrUndefined(prod.getMontoInicial()) && prod.getMontoInicial() < 0)
				result.append("- El monto debe llenarse y debe ser un valor numerico con 2 decimales -" + BREAK_NEW_LINE_HTML);
			
			//if (Utilerias.isNullOrUndefined(prod.getNumeroMesesSuscripcion()))
			//	result.append("- El numero de meses debe llenarse -" + BREAK_NEW_LINE_HTML);
			
			if (Utilerias.isNullOrUndefined(prod.getCliente()))
				result.append("- El cliente no puede ser vacio -" + BREAK_NEW_LINE_HTML);
			
			if (Utilerias.isNullOrUndefined(prod.getProducto()))
				result.append("- El producto no puede ser vacio -" + BREAK_NEW_LINE_HTML);
			
			if (Utilerias.isNullOrUndefined(prod.getEntrega())) 
				result.append("- El medio de entrega no puede quedar vacio -" + BREAK_NEW_LINE_HTML);
			
		}
		
		if (obj instanceof VentaEspecial) { 
			VentaEspecial prod = (VentaEspecial) obj;
			if (Utilerias.isNullOrUndefined(prod.getFechaInicialVentaEspecial()))
				result.append("- La fecha inicial debe ser llenada -" + BREAK_NEW_LINE_HTML);
			
			if (Utilerias.isNullOrUndefined(prod.getFechaFinalVentaEspecial()))
				result.append("- La fecha final debe ser llenada -" + BREAK_NEW_LINE_HTML);
			
			if (Utilerias.isNullOrUndefined(prod.getMontoInicial()) && prod.getMontoInicial() < 0)
				result.append("- El monto debe llenarse y debe ser un valor numerico con 2 decimales -" + BREAK_NEW_LINE_HTML);
			
			if (Utilerias.isNullOrUndefined(prod.getCliente()))
				result.append("- El cliente no puede ser nullo -" + BREAK_NEW_LINE_HTML);
			
			if (Utilerias.isNullOrUndefined(prod.getProducto()))
				result.append("- El producto no puede ser nullo -" + BREAK_NEW_LINE_HTML);
						
		}
		
		if (obj instanceof Empleado) { 
			Empleado prod = (Empleado) obj;
			if (Utilerias.isNullOrUndefined(prod.getNombre()))
				result.append("- Nombre del empleado -" + BREAK_NEW_LINE_HTML);
			
			if (Utilerias.isNullOrUndefined(prod.getDireccion()))
				result.append("- Direccion del empleado -" + BREAK_NEW_LINE_HTML);
			
			if (Utilerias.isNullOrUndefined(prod.getTelefono_1()))
				result.append("- Telefono del empleado -" + BREAK_NEW_LINE_HTML);
		}
		
		return result.toString();
	}

	public void desactivarInventariosNegocio(String tipo, Object obj) { 
		if (tipo.equals(Utilerias.COMPRAS_VALUE)) {
			Compras com = (Compras) obj;
			Iterator<InventarioActual> it = com.getListaInventario().iterator();
			Set<InventarioActual> newInventory = new HashSet<InventarioActual>();
			while (it.hasNext()) { 
				InventarioActual ele = it.next();
				ele.setFechaSalida(null);
				ele.setControlComprasId(0);			       
				ele.setEstatus("D"); //desactivado
				newInventory.add(ele);
			}
			com.setListaInventario(newInventory);
			com.setActivo(Utilerias.NO_VALUE);
		} else if (tipo.equals(Utilerias.VENTAS_VALUE)) {
			Remision com = (Remision) obj;
			Iterator<InventarioActual> it = com.getListaInventario().iterator();
			Set<InventarioActual> newInventory = new HashSet<InventarioActual>();
			while (it.hasNext()) { 
				InventarioActual ele = it.next();
				ele.setFechaSalida(null);
				ele.setControlComprasId(0);			       
				ele.setEstatus("E");
				newInventory.add(ele);
			}
			com.setListaInventario(newInventory);
			com.setActivo(Utilerias.NO_VALUE);
		} else if (tipo.equals(Utilerias.SUSCRIPCION_VALUE)) { 
			Suscripcion com = (Suscripcion) obj;
			Iterator<InventarioActual> it = com.getListaInventario().iterator();
			Set<InventarioActual> newInventory = new HashSet<InventarioActual>();
			while (it.hasNext()) { 
				InventarioActual ele = it.next();
				ele.setFechaSalida(null);
				ele.setControlComprasId(0);			       
				ele.setEstatus("E");
				newInventory.add(ele);
			}
			com.setListaInventario(newInventory);
			com.setActivo(Utilerias.NO_VALUE);
		} else if (tipo.equals(Utilerias.VENTAS_ESPECIALES_VALUE)) {
			VentaEspecial com = (VentaEspecial) obj;
			Iterator<InventarioActual> it = com.getListaInventario().iterator();
			Set<InventarioActual> newInventory = new HashSet<InventarioActual>();
			while (it.hasNext()) { 
				InventarioActual ele = it.next();
				ele.setFechaSalida(null);
				ele.setControlComprasId(0);			       
				ele.setEstatus("E");
				newInventory.add(ele);
			}
			com.setListaInventario(newInventory);
			com.setActivo(Utilerias.NO_VALUE);
		} else if (tipo.equals(Utilerias.GASTO_VALUE)) {
			Gastos com = (Gastos) obj;
			com.setActivo(Utilerias.NO_VALUE);
		} else { 
			Logs.debug(CatalogMGR.class, "No existe mapeo en desactivarInventarios");
		}
	}
	
	@SuppressWarnings({ "unchecked" })
	public String desactivarNegocios(String tipo, int id) {
		StringBuffer sb = new StringBuffer();
		if (tipo.equals(Utilerias.COMPRAS_VALUE)) { 
			Compras com = new Compras();
			com.setComprasId(id);
			com = (Compras) getObjectData(com);
			
			/**
			 * SE AGREGO LA PALABRA DISTINCT, ESTO ES PORQUE PUEDE PASAR QUE HAYA DIFERENTES INVENTARIO, UNO PARA DIFERENTE COMPRA
			 * POR LO QUE SE DUPLICABA LA INFORMACION, ESTO PASO PARA UNA VENTA ESPECIAL
			 */
			//checar si la compra tiene relacion ya con una venta
			List<Object[]> list = 
				(ArrayList<Object[]>) 
					getListParameters(" select distinct rem.remisionId, 1 " + 
									  "   from Remision rem " + 
									  "   join rem.listaInventario inv " +
									  "  where inv.inventarioId in (select i.inventarioId " +
									  "                               from Compras com " +
									  "                               join com.listaInventario i " +
									  "                              where com.activo = 'Y' " + 
									  "		  				        ) " + 
									  "    and rem.activo = 'Y' ",
									  null);
			
			if (list != null && !list.isEmpty()) {
				Utilerias.agregarMensaje(sb, list, 
					"La compra tiene asignadas las siguientes ventas: ", 
					"Por lo que no es posible desactivar la compra, primero desactive estas ventas.");
				
			} 
			
			//checar si la compra tiene relacion ya con una suscripcion
			list = 
				(ArrayList<Object[]>) 
					getListParameters(" select distinct sus.suscripcionId, 1 " + 
									  "   from Suscripcion sus " + 
									  "   join sus.listaInventario inv " +
									  "  where inv.inventarioId in (select i.inventarioId " +
									  "                               from Compras com " +
									  "                               join com.listaInventario i " +
									  "                              where com.activo = 'Y' " + 
									  "		  				        ) " + 
									  "    and sus.activo = 'Y' ",
									  null);
			
			if (list != null && !list.isEmpty()) {
				Utilerias.agregarMensaje(sb, list, 
					"La compra tiene asignadas las siguientes suscripciones: ", 
					"Por lo que no es posible desactivar la compra, primero desactive estas suscripciones.");
			} 
			
			//checar si la compra tiene relacion ya con una venta especial
			list = 
				(ArrayList<Object[]>) 
					getListParameters(" select distinct ve.ventaEspecialId, 1 " + 
									  "   from VentaEspecial ve " + 
									  "   join ve.listaInventario inv " +
									  "  where inv.inventarioId in (select i.inventarioId " +
									  "                               from Compras com " +
									  "                               join com.listaInventario i " +
									  "                              where com.activo = 'Y' " + 
									  "		  				        ) " + 
									  "    and ve.activo = 'Y' ",
									  null);
			
			if (list != null && !list.isEmpty()) {
				Utilerias.agregarMensaje(sb, list, 
					"La compra tiene asignadas las siguientes ventas especiales: ", 
					"Por lo que no es posible desactivar la compra, primero desactive estas ventas especiales.");
			}
			
			if (sb.toString().equals(Utilerias.STRING_VACIO)) { 
				desactivarInventariosNegocio(tipo, com);
				if (dmlOperations(1, com) == 1) {
					sb.append("- Se ha desactivado exitosamente la compra -");
				} else { 
					sb.append("- Problemas al desactivar la compra -");
				}
			}
			
		} else if (tipo.equals(Utilerias.VENTAS_VALUE)) {
			Remision rem = new Remision();
			rem.setRemisionId(id);
			rem = (Remision) getObjectData(rem);
			desactivarInventariosNegocio(tipo, rem);
			if (dmlOperations(1, rem) == 1) {
				sb.append("- Se ha desactivado exitosamente la venta -");
			} else { 
				sb.append("- Problemas al desactivar la venta -");
			}
		} else if (tipo.equals(Utilerias.SUSCRIPCION_VALUE)) { 
			Suscripcion sus = new Suscripcion();
			sus.setSuscripcionId(id);
			sus = (Suscripcion) getObjectData(sus);
			desactivarInventariosNegocio(tipo, sus);
			if (dmlOperations(1, sus) == 1) {
				sb.append("- Se ha desactivado exitosamente la suscripcion -");
			} else { 
				sb.append("- Problemas al desactivar la suscripcion -");
			}
		} else if (tipo.equals(Utilerias.VENTAS_ESPECIALES_VALUE)) {
			VentaEspecial ve = new VentaEspecial();
			ve.setVentaEspecialId(id);
			ve = (VentaEspecial) getObjectData(ve);
			desactivarInventariosNegocio(tipo, ve);
			if (dmlOperations(1, ve) == 1) {
				sb.append("- Se ha desactivado exitosamente la venta especial -");
			} else { 
				sb.append("- Problemas al desactivar la venta especial -");
			}
		} else if (tipo.equals(Utilerias.GASTO_VALUE)) {
			Gastos gasto = new Gastos();
			gasto.setGastosId(id);
			gasto = (Gastos) getObjectData(gasto);
			desactivarInventariosNegocio(tipo, gasto);
			if (dmlOperations(1, gasto) == 1) {
				sb.append("- Se ha desactivado exitosamente el gasto aplicado -");
			} else { 
				sb.append("- Problemas al desactivar el gasto aplicado -");
			}
		} else { 
			Logs.debug(CatalogMGR.class, "no hay mapping en desactivarNegocios ");
		}
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public List<InventarioActual> getInventarioAgrupadoCantidad(String tipo, int id) { 
		
		List<Object[]> lista = null;
		
		if (tipo.equals(Utilerias.COMPRAS_VALUE)) {
			
			lista = (ArrayList<Object[]>)
				getListParameters(
					"   select sum(inv.cantidad), inv.producto " +
				    "     from Compras rem " +
				    "     join rem.listaInventario inv " +
				    "    where rem.comprasId = ? " + 
				    "      and rem.activo = 'Y' " + 
				    "    group by inv.producto " +
				    "    order by sum(inv.cantidad) desc ", 
			        new Object[]{ id });
			
		} else if (tipo.equals(Utilerias.VENTAS_VALUE)) { 
			
			lista = (ArrayList<Object[]>)
				getListParameters(
					"   select sum(inv.cantidad), inv.producto " +
					"     from Remision rem " +
					"     join rem.listaInventario inv " +
					"    where rem.remisionId = ? " + 
					"      and rem.activo = 'Y' " + 
					"    group by inv.producto " +
					"    order by sum(inv.cantidad) desc ", 
					new Object[]{ id });
			
		} else if (tipo.equals(Utilerias.SUSCRIPCION_VALUE)) {
			
			lista = (ArrayList<Object[]>)
				getListParameters(
					"   select sum(inv.cantidad), inv.producto " +
					"     from Suscripcion rem " +
					"     join rem.listaInventario inv " +
					"    where rem.suscripcionId = ? " + 
					"      and rem.activo = 'Y' " + 
					"    group by inv.producto " +
					"    order by sum(inv.cantidad) desc ", 
					new Object[]{ id });
			
		} else if (tipo.equals(Utilerias.VENTAS_ESPECIALES_VALUE)) {
			lista = (ArrayList<Object[]>)
				getListParameters(
					"   select sum(inv.cantidad), inv.producto " +
					"     from VentaEspecial rem " +
					"     join rem.listaInventario inv " +
					"    where rem.ventaEspecialId = ? " + 
					"      and rem.activo = 'Y' " + 
					"    group by inv.producto " +
					"    order by sum(inv.cantidad) desc ", 
					new Object[]{ id });
		}
		
		List<InventarioActual> list = new ArrayList<InventarioActual>();
		
		//transpasar a la otra lista de inventarios
		for (int i=0; i < lista.size(); i++) {
			Object[] row = lista.get(i);
			
			InventarioActual elemento = new InventarioActual();
			elemento.setCantidad(Utilerias.strToInt(row[0].toString()));
			elemento.setProducto((Producto) row[1]);
			list.add(elemento);
		}
		
		return list;
		
	}

	public float getAbonosTotales(Set<Abono> abonos) { 
		float resultado = 0;
		if (!Utilerias.isNullOrUndefined(abonos)) { 
			Iterator<Abono> it = abonos.iterator();
			while (it.hasNext()) { 
				Abono ele = it.next();
				resultado += ele.getMonto();
			}
		}
		return resultado;
	}

	@SuppressWarnings("unchecked")
	public String getEmpleadoNomina() { 
		StringBuffer sb = new StringBuffer();
		Empleado emp = new Empleado();
		List<Empleado> list = (ArrayList<Empleado>) getList(emp);
		for (int i=0; i < list.size(); i++) { 
			emp = list.get(i);
			sb.append("Sueldo " + emp.getEmpleadoId() + " " + emp.getNombre() + ";");
		}
		return sb.toString();
	}
	
	public static void main(String[] args) { 
		ProductMGR mgr = (ProductMGR) ObjectFactory.getBean("productMGR");
		mgr.getAllNameArticulos();		
	}
}
