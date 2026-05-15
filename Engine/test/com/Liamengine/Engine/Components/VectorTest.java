package com.Liamengine.Engine.Components;

import static org.junit.Assert.*;
import org.junit.Test;

public class VectorTest {

    private static final float DELTA = 0.001f;

    @Test
    public void zeroReturnsZeroVector() {
        Vector v = Vector.Zero();
        assertEquals(0f, v.getX(), DELTA);
        assertEquals(0f, v.getY(), DELTA);
    }

    @Test
    public void oneReturnsUnitVector() {
        Vector v = Vector.One();
        assertEquals(1f, v.getX(), DELTA);
        assertEquals(1f, v.getY(), DELTA);
    }

    @Test
    public void constructorSetsXY() {
        Vector v = new Vector(3f, 4f);
        assertEquals(3f, v.getX(), DELTA);
        assertEquals(4f, v.getY(), DELTA);
    }

    @Test
    public void copyConstructorCopiesValues() {
        Vector original = new Vector(5f, 7f);
        Vector copy = new Vector(original);
        assertEquals(5f, copy.getX(), DELTA);
        assertEquals(7f, copy.getY(), DELTA);
    }

    @Test
    public void addMutatesAndReturnsSelf() {
        Vector v = new Vector(1f, 2f);
        Vector result = v.add(new Vector(3f, 4f));
        assertSame(v, result);
        assertEquals(4f, v.getX(), DELTA);
        assertEquals(6f, v.getY(), DELTA);
    }

    @Test
    public void addtoCreatesNewVector() {
        Vector v = new Vector(1f, 2f);
        Vector result = v.addto(new Vector(3f, 4f));
        assertNotSame(v, result);
        assertEquals(4f, result.getX(), DELTA);
        assertEquals(6f, result.getY(), DELTA);
        assertEquals(1f, v.getX(), DELTA);
    }

    @Test
    public void multVectorScalesComponents() {
        Vector v = new Vector(2f, 3f);
        v.mult(new Vector(4f, 5f));
        assertEquals(8f, v.getX(), DELTA);
        assertEquals(15f, v.getY(), DELTA);
    }

    @Test
    public void multFloatScalesUniformly() {
        Vector v = new Vector(2f, 3f);
        v.mult(3f);
        assertEquals(6f, v.getX(), DELTA);
        assertEquals(9f, v.getY(), DELTA);
    }

    @Test
    public void lengthReturnsUnrootedMagnitude() {
        Vector v = new Vector(3f, 4f);
        assertEquals(25.0, v.Length(), DELTA);
    }

    @Test
    public void lengthsqrtReturnsRootedMagnitude() {
        Vector v = new Vector(3f, 4f);
        assertEquals(5.0, v.Lengthsqrt(), DELTA);
    }

    @Test
    public void normalizedReturnsUnitVector() {
        Vector v = new Vector(3f, 4f);
        Vector n = v.Normalized();
        assertEquals(0.6f, n.getX(), DELTA);
        assertEquals(0.8f, n.getY(), DELTA);
    }

    @Test
    public void normalizedOfZeroVectorReturnsNaN() {
        Vector v = Vector.Zero();
        Vector n = v.Normalized();
        assertTrue(Float.isNaN(n.getX()));
        assertTrue(Float.isNaN(n.getY()));
    }

    @Test
    public void constructorWithNaNDefaultsToZero() {
        Vector v = new Vector(Float.NaN, 5f);
        assertEquals(0f, v.getX(), DELTA);
        assertEquals(0f, v.getY(), DELTA);
    }

    @Test
    public void angleConstructorUsesPolarToCartesian() {
        Vector v = new Vector((float) Math.PI / 2);
        assertEquals(1f, v.getX(), DELTA);
        assertEquals(0f, v.getY(), DELTA);
    }

    @Test
    public void directionalVectorFromAngle() {
        Vector v = new Vector(0f, 0f);
        Vector d = v.DirectionalVector(0f, 5f);
        assertEquals(0f, d.getX(), DELTA);
        assertEquals(-5f, d.getY(), DELTA);
    }

    @Test
    public void setToVectorCopiesValues() {
        Vector a = new Vector(1f, 2f);
        Vector b = new Vector(3f, 4f);
        a.setToVector(b);
        assertEquals(3f, a.getX(), DELTA);
        assertEquals(4f, a.getY(), DELTA);
    }

    @Test
    public void isNaNReturnsTrueForNaNVector() {
        Vector v = new Vector(Float.NaN, Float.NaN);
        assertTrue(v.isNaN(v));
    }

    @Test
    public void isNaNReturnsFalseForValidVector() {
        Vector v = new Vector(1f, 2f);
        assertFalse(v.isNaN(v));
    }

    @Test
    public void addXAndAddYModifyComponents() {
        Vector v = new Vector(1f, 2f);
        v.addX(5f);
        v.addY(6f);
        assertEquals(6f, v.getX(), DELTA);
        assertEquals(8f, v.getY(), DELTA);
    }
}
