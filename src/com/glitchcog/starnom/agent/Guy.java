package com.glitchcog.starnom.agent;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.StringTokenizer;

import com.glitchcog.starnom.StarNom;

/**
 * The guy that noms on the stars
 * 
 * @author Matt Yanos
 */
public class Guy extends Agent
{
    /**
     * The size of the guy
     */
    private final int radius;

    /**
     * What percentage of velocity should be preserved when bouncing
     */
    private float elasticity = 0.75f;

    /**
     * The color to draw the inside of the guy's mouth
     */
    public static Color MOUTH_COLOR = Color.RED.darker().darker().darker();

    /**
     * The count to set the hit count timer to when the guy hits a border
     */
    private static final int HIT_MAX = 24;

    /**
     * The count to set the floor count timer to when the guy hits the floor
     */
    private static final int FLOOR_MAX = 5;

    /**
     * The count to set the eat timer to when the guy eats
     */
    private static final int EAT_COUNT_MAX = 16;

    /**
     * Whether the guy is moving to the left
     */
    private boolean left;

    /**
     * Count timer for when the guy hits a border
     */
    private int hit;

    /**
     * Count timer for when the guy hits the floor
     */
    private int floor;

    /**
     * Count timer for when the guy eats
     */
    private int eatCount;

    /**
     * The phrase being spoken in the guy's speech bubble
     */
    public String speech = "";

    /**
     * The timer for how long the thing being spoken should be displayed
     */
    public int speechTimer;

    /**
     * Construct a guy to be initialized at the center of a screen of the specified dimensions
     * 
     * @param screenWidth
     * @param screenHeight
     */
    public Guy(int screenWidth, int screenHeight)
    {
        this.radius = 32;
        reset(screenWidth, screenHeight);
    }

    /**
     * Get the guy's radius
     * 
     * @return
     */
    @Override
    public int getSize()
    {
        return radius;
    }

    /**
     * Update the guy logic, to be run once per update loop cycle
     * 
     * @param screenWidth
     * @param screenHeight
     */
    public void update(int screenWidth, int screenHeight)
    {
        super.update();

        // Keep the guy on the screen, bouncing him around as appropriate
        enforceBoundaries(screenWidth, screenHeight);

        // Reduce the timers if any are active

        if (hit > 0)
        {
            hit--;
        }

        if (floor > 0)
        {
            floor--;
        }

        if (eatCount > 0)
        {
            eatCount--;
        }

        if (speechTimer > 0)
        {
            speechTimer--;
        }
    }

    /**
     * Phrases the guy can speak
     */
    private static final String[] EXCLAMATIONS = {
        "Mmmm!", "Delicious!", "Delectable!", 
        "Ooo... that one\nwas savory.", "Spicy!", 
        "Scrumptious!", "Tastey", "NOM NOM NOM", 
        "More!", "Tastacular!", "De-lish!", 
        "Crunchy!", "That one\nhit the spot!", 
        "Do I detect\na hint of helium?"
    };

    /**
     * Make the guy speak one of the random exclamations
     */
    private void talk()
    {
        talk(EXCLAMATIONS[StarNom.RND.nextInt(EXCLAMATIONS.length)]);
    }

    /**
     * Make the guy speak the specified speech text
     * 
     * @param speech
     *            the thing to make the guy say
     */
    public void talk(String speech)
    {
        this.speech = speech;
        this.speechTimer = speech.length() * 3;
    }

    /**
     * Modify the guy's position and velocity so boundaries are not traversed
     * 
     * @param width
     *            The width of the boundaries to enforce, typically the screen's width
     * @param height
     *            The height of the boundaries to enforce, typically the screen's height
     */
    private void enforceBoundaries(int width, int height)
    {
        // Keep the guy within the sides
        if (mo.pos.x < radius + StarNom.STROKE_SIZE)
        {
            mo.pos.x = radius + StarNom.STROKE_SIZE;
            mo.vel.x = -mo.vel.x * elasticity;
            hit = HIT_MAX;
            left = true;
        }
        else if (mo.pos.x > width - radius - StarNom.STROKE_SIZE)
        {
            mo.pos.x = width - radius - StarNom.STROKE_SIZE;
            mo.vel.x = -mo.vel.x * elasticity;
            hit = HIT_MAX;
            left = false;
        }

        // On the bottom of the screen
        if (mo.pos.y > height - radius - StarNom.STROKE_SIZE)
        {
            floor = FLOOR_MAX;

            mo.pos.y = height - radius - StarNom.STROKE_SIZE;

            mo.vel.y = -mo.vel.y * elasticity; // Absorb a quarter of the impact

            // Friction is annoying like this, because it always acts opposite the direction...
            if (mo.vel.x < 0.0)
            {
                mo.vel.x += StarNom.FRICTION;
                if (mo.vel.x > 0.0) // ...so you have to check if you passed it when incrementing
                    mo.vel.x = 0.0f;
            }
            if (mo.vel.x > 0.0)
            {
                mo.vel.x -= StarNom.FRICTION;
                if (mo.vel.x < 0.0)
                    mo.vel.x = 0.0f;
            }
        }
    }

    /**
     * Render a the guy on the specified Graphics2D object
     */
    public void draw(Graphics2D g2d)
    {
        g2d.setStroke(new BasicStroke(radius / 8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        g2d.setColor(Color.CYAN.darker().darker());
        g2d.fillRoundRect((int) (mo.pos.x - radius), (int) (mo.pos.y - radius), radius * 2, radius * 2, 25, 25);

        g2d.setColor(Color.ORANGE);
        g2d.fillRoundRect((int) (mo.pos.x - radius), (int) (mo.pos.y - radius), radius * 2, radius, 25, 25);

        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect((int) (mo.pos.x - radius), (int) (mo.pos.y - radius), radius * 2, radius * 2, 25, 25);

        drawFace(g2d);
    }

    /**
     * Render the guy's face
     * 
     * @param g2d
     */
    public void drawFace(Graphics2D g2d)
    {
        final int eyeSize;
        final int pupilSize = 6;
        final int mouthSize = 10;

        if (eatCount > 0)
        {
            eyeSize = radius / 16;
            g2d.setColor(Color.WHITE);
            g2d.fillOval((int) (mo.pos.x - radius / 2) - eyeSize / 2, (int) (mo.pos.y - radius / 2) - eyeSize / 2, eyeSize, eyeSize);
            g2d.fillOval((int) (mo.pos.x + radius / 2) - eyeSize / 2, (int) (mo.pos.y - radius / 2) - eyeSize / 2, eyeSize, eyeSize);
            g2d.setColor(Color.BLACK);
            g2d.drawOval((int) (mo.pos.x - radius / 2) - eyeSize / 2, (int) (mo.pos.y - radius / 2) - eyeSize / 2, eyeSize, eyeSize);
            g2d.drawOval((int) (mo.pos.x + radius / 2) - eyeSize / 2, (int) (mo.pos.y - radius / 2) - eyeSize / 2, eyeSize, eyeSize);

            int growMouthSize = eatCount * radius / 32 + 3;

            g2d.setColor(MOUTH_COLOR);
            g2d.fillOval((int) (mo.pos.x - growMouthSize / 2), (int) (mo.pos.y + radius / 2), growMouthSize, growMouthSize);

            g2d.setColor(Color.BLACK);
            g2d.drawOval((int) (mo.pos.x - growMouthSize / 2), (int) (mo.pos.y + radius / 2), growMouthSize, growMouthSize);
        }
        else if (hit > 0 && floor <= 0)
        {
            eyeSize = radius / 3;
            g2d.setColor(Color.WHITE);
            g2d.fillOval((int) (mo.pos.x - radius / 2) - eyeSize / 2, (int) (mo.pos.y - radius / 2) - eyeSize / 2, eyeSize, eyeSize);
            g2d.fillOval((int) (mo.pos.x + radius / 2) - eyeSize / 2, (int) (mo.pos.y - radius / 2) - eyeSize / 2, eyeSize, eyeSize);
            g2d.setColor(Color.BLACK);
            g2d.drawOval((int) (mo.pos.x - radius / 2) - eyeSize / 2, (int) (mo.pos.y - radius / 2) - eyeSize / 2, eyeSize, eyeSize);
            g2d.drawOval((int) (mo.pos.x + radius / 2) - eyeSize / 2, (int) (mo.pos.y - radius / 2) - eyeSize / 2, eyeSize, eyeSize);

            int offset = left ? -2 : 2;
            g2d.fillOval((int) (mo.pos.x - radius / 2) - pupilSize / 2 + offset, (int) (mo.pos.y - radius / 2) - pupilSize / 2, pupilSize, pupilSize);
            g2d.fillOval((int) (mo.pos.x + radius / 2) - pupilSize / 2 + offset, (int) (mo.pos.y - radius / 2) - pupilSize / 2, pupilSize, pupilSize);

            g2d.setColor(MOUTH_COLOR);
            g2d.fillOval((int) (mo.pos.x - mouthSize / 2), (int) (mo.pos.y + radius / 2), mouthSize, mouthSize);

            g2d.setColor(Color.WHITE);
            g2d.fillRoundRect((int) (mo.pos.x - mouthSize / 2), (int) (mo.pos.y + radius / 2), mouthSize, mouthSize / 2, 5, 5);

            g2d.setColor(Color.BLACK);
            g2d.drawOval((int) (mo.pos.x - mouthSize / 2), (int) (mo.pos.y + radius / 2), mouthSize, mouthSize);
        }
        else
        {
            eyeSize = 6;
            g2d.setColor(Color.BLACK);
            g2d.drawArc((int) (mo.pos.x - radius / 2) - eyeSize / 2, (int) (mo.pos.y - radius / 2) - eyeSize / 2, eyeSize, eyeSize, 0, 180);
            g2d.drawArc((int) (mo.pos.x + radius / 2) - eyeSize / 2, (int) (mo.pos.y - radius / 2) - eyeSize / 2, eyeSize, eyeSize, 0, 180);

            g2d.drawLine((int) (mo.pos.x - mouthSize / 2), (int) (mo.pos.y + radius / 2 + mouthSize / 2), (int) (mo.pos.x + mouthSize / 2), (int) (mo.pos.y + radius / 2 + mouthSize / 2));
        }

        if (speechTimer > 0)
            drawSpeech(g2d, speech);
    }

    /**
     * Horizontal polygon points of the speech bubble's triangle
     */
    private int[] speechTriangleX = new int[3];

    /**
     * Vertical polygon points of the speech bubble's triangle
     */
    private int[] speechTriangleY = new int[3];

    /**
     * Draw the guy's speech bubble with the specified text inside it
     * 
     * @param g2d
     * @param text
     */
    public void drawSpeech(Graphics2D g2d, String text)
    {
        final int triAngle = 5;
        final int triAngleHeight = 16;

        StringTokenizer tokenizer = new StringTokenizer(text, "\n");
        String[] lines = new String[tokenizer.countTokens()];

        int pixelHeight = (int) (g2d.getFontMetrics().getHeight() * (tokenizer.countTokens() + 0.5f));
        int pixelWidth = 0;
        for (int i = 0; i < lines.length; i++)
        {
            lines[i] = tokenizer.nextToken();
            int w = g2d.getFontMetrics().charsWidth(lines[i].toCharArray(), 0, lines[i].length());
            if (pixelWidth < w)
                pixelWidth = w;
        }

        int buffer = 10 + (int) StarNom.STROKE_SIZE;

        int cornerX = (int) (mo.pos.x - (pixelWidth + buffer) / 2);
        int cornerY = (int) (mo.pos.y - (pixelHeight + buffer) / 2) - radius - pixelHeight - triAngleHeight;

        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(cornerX, cornerY, pixelWidth + buffer, pixelHeight + buffer, buffer * 2, buffer * 2);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(cornerX, cornerY, pixelWidth + buffer, pixelHeight + buffer, buffer * 2, buffer * 2);

        speechTriangleX[0] = (int) (mo.pos.x - triAngle);
        speechTriangleX[1] = speechTriangleX[0] + triAngle;
        speechTriangleX[2] = speechTriangleX[1] + triAngle;

        speechTriangleY[0] = cornerY + pixelHeight + buffer;
        speechTriangleY[1] = speechTriangleY[0] + triAngleHeight;
        speechTriangleY[2] = speechTriangleY[0];

        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(speechTriangleX, speechTriangleY, speechTriangleX.length);

        speechTriangleX[0] -= 1;
        speechTriangleY[0] -= 5;
        speechTriangleX[2] += 1;
        speechTriangleY[2] -= 5;

        g2d.setColor(Color.WHITE);
        g2d.fillPolygon(speechTriangleX, speechTriangleY, speechTriangleX.length);

        buffer /= 2;

        int center;
        g2d.setColor(Color.BLACK);
        for (int i = 0; i < lines.length; i++)
        {
            center = (pixelWidth - g2d.getFontMetrics().charsWidth(lines[i].toCharArray(), 0, lines[i].length())) / 2;
            g2d.drawString(lines[i], cornerX + buffer + center, cornerY + buffer + (i + 1) * g2d.getFontMetrics().getHeight());
        }

    }

    /**
     * Reset the guy to the initial game state
     * 
     * @param screenWidth
     * @param screenHeight
     */
    public void reset(int screenWidth, int screenHeight)
    {
        speechTimer = 0;
        hit = 0;
        floor = 0;
        left = false;
        eatCount = 0;
        mo.pos.x = screenWidth >> 1;
        mo.pos.y = screenHeight >> 1;
        mo.vel.reset();
        mo.acc.x = 0.0f;
        mo.acc.y = StarNom.GRAVITY;
    }

    /**
     * Call when the guy just ate a star
     */
    public void eat()
    {
        eatCount = EAT_COUNT_MAX;
        if (StarNom.RND.nextInt(10) == 0)
        {
            talk();
        }
    }

    /**
     * Call when the guy is looking up
     */
    public void lookUp()
    {
        eatCount = EAT_COUNT_MAX;
    }
}
