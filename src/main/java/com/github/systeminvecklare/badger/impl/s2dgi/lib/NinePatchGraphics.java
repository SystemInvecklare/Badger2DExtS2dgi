package com.github.systeminvecklare.badger.impl.s2dgi.lib;

import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.core.widget.IRectangle;
import com.github.systeminvecklare.badger.core.widget.IRectangleInterface;
import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;
import com.github.systeminvecklare.badger.impl.s2dgi.graphics.INinePatchReference;

public class NinePatchGraphics extends AbstractNinePatchGraphics<NinePatchGraphics> implements IMovieClipLayer {
	public NinePatchGraphics(INinePatchReference ninepatch, int width, int height) {
		super(ninepatch, width, height, true);
	}

	@Override
	public void init() {
	}

	@Override
	public void dispose() {
	}
	
	public static void renderNinePatch(S2dgiDrawCycle drawCycle, INinePatchReference ninepatch, int x, int y, int width, int height) {
		renderNinePatch(drawCycle, ninepatch, x, y, width, height, true);
	}
	
	public static <R> void renderNinePatch(S2dgiDrawCycle drawCycle, INinePatchReference ninepatch, R rectangle, IRectangleInterface<R> rectangleInterface) {
		renderNinePatch(drawCycle, ninepatch, rectangleInterface.getX(rectangle), rectangleInterface.getY(rectangle), rectangleInterface.getWidth(rectangle), rectangleInterface.getHeight(rectangle));
	}
	
	public static void renderNinePatch(S2dgiDrawCycle drawCycle, INinePatchReference ninepatch, IRectangle rectangle) {
		renderNinePatch(drawCycle, ninepatch, rectangle, IRectangle.INTERFACE);
	}
}
