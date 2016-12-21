package com.glitchcog.starnom.physics;

/**
 * A collection of two-dimensional vectors that represent position, velocity, and acceleration
 * 
 * @author Matt Yanos
 */
public class Motion
{
    /**
     * Position
     */
    public Vector pos;

    /**
     * Velocity
     */
    public Vector vel;

    /**
     * Acceleration
     */
    public Vector acc;

    /**
     * Construct a motion object with zero vectors for all its components
     */
    public Motion()
    {
        this(new Vector(), new Vector(), new Vector());
    }

    /**
     * Construct a motion object with the specified components
     * 
     * @param pos
     *            position
     * @param vel
     *            velocity
     * @param acc
     *            acceleration
     */
    public Motion(Vector pos, Vector vel, Vector acc)
    {
        this.pos = pos;
        this.vel = vel;
        this.acc = acc;
    }

    /**
     * Get the distance squared from this motion's position to the specified vector
     * 
     * @param other
     * @return distance squared
     */
    public float distanceSqrd(Vector other)
    {
        return pos.distanceSqrd(other);
    }
}
