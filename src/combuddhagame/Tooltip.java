/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package combuddhagame;

/**
 *
 * @author andreas
 */
public class Tooltip {
    String message;
    int[] spells;
    int x,y;
    volatile int life;
    public Tooltip(int[] spells,String message, int x,int y, int life){
    this.spells=spells;
    this.message=message;
    this.x=x;
    this.y=y;
    this.life=life;
    }
    public int[] getSpells(){
        return spells;
    }
    public String getMessage(){
        life--;
    return message;
    }
}

