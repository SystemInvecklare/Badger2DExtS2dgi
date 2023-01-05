package com.github.systeminvecklare.badger.impl.s2dgi.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.core.widget.IWidget;
import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;
import com.github.systeminvecklare.badger.impl.s2dgi.font.IFont;
import com.github.systeminvecklare.badger.impl.s2dgi.font.IText;
import com.github.systeminvecklare.badger.impl.s2dgi.font.ITextLayoutProcedure;
import com.github.systeminvecklare.badger.impl.s2dgi.lib.AbstractTextGraphics.BaseCacheKey;
import com.github.systeminvecklare.badger.impl.s2dgi.lib.AbstractTextGraphics.IComputedValues;
import com.github.systeminvecklare.badger.impl.s2dgi.util.EqualsUtil;

public class MultilineTextGraphics extends AbstractTextGraphics<MultilineTextGraphics, CacheKey, ComputedValues> implements IMovieClipLayer, IWidget {
	private int maxWidth;
	private float rowAlign = 0;
	
	public MultilineTextGraphics(IFont font, String text, int maxWidth) {
		super(font, text);
		this.maxWidth = maxWidth;
	}
	
	@Override
	protected ComputedValues recomputeValue(CacheKey cacheKey) {
		ITextLayoutProcedure layoutProcedure = ITextLayoutProcedure.maxWidth(cacheKey.font, cacheKey.maxWidth);
		List<String> lines = layoutProcedure.layoutText(cacheKey.text, new ArrayList<>());
		
		List<IText> texts = new ArrayList<>();
		for(String line : lines) {
			texts.add(cacheKey.font.complile(line));
		}
		int lineHeight = cacheKey.font.complile(" \n ").getHeight()-cacheKey.font.complile(" ").getHeight();
		return new ComputedValues(texts, lineHeight);
	}
	
	@Override
	protected CacheKey getFreshCachedKey() {
		return new CacheKey(getText(), getFont(), getMaxWidth());
	}
	
	@Override
	protected void drawImpl(S2dgiDrawCycle drawCycle, int x, int y,
			ComputedValues preparedText) {
		float currentRowAlign = getRowAlign();
		int lineOffset = preparedText.lineHeight - preparedText.height;
		for(IText line : preparedText.lines) {
			int offsetFromAlign = align(currentRowAlign, line.getWidth(), preparedText.width);
			line.render(drawCycle, offsetFromAlign + x, y - lineOffset);
			lineOffset += preparedText.lineHeight;
		}
	}
	
	private static int align(float align, int length, int containerLength) {
		return (int) ((containerLength - length)*align);
	}
	
	public int getMaxWidth() {
		return maxWidth;
	}
	
	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}
	
	public MultilineTextGraphics alignRows(float alignX) {
		setRowAlign(alignX);
		return this;
	}
	
	public void setRowAlign(float rowAlign) {
		this.rowAlign = rowAlign;
	}
	
	public float getRowAlign() {
		return rowAlign;
	}
}
/*package-protected*/ class CacheKey extends BaseCacheKey {
	public final int maxWidth;
	
	public CacheKey(String text, IFont font, int maxWidth) {
		super(text, font);
		this.maxWidth = maxWidth;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), maxWidth);
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) && EqualsUtil.equalsOwn(CacheKey.class, obj, other -> 
		  this.maxWidth == other.maxWidth);
	}
}

/*package-protected*/ class ComputedValues implements IComputedValues {
	public final List<IText> lines;
	public final int width;
	public final int height;
	public final int lineHeight;
	
	public ComputedValues(List<IText> lines, int lineHeight) {
		this.lines = lines;
		this.width = LibUtil.maxInt(0, lines, (text) -> text.getWidth());
		this.height = lineHeight*lines.size();
		this.lineHeight = lineHeight;
	}
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
}
