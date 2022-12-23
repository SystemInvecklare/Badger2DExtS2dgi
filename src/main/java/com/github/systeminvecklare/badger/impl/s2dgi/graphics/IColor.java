package com.github.systeminvecklare.badger.impl.s2dgi.graphics;

import net.pointlessgames.libs.s2dgi.color.IColorInterpreter;

public interface IColor {
	int getARGB();
	IMutableColor withAlpha(int alpha);
	
	public static final IColor ZERO      = create(0, 0, 0, 0);
	public static final IColor WHITE     = create(255, 255, 255, 255);
	public static final IColor LIGHTGRAY = create(192, 192, 192, 255);
	public static final IColor GRAY      = create(128, 128, 128, 255);
	public static final IColor DARKGRAY  = create(64, 64, 64, 255);
	public static final IColor BLACK     = create(0, 0, 0, 255);
	public static final IColor RED       = create(255, 0, 0, 255);
	public static final IColor PINK      = create(255, 175, 175, 255);
	public static final IColor ORANGE    = create(255, 200, 0, 255);
	public static final IColor YELLOW    = create(255, 255, 0, 255);
	public static final IColor GREEN     = create(0, 255, 0, 255);
	public static final IColor MAGENTA   = create(255, 0, 255, 255);
	public static final IColor CYAN      = create(0, 255, 255, 255);
	public static final IColor BLUE      = create(0, 0, 255, 255);
	
	public static final IColorInterpreter<IColor> INTERPRETER = new IColorInterpreter<IColor>() {
		@Override
		public int getRed(IColor color) {
			return (color.getARGB() >> 16) & 0xff;
		}
		
		@Override
		public int getGreen(IColor color) {
			return (color.getARGB() >> 8) & 0xff;
		}
		
		@Override
		public int getBlue(IColor color) {
			return (color.getARGB() >> 0) & 0xff;
		}
		
		@Override
		public int getAlpha(IColor color) {
			return (color.getARGB() >> 24) & 0xff;
		}
	};
	
	public static IColor create(int r, int g, int b, int a) {
		return create(Util.toARGB(r, g, b, a));
	}
	
	public static IColor create(final int argb) {
		return new ColorImpl(argb);
	}
	
	public static IMutableColor createMutable(IColor color) {
		return createMutable(color.getARGB());
	}
	
	public static IMutableColor createMutable(final int argb) {
		return new MutableColorImpl(argb);
	}
}

/*package-private*/ abstract class ColorImplBase implements IColor {
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IColor) {
			return this.getARGB() == ((IColor) obj).getARGB();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.getARGB();
	}
	
	@Override
	public IMutableColor withAlpha(int alpha) {
		return IColor.createMutable(Util.toARGB(IColor.INTERPRETER.getRed(this), IColor.INTERPRETER.getGreen(this), IColor.INTERPRETER.getBlue(this), alpha));
	}
}

/*package-private*/ class ColorImpl extends ColorImplBase {
	private final int argb;
	

	public ColorImpl(int argb) {
		this.argb = argb;
	}

	@Override
	public int getARGB() {
		return argb;
	}
}

/*package-private*/ class MutableColorImpl extends ColorImplBase implements IMutableColor {
	private int mutableArgb;
	
	public MutableColorImpl(int argb) {
		this.mutableArgb = argb;
	}

	@Override
	public int getARGB() {
		return mutableArgb;
	}
	
	@Override
	public IMutableColor setTo(int r, int g, int b, int a) {
		this.mutableArgb = Util.toARGB(r, g, b, a);
		return this;
	}
	
	@Override
	public IMutableColor setTo(IColor other) {
		this.mutableArgb = other.getARGB();
		return this;
	}

}

/*package-private*/ class Util {
	public static int toARGB(int r, int g, int b, int a) {
		return ((a & 0xff) << 24)
	    | ((r & 0xff) << 16)
        | ((g & 0xff) << 8)
        | ((b & 0xff) << 0);
	}
}
