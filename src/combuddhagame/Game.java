/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package combuddhagame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author andreas
 * 
 *      PRIORITY    |   TODO:
 *      
 *      1               Add Launcher(Screensize,Fullscreen)
 *      2               Add items(armor, weapons for stats)
 *      3               Add Spells
 *      3               Add more textures+animations
 *      4               Add Tooltips
 *      4               Add NPCs with quests
 *      5               Add different areas with different mobs
 *      3               Make spells skillable
 * 
 */
public class Game extends JFrame {

    public DrawPanel dp;
    public static boolean fullscreen, wasd;
    
    public Game(boolean fullscreen, boolean wasd) {
        this.setUndecorated(false);
        this.fullscreen = fullscreen;
        this.wasd = wasd;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        setLayout(new BorderLayout());
        if (fullscreen) {

            this.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width, java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);

            this.setUndecorated(true);
            //this.setSize(gs[0].getConfigurations()[gs[0].getConfigurations().length-1].getBounds().getSize());
            gs[0].setFullScreenWindow(this);
            final Game frame = this;
            frame.addFocusListener(new FocusListener() {

                @Override
                public void focusGained(FocusEvent arg0) {

                    frame.setAlwaysOnTop(true);
                }

                @Override
                public void focusLost(FocusEvent arg0) {
                    frame.setAlwaysOnTop(false);
                }
            });
            this.setLocation(0, 0);
            gs[0].setFullScreenWindow(this);
        } else {
            this.setSize(1280, 720);
        }
        this.setResizable(false);
        dp = new DrawPanel(getWidth(), getHeight(), wasd);
        dp.setLocation(0, 0);
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        this.add(dp, BorderLayout.CENTER);
        dp.setVisible(true);
        dp.setMinimumSize(new Dimension(getWidth(), getHeight()));
        this.setVisible(true);
        this.setSize(getWidth(), getHeight());
        this.setLayout(new BorderLayout());
        KeyListena k = new KeyListena(dp);
        this.addKeyListener(k);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame lol = new Game(fullscreen, wasd);

        lol.setVisible(true);


    }
}


class PaintLoop extends java.util.TimerTask{

    DrawPanel dP;

    public PaintLoop(DrawPanel dP) {
        this.dP = dP;
    }
int i=0;
    @Override
    public void run() {
        
            i++;
                
            dP.repaint();
            if (!dP.paused) {
                    if (dP.walking
                            || (dP.mayWalk) < 1) {
                        dP.mayWalk += 2;
                        if (i >= 3) {
                            i = 0;
                            dP.mayDraw = true;
                        }
                    }
                    if (dP.jumping) {
                        dP.mayJump++;
                    }

                    dP.rainWalk += 2;
                
        }
    }
}

class PointEx extends Point {

    volatile int type;
    public double y;

    public PointEx(int x, double y, int type) {
        super(x, (int) y);
        y = (double) y;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

class ThreadI extends Thread {

    Statik s;

    public ThreadI(Statik s) {
        this.s = s;
    }
}



class DrawPanel extends JPanel implements MouseListener, MouseMotionListener {

    public BufferedImage[] character = new BufferedImage[10];
    public volatile int rainWalk = 0;
    public CopyOnWriteArrayList<Tooltip> tt=new CopyOnWriteArrayList<Tooltip>();
    public void getCharacter() throws IOException {

        for (int i = 1; i <= 10; i++) {
            character[i - 1] = ImageIO.read(new File(("character/" + i + ".png")));
        }

    }
    volatile CopyOnWriteArrayList<Statik> statik = new CopyOnWriteArrayList<Statik>();
    BufferedImage[] enemies = new BufferedImage[6];

    public void getStatic() throws IOException {

        for (int i = 1; i <= 3; i++) {
            enemies[i - 1] = ImageIO.read(new File(("character/e" + i + ".png")));
        }
        enemies[3] = ImageIO.read(new File("character/dragon.png"));
        enemies[4]=ImageIO.read(new File("character/warlock.png"));
        enemies[5]=ImageIO.read(new File("character/metallogo.png"));
    }
    public int height, width;
    public boolean wasd;

    public DrawPanel(int width, int height, boolean wasd) {
        //super();
        super(new FlowLayout(FlowLayout.LEADING));
        this.wasd = wasd;
        arrangeSpells();
        this.width = width;
        this.height = height;
        this.setSize(width, height);
        p = (width / 500) * 3;
        System.out.println("p is " + p);
        ob = new PointEx[p];
        y = height - 180;
        System.out.println("y:" + y);
        generatePoints();
       // try {
            //bg=ImageIO.read(new File("/home/andreas/Documents/kit.png"));
      //  } catch (IOException ex) {
       //     Logger.getLogger(DrawPanel.class.getName()).log(Level.SEVERE, null, ex);
       // }
        
        Timer timer=new Timer();
        timer.schedule(new PaintLoop(this),0,1000/60);
        try {
            getCharacter();
            getStatic();

        } catch (IOException ex) {
            Logger.getLogger(DrawPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            dragon = ImageIO.read(new File("character/dragon.png"));
        } catch (IOException ex) {
            Logger.getLogger(DrawPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        addMouseListener(this);
        addMouseMotionListener(this);
        bullet = new ArrayList<Bullet>();
        bulletcd = new CD(2000, this);
        bulletcd.start();
        statikwalk = new CD(10, this);
        statikwalk.start();
        respawn0.start();
        respawn0.tick = true;
        respawn1.start();
        respawn1.tick = false;
        respawn2.start();
        respawn2.tick = false;
        bs[2] = 0;
        load();
        int[] spells={};
        tt.add(new Tooltip(spells,"You can jump using the space bar.",(getWidth()/2+250),(getHeight()-190),1500));
        
                tt.add(new Tooltip(spells,"Try shooting by pressing and holding \nthe right mouse button",(getWidth()/2+250),(getHeight()-280),1500));

    }
    CD bulletcd;
    CD statikwalk;
    ArrayList<Bullet> bullet;
    float crit = 0.01f;
    int c = 1;
    volatile double x = 20, y;
    volatile long score = 0;
    volatile int xvar = 0;
    volatile int wx, wy;
    boolean alive = true;
    public volatile int mayJump = 0;
    int hp = 20;
    int maxhp = 20;
    volatile static int lvl = 1;
    volatile static int maxxp = 80;
    volatile static int xp = 0;
    int bulletCD = 500;
    volatile int chosenSpell = -1;
    double dmg = dmg = 1 + (0.1 + (0.01 * lvl)) * lvl;
    ;
    int abilityPower = 0;
    int invincible = 0;
    double bulletspeed = 1.0;
    double speed = 1;
    volatile boolean bmod = false;
    int p;
    volatile PointEx[] ob;
    public int mayWalk = 0;
    public volatile boolean mayDraw = false;
    public volatile float k;
    public volatile long personalhigh = 0;
    public volatile int jumpheight = 200;
    public volatile boolean jumping = false;
    public volatile int jumpstate = 0;
    public volatile int jumpspeed = -25;

    public void save() {
        File f = new File(System.getProperty("user.home") + "/.game/save.dat");
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdir();
        }
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(DrawPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(f));
            out.write("OLOL:5;");
            out.write(maxhp + ";");
            out.write(lvl + ";");
            out.write(maxxp + ";");
            out.write(xp + ";");
            out.write(dmg + ";");
            out.flush();
            out.write(speed + ";");
            out.write(bulletspeed + ";");
            out.write(personalhigh + ";");
            out.write(chosenSpell + ";");
            out.write(crit + ";");
            out.write(abilityPower + ";");
            out.flush();
            out.write(bs[0] + ";" + bs[1] + ";" + bs[2] + ";");

            out.flush();
            out.close();
            out = null;
        } catch (IOException ex) {
            Logger.getLogger(DrawPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void load() {
        File f = new File(System.getProperty("user.home") + "/.game/save.dat");
        try {
            if (f.exists()) {
                BufferedReader in = new BufferedReader(new FileReader(f));
                try {
                    String[] kit = in.readLine().split(";");
                    if (kit[0].contains("OLOL") && kit[0].split(":")[1].equals("5")) {
                        maxhp = Integer.parseInt(kit[1]);
                        lvl = Integer.parseInt(kit[2]);
                        maxxp = Integer.parseInt(kit[3]);
                        xp = Integer.parseInt(kit[4]);
                        dmg = Double.parseDouble(kit[5]);
                        speed = Double.parseDouble(kit[6]);
                        bulletspeed = Double.parseDouble(kit[7]);
                        if (kit.length > 8) {
                            System.out.println(kit[8]);
                            if (!kit[8].contains("-")) {
                                personalhigh = Long.parseLong(kit[8]);
                            } else {
                                personalhigh = 0;
                            }
                        }
                        chosenSpell = Integer.parseInt(kit[9]);
                        crit = Float.parseFloat(kit[10]);
                        abilityPower = Integer.parseInt(kit[11]);
                        if (kit.length > 10) {
                            bs[0] = Integer.parseInt(kit[12]);
                            if (kit.length > 11) {
                                bs[1] = Integer.parseInt(kit[13]);
                            }
                            if (kit.length > 12) {
                                bs[2] = Integer.parseInt(kit[14]);
                            }
                        }
                        bulletcd.cd = (int) ((double) 1500.0 / (double) bulletspeed);
                        abilityPower = (int) lvl + lvl * 2;
                        if (!bulletcd.isAlive()) {
                            bulletcd.start();
                        } else {
                            if (kit[0].contains("OLOL") && kit[0].split(":")[1].equals("4")) {
                                maxhp = Integer.parseInt(kit[1]);
                                lvl = Integer.parseInt(kit[2]);
                                maxxp = Integer.parseInt(kit[3]);
                                xp = Integer.parseInt(kit[4]);
                                dmg = Double.parseDouble(kit[5]);
                                speed = Double.parseDouble(kit[6]);
                                bulletspeed = Double.parseDouble(kit[7]);
                                if (kit.length > 8) {
                                    System.out.println(kit[8]);
                                    if (!kit[8].contains("-")) {
                                        personalhigh = Long.parseLong(kit[8]);
                                    } else {
                                        personalhigh = 0;
                                    }
                                }
                                chosenSpell = Integer.parseInt(kit[9]);
                                if (kit.length > 10) {
                                    bs[0] = Integer.parseInt(kit[10]);
                                    if (kit.length > 11) {
                                        bs[1] = Integer.parseInt(kit[11]);
                                    }
                                    if (kit.length > 12) {
                                        bs[2] = Integer.parseInt(kit[12]);
                                    }
                                }
                                bulletcd.cd = (int) ((double) 1500.0 / (double) bulletspeed);
                                abilityPower = (int) lvl + lvl * 2;

                                dmg = 1 + (0.1 + (0.01 * lvl)) * lvl;
                                if (!bulletcd.isAlive()) {
                                    bulletcd.start();
                                }
                            } else {
                                f.delete();
                            }
                        }
                    }
                    in.close();

                } catch (IOException ex) {
                    Logger.getLogger(DrawPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DrawPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int r() {
        return (int) (Math.random() * 1000.0);
    }

    public void generatePoints() {
        PointEx p;
        for (int i = 0; i < ob.length; i++) {
            int r = r();
            p = new PointEx((int) (0 + Math.random() * getWidth()), (int) (-50 + Math.random() * 50), 0);
            if (r > 750 && r <= 850) {
                p.setType(1);
            }
            if (r > 850 && r <= 935) {
                p.setType(2);
            }
            if (r > 935 && r <= 990) {
                p.setType(3);
            }
            if (r > 990) {
                p.setType(4);
            }
            ob[i] = p;
        }
    }
    volatile boolean walking = false;

    public void giveXP(int amount) {
        xp += amount;
        int remainder = -1;
        if (xp >= maxxp) {
            remainder = xp - maxxp;
        }
        if (remainder > -1) {
            xp = remainder;
            lvl++;
            score += 80 + lvl;
            maxxp = 80 * lvl * lvl * lvl;
            speed = 1.0 + (double) lvl * ((double) lvl / 8.0) / 30.0;
            bulletspeed = 2.0 + ((double) lvl * ((double) lvl / 8.0) / 20.0);
            bulletcd.cd = (int) (2000.0 / (double) bulletspeed);
            int t = (int) ((double) speed * 100.0);
            speed = (double) t / 100.0;
            dmg = (0.1 + (0.01 * lvl)) * lvl;
            t = (int) ((double) (dmg * 100.0));
            dmg = (double) t / 100.0;
            maxhp = 17 + 3 * lvl;
            abilityPower = (int) lvl + lvl * 2;
            crit += 0.05f / lvl;
            hp = maxhp;
            if (lvl == 8) {
                choosing = true;
                int[] spells={2,3};
                tt.add(new Tooltip(spells,"Choose carefully. Press H for Heal. I for Invincibility",(getWidth()/2+250),(getHeight()-280),-1));
            }
            
            if(lvl==2){
                int[] spells={1};
                if(tt.size()>0)tt.add(new Tooltip(spells,"Use your new spell by pressing 1 on the\nkeyboard. Place it using the right mouse button",(getWidth()/2+250),(getHeight()-480),1512));
                else tt.add(new Tooltip(spells,"Use your new spell by pressing 1 on the\nkeyboard. Place it using the right mouse button",(getWidth()/2+250),(getHeight()-280),1512));
            }

        }
        save();
    }
    public String lastPowerUp = "";
    public volatile boolean choosing = false;

    public void drawStats(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.drawChars(("Score: " + score).toCharArray(), 0, ("SCORE: " + score).length(), 10, 20);
        g.drawChars(("Level: " + lvl).toCharArray(), 0, ("LEVEL: " + lvl).length(), 10, 40);
        g.drawChars(("XP: " + xp).toCharArray(), 0, ("XP: " + xp).length(), 10, 60);
        g.drawChars(("MAXXP: " + maxxp).toCharArray(), 0, ("MAXXP: " + maxxp).length(), 10, 80);
        g.drawChars(("HP: " + hp + " / " + maxhp).toCharArray(), 0, ("HP: " + hp + " / " + maxhp).length(), 10, 300);

        g.drawChars(("" + lvl).toCharArray(), 0, ("" + lvl).length(), getWidth() / 2 - 40, getHeight() - 35);
        if (invincible > 0) {
            g.setColor(Color.ORANGE);
            g.drawChars(("INVINCIBLE: " + invincible).toCharArray(), 0, ("INVINCIBLE: " + invincible).length(), getWidth() / 2 - 65, 180);
        }
        g.setColor(Color.GRAY);
        g.drawChars(("Speed: " + speed).toCharArray(), 0, ("SPEED: " + speed).length(), 10, 120);
        g.drawChars(("Last Powerup: " + lastPowerUp).toCharArray(), 0, ("Last Powerup: " + lastPowerUp).length(), 10, 140);
        g.drawChars(("DMG: " + dmg).toCharArray(), 0, ("DMG: " + dmg).length(), 10, 160);
        g.drawChars(("Ability Power: " + abilityPower).toCharArray(), 0, ("Ability Power: " + abilityPower).length(), 10, 220);
        g.drawChars(("Crit Rate: " + (int) (crit * 100) + "%").toCharArray(), 0, ("Crit Rate: " + (int) (crit * 100) + "%").length(), 10, 240);
        g.drawChars(("Highscore: " + personalhigh).toCharArray(), 0, ("Highscore: " + personalhigh).length(), 10, 180);
        g.drawChars(("Bulletspeed: " + bulletspeed).toCharArray(), 0, ("Bulletspeed: " + bulletspeed).length(), 10, 200);

        double pixelPerXp = (double) (((double) (getWidth() - 20) / (double) maxxp));
        g.setColor(Color.BLACK);
        g.fillRect(9, getHeight() - 61, getWidth() - 18, 12);
        g.finalize();
        g.setColor(Color.CYAN);
        g.fillRect(10, getHeight() - 60, (int) ((double) ((double) xp * pixelPerXp)), 10);

        double pixelPerHp = (100.0) / (double) maxhp;
        g.setColor(Color.BLACK);
        g.fillRect(9, 304, 102, 12);
        g.finalize();
        g.setColor(Color.RED);
        g.fillRect(10, 305, (int) ((double) hp * pixelPerHp), 10);
    }
    ArrayList<BufferedImage> icon;
    ArrayList<BufferedImage> spell;
    ArrayList<CD> spellcd;
    volatile int placingspell = -1;
    volatile int display = 0;
    volatile int displayID = -1;
    BufferedImage dragon;
    BufferedImage placer;
    public boolean damageDone = false;

    public void arrangeSpells() {
        try {
            icon = new ArrayList<BufferedImage>();
            spell = new ArrayList<BufferedImage>();
            spellcd = new ArrayList<CD>();
            icon.add(ImageIO.read(new File("character/spell0/icon.png")));
            icon.add(ImageIO.read(new File("character/spell1/icon.png")));
            spell.add(ImageIO.read(new File("character/spell0/spell.png")));
            icon.add(ImageIO.read(new File("character/spell2/icon.png")));
            icon.add(ImageIO.read(new File("character/spell3/icon.png")));
            spellcd.add(new CD(5000, this));
            spellcd.get(0).start();
            spellcd.add(new CD(12000, this));
            spellcd.get(1).start();
            spellcd.add(new CD(7500, this));
            spellcd.get(2).start();
            spellcd.add(new CD(15000, this));
            spellcd.get(3).start();
            placer = ImageIO.read(new File("character/logo.png"));
        } catch (IOException ex) {
            Logger.getLogger(DrawPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
public void drawTooltips(Graphics2D g){
    for(Tooltip t:tt){
        if(t.life!=0){
            t.life--;
            g.setColor(new Color(30,60,45,140));
        if(t.spells.length>0){
            g.fillRect(t.x,t.y,340,120);
            g.setColor(new Color(200,220,45));
            
            String[] msg=t.message.split("\n");
            int i=0;
            for(String s:msg){
            g.drawString(s, t.x+5, t.y+90+i*24);
                i++;    
            }
        }else{
            g.fillRect(t.x, t.y, 340, 70);
            
            g.setColor(new Color(200,220,45));
            String[] msg=t.message.split("\n");
            int i=0;
            for(String s:msg){
            g.drawString(s, t.x+5, t.y+25+i*24);
                i++;    
            }
        }
        int c=0;
        for(int i:t.spells){
            g.drawImage(icon.get(i), null, t.x+5+c*90, t.y+5);
        c++;
        }}else{
            tt.remove(t);
            t=null;
        }
    }
}
    public void drawSpell(Graphics2D g) {
        
        if (placingspell != -1) {
            g.drawImage(placer, null, mx - (placer.getWidth() / 2), my - (placer.getHeight() / 2));
        }

        if (lvl >= 2) {
            g.drawImage(icon.get(0), null, 20, getHeight() - 70);
            if (!spellcd.get(0).tick) {
                g.setColor(Color.RED);
                g.drawLine(20, getHeight() - 70, 85, getHeight() - 5);
            }
            if (lvl >= 4) {
                g.drawImage(icon.get(1), null, 100, getHeight() - 70);
                if (!spellcd.get(1).tick) {
                    g.setColor(Color.RED);
                    g.drawLine(100, getHeight() - 70, 165, getHeight() - 5);
                }
            }
            if (lvl >= 8 && chosenSpell != -1) {
                g.drawImage(icon.get(chosenSpell), null, 180, getHeight() - 70);

                if (!spellcd.get(chosenSpell).tick) {
                    g.setColor(Color.RED);
                    g.drawLine(180, getHeight() - 70, 245, getHeight() - 5);
                }
            }
        }
        if (display > 0) {
            g.drawImage(spell.get(displayID), null, spellmx - (spell.get(displayID).getWidth() / 2), spellmy - (spell.get(displayID).getHeight() / 2));
            if (!damageDone) {
                for (Statik s : statik) {
                    if ((spellmx + spell.get(displayID).getWidth()) >= s.x
                            && (spellmx) <= s.x + enemies[s.picID].getWidth()
                            && (spellmy + spell.get(0).getHeight()) >= s.y
                            && (spellmy) <= s.y + enemies[s.picID].getHeight()) {
                        s.hp -= 3 + ((double) (abilityPower / 2));
                        if (s.hp <= 0) {
                            statik.remove(s);
                            giveXP(s.xp);
                            score += s.xp;
                            if (s.picID != 3) {
                                as[s.picID]--;
                            }
                            break;
                        }
                    }
                }
                damageDone = true;
            }
            display--;
            if (display == 0) {
                displayID = -1;
                damageDone = false;
            }
        }
    }
    volatile boolean dragonSpawned = false;

    public void drawPoints(Graphics2D g) {
        int size = 35;
        boolean binvincible = false;
        boolean bplusscore = false;
        boolean bplusattack = false;
        boolean bdmg = true;
        boolean bxp = false;
        boolean bplushp = false;
        int i = 0;
        for (PointEx p : ob) {
            switch (p.type) {
                case 0:
                    g.setColor(Color.yellow);
                    size = 35;
                    binvincible = false;
                    bplusscore = false;
                    bplusattack = false;
                    bdmg = true;
                    bxp = false;
                    bplushp = false;
                    break;
                case 1:
                    g.setColor(Color.RED);
                    binvincible = false;
                    bplusscore = false;
                    bplusattack = false;
                    bxp = true;
                    bdmg = false;
                    bplushp = true;
                    size = 40;
                    break;
                case 2:
                    g.setColor(Color.BLACK);
                    binvincible = false;
                    bplusscore = true;
                    bplusattack = false;
                    bplushp = false;
                    bxp = true;
                    bdmg = false;
                    size = 30;
                    break;
                case 3:
                    g.setColor(Color.PINK);
                    bplusattack = true;
                    bxp = true;
                    binvincible = false;
                    bplushp = true;
                    bdmg = false;
                    size = 20;
                    break;
                case 4:
                    g.setColor(Color.GREEN);
                    binvincible = true;
                    bplusscore = true;
                    bplusattack = true;
                    bxp = true;
                    bdmg = false;
                    size = 15;
                    break;
            }
            if (p.y > 0) {
                g.fill3DRect(p.x, (int) p.y, size, size, true);
                g.setColor(Color.BLACK);
                g.drawRect(p.x, (int) p.y, size, size);
            }
            int r;
            for (Statik s : statik) {
                if ((p.x + size) >= s.x && (p.x) <= s.x + enemies[s.picID].getWidth() && (p.y + size) >= y && (p.y) <= y + enemies[s.picID].getHeight()) {
                    p = new PointEx((int) (0 + Math.random() * getWidth()), (int) (-50 + Math.random() * 50), 0);
                    r = r();
                    if (!(p.type == 0)) {
                        if (r > 550 && r <= 650) {
                            p.setType(1);
                        }
                        if (r > 650 && r <= 735) {
                            p.setType(2);
                        }
                        if (r > 735 && r <= 950) {
                            p.setType(3);
                        }
                        if (r > 950) {
                            p.setType(4);
                        }
                    }
                    ob[i] = p;
                    /* boolean binvincible = false;
                    boolean bplusscore = false;
                    boolean bplusattack = false;
                    boolean bdmg = true;
                    boolean bxp = false;
                    boolean bplushp = false;*/
                    if (bdmg && invincible <= 0) {
                        s.hp -= 1 + 10 * lvl / 50;
                        if (s.hp <= 0) {
                            statik.remove(s);
                            giveXP(s.xp);
                            score += s.xp;
                            if (s.picID != 3) {
                                as[s.picID]--;
                            }
                            break;
                        }
                    } else {
                        if (binvincible) {
                            s.maxhp = (int) (((double) s.maxhp) * 1.5);
                            s.hp = (int) (((double) s.hp) * 1.5);
                            if (!s.name.contains("+HP")) {
                                s.name += " +HP";
                            } else {
                                String thisone = " +HP";
                                String[] drugs = {" ON SPEED", " +HP", " ON METH"};
                                for (String ss : drugs) {
                                    if (ss.equals(thisone)) {
                                        if (s.name.endsWith(ss)) {
                                            s.name += " x2";
                                        }
                                        for (int d = 0; d < 25; d++) {
                                            if (s.name.endsWith(ss + " x" + d)) {
                                                String n = "";
                                                for (int h = 0; h < s.name.length() - 1; h++) {
                                                    n += s.name.charAt(h);
                                                }
                                                n += "" + ((int) (d + 1));
                                                break;
                                            } else {
                                                if (s.name.contains(ss + " x" + d)) {


                                                    int h = s.name.lastIndexOf(ss + " x" + d);
                                                    String n = "";
                                                    for (int e = 0; e < s.name.length(); e++) {
                                                        if (e != h) {
                                                            n += s.name.charAt(e);
                                                        } else {
                                                            n += "" + ((int) (d + 1));
                                                        }
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            lastPowerUp = "";
                            if (bplusscore) {

                                s.speed = (((double) s.speed) * 1.5);

                                if (!s.name.contains("ON SPEED")) {
                                    s.name += " ON SPEED";
                                } else {
                                    String thisone = " ON SPEED";
                                    String[] drugs = {" ON SPEED", " +HP", " ON METH"};
                                    for (String ss : drugs) {
                                        if (ss.equals(thisone)) {
                                            if (s.name.endsWith(ss)) {
                                                s.name += " x2";
                                            }
                                            for (int d = 0; d < 25; d++) {
                                                if (s.name.endsWith(ss + " x" + d)) {
                                                    String n = "";
                                                    for (int h = 0; h < s.name.length() - 1; h++) {
                                                        n += s.name.charAt(h);
                                                    }
                                                    n += "" + ((int) (d + 1));
                                                    break;
                                                } else {
                                                    if (s.name.contains(ss + " x" + d)) {


                                                        int h = s.name.lastIndexOf(ss + " x" + d);
                                                        String n = "";
                                                        for (int e = 0; e < s.name.length(); e++) {
                                                            if (e != h) {
                                                                n += s.name.charAt(e);
                                                            } else {
                                                                n += "" + ((int) (d + 1));
                                                            }
                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (bxp) {
                                int c = s.name.length();

                                if (s.picID != 3) {
                                    bs[s.picID]++;
                                }


                                if (s.picID == 0) {
                                    s = (new Statik((s.hp / s.maxhp) * ((bs[0] * 2) + 1) * 5, (bs[0] * 2) * 5, bs[0] + 1, 1, 0, 350, getWidth(), height - 180, s.name, (bs[0] * bs[0] + 1) * 120));
                                    respawn0.tick = false;
                                }
                                if (s.picID == 1) {   //cont
                                    s = (new Statik((s.hp / s.maxhp) * ((bs[1] * 2) + 1) * 10, (bs[1] * 2) * 10, bs[1] + 2, 1.5, 1, 350, getWidth(), height - 180, s.name + bs[1], (bs[1] * bs[1] + 1) * 980));
                                    respawn1.tick = false;
                                }
                                if (s.picID == 2) {
                                    s = (new Statik((s.hp / s.maxhp) * ((bs[2] * 2) + 1) * 30, (bs[2] * 2) * 30, bs[2] + 3, 1.7, 2, 350, getWidth(), height - 180, s.name + bs[2], (bs[2] * bs[2] + 1) * 50000));
                                    respawn2.tick = false;
                                }
                                

                            }
                            if (bplusattack) {
                                s.dmg += 1 + (double) lvl / 6.0;
                                if (!s.name.contains("ON METH")) {
                                    s.name += " ON METH";
                                } else {
                                    String thisone = " ON METH";
                                    String[] drugs = {" ON SPEED", " +HP", " ON METH"};
                                    for (String ss : drugs) {
                                        if (ss.equals(thisone)) {
                                            if (s.name.endsWith(ss)) {
                                                s.name += " x2";
                                            }
                                            for (int d = 0; d < 25; d++) {
                                                if (s.name.endsWith(ss + " x" + d)) {
                                                    String n = "";
                                                    for (int h = 0; h < s.name.length() - 1; h++) {
                                                        n += s.name.charAt(h);
                                                    }
                                                    n += "" + ((int) (d + 1));
                                                    break;
                                                } else {
                                                    if (s.name.contains(ss + " x" + d)) {


                                                        int h = s.name.lastIndexOf(ss + " x" + d);
                                                        String n = "";
                                                        for (int e = 0; e < s.name.length(); e++) {
                                                            if (e != h) {
                                                                n += s.name.charAt(e);
                                                            } else {
                                                                n += "" + ((int) (d + 1));
                                                            }
                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (bplushp) {
                                s.hp += 1 + (((double) (double) s.maxhp / 4.0));
                            }
                        }
                    }
                }
            }
            if ((p.x + 35) >= x && (p.x) <= x + character[0].getWidth() && (p.y + 35) >= y && (p.y) <= y + character[0].getHeight()) {
                p = new PointEx((int) (0 + Math.random() * getWidth()), (int) (-50 + Math.random() * 50), 0);
                r = r();
                if (r > 770 && r <= 870) {
                    p.setType(1);
                }
                if (r > 870 && r <= 945) {
                    p.setType(2);
                }
                if (r > 945 && r <= 990) {
                    p.setType(3);
                }
                if (r > 990) {
                    p.setType(4);
                }
                ob[i] = p;
                /* boolean binvincible = false;
                boolean bplusscore = false;
                boolean bplusattack = false;
                boolean bdmg = true;
                boolean bxp = false;
                boolean bplushp = false;*/
                if (bdmg && invincible <= 0) {
                    hp -= 5 + 13 * (double) lvl / 30.0;
                    if (hp <= 0) {
                        walking = false;

                        alive = false;
                    }
                } else {
                    if (binvincible) {
                        invincible += 600 + lvl * 25;
                        lastPowerUp = "INVINCIBILITY";
                    } else {
                        lastPowerUp = "";
                        if (bplusscore) {
                            score += 80 * lvl;
                            if (!lastPowerUp.equals("")) {
                                lastPowerUp += ", PLUS SCORE " + (80 * lvl);
                            } else {
                                lastPowerUp += "PLUS SCORE " + (80 * lvl);
                            }
                        }
                        if (bxp) {
                            giveXP(40 * (lvl * lvl / 2));
                            if (!lastPowerUp.equals("")) {
                                lastPowerUp += ", PLUS XP " + (40 * (lvl * lvl / 2));
                            } else {
                                lastPowerUp += "PLUS XP " + (40 * (lvl * lvl / 2));
                            }
                        }
                        if (bplusattack) {
                            dmg += lvl / 10;
                            if (!lastPowerUp.equals("")) {
                                lastPowerUp += ", PLUS DMG " + (dmg / 10);
                            } else {
                                lastPowerUp += "PLUS DMG 1" + (dmg / 10);
                            }
                        }
                        if (bplushp) {
                            hp += 3 + ((double) maxhp / 4.0);
                            if (hp > maxhp) {
                                hp = maxhp;
                            }
                            if (!lastPowerUp.equals("")) {
                                lastPowerUp += ", PLUS HP <= " + (3 + ((double) maxhp / 4.0));
                            } else {
                                lastPowerUp += "PLUS HP <= " + (3 + ((double) maxhp / 4.0));
                            }
                        }

                    }
                }
            }

            if (p.y > getHeight()) {
                p = new PointEx((int) (0 + Math.random() * getWidth()), (int) (-50 + Math.random() * 50), 0);
                r = r();
                if (r > 800 && r <= 880) {
                    p.setType(1);
                }
                if (r > 880 && r <= 955) {
                    p.setType(2);
                }
                if (r > 995 && r <= 990) {
                    p.setType(3);
                }
                if (r > 990) {
                    p.setType(4);
                }
                ob[i] = p;
                giveXP(5 * lvl);
            }
            i++;





        }
    }
    long lastscore = 0;

    public void drawBullets(Graphics2D g) {
        while (bmod);
        bmod = true;
        ArrayList<Bullet> bt = new ArrayList<Bullet>();
        //bt=bullet;
        //for(Bullet b:bt){
        for (int c = 0; c < bullet.size(); c++) {
            Bullet b = bullet.get(c);

            g.setColor(b.color);
            g.fillRect((int) b.x, (int) b.y, 15, 5);
            Random r = new Random(1000);
            for (Statik s : statik) {
                if (b != null) {
                    if (s.hp > 0) {
                        if ((b.x + 15) >= s.x - 100
                                && (b.x) <= s.x - 100 + enemies[s.picID].getWidth()
                                && (b.y + 5) >= y - 100
                                && (b.y) <= y - 100 + enemies[s.picID].getHeight()) {
                            if (r.nextInt(1000) > (((double) crit) * 1000.0)) {
                                s.hp -= dmg;
                            } else {
                                s.hp -= 3 * dmg;
                                g.setColor(Color.RED);
                                g.drawString("CRIT", (int) b.x, (int) b.y);
                                System.out.println(r.nextInt() + "CRIT" + (((double) crit) * 1000.0));
                            }
                            if (s.hp <= 0) {
                                statik.remove(s);
                                giveXP(s.xp);

                                score += s.xp;
                                if (s.picID != 3) {
                                    as[s.picID]--;
                                }
                                break;
                            }
                            System.out.println("HIT");
                            b.nullify = true;

                        }
                    }
                }
            }

            if (b.x > getWidth() || b.y < 0 || b.y > getHeight() || b.x < 0 || (b.wx > b.x) && (b.wx - b.x <= 1 || (b.wx - b.x < 0 && b.x - b.wx <= 1)) && ((b.y - b.wy < 0 && b.wy - b.y <= 1) || (b.y > b.wy) && b.y - b.wy <= 1)) {
                b.nullify = true;

            } else {
                b.x += 8 * b.speed * b.xspeed;
                b.y += 8 * b.speed * (b.yspeed);
            }




            if (!b.nullify) {
                bt.add(b);
            }

        }
        bullet = bt;
        bmod = false;
    }
    volatile boolean drawingStatik = false;

    public void drawStatik(Graphics2D g) {
        if (!drawingStatik) {
            drawingStatik = true;
            
                int i = 0;
                for (Statik s : statik) {
                    i++;
                    if (s.hp != 0) {
                        int picWidth = enemies[s.picID].getWidth();
                        int picHeight = enemies[s.picID].getHeight();


                        g.setColor(Color.BLACK);
                        g.fillRect((int) s.x - (picWidth / 2 + 1) + picWidth / 2 - 50, (int) s.y - (picHeight / 2 + 36), 100, 22);
                        double pixelPerHpS = (double) ((double) (100.0) / (double) s.maxhp);
                        g.setColor(Color.RED);
                        g.fillRect((int) s.x - picWidth / 2 + picWidth / 2 - 50, (int) s.y - (picHeight / 2 + 35), (int) ((double) s.hp * (double) pixelPerHpS), 20);

                        g.drawImage(enemies[s.picID], null, (int) s.x - picWidth / 2, (int) s.y - picHeight / 2);
                        g.setColor(new Color(128, 127, 10));
                        g.drawChars(s.name.toCharArray(), 0, s.name.toCharArray().length, (int) s.x - 100 + enemies[s.picID].getWidth() / 2 - s.textwidth / 2, (int) s.y - enemies[s.picID].getHeight()/2 -60);
                        if (s.x >= x && (s.x - x) <= 5 || s.x <= x && (x - s.x) <= 5
                                && (((y - s.y) > 0 && (y - s.y) < 3)
                                || ((y - s.y) <= 0 && ((y - s.y) >= -3)))) {
                            if (s.maydmg && invincible <= 0) {
                                hp -= s.dmg;
                                s.maydmg = false;
                            }
                            if (hp <= 0) {
                                alive = false;
                                walking = false;
                            }
                        }
                        if (statikwalk.tick) {
                            if (s.x > x) {
                                s.x -= s.speed;
                            } else {
                                if (s.x < x) {
                                    s.x += s.speed;
                                }
                            }
                            if (i == statik.size()) {
                                statikwalk.tick = false;
                            }
                        }
                    } else {
                        statik.remove(s);
                        if (s.picID != 3) {
                            as[s.picID]--;
                        }
                    }
                }
            
            drawingStatik = false;
        }
    }
    int[] as = {0, 0, 0,0,0,0};
    int[] bs = {0, 0, 0};
    boolean first = true;
    public volatile boolean paused = false;
    CD respawn0 = new CD(25000, this);
    CD respawn1 = new CD(65000, this);
    CD respawn2 = new CD(90000, this);
    public volatile int mx, my;
    public volatile int spellmx,spellmy;
    public volatile boolean warlockSpawned=false;
    public volatile boolean metalSpawned=false;
    //BufferedImage bg;
    @Override
    public void paintComponent(Graphics g) {
        if (!paused) {
            Graphics2D g2d = (Graphics2D) g;
            if (first) {
                g.setFont(new Font("Helvetica", Font.PLAIN, 16));
                first = false;
            
            }
            g2d.setColor(new Color(5, 0, 255));
            g2d.fillRect(0, 0, width, getHeight() - 50);
            g2d.setColor(new Color(20, 200, 85));
            g2d.fillRect(0, getHeight() - 50, width, getHeight());
                        //g2d.drawImage(bg, null, 0,0-(bg.getHeight()-height)+400);
                if (as[0] <= (dmg / 2.0) && lvl > 0 && lvl < 16 && as[1] == 0 && as[2] == 0 && respawn0.tick && statik.size() < 4) {
                    as[0]++;
                    bs[0] = lvl / 4 + (int) ((double) (Math.random() * ((double) lvl / 2)));

                    statik.add(new Statik(((bs[0] * 2) + 1) * 10, (bs[0] + 1) * 10, bs[0] + 1, 1, 0, 950, getWidth(), height - 180, "CAT v" + bs[0], ((bs[0] + lvl / 2 + 1) * 360) + 120));
                    respawn0.tick = false;
                }else{
                if (lvl > 2 && as[1] <= (dmg / 3.0) && respawn1.tick && lvl >= 3 && statik.size() < 3) {
                    as[1]++;

                    bs[1] = lvl / 4 + (int) ((double) (Math.random() * ((double) lvl / 2)));    //cont
                    statik.add(new Statik(((bs[1] * 2) + 1) * 15, (bs[1] + 1) * 15, bs[1] + 2, 1.3, 1, 850, getWidth(), height - 180, "DERP v" + bs[1], (bs[1] + lvl / 2 + 1) * 1680));
                    respawn1.tick = false;
                }else{
                if (lvl > 3 && as[2] < 2 && lvl > 10 && respawn2.tick && statik.size() < 2) {
                    as[2]++;

                    bs[2] = lvl / 4 + (int) ((double) (Math.random() * ((double) lvl / 2)));
                    statik.add(new Statik(((bs[2] * 2) + 1) * 30, (bs[2] + 1) * 30, bs[2] + 3, 1.2, 2, 350, getWidth(), height - 180, "Lamb v" + bs[2], (bs[2] + lvl / 2 + 1) * 7800));
                    respawn2.tick = false;
                }else{
                if (!dragonSpawned && lvl > 15 && lvl < 25 && statik.size() < 1) {
                    statik.add(new Statik(450, 450, 14, 1.4, 3, 550, getWidth(), height - 280, "Dragon", 850000));
                    dragonSpawned = true;
                }else{
                if (!warlockSpawned && lvl > 19 && lvl < 30 && statik.size() < 1) {
                    statik.add(new Statik(1050, 1050, 25, 2.0, 4, 850, getWidth(), height - 220, "Warlock", 8500000));
                    warlockSpawned = true;
                }else{
                if (!metalSpawned && lvl > 26 && lvl < 35 && statik.size() < 1) {
                    statik.add(new Statik(5000, 5000, 35, 2.4, 5, 650, getWidth(), height - 220, "???", 50000000));
                    metalSpawned = true;
                }}}}}}
            
            drawStatik(g2d);


            g2d.setColor(new Color(20, 110, 200));
            
            
            if (alive) {

                
                    
               
                    
                    for (int i = 0; i < p; i++) {


                        ob[i].y += 1.0 + 1.0 * (double) 2.0;

                    }
                
                g2d.setColor(new Color(20, 110, 200));
                drawPoints(g2d);
                drawBullets(g2d);
                if (jumping && mayJump > 0) {
                    int rm = mayJump;
                    mayJump = 0;
                    for (int i = 0; i < rm; i++) {
                        if (jumpstate > 0) {
                            if (mayWalk > 0) {
                                y += (jumpspeed);
                                
                                if (jumpspeed < 25) {
                                    jumpspeed++;
                                } else {
                                    jumpstate = 0;
                                    jumping = false;
                                    jumpspeed = -25;
                                }
                            }
                        }

                    }
                }
                 if (xvar >= 0) {
                    g2d.drawImage(character[c - 1], null, (int) x, (int) y);
                }
                if (xvar < 0) {
                    g2d.drawImage(character[c + 5 - 1], null, (int) x, (int) y);
                }
                if (walking) {
                    xvar = -2*Integer.signum((int)(x-wx));
                
                
               
                    g2d.fill3DRect(wx - 4, wy - 4, 8, 8, false);
                    if (mayWalk > 0) {
                        int rm = mayWalk;
                        mayWalk = 0;
                        if (mayDraw) {
                            c++;
                            mayDraw = false;
                        }
                        if (c > 5) {
                            c = 1;
                        }
                        if (xvar > 0) {
                            if (wx - (x + xvar * rm) < 0) {
                                x = wx;
                            } else {

                                x += (int) ((double) xvar * 1.5 * rm * speed);
                            }
                        }
                        if (xvar < 0) {
                            if (((x - wx) - ((-1) * xvar * rm)) < 0) {
                                x = wx;
                            } else {

                                x += (int) ((double) xvar * rm * 1.5* speed);
                            }
                        }

                        if (x == wx || wx - x < 5 && wx - x > -5) {
                            xvar = 0;
                            x = wx;
                            walking = false;
                        }

                    }
                }
                if (invincible > 0) {
                    invincible--;
                }
                drawStats(g2d);
                drawSpell(g2d);
                drawTooltips(g2d);
            } else {
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(Color.darkGray);
                g2d.fill3DRect(20, 20, getWidth() - 40, getHeight() - 60, true);
                g2d.setColor(Color.RED);
                g2d.setFont(new Font("Helvetica", Font.BOLD, 30));
                g2d.drawChars("GAME OVER".toCharArray(), 0, 9, getWidth() / 2 - 80, getHeight() / 2 - 29);

                g2d.setColor(Color.WHITE);
                if (score > 0) {
                    lastscore = score;
                }
                g2d.drawChars(("SCORE: " + lastscore).toCharArray(), 0, ("SCORE: " + lastscore).length(), getWidth() / 2 - 75, getHeight() / 2 + 29);
                if (lastscore > personalhigh) {
                    personalhigh = lastscore;
                    g2d.setColor(Color.GREEN);
                    g2d.drawChars(("NEW HIGHSCORE!").toCharArray(), 0, ("NEW HIGHSCORE!").length(), getWidth() / 2 - 35, getHeight() / 2 + 59);
                }
                generatePoints();

                score = 0;
                invincible = 0;
                statik = new CopyOnWriteArrayList<Statik>();
                hp = maxhp;
                walking = false;
                as = new int[6];
                
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }
    volatile boolean mousePressed = false;

    @Override
    public void mousePressed(MouseEvent me) {
        mx=me.getX();
        my=me.getY();
        if(me.getButton()==3){
            mousePressed = true;
                            Thread t = new ThreadII(this) {

                                @Override
                                public void run() {
                                    while (mousePressed) {
                                        while (!bulletcd.tick) {
                                            try {
                                                Thread.currentThread().sleep(5);
                                            } catch (InterruptedException ex) {
                                                Logger.getLogger(DrawPanel.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                        if(mousePressed){bulletcd = new CD((int) (1500.0 / ((double) (bulletspeed))), dP);
                                        bulletcd.start();
                                        bulletcd.tick = false;
                                        while (bmod);
                                        bmod = true;
                                        bullet.add(new Bullet(1, (int) x + 40, (int) y + 40, mx, my, bulletspeed, Color.BLACK, dP, (float) (((double) my - (double) y) / ((double) mx - (double) x))));

                                        bmod = false;
                                    }}
                                }
                            };
                            t.start();
        }
    }
    @Override
    public void mouseReleased(MouseEvent me) {
        if (!paused) {
            if (display <= 0) {
                mx = me.getX();
                my = me.getY();
            }
            if (me.getButton() == 1) {
                if (alive && !wasd) {
                    walking = true;

                    wx = me.getX();
                    wy = me.getY();
                } else {
                    if (!alive) {
                        walking = true;
                        alive = true;
                        x = 20;
                        jumpstate = 0;
                        jumping = false;
                        jumpspeed = -25;
                        y = height - 180;
                        generatePoints();
                    }
                }
            } else {
                /*if (me.getButton() == 2) {
                giveXP(1280);
                System.out.println(me.getButton());
                System.out.println("Count of objects: " + p);
                System.out.println("getHeight(): " + getHeight());
                int i = 0;
                for (PointEx p1 : ob) {
                i++;
                System.out.println("Ob No. " + i + ": " + p1.x + " / " + p1.y + " with type " + p1.getType());
                }
                } else*/ {
                    if (me.getButton() == 3) {
                        mousePressed=false;
                        if (placingspell > -1) {
                            if (placingspell == 0) {
                                placingspell = -1;
                                displayID = 0;
                                display = 120;
                                spellmx=mx;
                                spellmy=my;
                                CD cd = spellcd.get(0);
                                cd = new CD(5000, this);
                                spellcd.set(0, cd);
                                spellcd.get(0).start();
                            } else {
                                if (placingspell == 1) {
                                    x = me.getX();
                                    walking = false;
                                    placingspell = -1;
                                    CD cd = spellcd.get(1);
                                    cd = new CD(12000, this);
                                    spellcd.set(1, cd);
                                    spellcd.get(1).start();
                                }
                            }
                        } 

                    }
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        mx = me.getX();
            my = me.getY();
    }

    @Override
    public void mouseMoved(MouseEvent me) {
            mx = me.getX();
            my = me.getY();
    }
}

class ThreadII extends Thread {

    DrawPanel dP;

    public ThreadII(DrawPanel dP) {
        this.dP = dP;
    }
}