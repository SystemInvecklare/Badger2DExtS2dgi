package com.github.systeminvecklare.badger.impl.s2dgi.lib;

import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.core.widget.IRectangle;
import com.github.systeminvecklare.badger.core.widget.IRectangleInterface;
import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;
import com.github.systeminvecklare.badger.impl.s2dgi.graphics.INinePatchReference;

/**
 * Like {@link NinePatchGraphics}, but doesn't render the center patch.
 */
public class BorderGraphics extends AbstractNinePatchGraphics<BorderGraphics> implements IMovieClipLayer {
	public BorderGraphics(INinePatchReference ninepatch, int width, int height) {
		super(ninepatch, width, height, false);
	}

	@Override
	public void init() {
	}

	@Override
	public void dispose() {
	}
	
	public static void renderBorder(S2dgiDrawCycle drawCycle, INinePatchReference ninepatch, int x, int y, int width, int height) {
		renderNinePatch(drawCycle, ninepatch, x, y, width, height, false);
	}
	
	public static <R> void renderBorder(S2dgiDrawCycle drawCycle, INinePatchReference ninepatch, R rectangle, IRectangleInterface<R> rectangleInterface) {
		renderBorder(drawCycle, ninepatch, rectangleInterface.getX(rectangle), rectangleInterface.getY(rectangle), rectangleInterface.getWidth(rectangle), rectangleInterface.getHeight(rectangle));
	}
	
	public static void renderBorder(S2dgiDrawCycle drawCycle, INinePatchReference ninepatch, IRectangle rectangle) {
		renderBorder(drawCycle, ninepatch, rectangle, IRectangle.INTERFACE);
	}
}
