package com.github.systeminvecklare.badger.impl.s2dgi.font.embellished;

import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;
import com.github.systeminvecklare.badger.impl.s2dgi.font.IText;

public class SimpleTextSegment implements ITextSegment {
	private final IText text;
	
	public SimpleTextSegment(IText text) {
		this.text = text;
	}

	@Override
	public int getWidth() {
		return text.getWidth();
	}

	@Override
	public int getHeight() {
		return text.getHeight();
	}
	
	public IText getText() {
		return text;
	}

	@Override
	public void render(S2dgiDrawCycle drawCycle, int x, int y) {
		text.render(drawCycle, x, y);
	}
}
