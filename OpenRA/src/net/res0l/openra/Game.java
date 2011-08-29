package net.res0l.openra;

import java.io.File;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.*;

import net.res0l.openra.InputHandler.DefaultInputHandler;
import net.res0l.openra.InputHandler.DefaultInputHandler.Modifiers;
import net.res0l.openra.OpenRAFileFormat.ActionQueue;
import net.res0l.openra.OpenRAFileFormat.FileSystem;
import net.res0l.openra.OpenRAFileFormat.Graphics.WindowMode;
import net.res0l.openra.OpenRAFileFormat.Mod;
import net.res0l.openra.OpenRAGame.*;
import net.res0l.openra.OpenRAGraphics.*;
import net.res0l.openra.OpenRAGameRules.*;
import net.res0l.openra.OpenRANetwork.*;
import net.res0l.openra.OpenRANetwork.Connection.*;
import net.res0l.openra.OpenRAWidget.Widget;
import net.res0l.openra.Server.Server;
import net.res0l.openra.Server.MasterServerQuery;
import net.res0l.openra.Sound.Sound;
import net.res0l.openra.Support.Arguments;

public class Game
{
	public Utilities Utilities;
	
	public static ModData modData = new ModData(null);
	public static int CellSize() { return modData.Manifest.TileSize; }

	public static WorldRenderer worldRenderer;
	public static Renderer Renderer;
	public boolean HasInputFocus = false;
	
	public static Viewport viewport;
	public static Settings Settings;

	public static OrderManager orderManager;
	public static Server server;

	public void MoveViewport(Vector2 loc)
	{
		viewport.Center(loc);
	}
	
	public void JoinServer(String host, int port)
	{
		String replayFilename = ChooseReplayFilename();
		String path = Game.SupportDir + "Replays";
		
		File d = new File( path );
		
		if( !d.exists()  ) 
			d.mkdir();
		
		File replayFile = new File( path + replayFilename );
	
		JoinInner(new OrderManager(host, port, new ReplayRecorderConnection(new NetworkConnection(host, port), replayFile)));
	}		
	
	String ChooseReplayFilename()
	{
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
		return dateFormat.format(date) + ".rep";
	}
	
	static void JoinInner(OrderManager om)
	{
		orderManager = om;
		lastConnectionState = ConnectionState.PreConnecting;
		ConnectionStateChanged(orderManager);
	}
	
	private static void ConnectionStateChanged(OrderManager om) {
		
	}

	public void JoinReplay(String replayFile)
	{
		JoinInner(new OrderManager("<no server>", -1, new ReplayConnection(replayFile)));
	}
	
	static void JoinLocal()
	{
		JoinInner(new OrderManager("<no server>", -1, new EchoConnection()));
	}
	
	public static int RenderFrame = 0;
	public static int NetFrameNumber() { return orderManager.NetFrameNumber; }
	public static int LocalTick() { return orderManager.LocalFrameNumber; }
	static int NetTickScale = 3;		// 120ms net tick for 40ms local tick
	
	static ConnectionState lastConnectionState = ConnectionState.PreConnecting;

	// Hacky workaround for orderManager visibility
	public Widget OpenWindow(World world, String widget)
	{
		Map<String, Object> args = new HashMap<String, Object>();
		
		args.put("world", world);
		args.put("orderManager", orderManager);
		args.put("worldRenderer", worldRenderer);
		
		return Widget.OpenWindow(widget, args);
	}
	
	ActionQueue afterTickActions = new ActionQueue();
	public void RunAfterTick(Action a) { afterTickActions.Add(a); }
	
	static void Tick( OrderManager orderManager, Viewport viewPort )
	{
		if (orderManager.Connection.ConnectionState != lastConnectionState)
		{
			lastConnectionState = orderManager.Connection.ConnectionState;
			ConnectionStateChanged( orderManager );
		}
	
		Tick( orderManager );
		if( worldRenderer != null && orderManager.world != worldRenderer.world )
			Tick( worldRenderer.world.orderManager );
	
		// Render
		++RenderFrame;
		viewport.DrawRegions(worldRenderer, new DefaultInputHandler( orderManager.world ));
		Sound.SetListenerPosition(viewport.Location().add(new Vector2(viewport.Width(), viewport.Height()).mul(0.5f)));
		
		MasterServerQuery.Tick();
	}
	
	private static void Tick( OrderManager orderManager )
	{
		long t = Environment.TickCount();
		long dt = t - orderManager.LastTickTime;
		
		if (dt >= Settings.Game.Timestep)
		{
			orderManager.LastTickTime += Settings.Game.Timestep;
			Widget.DoTick();
			World world = orderManager.world;
			
			if( orderManager.GameStarted && world.LocalPlayer != null )
				++Viewport.TicksSinceLastMove;
			
			Sound.Tick();

			if (world != null)
			{
				boolean isNetTick = LocalTick() % NetTickScale == 0;

				if (!isNetTick || orderManager.IsReadyForNextFrame)
				{
					++orderManager.LocalFrameNumber;

					if (isNetTick) orderManager.Tick();

					world.OrderGenerator.Tick(world);
					world.Selection.Tick(world);

					world.Tick();
				}
				else
					if (orderManager.NetFrameNumber == 0)
						orderManager.LastTickTime = Environment.TickCount();
			}
		}
	}

	public static void LobbyInfoChanged(){ };

	static void SyncLobbyInfo()
	{
		LobbyInfoChanged();
	}

	void StartGame(String mapUID)
	{
		GameMap map = modData.PrepareMap(mapUID);
		viewport = new Viewport(new Vector2(Renderer.Resolution), map.Bounds, Renderer);
		orderManager.world = new World(modData.Manifest, map, orderManager);
		worldRenderer = new WorldRenderer(orderManager.world);
	
		if (orderManager.GameStarted) return;
		Widget.SelectedWidget = null;
	
		orderManager.LocalFrameNumber = 0;
		orderManager.StartGame();
		worldRenderer.RefreshPalette();
	}
	
	public boolean IsHost()
	{
		return orderManager.Connection.LocalClientId == 0;
	}
	
	public static Map<String, Mod> CurrentMods = Mod.AllMods;

	Modifiers modifiers;
	public Modifiers GetModifierKeys() { return modifiers; }
	void HandleModifierKeys(Modifiers mods) { modifiers = mods; }
	
	static void Initialize(Arguments args)
	{
		String defaultSupport = "OpenRA"; // path
	
		FileSystem.SupportDir = defaultSupport;

		Settings = new Settings(SupportDir + "settings.yaml", args);
		Settings.Save();
	
		FileSystem.Mount("."); // Needed to access shaders
		
		Renderer = new Renderer();
		Renderer.Initialize( WindowMode.Fullscreen );
		Renderer.SheetSize = Settings.Game.SheetSize;
		
		Console.WriteLine("Available mods:");
		for (String n : Settings.Game.Mods)
			Console.WriteLine(n);
		
		Sound.Create();
		InitializeWithMods(Settings.Game.Mods);
	}
	
	public static void InitializeWithMods(String[] mods)
	{
		// Clear  state if we have switched mods
		LobbyInfoChanged ();
		worldRenderer = null;
		if (server != null)
			server.Shutdown();
		if (orderManager != null)
			orderManager.dispose();
		
		// Discard any invalid mods
		String[] mm = Mod.AllMods.keySet().toArray(new String[0]);
		Settings.Game.Mods = mm;
	
		Settings.Save();
	
		Sound.StopMusic();
		Sound.StopVideo();
		Sound.Initialize();
		
		modData = new ModData( mm );
		modData.LoadInitialAssets();
			
		JoinLocal();
		viewport = new Viewport(new Vector2(Renderer.Resolution), new Rectangle(), Renderer);
		
		Widget.RootWidget.RemoveChildren();
		
		Map<String, Object> args = new HashMap<String, Object>();
		
		modData.WidgetLoader.LoadWidget( args, Widget.RootWidget, "INIT_SETUP" );
	}
	
	public void LoadShellMap()
	{
		StartGame(ChooseShellmap());
		Game.orderManager.LastTickTime = Environment.TickCount();
	}
	
	String ChooseShellmap()
	{
		return "shellmap";
	}
	
	boolean quit;
	public void OnQuit(){};
	
	void Run()
	{
		while (!quit)
			Tick( orderManager, viewport );
		OnQuit();
	}
	
	public void Exit() { quit = true; }
	
	public void AddChatLine(Color c, String n, String s)
	{
		
	}
	
	public void Debug(String s)
	{
		AddChatLine(Color.WHITE, "Debug", s); 
	}
	
	public void Disconnect()
	{
		if (orderManager.world != null)
			orderManager.world.traitDict.PrintReport();
		
		if (IsHost() && server != null)
			server.Shutdown();
	
		orderManager.dispose();
		String shellmap = ChooseShellmap();
		JoinLocal();
		StartGame(shellmap);
	
		Widget.CloseWindow();
		Widget.OpenWindow("MAINMENU_BG");
	}
	
	String baseSupportDir = null;
	public static String SupportDir;

	public void CreateAndJoinServer(Settings settings, String map)
	{
		server = new Server(modData, settings, map);
		
		try {
			NetworkInterface ni = NetworkInterface.getByName("eth1");
			JoinServer(ni.getInetAddresses().toString(), settings.Server.ListenPort);
			
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public boolean IsCurrentWorld(World world)
	{
		return orderManager != null && orderManager.world == world;
	}
}
