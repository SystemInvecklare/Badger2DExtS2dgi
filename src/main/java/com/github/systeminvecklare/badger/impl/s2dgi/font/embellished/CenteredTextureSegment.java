package com.github.systeminvecklare.badger.impl.s2dgi.font.embellished;

import com.github.systeminvecklare.badger.impl.s2dgi.FlashyS2dgiEngine;
import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;
import com.github.systeminvecklare.badger.impl.s2dgi.font.IFont;
import com.github.systeminvecklare.badger.impl.s2dgi.graphics.ITextureReference;

import net.pointlessgames.libs.s2dgi.texture.ITexture;

public class CenteredTextureSegment implements ITextSegment {
	private final ITextureReference textureReference;
	private final int lineHeight;
	

	public CenteredTextureSegment(ITextureReference textureReference, int lineHeight) {
		this.textureReference = textureReference;
		this.lineHeight = lineHeight;
	}
	
	public CenteredTextureSegment(ITextureReference textureReference, IFont font) {
		this(textureReference, font.complile("M").getHeight());
	}

	@Override
	public int getWidth() {
		return textureReference.getWidth();
	}

	@Override
	public int getHeight() {
		return textureReference.getHeight();
	}

	@Override
	public void render(S2dgiDrawCycle drawCycle, int x, int y) {
		ITexture texture = FlashyS2dgiEngine.get().getTexture(textureReference);
		int yOffset = (lineHeight - texture.getHeight())/2;
		drawCycle.applyTransforms();
		drawCycle.render(texture, x, y + yOffset);
	}
}
