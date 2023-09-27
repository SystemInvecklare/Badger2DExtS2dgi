package com.github.systeminvecklare.badger.impl.s2dgi.drawcycle;

import org.junit.Assert;
import org.junit.Test;

public class OrientableRectangleTest {
	private static void assertRect(String expectedString, OrientableRectangle rectangle) {
		StringBuilder builder = new StringBuilder();
		builder.append("(")
			.append(rectangle.getX())
			.append(", ")
			.append(rectangle.getY())
			.append(", ")
			.append(rectangle.getWidth())
			.append(", ")
			.append(rectangle.getHeight())
			.append("; ")
			.append(rectangle.getQuarterRotations())
			.append(", ")
			.append(rectangle.getFlipX())
			.append(", ")
			.append(rectangle.getFlipY())
		.append(")");
		Assert.assertEquals(expectedString, builder.toString());
	}
	
	@Test
	public void testSimple() {
		OrientableRectangle orientableRectangle = new OrientableRectangle(null);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, false, false);
		orientableRectangle.scale(1, 1);
		assertRect("(5, 7, 10, 22; 0, false, false)", orientableRectangle);

		orientableRectangle.setTo(5, 7, 10, 22, 0, false, false);
		orientableRectangle.scale(-1, 1);
		assertRect("(-15, 7, 10, 22; 0, true, false)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, false, false);
		orientableRectangle.scale(1, -1);
		assertRect("(5, -29, 10, 22; 0, false, true)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, false, false);
		orientableRectangle.scale(-1, -1);
		assertRect("(-15, -29, 10, 22; 0, true, true)", orientableRectangle);
	}
	
	@Test
	public void testSetQuarterRotations() {
		OrientableRectangle orientableRectangle = new OrientableRectangle(null);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, false, false);
		orientableRectangle.rotate(0);
		assertRect("(5, 7, 10, 22; 0, false, false)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, false, false);
		orientableRectangle.rotate(1);
		assertRect("(-29, 5, 22, 10; 1, false, false)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, false, false);
		orientableRectangle.rotate(2);
		assertRect("(-15, -29, 10, 22; 2, false, false)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, false, false);
		orientableRectangle.rotate(3);
		assertRect("(7, -15, 22, 10; 3, false, false)", orientableRectangle);
	}
	
	@Test
	public void testFlipX() {
		OrientableRectangle orientableRectangle = new OrientableRectangle(null);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, true, false);
		orientableRectangle.scale(-1, 1);
		assertRect("(-15, 7, 10, 22; 0, false, false)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 1, false, false);
		orientableRectangle.scale(-1, 1);
		assertRect("(-15, 7, 10, 22; 3, true, false)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 2, false, false);
		orientableRectangle.scale(-1, 1);
		assertRect("(-15, 7, 10, 22; 2, true, false)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 3, false, false);
		orientableRectangle.scale(-1, 1);
		assertRect("(-15, 7, 10, 22; 1, true, false)", orientableRectangle);
	}
	
	@Test
	public void testFlipY() {
		OrientableRectangle orientableRectangle = new OrientableRectangle(null);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, false, false);
		orientableRectangle.scale(1, -1);
		assertRect("(5, -29, 10, 22; 0, false, true)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 1, false, false);
		orientableRectangle.scale(1, -1);
		assertRect("(5, -29, 10, 22; 3, false, true)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 1, false, true);
		orientableRectangle.scale(1, -1);
		assertRect("(5, -29, 10, 22; 3, false, false)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 2, false, false);
		orientableRectangle.scale(1, -1);
		assertRect("(5, -29, 10, 22; 2, false, true)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 2, false, true);
		orientableRectangle.scale(1, -1);
		assertRect("(5, -29, 10, 22; 2, false, false)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 2, true, false);
		orientableRectangle.scale(1, -1);
		assertRect("(5, -29, 10, 22; 2, true, true)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 3, true, false);
		orientableRectangle.scale(1, -1);
		assertRect("(5, -29, 10, 22; 1, true, true)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 3, true, true);
		orientableRectangle.scale(1, -1);
		assertRect("(5, -29, 10, 22; 1, true, false)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 3, false, true);
		orientableRectangle.scale(1, -1);
		assertRect("(5, -29, 10, 22; 1, false, false)", orientableRectangle);
	}
	
	@Test
	public void testFlipXAndY() {
		OrientableRectangle orientableRectangle = new OrientableRectangle(null);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, false, false);
		orientableRectangle.scale(-1, -1);
		assertRect("(-15, -29, 10, 22; 0, true, true)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, true, false);
		orientableRectangle.scale(-1, -1);
		assertRect("(-15, -29, 10, 22; 0, false, true)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, false, true);
		orientableRectangle.scale(-1, -1);
		assertRect("(-15, -29, 10, 22; 0, true, false)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, true, true);
		orientableRectangle.scale(-1, -1);
		assertRect("(-15, -29, 10, 22; 0, false, false)", orientableRectangle);
		
		//
		
		orientableRectangle.setTo(5, 7, 10, 22, 1, false, false);
		orientableRectangle.scale(-1, -1);
		assertRect("(-15, -29, 10, 22; 1, true, true)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 1, true, false);
		orientableRectangle.scale(-1, -1);
		assertRect("(-15, -29, 10, 22; 1, false, true)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 1, false, true);
		orientableRectangle.scale(-1, -1);
		assertRect("(-15, -29, 10, 22; 1, true, false)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 1, true, true);
		orientableRectangle.scale(-1, -1);
		assertRect("(-15, -29, 10, 22; 1, false, false)", orientableRectangle);
		
		//
		
		orientableRectangle.setTo(5, 7, 10, 22, 2, false, false);
		orientableRectangle.scale(-1, -1);
		assertRect("(-15, -29, 10, 22; 2, true, true)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 2, true, false);
		orientableRectangle.scale(-1, -1);
		assertRect("(-15, -29, 10, 22; 2, false, true)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 2, false, true);
		orientableRectangle.scale(-1, -1);
		assertRect("(-15, -29, 10, 22; 2, true, false)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 2, true, true);
		orientableRectangle.scale(-1, -1);
		assertRect("(-15, -29, 10, 22; 2, false, false)", orientableRectangle);
		
		//
		
		orientableRectangle.setTo(5, 7, 10, 22, 3, false, false);
		orientableRectangle.scale(-1, -1);
		assertRect("(-15, -29, 10, 22; 3, true, true)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 3, true, false);
		orientableRectangle.scale(-1, -1);
		assertRect("(-15, -29, 10, 22; 3, false, true)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 3, false, true);
		orientableRectangle.scale(-1, -1);
		assertRect("(-15, -29, 10, 22; 3, true, false)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 3, true, true);
		orientableRectangle.scale(-1, -1);
		assertRect("(-15, -29, 10, 22; 3, false, false)", orientableRectangle);
	}
	
	@Test
	public void testRotation() {
		OrientableRectangle orientableRectangle = new OrientableRectangle(null);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, false, false);
		orientableRectangle.rotate(1);
		assertRect("(-29, 5, 22, 10; 1, false, false)", orientableRectangle);

		orientableRectangle.setTo(5, 7, 10, 22, 0, true, false);
		orientableRectangle.rotate(1);
		assertRect("(-29, 5, 22, 10; 1, true, false)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, false, true);
		orientableRectangle.rotate(1);
		assertRect("(-29, 5, 22, 10; 1, false, true)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, true, true);
		orientableRectangle.rotate(1);
		assertRect("(-29, 5, 22, 10; 1, true, true)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, false, false);
		orientableRectangle.rotate(2);
		assertRect("(-15, -29, 10, 22; 2, false, false)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, true, false);
		orientableRectangle.rotate(2);
		assertRect("(-15, -29, 10, 22; 2, true, false)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, false, true);
		orientableRectangle.rotate(2);
		assertRect("(-15, -29, 10, 22; 2, false, true)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, true, true);
		orientableRectangle.rotate(2);
		assertRect("(-15, -29, 10, 22; 2, true, true)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, false, false);
		orientableRectangle.rotate(3);
		assertRect("(7, -15, 22, 10; 3, false, false)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, true, false);
		orientableRectangle.rotate(3);
		assertRect("(7, -15, 22, 10; 3, true, false)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, false, true);
		orientableRectangle.rotate(3);
		assertRect("(7, -15, 22, 10; 3, false, true)", orientableRectangle);
		
		orientableRectangle.setTo(5, 7, 10, 22, 0, true, true);
		orientableRectangle.rotate(3);
		assertRect("(7, -15, 22, 10; 3, true, true)", orientableRectangle);
	}
	
	@Test
	public void testComplicated() {
		OrientableRectangle orientableRectangle = new OrientableRectangle(null);
		
		orientableRectangle.setTo(5, 7, 10, 22, 1, true, false);
		orientableRectangle.rotate(1);
		orientableRectangle.scale(-1, 1);
		assertRect("(7, 5, 22, 10; 2, false, false)", orientableRectangle);
	}
}
