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
    public double height;
    public int spellID;
    public Tooltip(int spellID,String message, int x,int y, int life){
        this(new int[0],message,x,y,life);
        this.spellID=spellID;
    }
    public Tooltip(int[] spells,String message, int x,int y, int life){
        System.out.println("New tooltip msg: \""+message+"\" x:"+x+" y:"+y+" life="+life);
    this.spells=spells;
    this.message=message;
    this.x=x;
    this.y=y;
    this.life=life;
    this.spellID=-1;
    int lines=message.split("\n").length;
    height=0.5*lines;
    }
    public int[] getSpells(){
        return spells;
    }
    public String getMessage(){
        life--;
    return message;
    }
}

