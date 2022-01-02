package model;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Rol.class)
public abstract class Rol_ {

	public static volatile SingularAttribute<Rol, String> nombre;
	public static volatile SingularAttribute<Rol, Integer> id;
	public static volatile SingularAttribute<Rol, Boolean> estado;
	public static volatile SingularAttribute<Rol, String> descripcion;
	public static volatile SetAttribute<Rol, ?> rolmenus;
	public static volatile SetAttribute<Rol, ?> usuarios;

}

