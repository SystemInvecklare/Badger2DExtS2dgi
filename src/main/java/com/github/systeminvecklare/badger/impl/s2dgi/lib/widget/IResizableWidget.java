package com.github.systeminvecklare.badger.impl.s2dgi.lib.widget;

public interface IResizableWidget extends IWidget {
	void setWidth(int width);
	void setHeight(int height);
	
	public static final IResizableWidgetInterface<IResizableWidget, IWidget> INTERFACE = extend(IWidget.INTERFACE);
	
	public static <B> IResizableWidgetInterface<IResizableWidget,B> extend(IWidgetInterface<B> widgetInterface) {
		return new AbstractResizeableWidgetInterface<IResizableWidget, B>(widgetInterface) {
			@Override
			public void setWidth(IResizableWidget widget, int width) {
				widget.setWidth(width);
			}

			@Override
			public void setHeight(IResizableWidget widget, int height) {
				widget.setHeight(height);
			}
		};
	}
	
	@SuppressWarnings("unchecked")
	public static <W> IResizableWidgetInterface<W,W> tryCast(W widget, IWidgetInterface<W> widgetInterface) {
		if(widgetInterface instanceof IResizableWidgetInterface) {
			return (IResizableWidgetInterface<W,W>) widgetInterface;
		} else if(widget instanceof IResizableWidget) {
			return (IResizableWidgetInterface<W, W>) IResizableWidget.extend(widgetInterface);
		} else {
			return null;
		}
	}
}
