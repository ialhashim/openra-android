package net.res0l.openra.OpenRAFileFormat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.res0l.openra.Game;
import net.res0l.openra.OpenRAGame.ActorInitializer.IActorInit;

import org.yaml.snakeyaml.Yaml;

public class ActorReference implements Iterable
{
	public String Type;
	public TypeDictionary InitDict;

	public ActorReference( String type )
	{
		this( type, new HashMap<String, Yaml>() );
	}

	public ActorReference( String type, HashMap<String, Yaml> inits )
	{
		Type = type;
		InitDict = new TypeDictionary();
		
	    Iterator it = inits.entrySet().iterator();
		
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        InitDict.Add( LoadInit( pairs.getKey(), pairs.getValue()));
	    }
	}

	static IActorInit LoadInit(String traitName, Yaml my)
	{
		Object info = Game.CreateObject(traitName + "Init");
		FieldLoader.Load(info, my);
		return (IActorInit)info;
	}

	public Yaml Save()
	{
		Yaml ret = new Yaml( Type );
		
		for( var init : InitDict )
		{
			var initName = init.GetType().Name;
			ret.Nodes.Add( new MiniYamlNode( initName.SubString( 0, initName.Length - 4 ), FieldSaver.Save( init ) ) );
		}
		
		return ret;
	}

	// for initialization syntax
	public void Add( Object o ) { InitDict.Add( o ); }

	@Override
	public Iterator iterator() {
		// TODO Auto-generated method stub
		return null;
	}
}
