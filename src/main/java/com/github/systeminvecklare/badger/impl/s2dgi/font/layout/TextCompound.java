package com.github.systeminvecklare.badger.impl.s2dgi.font.layout;

public final class TextCompound {
	private final ITextAtom[] atoms;
	private final int offset;
	private final int length;
	
	public TextCompound(ITextAtom[] atoms, int offset, int length) {
		this.atoms = atoms;
		this.offset = offset;
		this.length = length;
	}

	public int length() {
		return length;
	}

	public static TextCompound ofChar(char c) {
		return new TextCompound(new ITextAtom[] {new CharAtom(c)}, 0, 1);
	}

	public TextCompound substring(int beginIndex, int endIndex) {
		return new TextCompound(atoms, offset + beginIndex, endIndex - beginIndex);
	}
	
	public TextCompound substring(int beginIndex) {
		return new TextCompound(atoms, offset + beginIndex, length - beginIndex);
	}
	
	public void appendTo(Appendable appendable) {
		for(int i = 0; i < length; ++i) {
			atoms[i + offset].appendTo(appendable);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		appendTo(builder);
		return builder.toString();
	}
}
