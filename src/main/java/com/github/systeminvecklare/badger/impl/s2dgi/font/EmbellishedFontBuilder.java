package com.github.systeminvecklare.badger.impl.s2dgi.font;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.systeminvecklare.badger.impl.s2dgi.font.embellished.ITextSegment;
import com.github.systeminvecklare.badger.impl.s2dgi.font.embellished.SimpleTextSegment;
import com.github.systeminvecklare.badger.impl.s2dgi.font.layout.CharAtom;
import com.github.systeminvecklare.badger.impl.s2dgi.font.layout.ITextAtom;
import com.github.systeminvecklare.badger.impl.s2dgi.font.layout.StringAtom;

public class EmbellishedFontBuilder {
	private final IFont baseFont;
	private final List<SegmentPattern> patterns = new ArrayList<>();

	public EmbellishedFontBuilder(IFont baseFont) {
		this.baseFont = baseFont;
	}
	
	public EmbellishedFontBuilder addPattern(Pattern pattern, BiFunction<IFont, MatchResult, ITextSegment> segmentCreator) {
		patterns.add(new SegmentPattern(pattern, segmentCreator));
		return this;
	}
	
	public IFont build() {
		final FunctionPointer<CharSequence, Iterable<ITextAtom>> atomizer = new FunctionPointer<>();
		final FunctionPointer<CharSequence, List<ITextSegment>> segmenter = new FunctionPointer<>();
		final IFont embellishedFont = new EmbellishedFont(baseFont, atomizer, segmenter);
		
		List<SegmentPattern> segmentPatterns = new ArrayList<>(this.patterns);
		
		atomizer.assign(characters -> {
			return new PartAtomIterator(createParts(embellishedFont, characters, segmentPatterns));
		});
		segmenter.assign(text -> {
			List<IPart> texts = createParts(embellishedFont, text, segmentPatterns);
			
			List<ITextSegment> result = new ArrayList<>();
			for(IPart part : texts) {
				result.add(part.toTextSegment(baseFont));
			}
			return result;
		});
		
		return embellishedFont;
	}
	

	private static void applyPattern(List<IPart> parts, SegmentPattern segmentPattern, List<IPart> util_newParts) {
		for(int i = 0; i < parts.size(); ++i) {
			IPart part = parts.remove(i);
			util_newParts.clear();
			part.apply(segmentPattern, util_newParts);
			for(IPart newPart : util_newParts) {
				parts.add(i++, newPart);
			}
			i--;
		}
	}

	private static List<IPart> createParts(IFont font, CharSequence text, List<SegmentPattern> patterns) {
		List<IPart> parts = new ArrayList<>();
		List<IPart> util_newParts = new ArrayList<>();
		parts.add(new RawTextPart(font, text));
		for(SegmentPattern pattern : patterns) {
			applyPattern(parts, pattern, util_newParts);
		}
		return parts;
	}
	
	private static class SegmentPattern {
		private final Pattern pattern;
		private final BiFunction<IFont, MatchResult, ITextSegment> segmentCreator;
		
		public SegmentPattern(Pattern pattern, BiFunction<IFont, MatchResult, ITextSegment> segmentCreator) {
			this.pattern = pattern;
			this.segmentCreator = segmentCreator;
		}
	}
	
	private interface IPart {
		void apply(SegmentPattern segmentPattern, List<IPart> newParts);
		ITextSegment toTextSegment(IFont baseFont);
		Iterator<ITextAtom> textAtoms();
	}
	
	private static class RawTextPart implements IPart {
		private final IFont font;
		private final CharSequence text;

		public RawTextPart(IFont font, CharSequence text) {
			this.font = font;
			this.text = text;
		}

		@Override
		public void apply(SegmentPattern segmentPattern, List<IPart> newParts) {
			Matcher matcher = segmentPattern.pattern.matcher(text);
			int start = 0;
			while(matcher.find()) {
				int end = matcher.start();
				if(end - start > 0) {
					newParts.add(new RawTextPart(font, text.subSequence(start, end)));
				}
				newParts.add(new ProcessedPart(matcher.group(), segmentPattern.segmentCreator.apply(font, matcher.toMatchResult())));
				start = matcher.end();
			}
			int end = text.length();
			if(end  - start > 0) {
				newParts.add(new RawTextPart(font, text.subSequence(start, end)));
			}
		}
		
		@Override
		public ITextSegment toTextSegment(IFont baseFont) {
			return new SimpleTextSegment(baseFont.complile(text));
		}
		
		@Override
		public Iterator<ITextAtom> textAtoms() {
			return new Iterator<ITextAtom>() {
				private int index = 0;
				
				@Override
				public ITextAtom next() {
					return new CharAtom(text.charAt(index++));
				}
				
				@Override
				public boolean hasNext() {
					return index < text.length();
				}
			};
		}
	}
	
	private static class ProcessedPart implements IPart {
		private final String source;
		private final ITextSegment textSegment;

		public ProcessedPart(String source, ITextSegment textSegment) {
			this.source = source;
			this.textSegment = textSegment;
		}

		@Override
		public void apply(SegmentPattern segmentPattern, List<IPart> newParts) {
			newParts.add(this);
		}
		
		@Override
		public ITextSegment toTextSegment(IFont baseFont) {
			return textSegment;
		}
		
		@Override
		public Iterator<ITextAtom> textAtoms() {
			return singleIteration(new StringAtom(source));
		}
	}
	
	private static class PartAtomIterator implements Iterable<ITextAtom> {
		private final Iterable<IPart> parts;

		public PartAtomIterator(Iterable<IPart> parts) {
			this.parts = parts;
		}

		@Override
		public Iterator<ITextAtom> iterator() {
			return new Iterator<ITextAtom>() {
				private final Iterator<IPart> partIterator = parts.iterator();
				private Iterator<ITextAtom> currentIterator = null;
				
				@Override
				public ITextAtom next() {
					if(currentIterator == null || !currentIterator.hasNext()) {
						currentIterator = partIterator.next().textAtoms();
					}
					return currentIterator.next();
				}
				
				@Override
				public boolean hasNext() {
					if(currentIterator != null && currentIterator.hasNext()) {
						return true;
					}
					if(!partIterator.hasNext()) {
						return false;
					}
					currentIterator = partIterator.next().textAtoms();
					return hasNext();
				}
			};
		}
		
	}
	
	private static <T> Iterator<T> singleIteration(T value) {
		return new Iterator<T>() {
			private boolean used = false;
			
			@Override
			public boolean hasNext() {
				return !used;
			}

			@Override
			public T next() {
				if(used) {
					return null;
				}
				used = true;
				return value;
			}
		};
	}
	
	private static class FunctionPointer<T, R> implements Function<T, R> {
		private Function<T, R> impl;
		private boolean assigned = false;
		
		@Override
		public R apply(T arg0) {
			if(!assigned) {
				throw new NullPointerException("FunctionPointer was called before assigned");
			}
			return impl.apply(arg0);
		}
		
		public void assign(Function<T, R> impl) {
			this.impl = impl;
			this.assigned = true;
		}
	}
}
