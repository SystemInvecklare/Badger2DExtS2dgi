package com.github.systeminvecklare.badger.impl.s2dgi.font;

import java.util.Iterator;

import com.github.systeminvecklare.badger.impl.s2dgi.font.layout.CharAtom;
import com.github.systeminvecklare.badger.impl.s2dgi.font.layout.ITextAtom;

public interface IFont {
	IText complile(CharSequence text);
	
	default Iterable<ITextAtom> computeAtoms(CharSequence text) {
		return new Iterable<ITextAtom>() {
			@Override
			public Iterator<ITextAtom> iterator() {
				return new Iterator<ITextAtom>() {
					private int index = 0;
					
					@Override
					public ITextAtom next() {
						return new CharAtom(text.charAt(index++));
					}
					
					@Override
					public boolean hasNext() {
						return index < text.length();
					}
				};
			}
		};
	}
}
