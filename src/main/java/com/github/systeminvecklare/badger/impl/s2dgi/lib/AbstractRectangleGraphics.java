package com.github.systeminvecklare.badger.impl.s2dgi.lib;

import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.util.GeometryUtil;
import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;
import com.github.systeminvecklare.badger.impl.s2dgi.lib.widget.IWidgetInterface;

/*package-private*/ abstract class AbstractRectangleGraphics<T extends AbstractRectangleGraphics<?>> extends S2dgiMovieClipLayer {
	private int centerX = 0;
	private int centerY = 0;
	private boolean hittable = false;
	
	@Override
	protected void drawImpl(S2dgiDrawCycle drawCycle) {
		draw(drawCycle, getCenterX(), getCenterY());
	}
	
	@Override
	public boolean hitTest(IReadablePosition p) {
		if(hittable) {
			return GeometryUtil.isInRectangle(p.getX(), p.getY(), -getCenterX(), -getCenterY(), getWidth(), getHeight());
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public T setCenter(int x, int y) {
		this.centerX = x;
		this.centerY = y;
		return (T) this;
	}
	
	
	public T setCenter(int xAndY) {
		return setCenter(xAndY, xAndY);
	}
	
	public T setCenterRelative(float rx, float ry) {
		return setCenter((int) (rx*getWidth()), (int) (ry*getHeight()));
	}
	
	public T setCenterRelative(float xAndY) {
		return setCenterRelative(xAndY, xAndY);
	}
	
	@SuppressWarnings("unchecked")
	public T makeHittable() {
		hittable = true;
		return (T) this;
	}
	
	public int getCenterX() {
		return centerX;
	}
	
	public int getCenterY() {
		return centerY;
	}

	public abstract int getWidth();
	public abstract int getHeight();
	protected abstract void draw(S2dgiDrawCycle drawCycle, int centerX, int centerY);
	
	protected static final IWidgetInterface<AbstractRectangleGraphics<?>> ARG_WIDGET_INTERFACE = new IWidgetInterface<AbstractRectangleGraphics<?>>() {
		@Override
		public void setPosition(AbstractRectangleGraphics<?> widget, int x, int y) {
			widget.setCenter(-x, -y);
		}
		
		@Override
		public int getWidth(AbstractRectangleGraphics<?> widget) {
			return widget.getWidth();
		}
		
		@Override
		public int getHeight(AbstractRectangleGraphics<?> widget) {
			return widget.getHeight();
		}
		
		@Override
		public void addToPosition(AbstractRectangleGraphics<?> widget, int dx, int dy) {
			widget.setCenter(widget.getCenterX()-dx, widget.getCenterY()-dy);
		}
	};
}
