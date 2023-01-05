package com.github.systeminvecklare.badger.impl.s2dgi.lib.widget;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClip;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransformOperation;
import com.github.systeminvecklare.badger.core.util.MCUtil;

public interface IWidget extends IRectangle {
	void setPosition(int x, int y);
	void addToPosition(int dx, int dy);
	
	public static void setMovieClipPosition(IMovieClip movieClip, int x, int y) {
		if(movieClip.isInitialized()) {
			movieClip.modifyTransform(new ITransformOperation() {
				@Override
				public ITransform execute(ITransform transform) {
					return transform.setPosition(x, y);
				}
			}, false, false);
		} else {
			MCUtil.manipulate(movieClip).setPosition(x, y).end();
		}
	}
	
	public static void addToMovieClipPosition(IMovieClip movieClip, int dx, int dy) {
		if(movieClip.isInitialized()) {
			movieClip.modifyTransform(new ITransformOperation() {
				@Override
				public ITransform execute(ITransform transform) {
					return transform.addToPosition(dx, dy);
				}
			}, false, false);
		} else {
			MCUtil.manipulate(movieClip).addPosition(dx, dy).end();
		}
	}
	
	public static int getMovieClipX(IMovieClip movieClip) {
		ITransform transform = FlashyEngine.get().getPoolManager().getPool(ITransform.class).obtain();
		try {
			return (int) MCUtil.getTransform(movieClip, transform).getPosition().getX();
		} finally {
			transform.free();
		}
	}
	
	public static int getMovieClipY(IMovieClip movieClip) {
		ITransform transform = FlashyEngine.get().getPoolManager().getPool(ITransform.class).obtain();
		try {
			return (int) MCUtil.getTransform(movieClip, transform).getPosition().getY();
		} finally {
			transform.free();
		}
	}
}
