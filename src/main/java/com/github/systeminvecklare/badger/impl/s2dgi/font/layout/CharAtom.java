package com.github.systeminvecklare.badger.impl.s2dgi.font.layout;

import java.io.IOException;

public class CharAtom implements ITextAtom {
	private final char value;
	
	public CharAtom(char value) {
		this.value = value;
	}

	@Override
	public boolean is(char c) {
		return value == c;
	}

	@Override
	public boolean isWhitespace() {
		return Character.isWhitespace(value);
	}

	@Override
	public void appendTo(Appendable appendable) {
		try {
			appendable.append(value);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
