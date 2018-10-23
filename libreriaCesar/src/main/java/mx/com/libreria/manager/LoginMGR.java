package mx.com.libreria.manager;

import java.util.HashSet;

import javax.mail.Session;

import mx.com.libreria.factory.ObjectFactory;

import mx.com.libreria.interfases.persistencia.dao.BaseDao;

import mx.com.libreria.mail.context.ContextEmail;
import mx.com.libreria.mail.context.MailClient;
import mx.com.libreria.mail.context.interfases.ContextConstant;

import mx.com.libreria.marcador.Marker;

import mx.com.libreria.model.login.Rol;
import mx.com.libreria.model.login.Usuario;

public class LoginMGR implements ContextConstant {
	
	BaseDao baseDao;
	
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;		
	}

	public boolean sendEmail(Usuario user) {
		
		Usuario temporal = getDataUser(user);
		
		String mensaje = MESSAGE_BODY;
		
		mensaje = mensaje.replace("<usuarioId>", temporal.getUsuarioId());
		mensaje = mensaje.replace("<contrasena>", temporal.getContrasena());
		
		return sendEmail(USER_ADMIN, 
						 temporal.getCorreoElectronico(), 
						 SUBJECT_SISTEMA, 
						 mensaje, 
						 null);
	}
	
	public boolean sendEmail(String from, String to, String subject, String messageBody, String[] attach) { 
		boolean flag = false;
		try {
			MailClient mail = new MailClient();
			Logs.debug(LoginMGR.class, "Mesae");
			Session ses = ContextEmail.getSessionEmail(true, true);
			Logs.debug(LoginMGR.class, "Mesae  2222");
			mail.setSession(ses);
			mail.sendMail(from, to, subject, messageBody, attach);
		    flag = true;
		} catch (Exception e) {
			Logs.error(LoginMGR.class, "Errores: " + e.toString());
			flag = false;
		}
		return flag;
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
			if (obj instanceof Usuario) {
				Usuario prod = (Usuario) obj;
				if (operacion == -1) { 
					prod = (Usuario) getObjectData(prod);
				}				
				marker = prod;				
			} 
			
			switch (operacion) { 
					case -1: baseDao.delete(marker);
							break;
					case 0: baseDao.save(marker);
							break;
					case 1: baseDao.update(marker);
							break;
					default: break;		
				};
				
		} catch (Exception e) {
			Logs.debug(LoginMGR.class, " dmlOperations " + e.toString());
			result = -1;
		}
		
		return result;
	}

	
	public String validarCamposUsuario(Usuario user) {
		StringBuilder result = new StringBuilder("");
		
		if (user.getUsuarioId() == null || user.getUsuarioId().equals("")) 
          	result.append("- Clave del Usuario -");	
          	
        if (user.getNombreUsuario() == null || user.getNombreUsuario().equals(""))
          	result.append("- Nombre del Usuario -");	
          
        if (user.getRolUsuario() == null || user.getRolUsuario().getRolId() < 0)
          	result.append("- El rol no es correcto -");	
          
        if (user.getContrasena() == null || user.getContrasena().equals(""))
          	result.append("- Contraseña no introducida-");	
          
        if (user.getCorreoElectronico() == null || user.getCorreoElectronico().equals(""))
          	result.append("- Correo eletrònico no introducido -");	
          
        if (user.getTelefono() == null || user.getTelefono().equals(""))
          	result.append("- Telefono no introducido -");	
         
		return result.toString();
	}
	
	public String validarIdUsuario(Usuario user) {
		StringBuilder result = new StringBuilder("");
		if (user.getUsuarioId() == null || user.getUsuarioId().equals("")) 
			result.append("- Clave del Usuario -");	
        return result.toString();
	}
	
	public String checkContrasenas(Usuario user) {
		StringBuilder result = new StringBuilder("");
		if (user.getContrasena() != null && user.getContrasenaConf() != null
			&& user.getContrasena().equals(user.getContrasenaConf())) { 
			result.append("");
		} else { 
			result.append("- Las contrasenas no coinciden, favor de verificar -");
		}			
		return result.toString();
	}
	public boolean existUser(Usuario user) {
		boolean existe = false;
		if (user != null) { 
			Usuario temporal = getDataUser(user);
			if (temporal != null && temporal.getUsuarioId().equals(user.getUsuarioId())) { 
				existe = true;
			}
		}
		return existe;
	}
	
	public boolean existUserWithPass(Usuario user) {
		boolean existe = false;
		if (user != null) { 
			Usuario temporal = getDataUser(user);
			if (temporal != null 
				&& temporal.getUsuarioId().equals(user.getUsuarioId())
				&& temporal.getContrasena().equals(user.getContrasena())) {
				existe = true;
			}
		}
		return existe;
	}
		
	public Usuario getDataUser(Usuario user) {
		Usuario temporal = null;			
		if (user != null && !user.getUsuarioId().equals("")) {			
			try { 
				temporal = (Usuario) baseDao.getByPK(user, user.getUsuarioId());				
			} catch (Exception e) { 				
				temporal = null;
			}				
		} 
		return temporal;
	}
	
	public boolean registerUser(Usuario user) {
		boolean exitoso = false;
		String registroExitoso = (String) baseDao.save(user);
		if (registroExitoso.equals(user.getUsuarioId())) { 
			exitoso = true;
		}
		return exitoso;		
	}	
	
	public Object getObjectData(Object obj) { 		
		if (obj instanceof Rol) {
			Rol prod = (Rol) obj;			
			prod = (Rol) baseDao.getByPK(prod, prod.getRolId());							
			return prod;
		}
		return null;
	}
			
	public static void main(String[] args) { 
		
		LoginMGR login = (LoginMGR) ObjectFactory.getBean("loginMGR");
		Usuario user = new Usuario();
		user.setNombreUsuario("mau");
		Rol rol = new Rol();
		rol.setNombreRol("mau");
		HashSet<Rol> rolList = new HashSet<Rol>();
		rolList.add(rol);
		
		//user.setRolUsuario(rolList);
		user.setUsuarioId("mau");
		if (login.existUser(user)) { 
			Logs.debug(Logs.class, "Usuario ya existe");
		} else {
			login.registerUser(user);
			Logs.debug(Logs.class, "Usuario creado");
		}
		
		Usuario user2 = new Usuario();
		user2.setUsuarioId("mau");
		user2 = login.getDataUser(user2);
				
	}
}
