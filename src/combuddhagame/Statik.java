/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package combuddhagame;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andreas
 */
public class Statik {
    public volatile int hp, maxhp;
    public volatile int dmg;
    public volatile double speed;
    public volatile int picID;
    public volatile double x,y;
    public volatile boolean maydmg = false;
    public volatile int cooldown = 0;
    public String name = "DERP";
    public volatile int textwidth;
    public int xp;
    public int boss;
    public Statik(int hp, int maxhp, int dmg, double speed, int id, int cd, int x,int y, String name, int xp) {
        this.hp = hp;
        this.maxhp = hp;
        this.dmg = dmg;
        this.speed = speed;
        picID = id;
        this.xp = xp;
        cooldown = cd;
        this.name = name;
        this.x = x;
        this.y=y;
        boss=-1;
        //Font defaultFont=new Font("Helvetica",Font.PLAIN,16);
        //FontMetrics fm=new FontMetrics(defaultFont) {};
        //textwidth=fm.stringWidth(name);
        textwidth = 15 * name.length();
        new ThreadI(this) {

            @Override
            public synchronized void run() {
                while (s.hp > 0) {
                    try {
                        Thread.sleep(s.cooldown);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Statik.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    maydmg = true;
                }
            }
        }.start();
    }
    public Statik(int hp, int maxhp, int dmg, double speed, int id, int cd, int x,int y, String name, int xp, int boss) {
        this.hp = hp;
        this.maxhp = hp;
        this.dmg = dmg;
        this.speed = speed;
        picID = id;
        this.xp = xp;
        cooldown = cd;
        this.name = name;
        this.x = x;
        this.y=y;
        this.boss=boss;
        //Font defaultFont=new Font("Helvetica",Font.PLAIN,16);
        //FontMetrics fm=new FontMetrics(defaultFont) {};
        //textwidth=fm.stringWidth(name);
        textwidth = 15 * name.length();
        new ThreadI(this) {

            @Override
            public synchronized void run() {
                while (s.hp > 0) {
                    try {
                        Thread.sleep(s.cooldown);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Statik.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    maydmg = true;
                }
            }
        }.start();
    }
}
