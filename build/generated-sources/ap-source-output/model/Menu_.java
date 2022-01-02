package model;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Menu.class)
public abstract class Menu_ {

	public static volatile SingularAttribute<Menu, String> nombre;
	public static volatile SingularAttribute<Menu, Integer> orden;
	public static volatile SingularAttribute<Menu, Integer> id;
	public static volatile SetAttribute<Menu, ?> menus;
	public static volatile SingularAttribute<Menu, Boolean> estado;
	public static volatile SingularAttribute<Menu, String> icono;
	public static volatile SingularAttribute<Menu, Menu> menu;
	public static volatile SetAttribute<Menu, ?> rolmenus;
	public static volatile SingularAttribute<Menu, String> url;
	public static volatile SingularAttribute<Menu, Integer> nivel;

}

