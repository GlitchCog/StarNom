package com.glitchcog.starnom.physics;

/**
 * A two-dimensional vector
 * 
 * @author Matt Yanos
 */
public class Vector
{
    /**
     * The horizontal component
     */
    public float x;

    /**
     * The vertical component
     */
    public float y;

    /**
     * Construct a zero vector
     */
    public Vector()
    {
        this(0.0f, 0.0f);
    }

    /**
     * Construct a vector with the specified component values
     * 
     * @param x
     * @param y
     */
    public Vector(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Reset the vector to a zero vector
     */
    public void reset()
    {
        x = 0.0f;
        y = 0.0f;
    }

    /**
     * Get the distance squared from the other specified vector
     * 
     * @param other
     * @return distance squared
     */
    public float distanceSqrd(Vector other)
    {
        float dx = other.x - x;
        float dy = other.y - y;
        return dx * dx + dy * dy;
    }
}
