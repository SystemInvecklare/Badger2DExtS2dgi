package com.github.systeminvecklare.badger.impl.s2dgi.lib;

import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;
import com.github.systeminvecklare.badger.impl.s2dgi.font.IFont;
import com.github.systeminvecklare.badger.impl.s2dgi.font.IText;
import com.github.systeminvecklare.badger.impl.s2dgi.lib.AbstractTextGraphics.IComputedValues;
import com.github.systeminvecklare.badger.impl.s2dgi.lib.widget.IWidget;

public class TextGraphics extends AbstractTextGraphics<TextGraphics, AbstractTextGraphics.BaseCacheKey, TextWrapper> implements IMovieClipLayer, IWidget {
	public TextGraphics(IFont font, String text) {
		super(font, text);
	}
	
	@Override
	protected BaseCacheKey getFreshCachedKey() {
		//TODO Maybe have a pool for these? They are super-short-lived.
		return new BaseCacheKey(getText(), getFont());
	}
	
	@Override
	protected TextWrapper recomputeValue(BaseCacheKey cacheKey) {
		return cacheKey == null ? null : new TextWrapper(cacheKey.font.complile(cacheKey.text));
	}
	
	@Override
	protected void drawImpl(S2dgiDrawCycle drawCycle, int anchorOffsetX, int anchorOffsetY, TextWrapper preparedText) {
		preparedText.text.render(drawCycle, getOffsetX() - anchorOffsetX, getOffsetY() - anchorOffsetY);
	}
}
/*package-protected*/ class TextWrapper implements IComputedValues {
	public final IText text;
	
	public TextWrapper(IText text) {
		this.text = text;
	}

	@Override
	public int getWidth() {
		return text.getWidth();
	}

	@Override
	public int getHeight() {
		return text.getHeight();
	}
}
