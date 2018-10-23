package mx.com.libreria.model.login;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import mx.com.libreria.marcador.Marker;

/**
 * http://www.vaannila.com/hibernate/hibernate-example/hibernate-mapping-component-using-annotations-1.html
 * 
 * @author Mauricio D Gómez T
 *
 */
@Entity
@Table(name = "USUARIO")
public class Usuario implements Serializable, Marker {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 582154402930644862L;
	
	@Id
	@Column(name = "USUARIO_ID")
	private String usuarioId;
		
	@Column(name = "NOMBRE_USUARIO", nullable = false, length = 100)    
    private String nombreUsuario;
    
	@Column(name = "CONTRASENA", nullable = false, length = 100)    
    private String contrasena;

	@Column(name = "CONTRASENA_CONF", nullable = false, length = 100)    
    private String contrasenaConf;

	@Column(name = "EMAIL", nullable = false, length = 100)    
    private String correoElectronico;

	@Column(name = "TELEFONO", nullable = false, length = 10)    
    private String telefono;

	@OneToOne(cascade = CascadeType.ALL)
    private Rol rolUsuario;

	public String getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(String usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getContrasenaConf() {
		return contrasenaConf;
	}

	public void setContrasenaConf(String contrasenaConf) {
		this.contrasenaConf = contrasenaConf;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Rol getRolUsuario() {
		return rolUsuario;
	}

	public void setRolUsuario(Rol rolUsuario) {
		this.rolUsuario = rolUsuario;
	}       

	
}
