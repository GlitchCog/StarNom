package com.glitchcog.starnom.agent;

import java.awt.Graphics2D;

import com.glitchcog.starnom.physics.Motion;
import com.glitchcog.starnom.physics.Vector;

/**
 * Something in the game with two-dimensional position, velocity, and acceleration
 * 
 * @author Matt Yanos
 */
public abstract class Agent
{
    /**
     * The agent's position, velocity, and acceleration
     */
    public Motion mo;

    /**
     * Construct an agent with default motion
     */
    public Agent()
    {
        this(new Motion());
    }

    /**
     * Construct an agent with the specified motion
     * 
     * @param mo
     */
    public Agent(Motion mo)
    {
        this.mo = mo;
    }

    /**
     * Construct an agent with the specified motion components
     * 
     * @param pos
     *            position
     * @param vel
     *            velocity
     * @param acc
     *            acceleration
     */
    public Agent(Vector pos, Vector vel, Vector acc)
    {
        this(new Motion(pos, vel, acc));
    }

    /**
     * Update the agent's motion
     */
    public void update()
    {
        // Add acceleration to velocity
        mo.vel.x += mo.acc.x;
        mo.vel.y += mo.acc.y;

        // Add velocity to position
        mo.pos.x += mo.vel.x;
        mo.pos.y += mo.vel.y;
    }

    /**
     * Get the distance squared from another agent
     * 
     * @param other
     * @return distance squared
     */
    public float distanceSqrd(Agent other)
    {
        return mo.distanceSqrd(other.mo.pos);
    }

    /**
     * Get the size of the agent
     * 
     * @return size
     */
    public abstract int getSize();

    /**
     * Render the agent on the specified Graphics2D object
     * 
     * @param g2d
     */
    public abstract void draw(Graphics2D g2d);
}
