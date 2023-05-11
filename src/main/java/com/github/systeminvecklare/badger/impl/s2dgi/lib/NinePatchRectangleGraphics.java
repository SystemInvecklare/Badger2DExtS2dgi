package com.github.systeminvecklare.badger.impl.s2dgi.lib;

import com.github.systeminvecklare.badger.core.widget.IRectangle;
import com.github.systeminvecklare.badger.core.widget.IRectangleInterface;
import com.github.systeminvecklare.badger.impl.s2dgi.graphics.INinePatchReference;
import com.github.systeminvecklare.badger.impl.s2dgi.lib.NinePatchGraphics;

public class NinePatchRectangleGraphics extends NinePatchGraphics {
	private final IRectangle rectangle;

	public NinePatchRectangleGraphics(INinePatchReference ninepatch, IRectangle rectangle) {
		super(ninepatch, 0, 0);
		this.rectangle = rectangle;
	}
	
	public <R> NinePatchRectangleGraphics(INinePatchReference ninepatch, R rectangle, IRectangleInterface<R> rectangleInterface) {
		this(ninepatch, new RectangleAdaptor<>(rectangleInterface, rectangle));
	}
	
	@Override
	public int getX() {
		return rectangle.getX();
	}
	
	@Override
	public int getY() {
		return rectangle.getY();
	}
	
	@Override
	public int getWidth() {
		return rectangle.getWidth();
	}
	
	@Override
	public int getHeight() {
		return rectangle.getHeight();
	}
	
	private static class RectangleAdaptor<R> implements IRectangle {
		private final IRectangleInterface<R> rectangleInterface;
		private final R rectangle;
		
		public RectangleAdaptor(IRectangleInterface<R> rectangleInterface, R rectangle) {
			this.rectangleInterface = rectangleInterface;
			this.rectangle = rectangle;
		}

		@Override
		public int getX() {
			return rectangleInterface.getX(rectangle);
		}

		@Override
		public int getY() {
			return rectangleInterface.getY(rectangle);
		}

		@Override
		public int getWidth() {
			return rectangleInterface.getWidth(rectangle);
		}

		@Override
		public int getHeight() {
			return rectangleInterface.getHeight(rectangle);
		}
	}
}
