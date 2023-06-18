package com.github.systeminvecklare.badger.impl.s2dgi.font.embellished;

import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;

public interface ITextSegment {
	void render(S2dgiDrawCycle drawCycle, int x, int y);
	int getWidth();
	int getHeight();
}
