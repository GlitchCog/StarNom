package com.glitchcog.starnom.input;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.glitchcog.starnom.agent.Agent;

public class MouseInput extends MouseAdapter
{
    private final float minSpeed = 48.0f;

    private final float maxSpeed = 72.0f;

    /**
     * Stores the point the mouse is first pressed
     */
    private Point mouseStart;

    /**
     * Keep track of how long the mouse button is held
     */
    private int mouseTimer;

    /**
     * The game Agent being thrown by this mouse input
     */
    private Agent projectile;

    /**
     * Construct a MouseInput object for the specified game Agent projectile
     * 
     * @param projectile
     */
    public MouseInput(Agent projectile)
    {
        this.projectile = projectile;
    }

    public void update()
    {
        if (mouseStart != null)
        {
            mouseTimer++;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        // Make sure the mouse press was originally over the object
        if (mouseStart != null)
        {
            // And set the velocity accordingly
            projectile.mo.vel.x = (int) ((e.getPoint().getX() - mouseStart.getX()) / (float) mouseTimer);
            projectile.mo.vel.y = (int) ((e.getPoint().getY() - mouseStart.getY()) / (float) mouseTimer);

            final float totalSpeed = Math.abs(projectile.mo.vel.x) + Math.abs(projectile.mo.vel.y);

            if (totalSpeed > 0.0f)
            {
                float speed = (float) Math.sqrt((projectile.mo.vel.x * projectile.mo.vel.x) + (projectile.mo.vel.y * projectile.mo.vel.y));
                if (speed > maxSpeed)
                {
                    projectile.mo.vel.x = maxSpeed * projectile.mo.vel.x / totalSpeed;
                    projectile.mo.vel.y = maxSpeed * projectile.mo.vel.y / totalSpeed;
                }
                else if (speed < minSpeed)
                {
                    projectile.mo.vel.x = minSpeed * projectile.mo.vel.x / totalSpeed;
                    projectile.mo.vel.y = minSpeed * projectile.mo.vel.y / totalSpeed;
                }
            }
            mouseStart = null;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (mouseStart == null)
        {
            if (isOnProjectile(e))
            {
                mouseStart = e.getPoint();
            }
            else
            {
                mouseStart = null;
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        mouseReleased(e);
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        mouseTimer = 0;
    }

    private boolean isOnProjectile(MouseEvent e)
    {
        // Check to make sure the mouse is over the guy
        float dx = (float) e.getPoint().getX() - projectile.mo.pos.x;
        float dy = (float) e.getPoint().getY() - projectile.mo.pos.y;
        float distanceSquared = dx * dx + dy * dy;

        return (distanceSquared < projectile.getSize() * projectile.getSize());
    }

}
