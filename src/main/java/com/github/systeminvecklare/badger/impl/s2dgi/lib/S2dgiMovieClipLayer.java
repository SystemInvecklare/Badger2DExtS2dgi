package com.github.systeminvecklare.badger.impl.s2dgi.lib;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;

public abstract class S2dgiMovieClipLayer implements IMovieClipLayer {
	@Override
	public void draw(IDrawCycle drawCycle) {
		((S2dgiDrawCycle) drawCycle).applyTransforms();
		drawImpl((S2dgiDrawCycle) drawCycle);
	}

	protected abstract void drawImpl(S2dgiDrawCycle drawCycle);
}
