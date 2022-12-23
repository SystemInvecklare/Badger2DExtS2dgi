package com.github.systeminvecklare.badger.impl.s2dgi.input;

import com.github.systeminvecklare.badger.core.graphics.components.core.IIntSource;
import com.github.systeminvecklare.badger.core.graphics.components.scene.IScene;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.inputprocessor.FlashyInputHandler;
import com.github.systeminvecklare.badger.impl.s2dgi.FlashyS2dgiEngine;

public class ExtraFlashyInputHandler extends FlashyInputHandler {
	private final HoverCollector hoverCollector;

	public ExtraFlashyInputHandler(IIntSource heightSource) {
		super(heightSource);
		this.hoverCollector = new HoverCollector();
	}

	@Override
	public void handleInputs(IScene scene) {
		hoverCollector.beginHover(FlashyS2dgiEngine.get().getMouseX(), FlashyS2dgiEngine.get().getMouseY());
		scene.visitLayers(hoverCollector);
		hoverCollector.endHover();
		super.handleInputs(scene);
	}
}
