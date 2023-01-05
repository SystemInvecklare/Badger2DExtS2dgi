package com.github.systeminvecklare.badger.impl.s2dgi.lib;

import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.core.widget.IResizableWidget;
import com.github.systeminvecklare.badger.impl.s2dgi.FlashyS2dgiEngine;
import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;
import com.github.systeminvecklare.badger.impl.s2dgi.graphics.INinePatchReference;

import net.pointlessgames.libs.s2dgi.texture.ITexture;

/*package-private*/ abstract class AbstractNinePatchGraphics<T extends AbstractNinePatchGraphics<?>> extends AbstractRectangleGraphics<T> implements IMovieClipLayer, IResizableWidget {
	private final INinePatchReference ninepatch;
	private final boolean renderCenter;
	private int width;
	private int height;
	
	protected AbstractNinePatchGraphics(INinePatchReference ninepatch, int width, int height, boolean renderCenter) {
		this.ninepatch = ninepatch;
		this.width = width;
		this.height = height;
		this.renderCenter = renderCenter;
	}
	
	@Override
	protected void draw(S2dgiDrawCycle drawCycle, int centerX, int centerY) {
		INinePatchReference currentNinepatch = getNinepatch();
		ITexture texture = FlashyS2dgiEngine.get().getTexture(currentNinepatch.geTextureReference());
		final int textureWidth = texture.getWidth();
		final int textureHeight = texture.getHeight();
		
		final int leftPad = currentNinepatch.getPadLeft();
		final int rightPad = currentNinepatch.getPadRight();
		final int topPad = currentNinepatch.getPadTop();
		final int bottomPad = currentNinepatch.getPadBottom();
		final int currentWidth = getWidth();
		final int currentHeight = getHeight();
		
		final int midWidth = currentWidth - leftPad - rightPad;
		final int centerHeight = currentHeight - topPad - bottomPad;
		
		ITexture bottomMidTexture = texture.createSubTexture(leftPad, textureHeight - bottomPad, textureWidth - rightPad - leftPad, bottomPad);
		ITexture leftCenterTexture = texture.createSubTexture(0, topPad, leftPad, textureHeight - topPad - bottomPad);
		ITexture midCenterTexture = renderCenter ? texture.createSubTexture(leftPad, topPad, textureWidth - rightPad - leftPad, textureHeight - topPad - bottomPad) : null;
		ITexture rightCenterTexture = texture.createSubTexture(textureWidth - rightPad, topPad, rightPad, textureHeight - topPad - bottomPad);
		ITexture topMidTexture = texture.createSubTexture(leftPad, 0, textureWidth - rightPad - leftPad, topPad);
		
		drawCycle.renderTiled(texture, 0, textureHeight - bottomPad, -centerX, -centerY, leftPad, bottomPad);
		drawCycle.renderTiled(bottomMidTexture, 0, 0, (-centerX)+leftPad, -centerY, midWidth, bottomPad);
		drawCycle.renderTiled(texture, rightPad , textureHeight - bottomPad, (-centerX) + currentWidth - rightPad, -centerY, rightPad, bottomPad);
		
		drawCycle.renderTiled(leftCenterTexture, 0, 0, -centerX, (-centerY) +bottomPad, leftPad, centerHeight);
		if(renderCenter) {
			drawCycle.renderTiled(midCenterTexture, 0, 0, (-centerX) + leftPad, (-centerY) + bottomPad, midWidth, centerHeight);
		}
		drawCycle.renderTiled(rightCenterTexture, 0, 0, (-centerX) + currentWidth - rightPad, (-centerY) + bottomPad, rightPad, centerHeight);
		
		drawCycle.renderTiled(texture, 0, 0, -centerX, (-centerY) + currentHeight - topPad, leftPad, topPad);
		drawCycle.renderTiled(topMidTexture, 0, 0, (-centerX) + leftPad, (-centerY) + currentHeight - topPad, midWidth, topPad);
		drawCycle.renderTiled(texture, rightPad , 0, (-centerX) + currentWidth - rightPad, (-centerY) + currentHeight - topPad, rightPad, topPad);
	}
	
	protected INinePatchReference getNinepatch() {
		return ninepatch;
	}
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	@Override
	public void setWidth(int width) {
		this.width = width;
	}
	
	@Override
	public void setHeight(int height) {
		this.height = height;
	}
}
