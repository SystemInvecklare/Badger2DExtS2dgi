package com.github.systeminvecklare.badger.impl.s2dgi.font;

import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;

public interface IText {
	void render(S2dgiDrawCycle drawCycle, int x, int y);
	IFont getFont();
	String getString();
	int getXOffset();
	int getYOffset();
	int getWidth();
	int getHeight();
}
