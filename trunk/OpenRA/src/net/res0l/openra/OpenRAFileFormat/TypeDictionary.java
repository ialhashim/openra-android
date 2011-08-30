package net.res0l.openra.OpenRAFileFormat;

import java.util.HashMap;
import java.lang.reflect.*;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

public class TypeDictionary<T> 
{
	HashMap<Type, Object> dataSingular = new HashMap<Type, Object>();
	HashMap<Type, List<Object>> dataMultiple = new HashMap<Type, List<Object>>();

	public void Add( Object val )
	{
		Type t = val.getClass();

		for( Class<?> i : val.getClass().getInterfaces() )
			InnerAdd( i, val );
				
		for( Class<?> tt : val.getClass().getSuperclass().getClasses() )
			InnerAdd( tt, val );
	}

	@SuppressWarnings("serial")
	void InnerAdd( Type t, Object val )
	{
		ArrayList<Object> objs = new ArrayList<Object>();

		if( dataMultiple.containsKey(t) )
		{
			objs.add( val );
		}
		else if( dataSingular.containsKey(t) )
		{
			dataSingular.remove( t );
			
			ArrayList<Object> aa = new ArrayList<Object>();
			aa.add(dataSingular.get(t));
			aa.add(val);
			
			dataMultiple.put( t,  aa);
		}
		else
			dataSingular.put( t, val );
	}

	public boolean Contains(Type t)
	{
		return dataSingular.containsKey( t ) || dataMultiple.containsKey( t );
	}

	@SuppressWarnings("unchecked")
	public T Get(Type t)
	{
		Object ret = dataSingular.get(t);
		
		return (T)ret;
	}

	@SuppressWarnings("unchecked")
	public T GetOrDefault(Type t)
	{
		if( !dataSingular.containsKey( t ) )
			return (T) t.getClass().cast(t);
		
		return (T)dataSingular.get(t);
	}

	@SuppressWarnings("unchecked")
	public Iterable<T> WithInterface(Type t)
	{
		if( dataMultiple.containsKey(t) )
			return (Iterable<T>) dataMultiple.get(t);
		else if( dataSingular.containsKey(t) )
			return (Iterable<T>) dataSingular.get(t);
		else
			return null;
	}

	public Iterable GetEnumerator()
	{
		return null;
	}
}
