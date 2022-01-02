package model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Rolmenu.class)
public abstract class Rolmenu_ {

	public static volatile SingularAttribute<Rolmenu, Integer> id;
	public static volatile SingularAttribute<Rolmenu, Menu> menu;
	public static volatile SingularAttribute<Rolmenu, Rol> rol;

}

