#StarNom [[Download](../../raw/master/jar/StarNom.jar)]

StarNom is a little game I made just to demonstrate that computing physics of motion is easy in a game because you can just incrementally add the changes as the game loop ticks forward in time, rather than do the fancy calculus math based on how much time has passed.

Essentially, for each tick of time:

    vel += acc
    pos += vel

instead of calculating for some arbitrary amount of time:

    vel = init_vel + acc * time / 2
    pos = init_pos + init_vel * time + acc * time * time / 2

How's it control? I dunno. Click and hold the mouse pointer over him, drag the mouse quickly off him, and release the mouse button. There is no goal other than to interact with the walls and the stars flying overhead.

Anyhow, I liked the way the game felt to play so much, I decided to turn it into a touch screen game for the Android called [Star Chomp](http://www.starchomp.com/). But it isn't just a port of this little demo; it's a fully realized game with fancy graphics, sound, and most importantly super addictive gameplay. The Android version has been on extended hiatus for a few years now, but hopefully it will be done soon.

Enjoy!