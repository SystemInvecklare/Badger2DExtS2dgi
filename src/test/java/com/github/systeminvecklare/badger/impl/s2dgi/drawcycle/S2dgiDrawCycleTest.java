package com.github.systeminvecklare.badger.impl.s2dgi.drawcycle;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.math.Mathf;
import com.github.systeminvecklare.badger.core.pooling.FlashyPoolManager;
import com.github.systeminvecklare.badger.impl.s2dgi.FlashyS2dgiEngine;

import net.pointlessgames.libs.s2dgi.color.IColorInterpreter;
import net.pointlessgames.libs.s2dgi.key.IKeyListener;
import net.pointlessgames.libs.s2dgi.mouse.IMouseListener;
import net.pointlessgames.libs.s2dgi.mouse.IMouseListener.MouseButton;
import net.pointlessgames.libs.s2dgi.texture.ITexture;
import net.pointlessgames.libs.s2dgi.window.IClippingRectangle;
import net.pointlessgames.libs.s2dgi.window.IGraphics;
import net.pointlessgames.libs.s2dgi.window.IWindow;

public class S2dgiDrawCycleTest {
	
	private MockGraphics mockGraphics;
	private S2dgiDrawCycle drawCycle;
	
	@Before
	public void setUpDrawCycle() {
		this.mockGraphics = new MockGraphics();
		this.drawCycle = new S2dgiDrawCycle(new MockWindow(200, 110, mockGraphics));
		drawCycle.getTransform().setToIdentity();
		drawCycle.applyTransforms();
	}
	
	@BeforeClass
	public static void setUpEnvironment() {
		FlashyPoolManager poolManager = new FlashyPoolManager();
		FlashyS2dgiEngine.registerExtraPools(poolManager);
		FlashyEngine.set(new FlashyS2dgiEngine(poolManager));
	}
	
	@Test
	public void testSimpleRender() {
		drawCycle.render(new MockTexture("test", 57, 64), 10, 20);
		Assert.assertEquals(1, mockGraphics.renderLog.size());
		RenderLogEntry entry = mockGraphics.renderLog.get(0);
		Assert.assertEquals(57, entry.width);
		Assert.assertEquals(64, entry.height);
		Assert.assertEquals(false, entry.flipX);
		Assert.assertEquals(false, entry.flipY);
		Assert.assertEquals(0, entry.quarterRotations);
		Assert.assertEquals(false, entry.rotateDimensions);
		Assert.assertEquals(10, entry.x);
		Assert.assertEquals(26, entry.y);
	}
	
	@Test
	public void testFlipX() {
		drawCycle.render(new MockTexture("test", 15, 19), 71, 22, 15, 19, 71, 22, 0, true, false);
		drawCycle.getTransform().multiplyScale(-1, 2);
		drawCycle.applyTransforms();
		drawCycle.render(new MockTexture("other", 17, 10), 5, 31, 17, 10, 5, 31, 0, false, false);
		drawCycle.render(new MockTexture("other", 30, 20), 5, 17, 30, 20, 0, 0, 3, false, false);
		
		Assert.assertEquals(3, mockGraphics.renderLog.size());
		
		{
			RenderLogEntry entry = mockGraphics.renderLog.get(0);
			Assert.assertEquals(15, entry.width);
			Assert.assertEquals(19, entry.height);
			Assert.assertEquals(true, entry.flipX);
			Assert.assertEquals(false, entry.flipY);
			Assert.assertEquals(0, entry.quarterRotations);
			Assert.assertEquals(false, entry.rotateDimensions);
			Assert.assertEquals(71, entry.x);
			Assert.assertEquals(69, entry.y);
		}
		
		{
			RenderLogEntry entry = mockGraphics.renderLog.get(1);
			Assert.assertEquals(17, entry.width);
			Assert.assertEquals(20, entry.height);
			Assert.assertEquals(true, entry.flipX);
			Assert.assertEquals(false, entry.flipY);
			Assert.assertEquals(0, entry.quarterRotations);
			Assert.assertEquals(false, entry.rotateDimensions);
			Assert.assertEquals(-22, entry.x);
			Assert.assertEquals(28, entry.y);
		}
		
		{
			RenderLogEntry entry = mockGraphics.renderLog.get(2);
			Assert.assertEquals(30, entry.width);
			Assert.assertEquals(40, entry.height);
			Assert.assertEquals(true, entry.flipX);
			Assert.assertEquals(false, entry.flipY);
			Assert.assertEquals(1, entry.quarterRotations);
			Assert.assertEquals(false, entry.rotateDimensions);
			Assert.assertEquals(-35, entry.x);
			Assert.assertEquals(110-2*17-40, entry.y);
		}
	}
	
	@Test
	public void testFlipY() {
		drawCycle.render(new MockTexture("test", 15, 19), 71, 22, 15, 19, 71, 22, 0, false, true);
		drawCycle.getTransform().multiplyScale(1, -1).addToPosition(0, 110);
		drawCycle.applyTransforms();
		drawCycle.render(new MockTexture("other", 17, 10), 5, 31, 17, 10, 5, 31, 0, false, false);
		
		Assert.assertEquals(2, mockGraphics.renderLog.size());
		
		{
			RenderLogEntry entry = mockGraphics.renderLog.get(0);
			Assert.assertEquals(15, entry.width);
			Assert.assertEquals(19, entry.height);
			Assert.assertEquals(false, entry.flipX);
			Assert.assertEquals(true, entry.flipY);
			Assert.assertEquals(0, entry.quarterRotations);
			Assert.assertEquals(false, entry.rotateDimensions);
			Assert.assertEquals(71, entry.x);
			Assert.assertEquals(69, entry.y);
		}
		
		{
			RenderLogEntry entry = mockGraphics.renderLog.get(1);
			Assert.assertEquals(17, entry.width);
			Assert.assertEquals(10, entry.height);
			Assert.assertEquals(false, entry.flipX);
			Assert.assertEquals(true, entry.flipY);
			Assert.assertEquals(0, entry.quarterRotations);
			Assert.assertEquals(false, entry.rotateDimensions);
			Assert.assertEquals(5, entry.x);
			Assert.assertEquals(31, entry.y);
		}
	}
	
	@Test
	public void testRotateQuarter() {
		drawCycle.render(new MockTexture("test", 25, 15), 11, 40, 15, 25, 16, 45, 1, false, false);
		drawCycle.getTransform().setRotation(Mathf.PI/2).addToPosition(110, 0);
		drawCycle.applyTransforms();
		drawCycle.render(new MockTexture("other", 17, 10), 5, 31, 17, 10, 5, 31, -1, false, false);
		drawCycle.render(new MockTexture("other", 17, 10), 5, 31, 17, 10, 5, 31, 1, false, false);
		drawCycle.render(new MockTexture("other", 17, 10), 5, 31, 17, 10, 5, 31, 0, false, false);
		drawCycle.render(new MockTexture("other", 17, 10), 5, 31, 17, 10, 5, 31, 2, false, false);
		
		Assert.assertEquals(5, mockGraphics.renderLog.size());
		
		{
			RenderLogEntry entry = mockGraphics.renderLog.get(0);
			Assert.assertEquals(15, entry.width);
			Assert.assertEquals(25, entry.height);
			Assert.assertEquals(false, entry.flipX);
			Assert.assertEquals(false, entry.flipY);
			Assert.assertEquals(3, entry.quarterRotations);
			Assert.assertEquals(11, entry.x);
			Assert.assertEquals(45, entry.y);
		}
		
		{
			RenderLogEntry entry = mockGraphics.renderLog.get(1);
			Assert.assertEquals(10, entry.width);
			Assert.assertEquals(17, entry.height);
			Assert.assertEquals(false, entry.flipX);
			Assert.assertEquals(false, entry.flipY);
			Assert.assertEquals(0, entry.quarterRotations);
			Assert.assertEquals(69, entry.x);
			Assert.assertEquals(88, entry.y);
		}
		
		{
			RenderLogEntry entry = mockGraphics.renderLog.get(2);
			Assert.assertEquals(10, entry.width);
			Assert.assertEquals(17, entry.height);
			Assert.assertEquals(false, entry.flipX);
			Assert.assertEquals(false, entry.flipY);
			Assert.assertEquals(2, entry.quarterRotations);
			Assert.assertEquals(69, entry.x);
			Assert.assertEquals(88, entry.y);
		}
		
		{
			RenderLogEntry entry = mockGraphics.renderLog.get(3);
			Assert.assertEquals(10, entry.width);
			Assert.assertEquals(17, entry.height);
			Assert.assertEquals(false, entry.flipX);
			Assert.assertEquals(false, entry.flipY);
			Assert.assertEquals(3, entry.quarterRotations);
			Assert.assertEquals(69, entry.x);
			Assert.assertEquals(88, entry.y);
		}
		
		{
			RenderLogEntry entry = mockGraphics.renderLog.get(4);
			Assert.assertEquals(10, entry.width);
			Assert.assertEquals(17, entry.height);
			Assert.assertEquals(false, entry.flipX);
			Assert.assertEquals(false, entry.flipY);
			Assert.assertEquals(1, entry.quarterRotations);
			Assert.assertEquals(69, entry.x);
			Assert.assertEquals(88, entry.y);
		}
	}
	
	//TODO
//	@Test
	public void testFractionalRotation() {
		drawCycle.getTransform().addToRotation(Mathf.PI/6);
		drawCycle.applyTransforms();
		drawCycle.render(new MockTexture("test", 15, 19), 71, 22, 15, 19, 71, 22, 0, false, true);
		drawCycle.render(new MockTexture("test", 15, 19), 71, 22, 15, 19, 71+15, 22+19, 0, false, true);
		
		Assert.assertEquals(2, mockGraphics.renderLog.size());
		
		Assert.fail("Calculate manually what these should be");
	}
	
	private static class MockTexture implements ITexture {
		public final String name;
		private final int width;
		private final int height;
		
		public MockTexture(String name, int width, int height) {
			this.name = name;
			this.width = width;
			this.height = height;
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
		public ITexture createSubTexture(int sourceX, int sourceY, int width, int height) {
			throw new UnsupportedOperationException();
		}
	}
	
	private static class MockGraphics implements IGraphics {
		public final List<RenderLogEntry> renderLog = new ArrayList<RenderLogEntry>();

		@Override
		public void render(ITexture texture, int x, int y) {
			throw new RuntimeException("Not yet implemented!");
		}

		@Override
		public void render(ITexture texture, int x, int y, int width, int height) {
			throw new RuntimeException("Not yet implemented!");
		}

		@Override
		public void render(ITexture texture, int x, int y, int width, int height, int quarterRotations, boolean flipX,
				boolean flipY, boolean rotateDimensions) {
			renderLog.add(new RenderLogEntry(texture, x, y, width, height, quarterRotations, flipX, flipY, rotateDimensions));
		}

		@Override
		public void renderTiled(ITexture texture, int offsetX, int offsetY, int x, int y, int width, int height) {
			throw new RuntimeException("Not yet implemented!");
		}

		@Override
		public <C> void renderRectangle(int x, int y, int width, int height, C color,
				IColorInterpreter<C> colorInterpreter) {
			throw new RuntimeException("Not yet implemented!");
		}

		@Override
		public <C> void renderLine(int x1, int y1, int x2, int y2, C color, IColorInterpreter<C> colorInterpreter) {
			throw new RuntimeException("Not yet implemented!");
		}

		@Override
		public <C> void setTint(C color, IColorInterpreter<C> colorInterpreter) {
			throw new RuntimeException("Not yet implemented!");
		}

		@Override
		public <C> void setAdditive(C color, IColorInterpreter<C> colorInterpreter) {
			throw new RuntimeException("Not yet implemented!");
		}

		@Override
		public void setClip(IClippingRectangle clippingRectangle) {
			throw new RuntimeException("Not yet implemented!");
		}

		@Override
		public void setClip(int x, int y, int width, int height) {
			throw new RuntimeException("Not yet implemented!");
		}

		@Override
		public IClippingRectangle getClip() {
			throw new RuntimeException("Not yet implemented!");
		}
	}
	
	private static class RenderLogEntry {
		public final ITexture texture;
		public final int x;
		public final int y;
		public final int width;
		public final int height;
		public final int quarterRotations;
		public final boolean flipX;
		public final boolean flipY;
		public final boolean rotateDimensions;
		
		public RenderLogEntry(ITexture texture, int x, int y, int width, int height, int quarterRotations,
				boolean flipX, boolean flipY, boolean rotateDimensions) {
			this.texture = texture;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.quarterRotations = quarterRotations;
			this.flipX = flipX;
			this.flipY = flipY;
			this.rotateDimensions = rotateDimensions;
		}
	}
	
	private static class MockWindow implements IWindow {
		private int width;
		private int height;
		private IGraphics graphics;
		
		public MockWindow(int width, int height, IGraphics graphics) {
			this.width = width;
			this.height = height;
			this.graphics = graphics;
		}

		@Override
		public void renderBegin() {
		}

		@Override
		public IGraphics getGraphics() {
			return graphics;
		}

		@Override
		public void renderEnd() {
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
		public void addMouseListener(IMouseListener mouseListener) {
		}

		@Override
		public void removeMouseListener(IMouseListener mouseListener) {
		}

		@Override
		public void addKeyListener(IKeyListener keyListener) {
		}

		@Override
		public void removeKeyListener(IKeyListener keyListener) {
		}

		@Override
		public boolean isKeyDown(int keyCode) {
			return false;
		}

		@Override
		public boolean isMouseButtonDown(MouseButton mouseButton) {
			return false;
		}

		@Override
		public int getMouseX() {
			return 0;
		}

		@Override
		public int getMouseY() {
			return 0;
		}
	}
}
