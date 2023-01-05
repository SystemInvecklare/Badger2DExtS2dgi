package com.github.systeminvecklare.badger.impl.s2dgi.lib;

import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
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
}
