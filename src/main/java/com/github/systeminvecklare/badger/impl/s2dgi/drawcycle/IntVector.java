package com.github.systeminvecklare.badger.impl.s2dgi.drawcycle;

import com.github.systeminvecklare.badger.core.math.AbstractVector;
import com.github.systeminvecklare.badger.core.math.Mathf;
import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolable;

public class IntVector extends AbstractVector implements IReadableIntVector, IPoolable {
	private final IPool<? super IntVector> pool;
	private int x;
	private int y;
	
	public IntVector(IPool<? super IntVector> pool) {
		this.pool = pool;
	}
	
	public IntVector setTo(IReadableIntVector other) {
		this.x = other.getIntX();
		this.y = other.getIntY();
		return this;
	}
	
	public IntVector setTo(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public IntVector scale(int scaleX, int scaleY) {
		this.x *= scaleX;
		this.y *= scaleY;
		return this;
	}
	
	public IntVector scale(IReadableIntVector scale) {
		return scale(scale.getIntX(), scale.getIntY());
	}
	
	public IntVector scale(int scale) {
		return scale(scale, scale);
	}
	
	public IntVector add(IReadableIntVector other) {
		return add(other.getIntX(), other.getIntY());
	}
	
	public IntVector add(int x, int y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	public IntVector sub(IReadableIntVector other) {
		return sub(other.getIntX(), other.getIntY());
	}
	
	public IntVector sub(int x, int y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	@Override
	public int getIntX() {
		return x;
	}
	
	@Override
	public int getIntY() {
		return y;
	}
	
	@Override
	public float getX() {
		return getIntX();
	}
	@Override
	public float getY() {
		return getIntY();
	}

	public IntVector rotate(int quarterRotations) {
		int qr = Mathf.mod(quarterRotations, 4);
		if(qr == 1) {
			int temp = this.x;
			this.x = -this.y;
			this.y = temp;
		} else if(qr == 2) {
			this.x = -this.x;
			this.y = -this.y;
		} else if(qr == 3) {
			int temp = this.x;
			this.x = this.y;
			this.y = -temp;
		}
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(").append(x).append(", ").append(y).append(")");
		return builder.toString();
	}
	
	@Override
	public void free() {
		pool.free(this);
	}
}
