package com.github.systeminvecklare.badger.impl.s2dgi.lib;

import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
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
}
