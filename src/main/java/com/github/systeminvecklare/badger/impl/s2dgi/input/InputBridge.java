package com.github.systeminvecklare.badger.impl.s2dgi.input;

import com.github.systeminvecklare.badger.core.graphics.framework.engine.inputprocessor.IInputHandler;

import net.pointlessgames.libs.s2dgi.key.IKeyListener;
import net.pointlessgames.libs.s2dgi.mouse.IMouseListener;

public class InputBridge implements IKeyListener, IMouseListener {
	private final IInputHandler inputHandler;

	public InputBridge(IInputHandler inputHandler) {
		this.inputHandler = inputHandler;
	}

	@Override
	public void onPressed(int x, int y, MouseButton button) {
		inputHandler.registerPointerDown(x, y, 0, convertMouseButton(button));
		
	}

	private static int convertMouseButton(MouseButton button) {
		if(button == MouseButton.BUTTON_1) {
			return 1;
		} else if(button == MouseButton.BUTTON_2) {
			return 2;
		} else if(button == MouseButton.BUTTON_3) {
			return 3;
		} else {
			return -1;
		}
	}

	@Override
	public void onReleased(int x, int y, MouseButton button) {
		inputHandler.registerPointerUp(x, y, 0, convertMouseButton(button));
	}

	@Override
	public void onMoved(int x, int y) {
	}

	@Override
	public void onDragged(int x, int y) {
		inputHandler.registerPointerDragged(x, y, 0);		
	}

	@Override
	public void onScroll(int x, int y, int steps) {
	}

	@Override
	public void onKeyTyped(char c) {
		inputHandler.registerKeyTyped(c);
	}

	@Override
	public void onKeyPressed(int keyCode) {
		inputHandler.registerKeyDown(keyCode);
	}

	@Override
	public void onKeyReleased(int keyCode) {
		inputHandler.registerKeyUp(keyCode);
	}

}
