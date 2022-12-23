package com.github.systeminvecklare.badger.impl.s2dgi.lib;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.MovieClip;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.util.GeometryUtil;
import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;
import com.github.systeminvecklare.badger.impl.s2dgi.input.IHoverMasking;

public class ClippingClip extends MovieClip implements IHoverMasking {
	private int clipX;
	private int clipY;
	private int clipWidth;
	private int clipHeight;

	public ClippingClip(int clipX, int clipY, int clipWidth, int clipHeight) {
		this.clipX = clipX;
		this.clipY = clipY;
		this.clipWidth = clipWidth;
		this.clipHeight = clipHeight;
	}

	@Override
	public void drawWithoutTransform(IDrawCycle drawCycle) {
		((S2dgiDrawCycle) drawCycle).applyTransforms();
		((S2dgiDrawCycle) drawCycle).pushClipRectangle(getClipX(), getClipY(), getClipWidth(), getClipHeight());
		super.drawWithoutTransform(drawCycle);
		((S2dgiDrawCycle) drawCycle).popClipRectangle();
	}

	public int getClipX() {
		return clipX;
	}
	
	public int getClipY() {
		return clipY;
	}

	public int getClipWidth() {
		return clipWidth;
	}
	
	public int getClipHeight() {
		return clipHeight;
	}
	
	public void setClipX(int clipX) {
		this.clipX = clipX;
	}
	
	public void setClipY(int clipY) {
		this.clipY = clipY;
	}
	
	public void setClipWidth(int clipWidth) {
		this.clipWidth = clipWidth;
	}
	
	public void setClipHeight(int clipHeight) {
		this.clipHeight = clipHeight;
	}
	
	public void setClip(int x, int y, int width, int height) {
		this.clipX = x;
		this.clipY = y;
		this.clipWidth = width;
		this.clipHeight = height;
	}
	
	
	
	@Override
	public boolean hitTest(IReadablePosition p) {
		boolean inBounds;
		EasyPooler ep = EasyPooler.obtainFresh();
		try {
			Position ptDst = ep.obtain(Position.class);
			ITransform trans = ep.obtain(ITransform.class);
			getTransform(trans).invert().transform(ptDst.setTo(p));
			inBounds = GeometryUtil.isInRectangle(ptDst.getX(), ptDst.getY(), getClipX(), getClipY(), getClipWidth(), getClipHeight());
		} finally {
			ep.freeAllAndSelf();
		}
		return inBounds && super.hitTest(p);
	}

	@Override
	public boolean mayHoverChildren(IReadablePosition globalPosition) {
		EasyPooler ep = EasyPooler.obtainFresh();
		try {
			IReadablePosition p = toLocalTransform(ep.obtain(ITransform.class).setToIdentity().setPosition(globalPosition)).getPosition();
			return GeometryUtil.isInRectangle(p.getX(), p.getY(), getClipX(), getClipY(), getClipWidth(), getClipHeight());
		} finally {
			ep.freeAllAndSelf();
		}
	}
	
	
}
