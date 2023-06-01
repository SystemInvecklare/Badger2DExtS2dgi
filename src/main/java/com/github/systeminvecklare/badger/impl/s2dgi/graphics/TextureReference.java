package com.github.systeminvecklare.badger.impl.s2dgi.graphics;

import java.io.IOException;

import com.github.systeminvecklare.badger.impl.s2dgi.FlashyS2dgiEngine;

import net.pointlessgames.libs.s2dgi.core.ISimple2DGraphics;
import net.pointlessgames.libs.s2dgi.texture.ITexture;

public class TextureReference implements ITextureReference {
	private final String path;
	
	private TextureReference(String path) {
		this.path = path;
	}
	
	@Override
	public int hashCode() {
		return path.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof TextureReference) {
			return this.path.equals(((TextureReference) obj).path);
		}
		return false;
	}
	
	@Override
	public ITexture load(ISimple2DGraphics graphics) throws IOException {
		try {
			return graphics.loadTexture(path);
		} catch(IOException e) {
			throw new IOException(path, e);
		}
	}
	
	@Override
	public ITextureReference subTexture(int sourceX, int sourceY, int width, int height) {
		return new SubTextureReference(this, sourceX, sourceY, width, height);
	}
	
	@Override
	public int getWidth() {
		return FlashyS2dgiEngine.get().getTexture(this).getWidth();
	}
	
	@Override
	public int getHeight() {
		return FlashyS2dgiEngine.get().getTexture(this).getHeight();
	}
	
	@Override
	public String serialize() {
		return path;
	}
	
	public static ITextureReference create(String path) {
		return new TextureReference(path);
	}
	
	public static ITextureReference deserialize(String serialized) {
		//TODO Make much better! Use Base64 or some shit
		if(serialized.startsWith("{")) {
			String parentSerialization = serialized.substring(1, serialized.lastIndexOf("}"));
			ITextureReference parent = deserialize(parentSerialization);
			String[] parts = serialized.substring(serialized.lastIndexOf("}")+1).split("\\,");
			return new SubTextureReference(parent, Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
		} else {
			return create(serialized);
		}
	}
	
	private static class SubTextureReference implements ITextureReference {
		private final ITextureReference parent;
		private final int sourceX;
		private final int sourceY;
		private final int width;
		private final int height;

		public SubTextureReference(ITextureReference parent, int sourceX, int sourceY, int width, int height) {
			this.parent = parent;
			this.sourceX = sourceX;
			this.sourceY = sourceY;
			this.width = width;
			this.height = height;
		}

		@Override
		public ITexture load(ISimple2DGraphics graphics) throws IOException {
			return FlashyS2dgiEngine.get().getTexture(parent).createSubTexture(sourceX, sourceY, width, height);
		}
		
		@Override
		public ITextureReference subTexture(int sourceX, int sourceY, int width, int height) {
			return new SubTextureReference(this, sourceX, sourceY, width, height);
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
		public String serialize() {
			//TODO pack tighter!
			return "{"+parent.serialize()+"}"+sourceX+","+sourceY+","+width+","+height;
		}
	}
}
