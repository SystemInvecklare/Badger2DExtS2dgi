package com.github.systeminvecklare.badger.impl.s2dgi.font.embellished;

import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;
import com.github.systeminvecklare.badger.impl.s2dgi.font.IText;
import com.github.systeminvecklare.badger.impl.s2dgi.graphics.IColor;

public class ColoredTextSegment implements ITextSegment {
	private final IText text;
	private final IColor tint;
	private final IColor additive;

	public ColoredTextSegment(IText text, IColor tint, IColor additive) {
		this.text = text;
		this.tint = tint;
		this.additive = additive;
	}

	@Override
	public void render(S2dgiDrawCycle drawCycle, int x, int y) {
		drawCycle.pushColorAdjust(tint, additive);
		text.render(drawCycle, x, y);
		drawCycle.popColorAdjust();
	}

	@Override
	public int getWidth() {
		return text.getWidth();
	}

	@Override
	public int getHeight() {
		return text.getHeight();
	}
}
