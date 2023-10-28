package com.github.systeminvecklare.badger.impl.s2dgi.gameloop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executor;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.core.IIntSource;
import com.github.systeminvecklare.badger.core.graphics.components.scene.IScene;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.ApplicationContext;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.IApplicationContext;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.ISceneManager;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.SceneManager;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.gameloop.GameLoop;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.gameloop.GameLoopHooksAdapter;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.gameloop.IGameLoop;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.gameloop.IGameLoopHooks;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.inputprocessor.IInputHandler;
import com.github.systeminvecklare.badger.impl.s2dgi.FlashyS2dgiEngine;
import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;
import com.github.systeminvecklare.badger.impl.s2dgi.input.ExtraFlashyInputHandler;
import com.github.systeminvecklare.badger.impl.s2dgi.input.InputBridge;

import net.pointlessgames.libs.s2dgi.core.Simple2DGraphics;
import net.pointlessgames.libs.s2dgi.window.IWindow;

public abstract class AbstractS2dgiHelper implements ISceneManager {
	private IScene currentScene;
	private Collection<IScene> trashCan = new ArrayList<IScene>();
	private ApplicationContext applicationContext;
	private final S2dgiDrawCycle drawCycle;
	private final float step;
	
	private int startWidth;
	private int startHeight;
	
	private IGameLoop gameLoop;
	private IInputHandler inputHandler;
	
	private long lastTime = -1;
	
	public AbstractS2dgiHelper(FlashyS2dgiEngine flashys2dgiEngine) {
		this(flashys2dgiEngine, 60);
	}
	
	public AbstractS2dgiHelper(FlashyS2dgiEngine flashys2dgiEngine, int stepsPerSeconds) {
		FlashyEngine.set(flashys2dgiEngine);
		this.applicationContext = new ApplicationContext();
		SceneManager.set(this);
		this.step = 1f/stepsPerSeconds; //Use SceneManager.get().getStep() to reach
		this.drawCycle = new S2dgiDrawCycle(Simple2DGraphics.get().getMainWindow());
	}
	
	public void initialize () {
		IWindow mainWindow = Simple2DGraphics.get().getMainWindow();
//		resume();
		this.startWidth = mainWindow.getWidth();
		this.startHeight = mainWindow.getHeight();
		
		this.applicationContext.init();
		
		this.inputHandler = new ExtraFlashyInputHandler(createHeightSource(mainWindow));
		
		this.currentScene = getInitialScene();
		IGameLoopHooks hooks = createHooks();
		this.gameLoop = new GameLoop(inputHandler, applicationContext, hooks != null ? hooks : new GameLoopHooksAdapter()) {
			@Override
			protected IScene getCurrentScene() {
				return currentScene;
			}
			
			@Override
			protected IDrawCycle newDrawCycle() {
				return drawCycle.reset();
			}
			
			@Override
			protected void closeDrawCycle() {
				drawCycle.end();
			}
		};
		
		this.currentScene.init();
		
		InputBridge inputBridge = new InputBridge(inputHandler);
		mainWindow.addKeyListener(inputBridge);
		mainWindow.addMouseListener(inputBridge);
	}

	protected abstract IScene getInitialScene();
	
	protected IGameLoopHooks createHooks() {
		return null;
	}

	public void executeGameLoop() {
		long timeNow = System.nanoTime();
		if(lastTime != -1) {
			long deltaLong = timeNow-lastTime;
			float delta = ((float) (deltaLong/1000L))/1000000L;
			gameLoop.execute(delta);
		}
		lastTime = timeNow;
	}
	
	@Override
	public void skipQueuedUpdates() {
		gameLoop.skipQueuedUpdates();
	}
	
	@Override
	public void changeScene(IScene newScene) {
		this.currentScene = newScene;
	}
	
	@Override
	public void changeScene(IScene newScene, boolean initScene) {
		changeScene(newScene);
		if(initScene) {
			newScene.init();
		}
	}
	
	@Override
	public void emptyTrashCan() {
		synchronized (trashCan) {
			for(IScene doomedScene : trashCan)
			{
				doomedScene.dispose();
			}
			trashCan.clear();
		}
	}
	
	@Override
	public float getHeight() {
//		return 480f;
		return startHeight;
	}
	
	@Override
	public float getWidth() {
		return startWidth;
//		return startWidth*this.getHeight()/startHeight;
	}
	
	
	@Override
	public void sendToTrashCan(IScene sceneToBeDisposed) {
		synchronized (trashCan) {
			trashCan.add(sceneToBeDisposed);
		}
	}
	
//	@Override
//	public void resume() {
//		// Android app regained focus
//		super.resume();
//		FlashyGdxEngine.get().reloadStoreInventories();
//		this.drawCycle  = new GdxDrawCycle();
//	}
	
//	@Override
//	public void pause() {
//		super.pause();
//		// Android app lost focus
//		FlashyGdxEngine.get().disposeStoreInventories();
//	}
	
//	@Override
//	public void dispose() {
//		this.applicationContext.dispose(); //Do this?
//		this.applicationContext = null;
//		super.dispose();
//	}
	
	@Override
	public float getStep() {
		return step;
	}
	
	@Override
	public IApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	private static IIntSource createHeightSource(IWindow window) {
		return new IIntSource() {
			@Override
			public int getFromSource() {
				return window.getHeight();
			}
		};
	}

	public void launchOnThread(Executor executor) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				initialize();
				executor.execute(new Runnable() {
					@Override
					public void run() {
						executeGameLoop();
						executor.execute(this);
					}
				});
			}
		});
	}
	
	public void launchOnCurrentThread() {
		initialize();
		while(true) {
			executeGameLoop();
		}
	}
}
