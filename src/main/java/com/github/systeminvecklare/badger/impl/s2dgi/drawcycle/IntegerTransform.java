package com.github.systeminvecklare.badger.impl.s2dgi.drawcycle;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.transform.AbstractTransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.IReadableTransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.Transform;
import com.github.systeminvecklare.badger.core.math.AbstractPosition;
import com.github.systeminvecklare.badger.core.math.AbstractRotation;
import com.github.systeminvecklare.badger.core.math.AbstractVector;
import com.github.systeminvecklare.badger.core.math.IReadableDeltaRotation;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.IReadableRotation;
import com.github.systeminvecklare.badger.core.math.IReadableVector;
import com.github.systeminvecklare.badger.core.math.Mathf;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolManager;
import com.github.systeminvecklare.badger.core.pooling.IPoolable;
import com.github.systeminvecklare.badger.core.pooling.SimplePool;

public class IntegerTransform implements IReadableIntegerTransform, IPoolable {
	private static final IPool<TransformCenter> CENTER_POOL = new SimplePool<TransformCenter>(2, 10) {
		@Override
		public TransformCenter newObject() {
			return new TransformCenter();
		}
	};
	private final IPool<? super IntegerTransform> pool;

	private final IntVector position = new IntVector(null).setTo(0, 0);
	private int quarterRotations = 0;
	private final IntVector scale = new IntVector(null).setTo(1, 1);
	
	private final IReadableTransform fullTransformInterface = new FullTransformInterface();
	
	public IntegerTransform(IPool<? super IntegerTransform> pool) {
		this.pool = pool;
	}
	
	public IntegerTransform setTo(IReadableIntVector position, int quarterRotations, IReadableIntVector scale) {
		this.position.setTo(position);
		this.quarterRotations = Mathf.mod(quarterRotations, 4);
		this.scale.setTo(scale);
		return this;
	}
	
	public IntegerTransform setTo(IReadableIntegerTransform other) {
		return setTo(other.getPosition(), other.getQuarterRotations(), other.getScale());
	}
	
	public IntegerTransform setToIdentity() {
		this.position.setTo(0, 0);
		this.quarterRotations = 0;
		this.scale.setTo(1, 1);
		return this;
	}
	
	public IntegerTransform setPosition(IReadableIntVector position) {
		return setPosition(position.getIntX(), position.getIntY());
	}
	
	public IntegerTransform setPosition(int x, int y) {
		this.position.setTo(x, y);
		return this;
	}
	
	public IntegerTransform setQuarterRotations(int quarterRotations) {
		this.quarterRotations = Mathf.mod(quarterRotations, 4);
		return this;
	}
	
	public IntegerTransform setScale(IReadableIntVector scale) {
		return setScale(scale.getIntX(), scale.getIntY());
	}
	
	public IntegerTransform setScale(int x, int y) {
		this.scale.setTo(x, y);
		return this;
	}
	
	public IntegerTransform setScale(int scale) {
		return setScale(scale, scale);
	}
	
	public IntegerTransform mult(IReadableIntegerTransform other) {
		// (p1, c1)*(p2, c2)*z = (p3, c3)*z
		// = p1 + c1*(p2 + c2*z)
		// = p1 + c1*p2 + c1*c2*z
		// --> p3 = p1 + c1*p2,   c3 = c1*c2
		
		TransformCenter center = CENTER_POOL.obtain().setTo(this);
		IPool<IntVector> intVectorPool = FlashyEngine.get().getPoolManager().getPool(IntVector.class);
		IntVector p3 = center.apply(other.getPosition(), intVectorPool.obtain()).add(this.getPosition());
		
		IReadableIntVector otherScale = other.getScale();
		center.mult(other.getQuarterRotations(), otherScale.getIntX(), otherScale.getIntY());
		
		this.position.setTo(p3);
		this.quarterRotations = center.quarterRotations;
		this.scale.setTo(center.sx, center.sy);
		p3.free();
		CENTER_POOL.free(center);
		return this;
	}
	
	public IntegerTransform multLeft(IReadableIntegerTransform other) {
		TransformCenter center = CENTER_POOL.obtain().setTo(other);
		IPool<IntVector> intVectorPool = FlashyEngine.get().getPoolManager().getPool(IntVector.class);
		IntVector p3 = center.apply(this.getPosition(), intVectorPool.obtain()).add(other.getPosition());
		
		center.mult(this.quarterRotations, this.scale.getIntX(), this.scale.getIntY());
		
		this.position.setTo(p3);
		this.quarterRotations = center.quarterRotations;
		this.scale.setTo(center.sx, center.sy);
		p3.free();
		CENTER_POOL.free(center);
		return this;
	}
	
	public boolean invert() {
		return invert(this);
	}
	
	@Override
	public boolean invert(IntegerTransform result) {
		 // (p1, c1)*(p2, c2)*z = (0, I)*z
		// = p1 + c1*(p2 + c2*z)
		// = p1 + c1*p2 + c1*c2*z
		// --> 0 = p1 + c1*p2,   I = c1*c2
		// c2 = c1^-1
		// p2 = -c1^-1*p1
		TransformCenter center = CENTER_POOL.obtain().setTo(this);
		try {
			if(Math.abs(center.sx*center.sy) == 1) {
				center.quarterRotations = Mathf.mod(-center.quarterRotations, 4);
				if(Mathf.mod(center.quarterRotations, 2) == 1) {
					int swap = center.sx;
					center.sx = center.sy;
					center.sy = swap;
				}
				
				IPool<IntVector> intVectorPool = FlashyEngine.get().getPoolManager().getPool(IntVector.class);
				IntVector p2 = center.apply(this.getPosition(), intVectorPool.obtain()).scale(-1);
				
				result.position.setTo(p2);
				result.quarterRotations = center.quarterRotations;
				result.scale.setTo(center.sx, center.sy);
				
				p2.free();
				return true;
			} else {
				return false;
			}
		} finally {
			CENTER_POOL.free(center);
		}
	}
	
	public IntegerTransform addToPosition(IReadableIntVector delta) {
		return addToPosition(delta.getIntX(), delta.getIntY());
	}
	
	public IntegerTransform addToPosition(int dx, int dy) {
		this.position.add(dx, dy);
		return this;
	}
	
	public IntegerTransform addToQuarterRotations(int delta) {
		return setQuarterRotations(this.quarterRotations + delta);
	}
	
	public IntegerTransform multiplyScale(int sx, int sy) {
		return setScale(scale.getIntX()*sx, scale.getIntY()*sy);
	}
	
	public IntegerTransform multiplyScale(IReadableIntVector scale) {
		return multiplyScale(scale.getIntX(), scale.getIntY());
	}
	
	public IntegerTransform multiplyScale(int s) {
		return multiplyScale(s, s);
	}

	@Override
	public IReadableIntVector getPosition() {
		return position;
	}

	@Override
	public int getQuarterRotations() {
		return quarterRotations;
	}

	@Override
	public IReadableIntVector getScale() {
		return scale;
	}
	
	private static class TransformCenter {
		private int quarterRotations = 0;
		private int sx = 1;
		private int sy = 1;
		
		public TransformCenter setTo(IReadableIntegerTransform transform) {
			return setTo(transform.getQuarterRotations(), transform.getScale().getIntX(), transform.getScale().getIntY());
		}
		
		public TransformCenter setTo(int quarterRotations, int sx, int sy) {
			this.quarterRotations = Mathf.mod(quarterRotations, 4);
			this.sx = sx;
			this.sy = sy;
			return this;
		}

		public IntVector apply(IReadableIntVector vector, IntVector result) {
			return result.setTo(vector).scale(sx, sy).rotate(quarterRotations);
		}

		public TransformCenter mult(int otherQuarterRotation, int otherSx, int otherSy) {
			int multQ = Mathf.mod(this.quarterRotations + otherQuarterRotation, 4);
			boolean flipXY = Mathf.mod(otherQuarterRotation, 2) == 1;
			int thisSx = flipXY ? this.sy : this.sx;
			int thisSy = flipXY ? this.sx : this.sy;
			this.quarterRotations = multQ;
			this.sx = thisSx*otherSx;
			this.sy = thisSy*otherSy;
			return this;
		}
		
		public TransformCenter mult(TransformCenter other) {
			return mult(other.quarterRotations, other.sx, other.sy);
		}
	}
	
	@Override
	public IntVector transform(IntVector argumentAndResult) {
		return argumentAndResult.scale(this.scale).rotate(this.quarterRotations).add(this.position);
	}
	
	@Override
	public OrientableRectangle transform(OrientableRectangle argumentAndResult) {
		return argumentAndResult.scale(this.scale).rotate(this.quarterRotations).add(this.position);
	}
	
	@Override
	public IntVector transformNormal(IntVector argumentAndResult) {
		return argumentAndResult.scale(this.scale).rotate(this.quarterRotations);
	}
	
	private static String toString(IReadableIntegerTransform transform) {
		return new StringBuilder("[")
				.append(transform.getPosition())
				.append(", ")
				.append(transform.getQuarterRotations())
				.append(", ")
				.append(transform.getScale())
				.append("]")
				.toString();
	}
	
	@Override
	public void free() {
		pool.free(this);
	}
	
	@Override
	public IntegerTransform copy(IPool<IntegerTransform> pool) {
		IntegerTransform copy = pool == null ? new IntegerTransform(null) : pool.obtain();
		return copy.setTo(this);
	}
	
	public IReadableTransform asFullTransform() {
		return fullTransformInterface;
	}
	
	private class FullTransformInterface implements IReadableTransform {
		private final IReadablePosition position = new AbstractPosition() {
			@Override
			public float getX() {
				return IntegerTransform.this.position.getIntX();
			}
			
			@Override
			public float getY() {
				return IntegerTransform.this.position.getIntY();
			}
		};
		private final IReadableRotation rotation = new AbstractRotation() {
			
			@Override
			public float getTheta() {
				return IntegerTransform.this.quarterRotations*Mathf.PI/2f;
			}
		};
		private final IReadableVector scale = new AbstractVector() {
			@Override
			public float getX() {
				return IntegerTransform.this.scale.getIntX();
			}

			@Override
			public float getY() {
				return IntegerTransform.this.scale.getIntY();
			}
			
		};
		
		@Override
		public ITransform copy() {
			return new Transform(null).setTo(this);
		}

		@Override
		public ITransform copy(IPool<ITransform> pool) {
			return pool.obtain().setTo(this);
		}

		@Override
		public ITransform copy(EasyPooler ep) {
			return ep.obtain(ITransform.class).setTo(this);
		}

		@Override
		public ITransform copy(IPoolManager poolManager) {
			return copy(poolManager.getPool(ITransform.class));
		}

		@Override
		public IReadablePosition getPosition() {
			return position;
		}

		@Override
		public IReadableRotation getRotation() {
			return rotation;
		}

		@Override
		public IReadableVector getScale() {
			return scale;
		}

		@Override
		public float getShear() {
			return 0;
		}

		@Override
		public void transform(Position argumentAndResult) {
			IntVector intVector = new IntVector(null).setTo(Math.round(argumentAndResult.getX()), Math.round(argumentAndResult.getY()));
			IntegerTransform.this.transform(intVector);
			argumentAndResult.setTo(intVector.getX(), intVector.getY());
		}
	}
	
//	//TODO move into unit tests
//	private static void test(IReadableIntegerTransform transformA, IReadableIntegerTransform transformB, IReadableIntVector vector) {
//		IntVector v = new IntVector(null).setTo(vector);
//		System.out.println(toString(transformB)+"*"+v+" = "+transformB.transform(v));
//		System.out.println(toString(transformA)+"*"+v+" = "+transformA.transform(v));
//		int v1x = v.getIntX();
//		int v1y = v.getIntY();
//		IntegerTransform product = new IntegerTransform(null).setTo(transformA).mult(transformB);
//		v.setTo(vector);
//		System.out.println(toString(transformA) +"*"+ toString(transformB) +" = " +toString(product));
//		System.out.println(toString(product)+"*"+v+" = "+product.transform(v));
//		int v2x = v.getIntX();
//		int v2y = v.getIntY();
//		if(v1x != v2x || v1y != v2y) {
//			throw new RuntimeException("Test failed!");
//		}
//	}
//	
//	private static IntegerTransform make(int px, int py, int r, int sx, int sy) {
//		return new IntegerTransform(null).setToIdentity().setPosition(px, py).setQuarterRotations(r).setScale(sx, sy);
//	}
//	
//	private static IntegerTransform makeRandom(Random random) {
//		return make(random.nextInt(1001)-500, random.nextInt(1001)-500, random.nextInt(4), random.nextInt(10), random.nextInt(10));
//	}
//	
//	private static IntVector getS(OrientableRectangle rectangle) {
//		return new IntVector(null).setTo(rectangle.getFlipX() ? -1 : 1, 0).rotate(rectangle.getQuarterRotations()).scale(rectangle.getWidth(), rectangle.getHeight());
//	}
//	
//	private static IntVector getT(OrientableRectangle rectangle) {
//		return new IntVector(null).setTo(0, rectangle.getFlipY() ? -1 : 1).rotate(rectangle.getQuarterRotations()).scale(rectangle.getWidth(), rectangle.getHeight());
//	}
//	
//	private static void assertEqual(int expected, int actual, String messageTemplate) {
//		if(expected != actual) {
//			throw new RuntimeException(messageTemplate.replace("%e", String.valueOf(expected)).replace("%a", String.valueOf(actual)));
//		}
//	}
//	
//	private static void assertEqual(boolean expected, boolean actual, String messageTemplate) {
//		if(expected != actual) {
//			throw new RuntimeException(messageTemplate.replace("%e", String.valueOf(expected)).replace("%a", String.valueOf(actual)));
//		}
//	}
//	
//	private static void assertEqual(IReadableIntVector expected, IReadableIntVector actual, String messageTemplate) {
//		if(expected.getIntX() != actual.getIntX() || expected.getIntY() != actual.getIntY()) {
//			throw new RuntimeException(messageTemplate.replace("%e", String.valueOf(expected)).replace("%a", String.valueOf(actual)));
//		}
//	}
//	
//	private static void test(IReadableIntegerTransform transform, OrientableRectangle rectangle) {
//		OrientableRectangle copy = new OrientableRectangle(null).setTo(0, 0, 0, 0, 0, false, false).setTo(rectangle);
//		transform.transform(copy);
//		IntVector corner1 = new IntVector(null).setTo(rectangle.getX(), rectangle.getY());
//		IntVector corner2 = new IntVector(null).setTo(corner1).add(rectangle.getWidth(), rectangle.getHeight());
//		transform.transform(corner1);
//		transform.transform(corner2);
//		int expectedX = Math.min(corner1.getIntX(), corner2.getIntX());
//		int expectedY = Math.min(corner1.getIntY(), corner2.getIntY());
//		int expectedWidth = Math.max(corner1.getIntX(), corner2.getIntX()) - expectedX;
//		int expectedHeight = Math.max(corner1.getIntY(), corner2.getIntY()) - expectedY;
//		IntVector expectedS = transform.transformNormal(getS(rectangle));
//		IntVector expectedT = transform.transformNormal(getT(rectangle));
//		
//		assertEqual(expectedX, copy.getX(), "X differs! Expected %e and got %a.");
//		assertEqual(expectedY, copy.getY(), "Y differs! Expected %e and got %a.");
//		assertEqual(expectedWidth, copy.getWidth(), "Width differs! Expected %e and got %a.");
//		assertEqual(expectedHeight, copy.getHeight(), "Height differs! Expected %e and got %a.");
//		assertEqual(expectedS, getS(copy), "S-vector differs! Expected %e and got %a.");
//		assertEqual(expectedT, getT(copy), "T-vector differs! Expected %e and got %a.");
//	}
//	
//	private static void testInvert(IReadableIntegerTransform transform, boolean expectInvertible) {
//		IntegerTransform inverse = new IntegerTransform(null);
//		boolean invertible = transform.invert(inverse);
//		assertEqual(expectInvertible, invertible, "Expected transform to be "+(expectInvertible ? "invertible" : "NOT invertible"));
//		if(invertible) {
//			inverse.mult(transform);
//			assertEqual(new IntVector(null).setTo(0, 0), inverse.getPosition(), "Expected translation to be %e but it was %a");
//			assertEqual(0, inverse.getQuarterRotations(), "Expected rotation to be %e but it was %a");
//			assertEqual(new IntVector(null).setTo(1, 1), inverse.getScale(), "Expected scale to be %e but it was %a");
//		}
//	}
//
//	public static void main(String[] args) {
//		FlashyEngine.set(new FlashyS2dgiEngine());
//		Random random = new Random(2828385383L);
//		for(int i = 0; i < 10000; ++i) {
//			System.out.println("Point test "+i+":");
//			test(makeRandom(random), makeRandom(random), new IntVector(null).setTo(random.nextInt(101) - 50, random.nextInt(101) - 50));
//		}
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		for(int i = 0; i < 10000; ++i) {
//			System.out.println("Rectangle test "+i+":");
//			OrientableRectangle orientableRectangle = new OrientableRectangle(null).setTo(random.nextInt(11)-5, random.nextInt(11)-5, random.nextInt(10), random.nextInt(10), random.nextInt(4), random.nextBoolean(), random.nextBoolean());
//			IntegerTransform translate = make(random.nextInt(11)-5, random.nextInt(11)-5, random.nextInt(4), random.nextInt(11)-5, random.nextInt(11)-5);
//			System.out.println(toString(translate)+" * "+orientableRectangle +" = "+translate.transform(new OrientableRectangle(null).setTo(orientableRectangle)));
//			test(translate, orientableRectangle);
//		}
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		for(int i = 0; i < 10000; ++i) {
//			System.out.println("Invert test "+i+":");
//			IntegerTransform translate = make(random.nextInt(11)-5, random.nextInt(11)-5, random.nextInt(4), random.nextInt(5)-2, random.nextInt(5)-2);
//			boolean expectedInvertability = Math.abs(translate.getScale().getIntX()*translate.getScale().getIntY()) == 1;
//			System.out.println(expectedInvertability ? "Invertible" : "Not invertible");
//			testInvert(translate, expectedInvertability);
//		}
//	}
}
