package com.github.systeminvecklare.badger.impl.s2dgi.font;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.systeminvecklare.badger.impl.s2dgi.graphics.ITextureReference;
import com.github.systeminvecklare.badger.impl.s2dgi.graphics.TextureReference;

/**
 * This is a shit implementation, yes, but here's the thing: I was lazy. And you know what? It works. Kind of... So if you need it to work better, then YOU fix it! *slams door*
 * 
 * @author Mattias Selin
 */
public class BMFontLoader {
	public static IFont load(File fntFile) throws IOException { 
		FntData fntData = new FntData();
		FileInputStream fileInputStream = new FileInputStream(fntFile);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
			String line = null;
			while((line = reader.readLine()) != null) {
				fntData.parseLine(line);
			}
			reader.close();
		} finally {
			fileInputStream.close();
		}
		
		//TODO validate fntData
		if (fntData.commonData == null
		 || fntData.commonData.lineHeight == null) {
			throw new IllegalStateException("Invalid font file");
		}
		
		Map<Integer, ITextureReference> pageMap = new HashMap<Integer, ITextureReference>();
		for(FntData.PageData pageData : fntData.pageDatas) {
			if(pageData.id != null && pageData.file != null) {
				pageMap.put(pageData.id, TextureReference.create(pageData.file));
			}
		}
		int currentlyLoadedPage = -1;
		
		FontBuilder fontBuilder = new FontBuilder(fntData.commonData.lineHeight);
		for(FntData.CharData charData : fntData.charDatas) {
			int page = charData.page.intValue();
			if(currentlyLoadedPage != page) {
				fontBuilder.setSourceTexture(pageMap.get(page));
				currentlyLoadedPage = page;
			}
			fontBuilder.addGlyph(charData.x, charData.y, charData.width, charData.height, charData.xadvance, charData.xoffset, fntData.commonData.base-charData.height-charData.yoffset, (char) charData.id.intValue());
		}
		
		return fontBuilder.build();
	}
	
	private static class FntData {
		private CommonData commonData = null;
		private final List<PageData> pageDatas = new ArrayList<PageData>();
		private final List<CharData> charDatas = new ArrayList<CharData>();
		private final List<KerningData> kerningDatas = new ArrayList<KerningData>();

		public void parseLine(String line) {
			if(line.startsWith("common ")) {
				commonData = new CommonData(line);
			} else if(line.startsWith("page ")) {
				pageDatas.add(new PageData(line));
			} else if(line.startsWith("char ")) {
				charDatas.add(new CharData(line));
			} else if(line.startsWith("kerning ")) {
				kerningDatas.add(new KerningData(line));
			}
		}
		
		
		private static class CommonData {
			private Integer lineHeight;
			private Integer base;
			private Integer scaleW;
			private Integer scaleH;
			private Integer pages;
			private Integer packed;
			
			public CommonData(String line) {
				String[] parts = line.split(" ");
				for(int i = 1; i < parts.length; ++i) {
					String[] propParts = parts[i].trim().split("=");
					if("lineHeight".equals(propParts[0])) {
						lineHeight = Integer.parseInt(propParts[1]);
					} else if("base".equals(propParts[0])) {
						base = Integer.parseInt(propParts[1]);
					} else if("scaleW".equals(propParts[0])) {
						scaleW = Integer.parseInt(propParts[1]);
					} else if("scaleH".equals(propParts[0])) {
						scaleH = Integer.parseInt(propParts[1]);
					} else if("pages".equals(propParts[0])) {
						pages = Integer.parseInt(propParts[1]);
					} else if("packed".equals(propParts[0])) {
						packed = Integer.parseInt(propParts[1]);
					}
				}
			}
		}
		
		private static class PageData {
			private Integer id;
			private String file;

			public PageData(String line) {
				String[] parts = line.split(" ");
				for(int i = 1; i < parts.length; ++i) {
					String[] propParts = parts[i].trim().split("=");
					if("id".equals(propParts[0])) {
						id = Integer.parseInt(propParts[1]);
					} else if("file".equals(propParts[0])) {
						file = propParts[1].substring(1, propParts[1].length()-1);
					}
				}
			}
		}
		
		private static class CharData {
			private Integer id;
			private Integer x;
			private Integer y;
			private Integer width;
			private Integer height;
			private Integer xoffset;
			private Integer yoffset;
			private Integer xadvance;
			private Integer page;
			private Integer chnl;
			
			public CharData(String line) {
				String[] parts = line.split(" ");
				for(int i = 1; i < parts.length; ++i) {
					String[] propParts = parts[i].trim().split("=");
					if("id".equals(propParts[0])) {
						id = Integer.parseInt(propParts[1]);
					} else if("x".equals(propParts[0])) {
						x = Integer.parseInt(propParts[1]);
					} else if("y".equals(propParts[0])) {
						y = Integer.parseInt(propParts[1]);
					} else if("width".equals(propParts[0])) {
						width = Integer.parseInt(propParts[1]);
					} else if("height".equals(propParts[0])) {
						height = Integer.parseInt(propParts[1]);
					} else if("xoffset".equals(propParts[0])) {
						xoffset = Integer.parseInt(propParts[1]);
					} else if("yoffset".equals(propParts[0])) {
						yoffset = Integer.parseInt(propParts[1]);
					} else if("xadvance".equals(propParts[0])) {
						xadvance = Integer.parseInt(propParts[1]);
					} else if("page".equals(propParts[0])) {
						page = Integer.parseInt(propParts[1]);
					} else if("chnl".equals(propParts[0])) {
						chnl = Integer.parseInt(propParts[1]);
					}
				}
			}
		}
		
		private static class KerningData {
			private Integer first;
			private Integer second;
			private Integer amount;
			
			public KerningData(String line) {
				String[] parts = line.split(" ");
				for(int i = 1; i < parts.length; ++i) {
					String[] propParts = parts[i].trim().split("=");
					if("first".equals(propParts[0])) {
						first = Integer.parseInt(propParts[1]);
					} else if("second".equals(propParts[0])) {
						second = Integer.parseInt(propParts[1]);
					} else if("amount".equals(propParts[0])) {
						amount = Integer.parseInt(propParts[1]);
					}
				}
			}
		}
	}
}
