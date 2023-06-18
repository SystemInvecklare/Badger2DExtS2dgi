package com.github.systeminvecklare.badger.impl.s2dgi.font.layout;

public interface ITextAtom {
	boolean is(char c);
	boolean isWhitespace();
	void appendTo(Appendable appendable);
}
