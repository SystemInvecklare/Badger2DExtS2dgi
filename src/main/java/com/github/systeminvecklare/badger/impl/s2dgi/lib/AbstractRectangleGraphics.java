package com.github.systeminvecklare.badger.impl.s2dgi.lib;

import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.util.GeometryUtil;
import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;
import com.github.systeminvecklare.badger.impl.s2dgi.lib.widget.IWidget;

/*package-private*/ abstract class AbstractRectangleGraphics<T extends AbstractRectangleGraphics<?>> extends S2dgiMovieClipLayer implements IWidget {
	private int x = 0;
	private int y = 0;
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
		setPosition(-x, -y);
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
		return -getX();
	}
	
	public int getCenterY() {
		return -getY();
	}
	
	@Override
	public int getX() {
		return x;
	}
	
	@Override
	public int getY() {
		return y;
	}
	
	@Override
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void addToPosition(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}
	
	protected abstract void draw(S2dgiDrawCycle drawCycle, int centerX, int centerY);
}
