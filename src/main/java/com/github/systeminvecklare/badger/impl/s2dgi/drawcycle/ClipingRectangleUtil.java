package com.github.systeminvecklare.badger.impl.s2dgi.drawcycle;

import net.pointlessgames.libs.s2dgi.window.IClippingRectangle;

/*package-protected*/ class ClipingRectangleUtil {
	public static void intersect(IClippingRectangle existing, int x, int y, int width, int height, MutableClippingRectangle result) {
		if(existing == null) {
			result.x = x;
			result.y = y;
			result.width = width;
			result.height = height;
		} else {
			result.x = Math.max(existing.getX(), x);
			result.y = Math.max(existing.getY(), y);
			result.width = Math.min(existing.getX() + existing.getWidth(), x + width) - result.x;
			result.height = Math.min(existing.getY() + existing.getHeight(), y + height) - result.y;
		}
	}
}

/*package-protected*/ class MutableClippingRectangle implements IClippingRectangle {
	public int x;
	public int y;
	public int width;
	public int height;
	
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
		return width;
	}
	@Override
	public int getHeight() {
		return height;
	}
}
