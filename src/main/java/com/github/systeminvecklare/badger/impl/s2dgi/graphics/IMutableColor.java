package com.github.systeminvecklare.badger.impl.s2dgi.graphics;

public interface IMutableColor extends IColor {
	IMutableColor setTo(IColor other);
	IMutableColor setTo(int r, int g, int b, int a);
}
