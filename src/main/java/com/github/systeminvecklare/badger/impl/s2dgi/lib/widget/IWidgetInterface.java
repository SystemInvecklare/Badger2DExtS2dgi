package com.github.systeminvecklare.badger.impl.s2dgi.lib.widget;

public interface IWidgetInterface<W> {
	int getWidth(W widget);
	int getHeight(W widget);
	void setPosition(W widget, int x, int y);
	void addToPosition(W widget, int dx, int dy);
	
	@SuppressWarnings("unchecked")
	public static <W> IWidgetInterface<W> cast(IWidgetInterface<? super W> widgetInterface) {
		return (IWidgetInterface<W>) widgetInterface;
	}
}
