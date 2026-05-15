package com.Liamengine.Engine.Utils;

import com.Liamengine.Engine.Components.Collison;
import com.Liamengine.Engine.Components.Vector;
import static org.junit.Assert.*;
import org.junit.Test;

public class CollisonUtilsTest {

    private static final float DELTA = 0.001f;

    @Test
    public void intersectingLinesReturnsCollisionWithPoint() {
        Collison result = CollisonUtils.CheckForLineHits(
            new Vector(0, 0), new Vector(10, 10),
            new Vector(0, 10), new Vector(10, 0)
        );
        assertTrue(result.ISHIT);
        assertEquals(5f, result.HITLOCATION.getX(), DELTA);
        assertEquals(5f, result.HITLOCATION.getY(), DELTA);
    }

    @Test
    public void nonIntersectingLinesReturnsNoHit() {
        Collison result = CollisonUtils.CheckForLineHits(
            new Vector(0, 0), new Vector(1, 0),
            new Vector(0, 2), new Vector(1, 2)
        );
        assertFalse(result.ISHIT);
    }

    @Test
    public void parallelLinesReturnsNoHit() {
        Collison result = CollisonUtils.CheckForLineHits(
            new Vector(0, 0), new Vector(10, 0),
            new Vector(0, 5), new Vector(10, 5)
        );
        assertFalse(result.ISHIT);
    }

    @Test
    public void touchingAtEndpointReturnsHit() {
        Collison result = CollisonUtils.CheckForLineHits(
            new Vector(0, 0), new Vector(10, 10),
            new Vector(10, 10), new Vector(20, 0)
        );
        assertTrue(result.ISHIT);
        assertEquals(10f, result.HITLOCATION.getX(), DELTA);
        assertEquals(10f, result.HITLOCATION.getY(), DELTA);
    }

    @Test
    public void horizontalAndVerticalLinesIntersect() {
        Collison result = CollisonUtils.CheckForLineHits(
            new Vector(0, 5), new Vector(10, 5),
            new Vector(5, 0), new Vector(5, 10)
        );
        assertTrue(result.ISHIT);
        assertEquals(5f, result.HITLOCATION.getX(), DELTA);
        assertEquals(5f, result.HITLOCATION.getY(), DELTA);
    }

    @Test
    public void linesBeforeStartReturnNoHit() {
        Collison result = CollisonUtils.CheckForLineHits(
            new Vector(10, 10), new Vector(20, 20),
            new Vector(0, 10), new Vector(10, 0)
        );
        assertFalse(result.ISHIT);
    }

    @Test
    public void collisonDefaultsToNoHit() {
        Collison c = new Collison();
        assertFalse(c.ISHIT);
        assertEquals(0f, c.HITLOCATION.getX(), DELTA);
        assertEquals(0f, c.HITLOCATION.getY(), DELTA);
    }
}
