package com.github.systeminvecklare.badger.impl.s2dgi.font;

import com.github.systeminvecklare.badger.core.widget.IRectangle;
import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;

public interface IText {
	void render(S2dgiDrawCycle drawCycle, int x, int y);
	IFont getFont();
	String getString();
	int getXOffset();
	int getYOffset();
	int getWidth();
	int getHeight();
	
	//Convenience method
	default void renderIn(S2dgiDrawCycle drawCycle, IRectangle rectangle, float alignX, float alignY) {
		int x = rectangle.getX() + (int) ((rectangle.getWidth() - this.getWidth())*alignX);
		int y = rectangle.getY() + (int) ((rectangle.getHeight() - this.getHeight())*alignY);
		this.render(drawCycle, x, y);
	}
}
