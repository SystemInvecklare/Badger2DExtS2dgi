package com.github.systeminvecklare.badger.impl.s2dgi.lib.widget;

public interface IResizableWidgetInterface<W,B> extends IWidgetInterface<B> {
	void setWidth(W widget, int width);
	void setHeight(W widget, int height);
	
	
	@SuppressWarnings("unchecked")
	public static <W> IResizableWidgetInterface<W, W> cast(IResizableWidgetInterface<? super W, ? super W> resizableWidgetInterface) {
		return (IResizableWidgetInterface<W, W>) resizableWidgetInterface;
	}
}
