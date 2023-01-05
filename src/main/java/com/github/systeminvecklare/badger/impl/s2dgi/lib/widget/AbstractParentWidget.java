package com.github.systeminvecklare.badger.impl.s2dgi.lib.widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/*package-protected*/ abstract class AbstractParentWidget<C extends AbstractParentWidget.Child<?>> implements IWidget {
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	
	protected final List<C> children = new ArrayList<C>();
	private final CellLayoutSettings defaultLayoutSettings = createDefaultLayoutSettings();
	

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
	
	@Override
	public void addToPosition(int dx, int dy) {
		setPosition(this.x + dx, this.y + dy);
	}
	
	@Override
	public void setPosition(int x, int y) {
		for(Child<?> child : children) {
			child.addToPosition(x - this.x, y - this.y);
		}
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	

	public void removeChild(Object child) {
		Iterator<C> it = children.iterator();
		while(it.hasNext()) {
			if(Objects.equals(it.next().widget, child)) {
				it.remove();
			}
		}
	}
	
	protected void getDefaultInterface(IWidget widget, IDefaultInterfaceHandler handler) {
		if(widget instanceof IResizableWidget) {
			handler.onWidget((IResizableWidget) widget, IResizableWidgetInterface.RESIZABLE_WIDGET_INTERFACE);
		} else {
			handler.onWidget(widget, IWidgetInterface.WIDGET_INTERFACE);
		}
	}
	
	protected CellLayoutSettings createDefaultLayoutSettings() {
		return new CellLayoutSettings(0, 0, 0, 0, 0, 0, false, false, 0, 0);
	}
	
	public ICellLayoutSettings defaultLayoutSettings() {
		return defaultLayoutSettings;
	}
	
	public ICellLayoutSettings newLayoutSettings() {
		return defaultLayoutSettings.copy();
	}
	
	protected static class Child<W> {
		public final W widget;
		public final IWidgetInterface<? super W> widgetInterface;
		public final CellLayoutSettings layoutSettings;
		
		public Child(W widget, IWidgetInterface<? super W> widgetInterface, CellLayoutSettings layoutSettings) {
			this.widget = widget;
			this.widgetInterface = widgetInterface;
			this.layoutSettings = layoutSettings.copy();
		}
		

		public void addToPosition(int dx, int dy) {
			widgetInterface.addToPosition(widget, dx, dy);
		}

		public void setPosition(int x, int y, int availableSpaceX, int availableSpaceY) {
			availableSpaceX -= layoutSettings.paddingLeft+layoutSettings.paddingRight;
			availableSpaceY -= layoutSettings.paddingTop+layoutSettings.paddingBottom;
			int width = widgetInterface.getWidth(widget);
			int height = widgetInterface.getHeight(widget);
			
			if((availableSpaceX > 0 || availableSpaceY > 0) && (layoutSettings.fillHorizontal || layoutSettings.fillVertical)) {
				IResizableWidgetInterface<W> resizable = IResizableWidget.tryCast(widget, widgetInterface);
				if(resizable != null) {
					if(availableSpaceX > 0 && layoutSettings.fillHorizontal && width != availableSpaceX) {
						resizable.setWidth(widget, availableSpaceX);
						width = availableSpaceX;
					}
					if(availableSpaceY > 0 && layoutSettings.fillVertical && height != availableSpaceY) {
						resizable.setHeight(widget, availableSpaceY);
						height = availableSpaceY;
					}
				}
			}
			
			widgetInterface.setPosition(widget, x+layoutSettings.getOffsetX(availableSpaceX-width), y+layoutSettings.getOffsetY(availableSpaceY-height));
		}

		public int getWidth() {
			int width = layoutSettings.fillHorizontal ? layoutSettings.minWidth : widgetInterface.getWidth(widget);
			return width+layoutSettings.paddingLeft+layoutSettings.paddingRight;
		}
		
		public int getHeight() {
			int height = layoutSettings.fillVertical ? layoutSettings.minHeight : widgetInterface.getHeight(widget);
			return height+layoutSettings.paddingTop+layoutSettings.paddingBottom;
		}
	}
	
	protected interface IDefaultInterfaceHandler {
		<W extends IWidget> void onWidget(W widget, IWidgetInterface<W> widgetInterface);
	}
}
