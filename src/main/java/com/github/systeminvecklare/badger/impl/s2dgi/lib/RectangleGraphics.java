package com.github.systeminvecklare.badger.impl.s2dgi.lib;

import com.github.systeminvecklare.badger.core.widget.IResizableWidget;
import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;
import com.github.systeminvecklare.badger.impl.s2dgi.graphics.IColor;

public class RectangleGraphics extends AbstractRectangleGraphics<RectangleGraphics> implements IResizableWidget {
	private int x;
	private int y;
	private int width;
	private int height;
	private IColor color;
	
	public RectangleGraphics(int x, int y, int width, int height, IColor color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}

	@Override
	public void init() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public IColor getColor() {
		return color;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public void setWidth(int width) {
		this.width = width;
	}
	
	@Override
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setColor(IColor color) {
		this.color = color;
	}

	@Override
	protected void draw(S2dgiDrawCycle drawCycle, int centerX, int centerY) {
		drawCycle.renderRectangle(getX(), getY(), getWidth(), getHeight(), centerX, centerY, getColor());
	}
}
