package com.github.systeminvecklare.badger.impl.s2dgi.util;

import java.util.function.Function;

public class EqualsUtil {
	public static <T> boolean equalsOwn(Class<T> type, Object obj, Function<T, Boolean> equals) {
		if(type.isInstance(obj)) {
			return equals.apply(type.cast(obj));
		}
		return false;
	}
}
