package com.github.systeminvecklare.badger.impl.s2dgi.graphics;

import java.util.function.ToIntFunction;

import net.pointlessgames.libs.s2dgi.color.IColorInterpreter;

public interface IColor {
	int getARGB();
	IMutableColor withAlpha(int alpha);
	IMutableColor lerp(IColor other, float t);
	
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
	
	public static final IColor AQUA = CYAN;
	public static final IColor FUCHSIA = MAGENTA;
	public static final IColor SILVER = create(192, 192, 192, 255);
	public static final IColor OLIVE = create(128, 128, 0, 255);
	public static final IColor TEAL = create(0, 128, 128, 255);
	public static final IColor NAVY = create(0, 0, 128, 255);
	public static final IColor MAROON = create(128, 0, 0, 255);
	public static final IColor DARK_RED = create(139, 0, 0, 255);
	public static final IColor BROWN = create(165, 42, 42, 255);
	public static final IColor FIREBRICK = create(178, 34, 34, 255);
	public static final IColor CRIMSON = create(220, 20, 60, 255);
	public static final IColor TOMATO = create(255, 99, 71, 255);
	public static final IColor CORAL = create(255, 127, 80, 255);
	public static final IColor INDIAN_RED = create(205, 92, 92, 255);
	public static final IColor LIGHT_CORAL = create(240, 128, 128, 255);
	public static final IColor DARK_SALMON = create(233, 150, 122, 255);
	public static final IColor SALMON = create(250, 128, 114, 255);
	public static final IColor LIGHT_SALMON = create(255, 160, 122, 255);
	public static final IColor ORANGE_RED = create(255, 69, 0, 255);
	public static final IColor DARK_ORANGE = create(255, 140, 0, 255);
	public static final IColor GOLD = create(255, 215, 0, 255);
	public static final IColor DARK_GOLDEN_ROD = create(184, 134, 11, 255);
	public static final IColor GOLDEN_ROD = create(218, 165, 32, 255);
	public static final IColor PALE_GOLDEN_ROD = create(238, 232, 170, 255);
	public static final IColor DARK_KHAKI = create(189, 183, 107, 255);
	public static final IColor KHAKI = create(240, 230, 140, 255);
	public static final IColor YELLOW_GREEN = create(154, 205, 50, 255);
	public static final IColor DARK_OLIVE_GREEN = create(85, 107, 47, 255);
	public static final IColor OLIVE_DRAB = create(107, 142, 35, 255);
	public static final IColor LAWN_GREEN = create(124, 252, 0, 255);
	public static final IColor CHARTREUSE = create(127, 255, 0, 255);
	public static final IColor GREEN_YELLOW = create(173, 255, 47, 255);
	public static final IColor DARK_GREEN = create(0, 100, 0, 255);
	public static final IColor FOREST_GREEN = create(34, 139, 34, 255);
	public static final IColor LIME = GREEN;
	public static final IColor LIME_GREEN = create(50, 205, 50, 255);
	public static final IColor LIGHT_GREEN = create(144, 238, 144, 255);
	public static final IColor PALE_GREEN = create(152, 251, 152, 255);
	public static final IColor DARK_SEA_GREEN = create(143, 188, 143, 255);
	public static final IColor MEDIUM_SPRING_GREEN = create(0, 250, 154, 255);
	public static final IColor SPRING_GREEN = create(0, 255, 127, 255);
	public static final IColor SEA_GREEN = create(46, 139, 87, 255);
	public static final IColor MEDIUM_AQUA_MARINE = create(102, 205, 170, 255);
	public static final IColor MEDIUM_SEA_GREEN = create(60, 179, 113, 255);
	public static final IColor LIGHT_SEA_GREEN = create(32, 178, 170, 255);
	public static final IColor DARK_SLATE_GRAY = create(47, 79, 79, 255);
	public static final IColor DARK_CYAN = create(0, 139, 139, 255);
	public static final IColor LIGHT_CYAN = create(224, 255, 255, 255);
	public static final IColor DARK_TURQUOISE = create(0, 206, 209, 255);
	public static final IColor TURQUOISE = create(64, 224, 208, 255);
	public static final IColor MEDIUM_TURQUOISE = create(72, 209, 204, 255);
	public static final IColor PALE_TURQUOISE = create(175, 238, 238, 255);
	public static final IColor AQUA_MARINE = create(127, 255, 212, 255);
	public static final IColor POWDER_BLUE = create(176, 224, 230, 255);
	public static final IColor CADET_BLUE = create(95, 158, 160, 255);
	public static final IColor STEEL_BLUE = create(70, 130, 180, 255);
	public static final IColor CORN_FLOWER_BLUE = create(100, 149, 237, 255);
	public static final IColor DEEP_SKY_BLUE = create(0, 191, 255, 255);
	public static final IColor DODGER_BLUE = create(30, 144, 255, 255);
	public static final IColor LIGHT_BLUE = create(173, 216, 230, 255);
	public static final IColor SKY_BLUE = create(135, 206, 235, 255);
	public static final IColor LIGHT_SKY_BLUE = create(135, 206, 250, 255);
	public static final IColor MIDNIGHT_BLUE = create(25, 25, 112, 255);
	public static final IColor DARK_BLUE = create(0, 0, 139, 255);
	public static final IColor MEDIUM_BLUE = create(0, 0, 205, 255);
	public static final IColor ROYAL_BLUE = create(65, 105, 225, 255);
	public static final IColor BLUE_VIOLET = create(138, 43, 226, 255);
	public static final IColor INDIGO = create(75, 0, 130, 255);
	public static final IColor DARK_SLATE_BLUE = create(72, 61, 139, 255);
	public static final IColor SLATE_BLUE = create(106, 90, 205, 255);
	public static final IColor MEDIUM_SLATE_BLUE = create(123, 104, 238, 255);
	public static final IColor MEDIUM_PURPLE = create(147, 112, 219, 255);
	public static final IColor DARK_MAGENTA = create(139, 0, 139, 255);
	public static final IColor DARK_VIOLET = create(148, 0, 211, 255);
	public static final IColor DARK_ORCHID = create(153, 50, 204, 255);
	public static final IColor MEDIUM_ORCHID = create(186, 85, 211, 255);
	public static final IColor PURPLE = create(128, 0, 128, 255);
	public static final IColor THISTLE = create(216, 191, 216, 255);
	public static final IColor PLUM = create(221, 160, 221, 255);
	public static final IColor VIOLET = create(238, 130, 238, 255);
	public static final IColor ORCHID = create(218, 112, 214, 255);
	public static final IColor MEDIUM_VIOLET_RED = create(199, 21, 133, 255);
	public static final IColor PALE_VIOLET_RED = create(219, 112, 147, 255);
	public static final IColor DEEP_PINK = create(255, 20, 147, 255);
	public static final IColor HOT_PINK = create(255, 105, 180, 255);
	public static final IColor LIGHT_PINK = create(255, 182, 193, 255);
	public static final IColor ANTIQUE_WHITE = create(250, 235, 215, 255);
	public static final IColor BEIGE = create(245, 245, 220, 255);
	public static final IColor BISQUE = create(255, 228, 196, 255);
	public static final IColor BLANCHED_ALMOND = create(255, 235, 205, 255);
	public static final IColor WHEAT = create(245, 222, 179, 255);
	public static final IColor CORN_SILK = create(255, 248, 220, 255);
	public static final IColor LEMON_CHIFFON = create(255, 250, 205, 255);
	public static final IColor LIGHT_GOLDEN_ROD_YELLOW = create(250, 250, 210, 255);
	public static final IColor LIGHT_YELLOW = create(255, 255, 224, 255);
	public static final IColor SADDLE_BROWN = create(139, 69, 19, 255);
	public static final IColor SIENNA = create(160, 82, 45, 255);
	public static final IColor CHOCOLATE = create(210, 105, 30, 255);
	public static final IColor PERU = create(205, 133, 63, 255);
	public static final IColor SANDY_BROWN = create(244, 164, 96, 255);
	public static final IColor BURLY_WOOD = create(222, 184, 135, 255);
	public static final IColor TAN = create(210, 180, 140, 255);
	public static final IColor ROSY_BROWN = create(188, 143, 143, 255);
	public static final IColor MOCCASIN = create(255, 228, 181, 255);
	public static final IColor NAVAJO_WHITE = create(255, 222, 173, 255);
	public static final IColor PEACH_PUFF = create(255, 218, 185, 255);
	public static final IColor MISTY_ROSE = create(255, 228, 225, 255);
	public static final IColor LAVENDER_BLUSH = create(255, 240, 245, 255);
	public static final IColor LINEN = create(250, 240, 230, 255);
	public static final IColor OLD_LACE = create(253, 245, 230, 255);
	public static final IColor PAPAYA_WHIP = create(255, 239, 213, 255);
	public static final IColor SEA_SHELL = create(255, 245, 238, 255);
	public static final IColor MINT_CREAM = create(245, 255, 250, 255);
	public static final IColor SLATE_GRAY = create(112, 128, 144, 255);
	public static final IColor LIGHT_SLATE_GRAY = create(119, 136, 153, 255);
	public static final IColor LIGHT_STEEL_BLUE = create(176, 196, 222, 255);
	public static final IColor LAVENDER = create(230, 230, 250, 255);
	public static final IColor FLORAL_WHITE = create(255, 250, 240, 255);
	public static final IColor ALICE_BLUE = create(240, 248, 255, 255);
	public static final IColor GHOST_WHITE = create(248, 248, 255, 255);
	public static final IColor HONEYDEW = create(240, 255, 240, 255);
	public static final IColor IVORY = create(255, 255, 240, 255);
	public static final IColor AZURE = create(240, 255, 255, 255);
	public static final IColor SNOW = create(255, 250, 250, 255);
	public static final IColor DIM_GRAY = create(105, 105, 105, 255);
	public static final IColor GAINSBORO = create(220, 220, 220, 255);
	public static final IColor WHITE_SMOKE = create(245, 245, 245, 255);
	
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

	@Override
	public IMutableColor lerp(IColor other, float t) {
		return IColor.createMutable(this).lerp(other, t);
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
	
	private static int lerpChannel(IColor a, IColor b, float t, ToIntFunction<IColor> channel) {
		int aVal = channel.applyAsInt(a);
		int bVal = channel.applyAsInt(b);
		return (int) (aVal + t*(bVal - aVal));
	}

	@Override
	public IMutableColor lerp(IColor other, float t) {
		return setTo(lerpChannel(this, other, t, INTERPRETER::getRed),
					 lerpChannel(this, other, t, INTERPRETER::getGreen),
					 lerpChannel(this, other, t, INTERPRETER::getBlue),
					 lerpChannel(this, other, t, INTERPRETER::getAlpha));
	}
	
	@Override
	public IMutableColor setAlpha(int alpha) {
		return setTo(INTERPRETER.getRed(this), INTERPRETER.getGreen(this), INTERPRETER.getBlue(this), alpha);
	}
	
	@Override
	public IColor immutableCopy() {
		return new ColorImpl(mutableArgb);
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
