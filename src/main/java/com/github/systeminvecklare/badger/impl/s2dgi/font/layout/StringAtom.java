package com.github.systeminvecklare.badger.impl.s2dgi.font.layout;

import java.io.IOException;

public class StringAtom implements ITextAtom {
	private final String value;
	
	public StringAtom(String value) {
		this.value = value;
	}

	@Override
	public boolean is(char c) {
		return value.length() == 1 && value.charAt(0) == c;
	}

	@Override
	public boolean isWhitespace() {
		return false;
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
