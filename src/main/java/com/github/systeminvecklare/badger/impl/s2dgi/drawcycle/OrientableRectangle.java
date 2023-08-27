package com.github.systeminvecklare.badger.impl.s2dgi.drawcycle;

import com.github.systeminvecklare.badger.core.math.Mathf;
import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolable;
import com.github.systeminvecklare.badger.core.widget.IRectangle;

public class OrientableRectangle implements IRectangle, IPoolable {
	private final IPool<? super OrientableRectangle> pool;
	private final IntVector position = new IntVector(null).setTo(0, 0);
	private final IntVector corner = new IntVector(null).setTo(0, 0);
	private final IntVector sVector = new IntVector(null).setTo(1, 0);
	private final IntVector tVector = new IntVector(null).setTo(0, 1);
	private boolean flipX;
	private boolean flipY;
	
	public OrientableRectangle(IPool<? super OrientableRectangle> pool) {
		this.pool = pool;
	}
	
	public OrientableRectangle setTo(OrientableRectangle other) {
		this.position.setTo(other.position);
		this.corner.setTo(other.corner);
		this.flipX = other.flipX;
		this.flipY = other.flipY;
		this.sVector.setTo(other.sVector);
		this.tVector.setTo(other.tVector);
		return this;
	}
	
	
	public OrientableRectangle setTo(int x, int y, int width, int height, int quarterRotations, boolean flipX,
			boolean flipY) {
		this.position.setTo(x, y);
		this.corner.setTo(position).add(width, height);
		this.flipX = flipX;
		this.flipY = flipY;
		setQuarterRotations(quarterRotations);
		return this;
	}
	
	private void setQuarterRotations(int quarterRotations) {
		sVector.setTo(flipX ? -1 : 1, 0);
		tVector.setTo(0, flipY ? -1 : 1);
		int qr = Mathf.mod(quarterRotations, 4);
		if(qr != 0) {
			sVector.rotate(quarterRotations);
			tVector.rotate(quarterRotations);
			if(qr <= 2) {
				flipPosAndCornerX();
			}
			if(qr >= 2) {
				flipPosAndCornerY();
			}
		}
	}

	public int getQuarterRotations() {
		if(sVector.getIntX() > 0) {
			if(tVector.getIntY() > 0) {
				if(flipX && flipY) {
					return 2;
				} else {
					return 0;
				}
			} else {
				if(flipY) {
					return 0;
				} else {
					return 2;
				}
			}
		} else if(sVector.getIntX() < 0) {
			if(tVector.getIntY() < 0) {
				if(flipX && flipY) {
					return 0;
				} else {
					return 2;
				}
			} else {
				if(flipX) {
					return 0;
				} else {
					return 2;
				}
			}
		} else if(sVector.getIntY() > 0) {
			if(tVector.getIntX() < 0) {
				if(flipX && flipY) {
					return 3;
				} else {
					return 1;
				}
			} else {
				if(flipX) {
					return 3;
				} else {
					return 1;
				}
			}
		} else { // sVector.getIntY() < 0
			if(tVector.getIntX() > 0) {
				if(flipX && flipY) {
					return 1;
				} else {
					return 3;
				}
			} else {
				if(flipX) {
					return 1;
				} else {
					return 3;
				}
			}
		}
	}
	
	public boolean getFlipX() {
		return flipX;
	}
	
	public boolean getFlipY() {
		return flipY;
	}

	@Override
	public int getX() {
		return Math.min(position.getIntX(), corner.getIntX());
	}

	@Override
	public int getY() {
		return Math.min(position.getIntY(), corner.getIntY());
	}

	@Override
	public int getWidth() {
		return Math.max(position.getIntX(), corner.getIntX()) - getX();
	}

	@Override
	public int getHeight() {
		return Math.max(position.getIntY(), corner.getIntY()) - getY();
	}
	
	public OrientableRectangle scale(IReadableIntVector scale) {
		return scale(scale.getIntX(), scale.getIntY());
	}
	
	private void flipPosAndCornerX() {
		int temp = this.position.getIntX();
		this.position.setTo(this.corner.getIntX(), this.position.getIntY());
		this.corner.setTo(temp, this.corner.getIntY());
	}
	
	private void flipPosAndCornerY() {
		int temp = this.position.getIntY();
		this.position.setTo(this.position.getIntX(), this.corner.getIntY());
		this.corner.setTo(this.corner.getIntX(), temp);
	}

	public OrientableRectangle scale(int sx, int sy) {
		this.position.scale(sx, sy);
		this.corner.scale(sx, sy);
		
		if(sx < 0) {
			sVector.scale(-1, 1);
			tVector.scale(-1, 1);
			this.flipX = !this.flipX;
			flipPosAndCornerX();
		}
		
		if(sy < 0) {
			sVector.scale(1, -1);
			tVector.scale(1, -1);
			this.flipY = !this.flipY;
			flipPosAndCornerY();
		}
		
		return this;
	}

	public OrientableRectangle rotate(int quarterRotations) {
		position.rotate(quarterRotations);
		corner.rotate(quarterRotations);
		sVector.rotate(quarterRotations);
		tVector.rotate(quarterRotations);
		return this;
	}

	public OrientableRectangle add(IReadableIntVector offset) {
		return add(offset.getIntX(), offset.getIntY());
	}
	
	public OrientableRectangle add(int offsetX, int offsetY) {
		this.position.add(offsetX, offsetY);
		this.corner.add(offsetX, offsetY);
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(")
			.append(getX())
			.append(", ")
			.append(getY())
			.append(", ")
			.append(getWidth())
			.append(", ")
			.append(getHeight())
			.append("; ")
			.append(getQuarterRotations())
			.append(", ")
			.append(getFlipX())
			.append(", ")
			.append(getFlipY())
		.append(")");
		return builder.toString();
	}
	
	public OrientableRectangle setFlipX(boolean flipX) {
		return setTo(getX(), getY(), getWidth(), getHeight(), getQuarterRotations(), flipX, getFlipY());
	}
	
	public OrientableRectangle setFlipY(boolean flipY) {
		return setTo(getX(), getY(), getWidth(), getHeight(), getQuarterRotations(), getFlipX(), flipY);
	}
	
	@Override
	public void free() {
		pool.free(this);
	}
	
	//TODO convert to unit tests
//	private static void assertEqual(int expected, int actual, String messageTemplate) {
//		if(expected != actual) {
//			throw new RuntimeException(messageTemplate.replace("%e", String.valueOf(expected)).replace("%a", String.valueOf(actual)));
//		}
//	}
//	
//	public static void main(String[] args) {
//		for(int flipX = 0; flipX < 2; ++flipX) {
//			for(int flipY = 0; flipY < 2; ++flipY) {
//				for(int rot = 0; rot < 4; ++rot) {
//					OrientableRectangle rectangle = new OrientableRectangle(null).setTo(0, 0, 1, 1, 0, false, false);
//					rectangle.scale(flipX == 0 ? 1 : -1, flipY == 0 ? 1 : -1).rotate(rot);
//					assertEqual(rot, rectangle.getQuarterRotations(), "Expected rot to be %e, but was %a for %fx %fy".replace("%fx", flipX == 0 ? "" : "flipX").replace("%fy", flipY == 0 ? "" : "flipY"));
//				}
//			}
//		}
//	}
}
