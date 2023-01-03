package com.github.systeminvecklare.badger.impl.s2dgi.drawcycle;

import java.util.Stack;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.shader.IShader;
import com.github.systeminvecklare.badger.core.graphics.components.transform.IReadableTransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.Transform;
import com.github.systeminvecklare.badger.core.math.IReadableVector;
import com.github.systeminvecklare.badger.core.math.Mathf;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.math.Vector;
import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.impl.s2dgi.graphics.IColor;
import com.github.systeminvecklare.badger.impl.s2dgi.graphics.IMutableColor;

import net.pointlessgames.libs.s2dgi.texture.ITexture;
import net.pointlessgames.libs.s2dgi.util.ImplementationUtil;
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
	
	public void clipRectangle(int x, int y, int width, int height) {
		AwkwardTransform awkwardTransform = new AwkwardTransform(appliedTransform, x, y, width, height, 0, 0);
		IClippingRectangle currentClip = window.getGraphics().getClip();
		ClipingRectangleUtil.intersect(currentClip, awkwardTransform. x, awkwardTransform.y, awkwardTransform.width, awkwardTransform.height, clipRectangleUtil);
		window.getGraphics().setClip(clipRectangleUtil);
	}
	
	public void replaceClipRectangle(int x, int y, int width, int height) {
		AwkwardTransform awkwardTransform = new AwkwardTransform(appliedTransform, x, y, width, height, 0, 0);
		window.getGraphics().setClip(awkwardTransform. x, awkwardTransform.y, awkwardTransform.width, awkwardTransform.height);
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
		AwkwardTransform awkwardTransform = new AwkwardTransform(appliedTransform, x, y, width, height, centerX, centerY);
		window.getGraphics().render(texture, awkwardTransform.x, awkwardTransform.y, awkwardTransform.width,
				awkwardTransform.height, awkwardTransform.quarterRotations - quarterRotations, flipX, flipY, false);
	}
	
	public void renderTiled(ITexture texture, int offsetX, int offsetY, int x, int y, int width, int height) {
		renderTiled(texture, offsetX, offsetY, x, y, width, height, 0, 0);
	}

	public void renderTiled(ITexture texture, int offsetX, int offsetY, int x, int y, int width, int height, int centerX, int centerY) {
		if(width == 0 || height == 0) {
			return;
		}
		AwkwardTransform awkwardTransform = new AwkwardTransform(appliedTransform, x, y, width, height, centerX, centerY);
		window.getGraphics().renderTiled(texture, offsetX, -offsetY, awkwardTransform.x, awkwardTransform.y,
				awkwardTransform.width, awkwardTransform.height);
	}
	
	public void renderRectangle(int x, int y, int width, int height, IColor color) {
		renderRectangle(x, y, width, height, 0, 0, color);
	}

	public void renderRectangle(int x, int y, int width, int height, int centerX, int centerY, IColor color) {
		AwkwardTransform awkwardTransform = new AwkwardTransform(appliedTransform, x, y, width, height, centerX, centerY);
		window.getGraphics().renderRectangle(awkwardTransform.x, awkwardTransform.y, awkwardTransform.width,
				awkwardTransform.height, color, IColor.INTERPRETER);
	}

	public void renderLine(int x1, int y1, int x2, int y2, IColor color) {
		AwkwardTransform awkwardTransform1 = new AwkwardTransform(appliedTransform, x1, y1, 0, 0, x1, y1);
		AwkwardTransform awkwardTransform2 = new AwkwardTransform(appliedTransform, x2, y2, 0,0, x2, y2);
		window.getGraphics().renderLine(awkwardTransform1.x, awkwardTransform1.y, awkwardTransform2.x,
				awkwardTransform2.y, color, IColor.INTERPRETER);
	}

	private class AwkwardTransform {
		public final int x;
		public final int y;
		public final int width;
		public final int height;
		public final int quarterRotations;

		public AwkwardTransform(IReadableTransform transform, int x, int y, int width, int height, int centerX, int centerY) {
			EasyPooler ep = EasyPooler.obtainFresh();
			try {
				int quarterRotations = (int) (2 * transform.getRotation().getTheta() / Mathf.PI + 0.5f);
				
				Vector corner1 = ep.obtain(Vector.class).setTo(x, y).add(-centerX, -centerY);
				Vector corner2 = ep.obtain(Vector.class).setTo(x+width, y+height).add(-centerX, -centerY);
				reducedTransform(quarterRotations, transform.getScale(), corner1);
				reducedTransform(quarterRotations, transform.getScale(), corner2);
				
				float minX = Math.min(corner1.getX(), corner2.getX());
				float maxX = Math.max(corner1.getX(), corner2.getX());
				float minY = Math.min(corner1.getY(), corner2.getY());
				float maxY = Math.max(corner1.getY(), corner2.getY());

				Position position = ep.obtain(Position.class).setTo(centerX, centerY);
				transform.transform(position);
				
				x = Math.round(position.getX()+minX);
				y = Math.round(position.getY()+minY);
				width = (int) (maxX-minX+0.5f);
				height = (int) (maxY-minY+0.5f);
				
				this.width = width;
				this.height = height;
				this.x = x;
				this.y = window.getHeight() - y - this.height;
				this.quarterRotations = -quarterRotations;
			} finally {
				ep.freeAllAndSelf();
			}
		}

		private void reducedTransform(int quarterRot, IReadableVector scale, Vector pos) {
			pos.hadamardMult(scale);
			int qcos = ImplementationUtil.qCos(quarterRot);
			int qsin = ImplementationUtil.qSin(quarterRot);
			float npx = qcos*pos.getX()-qsin*pos.getY();
			float npy = qsin*pos.getX()+qcos*pos.getY();
			pos.setTo(npx, npy);
		}
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
