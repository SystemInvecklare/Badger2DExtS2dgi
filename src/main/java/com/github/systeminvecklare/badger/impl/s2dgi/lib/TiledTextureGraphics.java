package com.github.systeminvecklare.badger.impl.s2dgi.lib;

import com.github.systeminvecklare.badger.core.widget.IRectangle;
import com.github.systeminvecklare.badger.core.widget.IRectangleInterface;
import com.github.systeminvecklare.badger.core.widget.IResizableWidget;
import com.github.systeminvecklare.badger.impl.s2dgi.FlashyS2dgiEngine;
import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;
import com.github.systeminvecklare.badger.impl.s2dgi.graphics.ITextureReference;

import net.pointlessgames.libs.s2dgi.texture.ITexture;

public class TiledTextureGraphics extends AbstractRectangleGraphics<TiledTextureGraphics> implements IResizableWidget {
	private final ITextureReference textureReference;
	private int width;
	private int height;
	
	public TiledTextureGraphics(ITextureReference textureReference, IRectangle rectangle) {
		this(textureReference, rectangle, IRectangle.INTERFACE);
	}
	
	public <R> TiledTextureGraphics(ITextureReference textureReference, R rectangle, IRectangleInterface<R> rectangleInterface) {
		this(textureReference, rectangleInterface.getX(rectangle), rectangleInterface.getY(rectangle), rectangleInterface.getWidth(rectangle), rectangleInterface.getHeight(rectangle));
	}
	
	public TiledTextureGraphics(ITextureReference textureReference, int x, int y, int width, int height) {
		this.textureReference = textureReference;
		this.width = width;
		this.height = height;
		setPosition(x, y);
	}
	
	public TiledTextureGraphics(ITextureReference textureReference, int width, int height) {
		this(textureReference, 0, 0, width, height);
	}
	
	@Override
	protected void draw(S2dgiDrawCycle drawCycle, int centerX, int centerY) {
		ITexture texture = FlashyS2dgiEngine.get().getTexture(getTextureReference());
		drawCycle.renderTiled(texture, getSourceOffsetX(), getSourceOffsetY()-getHeight(), getX(), getY(), getWidth(), getHeight(), centerX, centerY);
	}

	public int getSourceOffsetX() {
		return 0;
	}
	
	public int getSourceOffsetY() {
		return 0;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void init() {
	}

	@Override
	public void dispose() {
	}

	
	public ITextureReference getTextureReference() {
		return textureReference;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setTo(IRectangle rectangle) {
		setTo(rectangle, IRectangle.INTERFACE);
	}

	public <R> void setTo(R rectangle, IRectangleInterface<R> rectangleInterface) {
		setPosition(rectangleInterface.getX(rectangle), rectangleInterface.getY(rectangle));
		setWidth(rectangleInterface.getWidth(rectangle));
		setHeight(rectangleInterface.getHeight(rectangle));
	}
}
