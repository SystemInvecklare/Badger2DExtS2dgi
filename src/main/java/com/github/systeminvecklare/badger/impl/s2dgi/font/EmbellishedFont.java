package com.github.systeminvecklare.badger.impl.s2dgi.font;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.github.systeminvecklare.badger.core.widget.GridWidget;
import com.github.systeminvecklare.badger.core.widget.IWidget;
import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;
import com.github.systeminvecklare.badger.impl.s2dgi.font.embellished.ITextSegment;
import com.github.systeminvecklare.badger.impl.s2dgi.font.layout.ITextAtom;

public class EmbellishedFont implements IFont {
	private final Function<CharSequence, Iterable<ITextAtom>> atomizer;
	private final Function<CharSequence, List<ITextSegment>> segmenter;

	public EmbellishedFont(IFont baseFont, Function<CharSequence, List<ITextSegment>> segmenter) {
		this(baseFont, text -> baseFont.computeAtoms(text), segmenter);
	}
	
	public EmbellishedFont(IFont baseFont, Function<CharSequence, Iterable<ITextAtom>> atomizer, Function<CharSequence, List<ITextSegment>> segmenter) {
		this.atomizer = atomizer;
		this.segmenter = segmenter;
	}

	@Override
	public IText complile(CharSequence text) {
		return new SegmentText(this, text, segmenter.apply(text));
	}
	
	@Override
	public Iterable<ITextAtom> computeAtoms(CharSequence text) {
		return atomizer.apply(text);
	}
	
	private static class SegmentText implements IText {
		private final IFont font;
		private final CharSequence source;
		private final int width;
		private final int height;
		private final List<OffsetSegment> offsetSegments = new ArrayList<>();
		
		public SegmentText(IFont font, CharSequence source, List<ITextSegment> segments) {
			this.font = font;
			this.source = source;
			int width = 0;
			int height = 0;
			GridWidget grid = new GridWidget();
			int col = 0;
			for(ITextSegment textSegment : segments) {
				width += textSegment.getWidth();
				height = Math.max(textSegment.getHeight(), height);
				offsetSegments.add(grid.addChild(new OffsetSegment(textSegment), 0, col++));
			}
			grid.pack();
			this.width = width;
			this.height = height;
		}

		@Override
		public void render(S2dgiDrawCycle drawCycle, int x, int y) {
			for(OffsetSegment offsetSegment : offsetSegments) {
				offsetSegment.textSegment.render(drawCycle, x + offsetSegment.x, y + offsetSegment.y);
			}
		}

		@Override
		public IFont getFont() {
			return font;
		}

		@Override
		public String getString() {
			return source.toString();
		}

		@Override
		public int getXOffset() {
			return 0;
		}

		@Override
		public int getYOffset() {
			return 0;
		}

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public int getHeight() {
			return height;
		}
		
		private static class OffsetSegment implements IWidget {
			private int x;
			private int y;
			private final ITextSegment textSegment;
			
			public OffsetSegment(ITextSegment textSegment) {
				this.textSegment = textSegment;
			}
			
			@Override
			public int getX() {
				return x;
			}
			
			@Override
			public int getY() {
				return y;
			}
			
			@Override
			public int getWidth() {
				return textSegment.getWidth();
			}
			
			@Override
			public int getHeight() {
				return textSegment.getHeight();
			}
			
			@Override
			public void setPosition(int x, int y) {
				this.x = x;
				this.y = y;
			}
			
			@Override
			public void addToPosition(int dx, int dy) {
				this.x += dx;
				this.y += dy;
			}
		}
	}
}
