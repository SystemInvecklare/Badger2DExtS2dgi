package com.github.systeminvecklare.badger.impl.s2dgi.graphics;

import java.io.IOException;

import com.github.systeminvecklare.badger.impl.s2dgi.FlashyS2dgiEngine;

import net.pointlessgames.libs.s2dgi.core.ISimple2DGraphics;
import net.pointlessgames.libs.s2dgi.texture.ITexture;

public interface ITextureReference {
	/**
	 * Don't call this directly! Use {@link FlashyS2dgiEngine#getTexture(ITextureReference)}.
	 */
	ITexture load(ISimple2DGraphics graphics) throws IOException;
	
	int getWidth();
	int getHeight();
	
	ITextureReference subTexture(int sourceX, int sourceY, int width, int height);
	
	String serialize();
}
