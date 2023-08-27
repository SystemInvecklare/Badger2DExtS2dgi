package com.github.systeminvecklare.badger.impl.s2dgi.drawcycle;

import java.util.Stack;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.shader.IShader;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.Transform;
import com.github.systeminvecklare.badger.core.math.Mathf;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolManager;
import com.github.systeminvecklare.badger.impl.s2dgi.graphics.IColor;
import com.github.systeminvecklare.badger.impl.s2dgi.graphics.IMutableColor;

import net.pointlessgames.libs.s2dgi.texture.ITexture;
import net.pointlessgames.libs.s2dgi.window.IClippingRectangle;
import net.pointlessgames.libs.s2dgi.window.IWindow;

public class S2dgiDrawCycle implements IDrawCycle {
	private final Transform transform = new Transform(null);
	private final Transform appliedTransform = new Transform(null);
	private final IWindow window;
	private final ColorAdjustComponent tintManager = new ColorAdjustComponent(IColor.WHITE) {
		@Override
		protected void apply(IWindow window, IColor currentValue) {
			window.getGraphics().setTint(currentValue, IColor.INTERPRETER);
		}
	};
	private final ColorAdjustComponent additiveManager = new ColorAdjustComponent(IColor.ZERO) {
		@Override
		protected void apply(IWindow window, IColor currentValue) {
			window.getGraphics().setAdditive(currentValue, IColor.INTERPRETER);
		}
	};
	private final Stack<IClippingRectangle> clipStack = new Stack<>();
	
	private final MutableClippingRectangle clipRectangleUtil = new MutableClippingRectangle();

	public S2dgiDrawCycle(IWindow window) {
		this.window = window;
	}

	@Override
	public ITransform getTransform() {
		return transform;
	}

	@Override
	public void setShader(IShader shader) {
		// TODO Auto-generated method stub

	}

	public IDrawCycle reset() {
		window.renderBegin();
		transform.setToIdentity();
		tintManager.reset();
		additiveManager.reset();
		clipStack.clear();
		return this;
	}

	public void end() {
		window.renderEnd();
	}

	public void applyTransforms() {
		appliedTransform.setTo(transform);
	}
	
	public void setTint(IColor color) {
		tintManager.set(window, color);
	}

	public void setAdditive(IColor color) {
		additiveManager.set(window, color);
	}
	
	public void setColorAdjust(IColor tintColor, IColor additiveColor) {
		setTint(tintColor);
		setAdditive(additiveColor);
	}
	
	public IColor getTint() {
		return tintManager.get();
	}
	
	public IColor getAdditive() {
		return additiveManager.get();
	}
	
	public IMutableColor getTint(IMutableColor result) {
		return tintManager.get(result);
	}
	
	public IMutableColor getAdditive(IMutableColor result) {
		return additiveManager.get(result);
	}
	
	/**
	 * Gets both tint and additive.
	 */
	public void getColorAdjust(IMutableColor tintResult, IMutableColor additiveResult) {
		getTint(tintResult);
		getAdditive(additiveResult);
	}
	
	public void pushTint(IColor color) {
		tintManager.push(window, color);
	}
	
	public void pushAdditive(IColor color) {
		additiveManager.push(window, color);
	}
	
	public void pushColorAdjust(IColor tintColor, IColor additiveColor) {
		pushTint(tintColor);
		pushAdditive(additiveColor);
	}
	
	public void popTint() {
		tintManager.pop(window);
	}
	
	public void popAdditive() {
		additiveManager.pop(window);
	}
	
	public void popColorAdjust() {
		popTint();
		popAdditive();
	}
	
	private void pushDownCurrentClip() {
		clipStack.add(window.getGraphics().getClip());
	}
	
	public void pushClipRectangle(int x, int y, int width, int height) {
		pushDownCurrentClip();
		clipRectangle(x, y, width, height);
	}
	
	public void pushReplaceClipRectangle(int x, int y, int width, int height) {
		pushDownCurrentClip();
		replaceClipRectangle(x, y, width, height);
	}
	
	public void pushReplaceClipRectangleWithInfinite() {
		pushDownCurrentClip();
		replaceClipRectangleWithInfinite();
	}
	
	private OrientableRectangle getTransformedRectangle(OrientableRectangle result, int centerX, int centerY) {
		// 1. get the center, apply full transform
		// 2. apply the core (scale + rot) of the transform to the rectangle, relative to the center
		// 3. Use the transformed center to adjust the transformed rectangle.
		
		Position center = FlashyEngine.get().getPoolManager().getPool(Position.class).obtain().setTo(centerX, centerY);
		result.add(-centerX, -centerY);
		
		appliedTransform.transform(center);
		
		int quarterRotations = Mathf.mod((int) (2 * appliedTransform.getRotation().getTheta() / Mathf.PI + 0.5f), 4);
		IntegerTransform integerTransform = FlashyEngine.get().getPoolManager().getPool(IntegerTransform.class).obtain().setToIdentity().setQuarterRotations(quarterRotations);
		
		float sx = appliedTransform.getScale().getX();
		float sy = appliedTransform.getScale().getY();
		
		int scaledX = Math.round(sx*result.getX());
		int scaledY = Math.round(sy*result.getY());
		int scaledWidth = Math.round(sx*result.getWidth());
		int scaledHeight = Math.round(sy*result.getHeight());
		boolean scaledFlipX = sx > 0 ? result.getFlipX() : !result.getFlipX();
		boolean scaledFlipY = sy > 0 ? result.getFlipY() : !result.getFlipY();
		
		result.setTo(scaledX, scaledY, scaledWidth, scaledHeight, result.getQuarterRotations(), scaledFlipX, scaledFlipY);
		
		integerTransform.transform(result);
		
		result.add(Math.round(center.getX()), Math.round(center.getY()));
		
		{//Flip screenY
			integerTransform.setToIdentity().setScale(1, -1).addToPosition(0, window.getHeight()).transform(result);
			result.setFlipY(!result.getFlipY());
		}
		
		integerTransform.free();
		center.free();
		
		return result;
	}
	
	private IntVector getTransformedIntVector(IntVector result, IPoolManager poolManager) {
		Position position = poolManager.getPool(Position.class).obtain().setTo(result);
		appliedTransform.transform(position);
		result.setTo(Math.round(position.getX()), Math.round(position.getY()));
		position.free();
		
		return result.scale(1, -1).add(0, window.getHeight());
	}
	
	public void clipRectangle(int x, int y, int width, int height) {
		OrientableRectangle orientableRectangle = getTransformedRectangle(FlashyEngine.get().getPoolManager().getPool(OrientableRectangle.class).obtain().setTo(x, y, width, height, 0, false, false), 0, 0);
		
		IClippingRectangle currentClip = window.getGraphics().getClip();
		ClipingRectangleUtil.intersect(currentClip, orientableRectangle.getX(), orientableRectangle.getY(), orientableRectangle.getWidth(), orientableRectangle.getHeight(), clipRectangleUtil);
		window.getGraphics().setClip(clipRectangleUtil);
		orientableRectangle.free();
	}
	
	public void replaceClipRectangle(int x, int y, int width, int height) {
		OrientableRectangle orientableRectangle = getTransformedRectangle(FlashyEngine.get().getPoolManager().getPool(OrientableRectangle.class).obtain().setTo(x, y, width, height, 0, false, false), 0, 0);
		window.getGraphics().setClip(orientableRectangle.getX(), orientableRectangle.getY(), orientableRectangle.getWidth(), orientableRectangle.getHeight());
		orientableRectangle.free();
	}
	
	public void replaceClipRectangleWithInfinite() {
		window.getGraphics().setClip(null);
	}
	
	public void popClipRectangle() {
		IClippingRectangle popped = clipStack.isEmpty() ? null : clipStack.pop();
		window.getGraphics().setClip(popped);
	}

	public void render(ITexture texture, int x, int y) {
		render(texture, x, y, texture.getWidth(), texture.getHeight(), 0, 0, 0, false, false);
	}
	
	public void render(ITexture texture, int x, int y, int width, int height) {
		render(texture, x, y, width, height, 0, false, false);
	}
	
	public void render(ITexture texture, int x, int y, int width, int height, int quarterRotations, boolean flipX, boolean flipY) {
		render(texture, x, y, width, height, 0, 0, quarterRotations, flipX, flipY);
	}

	public void render(ITexture texture, int x, int y, int width, int height, int centerX, int centerY, int quarterRotations, boolean flipX,
			boolean flipY) {
		OrientableRectangle rectangle = FlashyEngine.get().getPoolManager().getPool(OrientableRectangle.class).obtain().setTo(x, y, width, height, quarterRotations, flipX, flipY);
		getTransformedRectangle(rectangle, centerX, centerY);
		window.getGraphics().render(texture, rectangle.getX(), rectangle.getY(), rectangle.getWidth(),
				rectangle.getHeight(), rectangle.getQuarterRotations(), rectangle.getFlipX(), rectangle.getFlipY(), false);
		rectangle.free();
	}
	
	public void renderTiled(ITexture texture, int offsetX, int offsetY, int x, int y, int width, int height) {
		renderTiled(texture, offsetX, offsetY, x, y, width, height, 0, 0);
	}

	public void renderTiled(ITexture texture, int offsetX, int offsetY, int x, int y, int width, int height, int centerX, int centerY) {
		if(width == 0 || height == 0) {
			return;
		}
		OrientableRectangle rectangle = FlashyEngine.get().getPoolManager().getPool(OrientableRectangle.class).obtain().setTo(x, y, width, height, 0, false, false);
		getTransformedRectangle(rectangle, centerX, centerY);
		window.getGraphics().renderTiled(texture, offsetX, -offsetY, rectangle.getX(), rectangle.getY(),
				rectangle.getWidth(), rectangle.getHeight());
		rectangle.free();
	}
	
	public void renderRectangle(int x, int y, int width, int height, IColor color) {
		renderRectangle(x, y, width, height, 0, 0, color);
	}

	public void renderRectangle(int x, int y, int width, int height, int centerX, int centerY, IColor color) {
		OrientableRectangle rectangle = FlashyEngine.get().getPoolManager().getPool(OrientableRectangle.class).obtain().setTo(x, y, width, height, 0, false, false);
		getTransformedRectangle(rectangle, centerX, centerY);
		window.getGraphics().renderRectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(),
				rectangle.getHeight(), color, IColor.INTERPRETER);
		rectangle.free();
	}

	public void renderLine(int x1, int y1, int x2, int y2, IColor color) {
		IPoolManager poolManager = FlashyEngine.get().getPoolManager();
		IPool<IntVector> intVectorPool = poolManager.getPool(IntVector.class);
		IntVector p1 = getTransformedIntVector(intVectorPool.obtain().setTo(x1, y1), poolManager);
		IntVector p2 = getTransformedIntVector(intVectorPool.obtain().setTo(x2, y2), poolManager);
		window.getGraphics().renderLine(p1.getIntX(), p1.getIntY(), p2.getIntX(),
				p2.getIntY(), color, IColor.INTERPRETER);
		p1.free();
		p2.free();
	}

	
	private static abstract class ColorAdjustComponent {
		private final IColor zeroState;
		private final IMutableColor currentValue;
		private final Stack<IColor> stack = new Stack<>();

		public ColorAdjustComponent(IColor zeroState) {
			this.zeroState = zeroState;
			this.currentValue = IColor.createMutable(zeroState);
		}


		public void push(IWindow window, IColor color) {
			stack.add(currentValue.immutableCopy());
			set(window, color);
		}
		
		public void pop(IWindow window) {
			IColor popped = stack.isEmpty() ? null : stack.pop();
			set(window, popped);
		}

		public void set(IWindow window, IColor color) {
			color = color != null ? color : zeroState;
			if (!currentValue.equals(color)) {
				currentValue.setTo(color);
				apply(window, currentValue);
			}
		}
		
		public IColor get() {
			return currentValue.immutableCopy();
		}
		
		public IMutableColor get(IMutableColor result) {
			return result.setTo(currentValue);
		}

		public void reset() {
			currentValue.setTo(zeroState);
			stack.clear();
		}
		
		protected abstract void apply(IWindow window, IColor currentValue);
	}
}
