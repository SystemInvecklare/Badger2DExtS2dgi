package com.github.systeminvecklare.badger.impl.s2dgi.lib.widget;

public interface IWidget {
	int getWidth();
	int getHeight();
	void setPosition(int x, int y);
	void addToPosition(int dx, int dy);
	
	public static final IWidgetInterface<IWidget> INTERFACE = new IWidgetInterface<IWidget>() {
		@Override
		public int getWidth(IWidget widget) {
			return widget.getWidth();
		}

		@Override
		public int getHeight(IWidget widget) {
			return widget.getHeight();
		}
		
		@Override
		public void setPosition(IWidget widget, int x, int y) {
			widget.setPosition(x, y);
		}
		
		@Override
		public void addToPosition(IWidget widget, int dx, int dy) {
			widget.addToPosition(dx, dy);
		}
	};
}
