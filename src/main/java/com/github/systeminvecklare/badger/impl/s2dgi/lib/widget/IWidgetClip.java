package com.github.systeminvecklare.badger.impl.s2dgi.lib.widget;

import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClip;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransformOperation;
import com.github.systeminvecklare.badger.core.util.MCUtil;

public interface IWidgetClip extends IMovieClip {
	int getWidth();
	int getHeight();
	
	public static IWidgetInterface<IWidgetClip> INTERFACE = new IWidgetInterface<IWidgetClip>() {
		@Override
		public int getWidth(IWidgetClip widget) {
			return widget.getWidth();
		}
		
		@Override
		public int getHeight(IWidgetClip widget) {
			return widget.getHeight();
		}

		@Override
		public void setPosition(IWidgetClip widget, int x, int y) {
			if(widget.isInitialized()) {
				widget.modifyTransform(new ITransformOperation() {
					@Override
					public ITransform execute(ITransform transform) {
						return transform.setPosition(x, y);
					}
				}, false, false);
			} else {
				MCUtil.manipulate(widget).setPosition(x, y).end();
			}
		}
		
		@Override
		public void addToPosition(IWidgetClip widget, int dx, int dy) {
			if(widget.isInitialized()) {
				widget.modifyTransform(new ITransformOperation() {
					@Override
					public ITransform execute(ITransform transform) {
						return transform.addToPosition(dx, dy);
					}
				}, false, false);
			} else {
				MCUtil.manipulate(widget).addPosition(dx, dy).end();
			}
		}
	};
}
