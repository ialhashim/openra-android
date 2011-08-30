package net.res0l.openra.OpenRAGame;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import net.res0l.openra.OpenRAFileFormat.Manifest;

public class ObjectCreator<T> {

	public ObjectCreator(Manifest manifest) 
	{
		
	}
	
	public T CreateObject(String className)
	{

		return CreateObject( className, new HashMap<String, Object>() );
	}
	
	public T CreateObject( String className, Map<String, Object> args )
	{
		T t = null;
	
		if(className == "")
			return t;
		
		Constructor[] ctors = t.getClass().getDeclaredConstructors();
		Constructor ctor = null;

		for (int i = 0; i < ctors.length; i++) {
		    ctor = ctors[i];
		    if (ctor.getGenericParameterTypes().length == args.size())
			break;
		}
		
	    ctor.setAccessible(true);

		try {
			return (T) ctor.newInstance();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
