package com.github.systeminvecklare.badger.impl.s2dgi.graphics;

public class NinePatchReference implements INinePatchReference {
	private final ITextureReference textureReference;
	private final int padLeft;
	private final int padRight;
	private final int padTop;
	private final int padBottom;
	
	private NinePatchReference(ITextureReference textureReference, int padLeft, int padRight, int padTop, int padBottom) {
		this.textureReference = textureReference;
		this.padLeft = padLeft;
		this.padRight = padRight;
		this.padTop = padTop;
		this.padBottom = padBottom;
	}

	@Override
	public ITextureReference geTextureReference() {
		return textureReference;
	}

	@Override
	public int getPadLeft() {
		return padLeft;
	}

	@Override
	public int getPadRight() {
		return padRight;
	}

	@Override
	public int getPadTop() {
		return padTop;
	}

	@Override
	public int getPadBottom() {
		return padBottom;
	}
	
	@Override
	public INinePatchReference subNinePatch(int inset, int pad) {
		return subNinePatch(inset, pad, pad);
	}
	
	@Override
	public INinePatchReference subNinePatch(int inset, int horizontalPad, int verticalPad) {
		return subNinePatch(inset, horizontalPad, horizontalPad, verticalPad, verticalPad);
	}
	
	@Override
	public INinePatchReference subNinePatch(int inset, int padLeft, int padRight, int padTop, int padBottom) {
		ITextureReference insetTexture = textureReference.subTexture(inset, inset, textureReference.getWidth()-inset*2, textureReference.getHeight()-inset*2);
		return create(insetTexture, padLeft, padRight, padTop, padBottom);
	}
	
	public static INinePatchReference create(ITextureReference textureReference, int pad) {
		return create(textureReference, pad, pad);
	}
	
	public static INinePatchReference create(ITextureReference textureReference, int horizontalPad, int verticalPad) {
		return create(textureReference, horizontalPad, horizontalPad, verticalPad, verticalPad);
	}
	
	public static INinePatchReference create(ITextureReference textureReference, int padLeft, int padRight, int padTop, int padBottom) {
		return new NinePatchReference(textureReference, padLeft, padRight, padTop, padBottom);
	}
	
	public static INinePatchReference create(String texturePath, int pad) {
		return create(TextureReference.create(texturePath), pad);
	}
	
	public static INinePatchReference create(String texturePath, int horizontalPad, int verticalPad) {
		return create(TextureReference.create(texturePath), horizontalPad, verticalPad);
	}
	
	public static INinePatchReference create(String texturePath, int padLeft, int padRight, int padTop, int padBottom) {
		return create(TextureReference.create(texturePath), padLeft, padRight, padTop, padBottom);
	}
}
