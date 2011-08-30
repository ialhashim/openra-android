package net.res0l.openra.OpenRAFileFormat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeDictionary<T> 
{
	Map<Class<?>, Object> dataSingular = new HashMap<Class<?>, Object>();
	Map<Class<?>, List<Object>> dataMultiple = new HashMap<Class<?>, List<Object>>();

	public void Add( Object val )
	{
		Class<?> t = val.getClass();

		for( Class<?> i : t.getInterfaces() )
			Innerput( i, val );
				
		for( var tt : t.BaseTypes() )
			Innerput( tt, val );
	}
	
	void Innerput( Class<?> i, Object val )
	{
		List<Object> objs;
		Object obj;

		if( dataMultiple.TryGetValue( i, out objs ) )
			objs.put( val );
		else if( dataSingular.TryGetValue( i, out obj ) )
		{
			dataSingular.Remove( i );
			dataMultiple.put( i, new List<Object> { obj, val } );
		}
		else
			dataSingular.put( i, val );
	}

	public boolean Contains()
	{
		return dataSingular.containsKey( T ) || dataMultiple.containsKey( T );
	}

	public T Get()
	{

		Object ret;
		if( !dataSingular.TryGetValue( T, ret ) )
		return (T)ret;
	}

	public T GetOrDefault()
	{
		Object ret;
		if( !dataSingular.TryGetValue( T, ret ) )
			return default( T );
		return (T)ret;
	}

	public Iterable<T> WithInterface()
	{
		List<Object> objs;
		Object obj;

		if( dataMultiple.TryGetValue( T, out objs ) )
			return objs.Cast<T>();
		else if( dataSingular.TryGetValue( T, out obj ) )
			return new T[] { (T)obj };
		else
			return new T[ 0 ];
	}

	public Iterable GetEnumerator()
	{
		return WithInterface().GetEnumerator();
	}
}
