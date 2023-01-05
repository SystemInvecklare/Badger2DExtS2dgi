package com.github.systeminvecklare.badger.impl.s2dgi.lib.widget;

public interface IResizableWidget extends IWidget {
	void setWidth(int width);
	void setHeight(int height);
	
	
	@SuppressWarnings("unchecked")
	public static <W> IResizableWidgetInterface<W> tryCast(W widget, IWidgetInterface<? super W> widgetInterface) {
		if(widgetInterface instanceof IResizableWidgetInterface) {
			return (IResizableWidgetInterface<W>) widgetInterface;
		} else if(widget instanceof IResizableWidget) {
			if(widgetInterface == IWidgetInterface.WIDGET_INTERFACE) {
				return (IResizableWidgetInterface<W>) IResizableWidgetInterface.RESIZABLE_WIDGET_INTERFACE;
			} else {
				return (IResizableWidgetInterface<W>) IResizableWidgetInterface.createExtension((IWidgetInterface<IResizableWidget>) widgetInterface, IResizableWidgetInterface._EXTENSION_RESIZABLE_WIDGET_INTERFACE);
			}
		}
		return null;
	}
}
