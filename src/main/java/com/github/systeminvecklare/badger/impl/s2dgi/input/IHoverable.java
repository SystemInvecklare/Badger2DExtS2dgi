package com.github.systeminvecklare.badger.impl.s2dgi.input;

import com.github.systeminvecklare.badger.core.math.IReadablePosition;

public interface IHoverable {
	void onHoverChanged(boolean hovered, IReadablePosition globalHoverPos);
	boolean consumesHover();
}
