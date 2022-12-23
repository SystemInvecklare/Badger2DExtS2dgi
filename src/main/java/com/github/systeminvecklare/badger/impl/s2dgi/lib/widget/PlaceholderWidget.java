package com.github.systeminvecklare.badger.impl.s2dgi.lib.widget;

public class PlaceholderWidget implements IResizableWidget {
	private int x;
	private int y;
	private int width;
	private int height;
	
	public PlaceholderWidget() {
	}
	
	public PlaceholderWidget(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void addToPosition(int dx, int dy) {
		setPosition(this.x+dx, this.y+dy);
	}
	
	@Override
	public void setWidth(int width) {
		this.width = width;
	}
	
	@Override
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
}
