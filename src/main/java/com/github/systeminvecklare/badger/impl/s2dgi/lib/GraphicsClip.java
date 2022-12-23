package com.github.systeminvecklare.badger.impl.s2dgi.lib;

import com.github.systeminvecklare.badger.core.graphics.components.movieclip.MovieClip;
import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.core.util.MCUtil;

public class GraphicsClip extends MovieClip {
	public GraphicsClip(IMovieClipLayer graphics, float x, float y) {
		addGraphics(graphics);
		MCUtil.manipulate(this).setPosition(x, y).end();
	}
}
