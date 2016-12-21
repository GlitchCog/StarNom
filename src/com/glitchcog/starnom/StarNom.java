package com.glitchcog.starnom;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.glitchcog.starnom.agent.Guy;
import com.glitchcog.starnom.agent.Star;
import com.glitchcog.starnom.input.MouseInput;
import com.glitchcog.starnom.physics.Vector;

/**
 * The game and render loop logic, and the game logic for StarNom
 * 
 * @author Matt Yanos
 */
public class StarNom extends JPanel
{
    private static final long serialVersionUID = 1L;

    /**
     * The constant width of the game screen in pixels
     */
    public static final int SCREEN_WIDTH = 640;

    /**
     * The constant height of the game screen in pixels
     */
    public static final int SCREEN_HEIGHT = 480;

    /**
     * Used to add randomness to launching stars and to randomize the guy's speech
     */
    public static final Random RND = new Random();

    /**
     * For the guy's speech bubble
     */
    private static final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 16);

    /**
     * Keep score
     */
    private int points;

    /**
     * Keep track of a small delay by counting down before the guy speaks the instructions at the start of the game
     */
    private int startTimer;

    /**
     * The delay in milliseconds to wait before the guy speaks the instructions when the game begins
     */
    private static final int START_TIMER_MAX = 90;

    /**
     * Keep track of game-time to regulate the update/draw loop. It's generally not a good idea to use a Swing timer for
     * game loop timing, but this game is simple enough that it doesn't really matter.
     */
    private Timer clock;

    /**
     * The guy who noms the stars
     */
    private Guy guy;

    /**
     * The stars that the guy noms
     */
    private Star[] stars;

    /**
     * Coefficient of friction for the guy when he bounces
     */
    public static final float FRICTION = 5.0f;

    /**
     * Gravitational coefficient that pulls everything downward
     */
    public static final float GRAVITY = 3.0f;

    /**
     * The number of stars to keep in the pool
     */
    private static final int STAR_BATCH_SIZE = 16;

    /**
     * Handle all the player input via a MouseAdapter
     */
    private MouseInput input;

    /**
     * The color of the sky
     */
    private static final Color SKY_COLOR = Color.PINK.darker().darker();

    public StarNom()
    {
        initializeAgents();

        input = new MouseInput(guy);
        addMouseListener(input);
        addMouseMotionListener(input);

        clock = new Timer(24, new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                update();
                repaint();
            }
        });
    }

    public void reset()
    {
        startTimer = START_TIMER_MAX;
        points = 0;
        guy.reset(Math.max(getWidth(), SCREEN_WIDTH), Math.max(getHeight(), SCREEN_HEIGHT));
        for (int i = 0; i < stars.length; i++)
            stars[i].reset(getWidth(), getHeight());
    }

    /**
     * Construct the agents to set up the game
     */
    private void initializeAgents()
    {
        guy = new Guy(SCREEN_WIDTH, SCREEN_HEIGHT);

        stars = new Star[STAR_BATCH_SIZE];
        for (int i = 0; i < stars.length; i++)
        {
            stars[i] = new Star(SCREEN_WIDTH, SCREEN_HEIGHT);
        }
    }

    public Guy getGuy()
    {
        return guy;
    }

    /**
     * Kick off the gameplay
     */
    public void start()
    {
        reset();
        clock.start();
    }

    /**
     * Game update logic, to be run once per update loop cycle
     */
    private void update()
    {
        input.update();

        if (checkStarCollisions(guy.mo.pos))
            guy.eat();

        guy.update(getWidth(), getHeight());

        for (int i = 0; i < stars.length; i++)
            stars[i].update(getWidth(), getHeight());

        if (startTimer > 0)
        {
            startTimer--;
            if (startTimer == 0)
            {
                guy.lookUp();
                guy.talk("Ooo, look up there!\nThrow me with your mouse pointer\nso I can catch all these yummy stars!");
            }
        }
    }

    /**
     * Determine whether the guy has collided with any stars, indicating that the guy should eat it
     * 
     * @param guyPos
     * @return hit
     */
    private boolean checkStarCollisions(Vector guyPos)
    {
        boolean hit = false;
        float colDist;
        for (int i = 0; i < stars.length; i++)
        {
            colDist = guy.distanceSqrd(stars[i]);
            if (colDist < guy.getSize() * guy.getSize() + stars[i].getSize() * stars[i].getSize())
            {
                points += stars[i].getSize() * 10 / Star.MAX_SIZE;
                stars[i].reset(getWidth(), getHeight());
                hit = true;
            }
        }
        return hit;
    }

    /**
     * The size of the stroke to use to draw lines
     */
    public static final float STROKE_SIZE = 4.0f;

    /**
     * The stroke to use to draw lines
     */
    protected static final Stroke STROKE = new BasicStroke(STROKE_SIZE, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    /**
     * The render logic
     */
    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(StarNom.FONT);

        // Clear screen
        g2d.setColor(SKY_COLOR);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(STROKE);

        // Draw border
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, -10, getWidth() - 1, getHeight() + 10 - 1);

        guy.draw(g2d);

        for (int i = 0; i < stars.length; i++)
            stars[i].draw(g2d);

        g2d.drawString("SCORE: " + points, STROKE_SIZE * 2, g2d.getFontMetrics().getHeight());
    }

}
