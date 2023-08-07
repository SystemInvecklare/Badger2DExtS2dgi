package com.github.systeminvecklare.badger.impl.s2dgi.drawcycle;

public interface IReadableIntegerTransform {
	IReadableIntVector getPosition();
	int getQuarterRotations();
	IReadableIntVector getScale();
	IntVector transform(IntVector argumentAndResult);
	OrientableRectangle transform(OrientableRectangle argumentAndResult);
	IntVector transformNormal(IntVector argumentAndResult);
	boolean invert(IntegerTransform result);
}
