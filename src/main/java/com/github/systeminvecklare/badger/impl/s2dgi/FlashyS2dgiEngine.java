package com.github.systeminvecklare.badger.impl.s2dgi;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.core.IKeyPressListener;
import com.github.systeminvecklare.badger.core.graphics.components.layer.ILayerDelegate;
import com.github.systeminvecklare.badger.core.graphics.components.layer.Layer;
import com.github.systeminvecklare.badger.core.graphics.components.layer.LayerDelegate;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClipDelegate;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.MovieClip;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.MovieClipDelegate;
import com.github.systeminvecklare.badger.core.graphics.components.scene.ISceneDelegate;
import com.github.systeminvecklare.badger.core.graphics.components.scene.Scene;
import com.github.systeminvecklare.badger.core.graphics.components.scene.SceneDelegate;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.IFlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.framework.smartlist.ISmartList;
import com.github.systeminvecklare.badger.core.graphics.framework.smartlist.SmartList;
import com.github.systeminvecklare.badger.core.pooling.FlashyPoolManager;
import com.github.systeminvecklare.badger.core.pooling.IPoolManager;
import com.github.systeminvecklare.badger.core.standard.input.keyboard.IKeyPressEvent;
import com.github.systeminvecklare.badger.impl.s2dgi.graphics.ITextureReference;

import net.pointlessgames.libs.s2dgi.clipboard.ISimpleClipboard;
import net.pointlessgames.libs.s2dgi.core.Simple2DGraphics;
import net.pointlessgames.libs.s2dgi.texture.ITexture;
import net.pointlessgames.libs.s2dgi.window.IWindow;

public class FlashyS2dgiEngine implements IFlashyEngine {
	private FlashyPoolManager poolManager;
	
	public FlashyS2dgiEngine() {
		this.poolManager = new FlashyPoolManager();
	}

	@Override
	public IPoolManager getPoolManager() {
		return poolManager;
	}

	@Override
	public ISceneDelegate newSceneDelegate(Scene wrapper) {
		return new SceneDelegate(wrapper) {
			@Override
			public void init() {
				super.init();
				getWrapper().addKeyPressListener(new IKeyPressListener() {
					@Override
					public void onKeyPress(IKeyPressEvent event) {
						if(event.getKeyCode() == KeyEvent.VK_F5) {
							textureStore.clear();
						}
					}
				});
			}
		};
	}

	@Override
	public ILayerDelegate newLayerDelegate(Layer wrapper) {
		return new LayerDelegate(wrapper);
	}

	@Override
	public IMovieClipDelegate newMovieClipDelegate(MovieClip wrapper) {
		return new MovieClipDelegate(wrapper);
	}

	@Override
	public <T> ISmartList<T> newSmartList() {
		return new SmartList<T>();
	}
	
	@Override
	public void copyToClipboard(CharSequence text) {
		ISimpleClipboard clipboard = Simple2DGraphics.get().getClipboard();
		if(clipboard != null) {
			clipboard.setText(text);
		}
	}
	
	@Override
	public String pasteFromClipboard() {
		ISimpleClipboard clipboard = Simple2DGraphics.get().getClipboard();
		return clipboard != null ? clipboard.getText(null) : null;
	}
	
	public static FlashyS2dgiEngine get() {
		return (FlashyS2dgiEngine) FlashyEngine.get();
	}
	
	//TODO use proper store
	private final Map<ITextureReference, ITexture> textureStore = new HashMap<>();

	public ITexture getTexture(ITextureReference textureReference) {
		ITexture texture = textureStore.get(textureReference);
		if(texture == null) {
			try {
				texture = textureReference.load(Simple2DGraphics.get());
				textureStore.put(textureReference, texture);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return texture;
	}

	public float getMouseX() {
		return Simple2DGraphics.get().getMainWindow().getMouseX();
	}
	
	public float getMouseY() {
		IWindow window = Simple2DGraphics.get().getMainWindow();
		return window.getHeight() - window.getMouseY();
	}
	
	public boolean isKeyDown(int keyCode) {
		return Simple2DGraphics.get().getMainWindow().isKeyDown(keyCode);
	}
}
