package com.glitchcog.starnom.agent;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import com.glitchcog.starnom.StarNom;

/**
 * A star that flies through the sky in an arc to potentially be eaten by the guy
 * 
 * @author Matt Yanos
 */
public class Star extends Agent
{
    /**
     * The largest a Star can be
     */
    public static final int MAX_SIZE = 20;

    /**
     * How large is the star, which influences its physical size and how many points it is worth
     */
    private int size;

    /**
     * How long to delay the star's initialization
     */
    private int delay;

    /**
     * Variation in the star's size
     */
    private static int sizeVariation = 10;

    /**
     * The color to draw the star
     */
    private static Color[] COLORS = new Color[] { Color.PINK, Color.CYAN.darker(), Color.ORANGE.brighter() };

    private Color color;

    /**
     * Construct a star for the given width and height boundaries
     * 
     * @param screenWidth
     * @param screenHeight
     */
    public Star(int screenWidth, int screenHeight)
    {
        reset(screenWidth, screenHeight);
    }

    /**
     * Get the size of this star
     * 
     * @return size
     */
    @Override
    public int getSize()
    {
        return size;
    }

    /**
     * Reset the star to shoot again
     * 
     * @param screenWidth
     * @param screenHeight
     */
    public void reset(int screenWidth, int screenHeight)
    {
        delay = StarNom.RND.nextInt(100);
        mo.pos.x = -100.0f;
        mo.pos.y = screenHeight - 175.0f - StarNom.RND.nextInt(85);
        mo.vel.x = 7.5f + StarNom.RND.nextInt(5);
        mo.vel.y = -7.0f - StarNom.RND.nextInt(3);
        mo.acc.x = 0.0f;
        mo.acc.y = 0.25f;
        color = COLORS[StarNom.RND.nextInt(COLORS.length)];
        size = MAX_SIZE - sizeVariation + StarNom.RND.nextInt(sizeVariation);
    }

    /**
     * Update the star logic, to be run once per update loop cycle
     * 
     * @param screenWidth
     * @param screenHeight
     */
    public void update(int screenWidth, int screenHeight)
    {
        if (delay > 0)
        {
            delay--;
            return;
        }
        if (mo.pos.x + size + 20 > screenWidth || mo.pos.y > screenHeight)
        {
            reset(screenWidth, screenHeight);
        }
        super.update();
    }

    /**
     * Draw the star
     */
    public void draw(Graphics2D g2d)
    {
        Star.draw(g2d, mo.pos.x, mo.pos.y, size, color);
    }

    /**
     * Render a star on the specified Graphics2D object with the specified position and size
     * 
     * @param g2d
     * @param x
     *            horizontal position coordinate
     * @param y
     *            vertical position coordinate
     * @param radius
     *            star size
     * @param color
     *            star color
     */
    private static void draw(Graphics2D g2d, float x, float y, int radius, Color color)
    {
        g2d.setStroke(new BasicStroke(radius / 5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        int[] xPts = new int[10];
        int[] yPts = new int[10];

        final float thirtySixDegrees = 0.62831853f;

        float theta;
        for (int i = 0; i < xPts.length; i++)
        {
            theta = (float) Math.PI * 3.0f / 2.0f + thirtySixDegrees * i;
            xPts[i] = (int) (x + Math.cos(theta) * radius / (i % 2 + 1));
            yPts[i] = (int) (y + Math.sin(theta) * radius / (i % 2 + 1));
        }

        g2d.setColor(color);
        g2d.fillPolygon(xPts, yPts, xPts.length);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(xPts, yPts, xPts.length);
    }
}
