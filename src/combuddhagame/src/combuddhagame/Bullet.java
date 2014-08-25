/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package combuddhagame;

import java.awt.Color;

/**
 *
 * @author andreas
 */
public class Bullet {

    public int dmg;
    public volatile double x, y;
    public volatile int wx, wy;
    public double speed;
    public volatile double k;
    public Color color;
    public double xspeed;
    public double yspeed;
    public volatile boolean nullify = false;

    public Bullet(int dmg, int x, int y, int wx, int wy, double speed, Color color, DrawPanel dP, double k) {
        this.dmg = dmg;
        this.x = x;
        this.y = y;
        this.wx = wx;
        this.wy = wy;
        this.speed = speed;
        this.color = color;
        this.k = k;
        int xdiff = wx - x;
        int ydiff = wy - y;
        double length = (int) Math.sqrt(Math.pow(wx - x, 2) + Math.pow(wy - y, 2));
        xspeed = xdiff / length;
        yspeed = ydiff / length;
    }
}