package com.github.systeminvecklare.badger.impl.s2dgi.font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.systeminvecklare.badger.impl.s2dgi.FlashyS2dgiEngine;
import com.github.systeminvecklare.badger.impl.s2dgi.drawcycle.S2dgiDrawCycle;
import com.github.systeminvecklare.badger.impl.s2dgi.graphics.ITextureReference;

import net.pointlessgames.libs.s2dgi.texture.ITexture;

public class FontBuilder {
	private ITextureReference sourceTexture = null;
	private final Map<Character, IGlyphTemplate> glyphs = new HashMap<Character, IGlyphTemplate>();
	private final int lineHeight;
	private int defaultSpacing = 0; 
	
	public FontBuilder(int lineHeight) {
		this.lineHeight = lineHeight;
	}

	public void setSourceTexture(ITextureReference reference) {
		this.sourceTexture = reference;
	}
	
	public void setDefaultSpacing(int defaultSpacing) {
		this.defaultSpacing = defaultSpacing;
	}
	
	public void addGlyph(int x, int y, int width, int height, char ... chars) {
		addGlyph(x, y, width, height, 0, 0, chars);
	}
	
	public void addGlyph(int x, int y, int width, int height, int glyphOffsetX, int glyphOffsetY, char ... chars) {
		addGlyph(x, y, width, height, width, glyphOffsetX, glyphOffsetY, chars);
	}
	
	public void addGlyph(int x, int y, int width, int height, int advance, int glyphOffsetX, int glyphOffsetY, char ... chars) {
		IGlyphTemplate glyph = new GlyphTexture(sourceTexture.subTexture(x, y, width, height), advance, glyphOffsetX, glyphOffsetY);
		for(char c : chars) {
			glyphs.put(c, glyph);
		}
	}
	
	public void addSpaceGlyph(int width, int height, char ... chars) {
		IGlyphTemplate glyph = new SpaceGlyph(width, height);
		for(char c : chars) {
			glyphs.put(c, glyph);
		}
	}
	
	public IFont build() {
		return new Font(glyphs, defaultSpacing, lineHeight);
	}
	
	private static class Font implements IFont {
		private final Map<Character, IGlyphTemplate> glyphs = new HashMap<Character, IGlyphTemplate>();
		private final int defaultSpacing;
		private final int lineHeight; 

		public Font(Map<Character, IGlyphTemplate> glyphs, int defaultSpacing, int lineHeight) {
			this.glyphs.putAll(glyphs);
			this.defaultSpacing = defaultSpacing;
			this.lineHeight = lineHeight;
		}

		@Override
		public IText complile(CharSequence originalText) {
			String originalTextAsString = originalText.toString();
			String[] lines = originalTextAsString.split("\n");
			List<CompiledGlyph> compiledGlyphs = new ArrayList<FontBuilder.CompiledGlyph>();
			int width = 0;
			int height = 0;
			int offsetX = 0;
			int offsetY = 0;
			char lastC = Character.MIN_VALUE;
			for(int li = 0; li < lines.length; ++li) {
				String text = lines[lines.length-li-1];
				if(li > 0) {
					offsetX = 0;
					offsetY += lineHeight;
					lastC = Character.MIN_VALUE;
				}
				for(int i = 0; i < text.length(); ++i) {
					char c = text.charAt(i);
					IGlyphTemplate compilableGlyph = glyphs.get(c);
					if(compilableGlyph != null) {
						if(lastC != Character.MIN_VALUE) {
							offsetX += getSpacing(lastC, c);
						}
						compiledGlyphs.add(compilableGlyph.compile(offsetX, offsetY));
						offsetX += compilableGlyph.getWidth();
						lastC = c;
						width = Math.max(width, offsetX);
						height = Math.max(height, offsetY + compilableGlyph.getHeight());
					}
				}
			}
			return new Text(originalTextAsString, this, compiledGlyphs, width, height);
		}

		private int getSpacing(char lastC, char currentChar) {
			return defaultSpacing;
		}
	}
	
	private static class Text implements IText {
		private final String string;
		private final IFont font;
		private final List<CompiledGlyph> compiledGlyphs;
		private final int width;
		private final int height;
		
		public Text(String string, IFont font, List<CompiledGlyph> compiledGlyphs, int width, int height) {
			this.string = string;
			this.font = font;
			this.compiledGlyphs = compiledGlyphs;
			this.width = width;
			this.height = height;
		}

		@Override
		public void render(S2dgiDrawCycle drawCycle, int x, int y) {
			for(CompiledGlyph compiledGlyph : compiledGlyphs) {
				compiledGlyph.render(drawCycle, x, y);
			}
		}

		@Override
		public IFont getFont() {
			return font;
		}

		@Override
		public String getString() {
			return string;
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
	}
	
	private interface IGlyphTemplate {
		int getWidth();
		int getHeight();
		CompiledGlyph compile(int offsetX, int offsetY);
		void render(S2dgiDrawCycle drawCycle, int x, int y);
	}
	
	private static class GlyphTexture implements IGlyphTemplate {
		private final ITextureReference subTexture;
		private final int advance;
		private final int glyphOffsetX;
		private final int glyphOffsetY;
		
		public GlyphTexture(ITextureReference subTexture, int advance, int glyphOffsetX, int glyphOffsetY) {
			this.subTexture = subTexture;
			this.advance = advance;
			this.glyphOffsetX = glyphOffsetX;
			this.glyphOffsetY = glyphOffsetY;
		}

		@Override
		public int getWidth() {
			return advance;
		}
		
		@Override
		public int getHeight() {
			return subTexture.getHeight();
		}

		@Override
		public CompiledGlyph compile(int offsetX, int offsetY) {
			return new CompiledGlyph(this, offsetX+glyphOffsetX, offsetY+glyphOffsetY);
		}
		
		@Override
		public void render(S2dgiDrawCycle drawCycle, int x, int y) {
			ITexture texture = FlashyS2dgiEngine.get().getTexture(subTexture);
			drawCycle.render(texture, x, y);
		}
	}
	
	private static class SpaceGlyph implements IGlyphTemplate {
		private final int width;
		private final int height;

		public SpaceGlyph(int width, int height) {
			this.width = width;
			this.height = height;
		}

		@Override
		public int getWidth() {
			return width;
		}
		
		@Override
		public int getHeight() {
			return height;
		}

		@Override
		public CompiledGlyph compile(int offsetX, int offsetY) {
			//TODO I don't think we need this. Inline this shit!
			return new CompiledGlyph(this, offsetX, offsetY);
		}

		@Override
		public void render(S2dgiDrawCycle drawCycle, int x, int y) {
		}
	}
	
	private static class CompiledGlyph {
		private final IGlyphTemplate template;
		private final int xOffset;
		private final int yOffset;
		
		public CompiledGlyph(IGlyphTemplate template, int xOffset, int yOffset) {
			this.template = template;
			this.xOffset = xOffset;
			this.yOffset = yOffset;
		}

		public void render(S2dgiDrawCycle drawCycle, int x, int y) {
			template.render(drawCycle, x + xOffset, y + yOffset);
		}
	}
}
