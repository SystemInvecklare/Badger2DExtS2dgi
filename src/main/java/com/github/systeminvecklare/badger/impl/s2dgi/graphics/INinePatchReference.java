package com.github.systeminvecklare.badger.impl.s2dgi.graphics;

public interface INinePatchReference {
	ITextureReference geTextureReference();
	int getPadLeft();
	int getPadRight();
	int getPadTop();
	int getPadBottom();
	INinePatchReference subNinePatch(int inset, int pad);
	INinePatchReference subNinePatch(int inset, int horizontalPad, int verticalPad);
	INinePatchReference subNinePatch(int inset, int padLeft, int padRight, int padTop, int padBottom);
}
