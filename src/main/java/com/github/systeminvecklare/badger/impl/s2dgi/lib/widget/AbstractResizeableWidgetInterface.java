package com.github.systeminvecklare.badger.impl.s2dgi.lib.widget;

public abstract class AbstractResizeableWidgetInterface<W,B> implements IResizableWidgetInterface<W,B> {
	private final IWidgetInterface<B> base;
	
	public AbstractResizeableWidgetInterface(IWidgetInterface<B> base) {
		this.base = base;
	}

	@Override
	public int getWidth(B widget) {
		return base.getWidth(widget);
	}

	@Override
	public int getHeight(B widget) {
		return base.getHeight(widget);
	}

	@Override
	public void setPosition(B widget, int x, int y) {
		base.setPosition(widget, x, y);
	}

	@Override
	public void addToPosition(B widget, int dx, int dy) {
		base.addToPosition(widget, dx, dy);
	}
}
