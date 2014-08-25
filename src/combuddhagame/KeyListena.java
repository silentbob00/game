/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package combuddhagame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author andreas
 */
public class KeyListena implements KeyListener {

    DrawPanel D;

    public KeyListena(DrawPanel D) {
        this.D = D;
        D.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        System.out.println("Key pressed:" + ke.getKeyCode());
        if(ke.getKeyCode()==107){
                    D.giveXP(80*D.lvl*D.lvl*D.lvl);
                }
        if (ke.getKeyCode() == 68) {
            D.walking = true;
            D.wx = D.width;
        }
        if (ke.getKeyCode() == 65) {
            D.walking = true;
            D.wx = 0;
        }
        if (ke.getKeyCode() == 80) {
            if (D.paused) {
                D.paused = false;
                D.respawn0 = new CD(25000, D);
                D.respawn0.start();
                D.respawn0.tick = false;
                D.respawn1 = new CD(65000, D);
                D.respawn1.tick = false;
                D.respawn1.start();
                D.respawn2 = new CD(90000, D);
                D.respawn2.tick = false;
                D.respawn2.start();

                for (CD spellcd : D.spellcd) {
                    int cd = spellcd.cd;
                    spellcd = new CD(cd, D);
                    spellcd.start();
                    spellcd.tick = false;
                }
            } else {
                D.paused = true;
            }
        } else {
            if (!D.paused) {

                if (ke.getKeyCode() == 32) {
                    if (!(D.jumping)) {
                        D.jumping = true;
                        D.jumpstate = 1;
                    }
                } else {
                    if (ke.getKeyCode() == 49) {
                        if (D.lvl >= 2 && D.placingspell == -1 && D.spellcd.get(0).tick) {
                            D.placingspell = 0;
                            D.spellcd.get(0).tick = false;
                        }
                    } else {
                        if (ke.getKeyCode() == 50) {
                            if (D.lvl >= 4 && D.placingspell == -1 && D.spellcd.get(1).tick) {
                                D.placingspell = 1;
                                D.spellcd.get(1).tick = false;
                            }
                        } else {


                            if (ke.getKeyCode() == 51) {
                                if (D.lvl >= 8 && D.placingspell == -1 && D.spellcd.get(D.chosenSpell).tick) {
                                    switch (D.chosenSpell) {
                                        case 2:
                                            D.hp += ((D.maxhp / 10) + (int)(((double)(D.abilityPower/2))));
                                            if (D.hp > D.maxhp) {
                                                D.hp = D.maxhp;
                                            }
                                            break;
                                        case 3:
                                            D.invincible += 500 + D.dmg * 10;
                                            break;
                                    }
                                    D.spellcd.get(D.chosenSpell).tick = false;
                                }
                            } else {
                                if (D.choosing) {
                                    switch (ke.getKeyCode()) {
                                        case 72:
                                            D.chosenSpell = 2;
                                            D.choosing = false;
                                            D.tt.remove(D.tt.size()-1);
                                            break;
                                        case 73:
                                            D.chosenSpell = 3;
                                            D.choosing = false;
                                            D.tt.remove(D.tt.size()-1);
                                            break;
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {

        if (ke.getKeyCode() == 68 && (D.wx>D.x)){
            D.walking = false;
            D.wx = (int)D.x;
            
        }
        if (ke.getKeyCode() == 65 && (D.wx<=D.x)){
                D.walking = false;
            D.wx = (int)D.x;
            
        }
        if (ke.getKeyCode() == 27) {
            D.placingspell=-1;
        }
                
    }
}