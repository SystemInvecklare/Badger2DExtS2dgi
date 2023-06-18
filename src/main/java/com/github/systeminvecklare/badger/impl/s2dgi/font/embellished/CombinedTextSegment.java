package com.github.systeminvecklare.badger.impl.s2dgi.font.embellished;

import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;

public class CombinedTextSegment implements ITextSegment {
	private final int spacing;
	private final ITextSegment[] segments;
	private final int width;
	private final int height;

	public CombinedTextSegment(ITextSegment ... segments) {
		this(0, segments);
	}
	
	public CombinedTextSegment(int spacing, ITextSegment ... segments) {
		this.spacing = spacing;
		this.segments = segments;
		int width = spacing*(segments.length - 1);
		int height = 0;
		for(ITextSegment segment : segments) {
			width += segment.getWidth();
			height = Math.max(segment.getHeight(), height);
		}
		this.width = width;
		this.height = height;
	}

	@Override
	public void render(S2dgiDrawCycle drawCycle, int x, int y) {
		for(ITextSegment segment : segments) {
			segment.render(drawCycle, x, y);
			x += spacing + segment.getWidth();
		}
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
}
