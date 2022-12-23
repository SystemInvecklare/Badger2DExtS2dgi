package com.github.systeminvecklare.badger.impl.s2dgi.util;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;

public interface ICached<K,V> {
	V getValue(K key);
	void clear();
	
	static <K,V> ICached<K, V> of(Function<K, V> valueFactory) {
		return of(valueFactory, Objects::equals);
	}
	
	static <K,V> ICached<K, V> of(Function<K, V> valueFactory, BiPredicate<? super K, ? super K> keyEqualityMethod) {
		class Cached implements ICached<K,V> {
			private final Function<K, V> valueFactory;
			private final BiPredicate<? super K, ? super K> keyEqualityMethod;
			private boolean hasCached = false;
			private K cacheKey;
			private V cachedValue;
			
			public Cached(Function<K, V> valueFactory, BiPredicate<? super K, ? super K> keyEqualityMethod) {
				this.valueFactory = valueFactory;
				this.keyEqualityMethod = keyEqualityMethod;
			}
			
			private void recalculate(K key) {
				cachedValue = valueFactory.apply(key);
				cacheKey = key;
				hasCached = true;
			}

			@Override
			public V getValue(K key) {
				if(!hasCached || !keyEqualityMethod.test(key, cacheKey)) {
					recalculate(key);
				}
				return cachedValue;
			}
			
			@Override
			public void clear() {
				cachedValue = null;
				cacheKey = null;
				hasCached = false;
			}
		}
		return new Cached(valueFactory, keyEqualityMethod);
	}
}
