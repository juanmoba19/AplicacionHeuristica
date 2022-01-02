package model;

import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Usuario.class)
public abstract class Usuario_ {

	public static volatile SingularAttribute<Usuario, Integer> id;
	public static volatile SingularAttribute<Usuario, Date> fechacreacion;
	public static volatile SingularAttribute<Usuario, String> usuariocreacion;
	public static volatile SingularAttribute<Usuario, String> usuariomodificacion;
	public static volatile SingularAttribute<Usuario, Boolean> estado;
	public static volatile SingularAttribute<Usuario, String> email;
	public static volatile SingularAttribute<Usuario, String> usuario;
	public static volatile SingularAttribute<Usuario, String> descripcion;
	public static volatile SingularAttribute<Usuario, Rol> rol;
	public static volatile SingularAttribute<Usuario, String> clave;
	public static volatile SingularAttribute<Usuario, Date> fechamodificacion;

}

