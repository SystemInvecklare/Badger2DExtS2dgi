package com.github.systeminvecklare.badger.impl.s2dgi.lib;

import java.util.Objects;

import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.util.GeometryUtil;
import com.github.systeminvecklare.badger.core.widget.IWidget;
import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;
import com.github.systeminvecklare.badger.impl.s2dgi.font.IFont;
import com.github.systeminvecklare.badger.impl.s2dgi.graphics.IColor;
import com.github.systeminvecklare.badger.impl.s2dgi.util.EqualsUtil;
import com.github.systeminvecklare.badger.impl.s2dgi.util.ICached;

/*package-protected*/ abstract class AbstractTextGraphics<Self, CK, CV extends AbstractTextGraphics.IComputedValues> extends S2dgiMovieClipLayer implements IMovieClipLayer, IWidget {
	private boolean hittable = false;
	private IFont font;
	private String text;
	private IColor tint = null;
	private IColor additive = null;
	
	protected ICached<CK, CV> cache = ICached.of(this::recomputeValue);
	
	private int offsetX = 0;
	private int offsetY = 0;
	private float anchorX = 0;
	private float anchorY = 0;
	
	public AbstractTextGraphics(IFont font, String text) {
		this.font = font;
		this.text = text;
	}
	
	@Override
	protected final void drawImpl(S2dgiDrawCycle drawCycle) {
		CV preparedText = getFreshValues();
		if(preparedText != null) {
			final IColor currentTint = getTint();
			final IColor currentAdditive = getAdditive();
			if(currentTint != null) {
				drawCycle.pushTint(currentTint);
			}
			if(currentAdditive != null) {
				drawCycle.pushAdditive(currentAdditive);
			}
			drawImpl(drawCycle, getX(), getY(), preparedText);
			if(currentTint != null) {
				drawCycle.popTint();
			}
			if(currentAdditive != null) {
				drawCycle.popAdditive();
			}
		}
	}
	
	/**
	 * Override getOffsetX instead
	 */
	@Override
	public final int getX() {
		CV preparedText = getFreshValues();
		if(preparedText != null) {
			return getOffsetX() - anchor(getAnchorX(), 0, preparedText.getWidth());
		}
		return getOffsetX();
	}
	
	/**
	 * Override getOffsetY instead
	 */
	@Override
	public final int getY() {
		CV preparedText = getFreshValues();
		if(preparedText != null) {
			return getOffsetY() - anchor(getAnchorY(), 0, preparedText.getHeight());
		}
		return getOffsetY();
	}
	
	private static int anchor(float anchorPos, int start, int length) {
		return start + (int) (anchorPos*length);
	}
	
	public IFont getFont() {
		return font;
	}
	
	public void setFont(IFont font) {
		this.font = font;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public int getOffsetX() {
		return offsetX;
	}
	
	public int getOffsetY() {
		return offsetY;
	}
	
	public float getAnchorX() {
		return anchorX;
	}
	
	public float getAnchorY() {
		return anchorY;
	}
	
	public Self setTint(IColor tint) {
		this.tint = tint;
		return self();
	}
	
	public Self setAdditive(IColor additive) {
		this.additive = additive;
		return self();
	}
	
	public IColor getTint() {
		return tint;
	}
	
	public IColor getAdditive() {
		return additive;
	}
	
	@SuppressWarnings("unchecked")
	private Self self() {
		return (Self) this;
	}
	
	public Self setAnchors(float anchorX, float anchorY) {
		this.anchorX = anchorX;
		this.anchorY = anchorY;
		return self();
	}

	public Self setOffset(int offsetX, int offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		return self();
	}

	@Override
	public void setPosition(int x, int y) {
		setOffset(x, y);
	}

	@Override
	public void addToPosition(int dx, int dy) {
		setOffset(this.offsetX + dx, this.offsetY + dy);
	}
	
	@Override
	public void init() {
	}
	
	@Override
	public boolean hitTest(IReadablePosition p) {
		if(hittable) {
			return GeometryUtil.isInRectangle(p.getX(), p.getY(), getX(), getY(), getWidth(), getHeight());
		}
		return false;
	}
	
	public Self makeHittable() {
		hittable = true;
		return self();
	}
	
	@Override
	public void dispose() {
		cache = null;
	}
	
	protected CV getFreshValues() {
		return cache.getValue(getFreshCachedKey());
	}
	

	@Override
	public int getWidth() {
		IComputedValues freshValues = getFreshValues();
		return freshValues != null ? freshValues.getWidth() : 0;
	}
	
	@Override
	public int getHeight() {
		IComputedValues freshValues = getFreshValues();
		return freshValues != null ? freshValues.getHeight() : 0;
	}
	
	protected abstract CK getFreshCachedKey();
	
	protected abstract CV recomputeValue(CK cacheKey);
	
	protected abstract void drawImpl(S2dgiDrawCycle drawCycle, int anchorOffsetX, int anchorOffsetY, CV preparedText);
	
	protected interface IComputedValues {
		int getWidth();
		int getHeight();
	}
	
	protected static class BaseCacheKey {
		public final String text;
		public final IFont font;
		
		public BaseCacheKey(String text, IFont font) {
			this.text = text;
			this.font = font;
		}

		@Override
		public int hashCode() {
			return Objects.hash(text, font);
		}
		
		@Override
		public boolean equals(Object obj) {
			return EqualsUtil.equalsOwn(BaseCacheKey.class, obj, other -> 
			  Objects.equals(this.text, other.text)
		   && Objects.equals(this.font, other.font));
		}
	}
}