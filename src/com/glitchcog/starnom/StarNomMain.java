package com.glitchcog.starnom;

import javax.swing.JFrame;

/**
 * Houses the main class for StarNom
 * 
 * @author Matt Yanos
 */
public class StarNomMain
{
    /**
     * Run the game
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        StarNom game = new StarNom();
        JFrame window = guiSetup(game);
        window.add(game);
        window.setVisible(true);
        game.start();
    }

    /**
     * Construct and configure the Window for the game
     * 
     * @param ml
     *            The mouse listener to add to the window for the game control
     * @return window
     */
    public static JFrame guiSetup(StarNom game)
    {
        JFrame window = new JFrame();
        window.setSize(StarNom.SCREEN_WIDTH, StarNom.SCREEN_HEIGHT);
        window.setMinimumSize(window.getSize());
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setTitle("Star Nom");
        return window;
    }
}
