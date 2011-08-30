package net.res0l.openra.OpenRAFileFormat;

import java.util.HashMap;
import java.util.Iterator;

public class ActorReference implements Iterable
{
	public String Type;
	public TypeDictionary InitDict;

	public ActorReference( String type )
	{
		this( type, new HashMap<String, MiniYaml>() );
	}

	public ActorReference( String type, Map<String, MiniYaml> inits )
	{
		Type = type;
		InitDict = new TypeDictionary();
		
		for( var i : inits )
			InitDict.Add( LoadInit( i.Key, i.Value ) );
	}

	static IActorInit LoadInit(String traitName, MiniYaml my)
	{
		var info = Game.CreateObject<IActorInit>(traitName + "Init");
		FieldLoader.Load(info, my);
		return info;
	}

	public MiniYaml Save()
	{
		var ret = new MiniYaml( Type );
		
		foreach( var init : InitDict )
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
		return InitDict.GetEnumerator();
	}
}
