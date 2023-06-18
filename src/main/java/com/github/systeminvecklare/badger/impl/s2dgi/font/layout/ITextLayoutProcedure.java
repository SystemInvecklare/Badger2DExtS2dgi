package com.github.systeminvecklare.badger.impl.s2dgi.font.layout;

import java.util.Collection;

import com.github.systeminvecklare.badger.impl.s2dgi.font.IFont;

public interface ITextLayoutProcedure {
	<C extends Collection<String>> C layoutText(Iterable<ITextAtom> text, C result);
	
	public static ITextLayoutProcedure maxWidth(IFont font, int maxWidth) {
		return new LineSpittingTextLayoutProcedure(new LineSpittingTextLayoutProcedure.ILineConstraints() {
			@Override
			public boolean fits(CharSequence line) {
				return font.complile(line.toString()).getWidth() <= maxWidth;
			}
		});
	}
}
