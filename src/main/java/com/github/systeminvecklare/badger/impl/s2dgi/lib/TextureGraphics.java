package com.github.systeminvecklare.badger.impl.s2dgi.lib;

import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.impl.s2dgi.FlashyS2dgiEngine;
import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;
import com.github.systeminvecklare.badger.impl.s2dgi.graphics.ITextureReference;
import com.github.systeminvecklare.badger.impl.s2dgi.lib.widget.IWidgetInterface;

import net.pointlessgames.libs.s2dgi.texture.ITexture;

public class TextureGraphics extends AbstractRectangleGraphics<TextureGraphics> implements IMovieClipLayer {
	private final ITextureReference textureReference;
	private boolean flipX = false;
	private boolean flipY = false;
	private int quarterRotation = 0;
	
	public TextureGraphics(ITextureReference textureReference) {
		this.textureReference = textureReference;
	}
	
	@Override
	protected void draw(S2dgiDrawCycle drawCycle, int centerX, int centerY) {
		ITexture texture = FlashyS2dgiEngine.get().getTexture(getTextureReference());
		drawCycle.render(texture, -centerX, -centerY, getWidth(), getHeight(), getQuarterRotation(), getFlipX(), getFlipY());
	}
	
	public ITextureReference getTextureReference() {
		return textureReference;
	}
	
	@Override
	public void init() {
	}

	@Override
	public void dispose() {
	}
	
	@Override
	public int getWidth() {
		return FlashyS2dgiEngine.get().getTexture(getTextureReference()).getWidth();
	}
	
	@Override
	public int getHeight() {
		return FlashyS2dgiEngine.get().getTexture(getTextureReference()).getHeight();
	}
	
	public boolean getFlipX() {
		return flipX;
	}
	
	public boolean getFlipY() {
		return flipY;
	}
	
	public int getQuarterRotation() {
		return quarterRotation;
	}
	
	public TextureGraphics setFlipX(boolean flipX) {
		this.flipX = flipX;
		return this;
	}
	
	public TextureGraphics setFlipY(boolean flipY) {
		this.flipY = flipY;
		return this;
	}
	
	public TextureGraphics setQuarterRotation(int quarterRotation) {
		this.quarterRotation = quarterRotation;
		return this;
	}
	
	public static final IWidgetInterface<TextureGraphics> WIDGET_INTERFACE = IWidgetInterface.cast(ARG_WIDGET_INTERFACE);
}
