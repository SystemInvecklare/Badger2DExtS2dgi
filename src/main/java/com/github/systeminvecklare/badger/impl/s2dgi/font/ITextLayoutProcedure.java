package com.github.systeminvecklare.badger.impl.s2dgi.font;

import java.util.Collection;

public interface ITextLayoutProcedure {
	<C extends Collection<String>> C layoutText(String text, C result);
	
	public static ITextLayoutProcedure maxWidth(IFont font, int maxWidth) {
		return new LineSpittingTextLayoutProcedure(new LineSpittingTextLayoutProcedure.ILineConstraints() {
			@Override
			public boolean fits(CharSequence line) {
				return font.complile(line.toString()).getWidth() <= maxWidth;
			}
		});
	}
}
