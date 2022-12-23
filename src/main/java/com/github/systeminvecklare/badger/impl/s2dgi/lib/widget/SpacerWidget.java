package com.github.systeminvecklare.badger.impl.s2dgi.lib.widget;

public class SpacerWidget implements IWidget {
	private final int width;
	private final int height;
	
	public SpacerWidget(int width, int height) {
		this.width = width;
		this.height = height;
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
	public void setPosition(int x, int y) {
	}

	@Override
	public void addToPosition(int dx, int dy) {
	}

	public static SpacerWidget width(int width) {
		return new SpacerWidget(width, 0);
	}
	
	public static SpacerWidget height(int height) {
		return new SpacerWidget(0, height);
	}
}
