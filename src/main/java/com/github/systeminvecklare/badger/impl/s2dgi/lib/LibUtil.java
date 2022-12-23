package com.github.systeminvecklare.badger.impl.s2dgi.lib;

import java.util.function.BiFunction;
import java.util.function.Function;

/*package-protected*/ class LibUtil {
	public static <V,T> V findExtreme(V start, BiFunction<V, V, V> updater, Iterable<T> iterable, Function<T, V> intGetter) {
		V extreme = start;
		for(T object : iterable) {
			extreme = updater.apply(extreme, intGetter.apply(object));
		}
		return extreme;
	}
	
	public static <T> int maxInt(int start, Iterable<T> iterable, Function<T, Integer> intGetter) {
		return findExtreme(start, Math::max, iterable, intGetter);
	}
	
	public static <T> int minInt(int start, Iterable<T> iterable, Function<T, Integer> intGetter) {
		return findExtreme(start, Math::min, iterable, intGetter);
	}

	public static <T> int sumInt(Iterable<T> iterable, Function<T, Integer> intGetter) {
		return findExtreme(0, (a,b) -> a+b, iterable, intGetter);
	}
}
