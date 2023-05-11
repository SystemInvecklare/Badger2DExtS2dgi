package com.github.systeminvecklare.badger.impl.s2dgi.lib;

import java.util.function.BiConsumer;

import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.core.widget.IWidget;
import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;
import com.github.systeminvecklare.badger.impl.s2dgi.font.IFont;
import com.github.systeminvecklare.badger.impl.s2dgi.font.IText;
import com.github.systeminvecklare.badger.impl.s2dgi.lib.AbstractTextGraphics.IComputedValues;

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
	protected final void drawImpl(S2dgiDrawCycle drawCycle, int x, int y, TextWrapper preparedText) {
		overridableDrawImpl(drawCycle, x, y, (_x, _y) -> preparedText.text.render(drawCycle, _x, _y));
	}
	
	protected void overridableDrawImpl(S2dgiDrawCycle drawCycle, int x, int y, BiConsumer<Integer, Integer> superDraw) {
		superDraw.accept(x, y);
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
