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
public class CD extends Thread{

    public int cd;
    public DrawPanel dP;
    public volatile boolean tick = false;

    public CD(int i, DrawPanel dP) {
        cd = i;
        this.dP = dP;

    }

    @Override
    public synchronized void run() {
        while (true) {
            try {
                Thread.currentThread().sleep(cd);
                tick = true;
            } catch (InterruptedException ex) {
                Logger.getLogger(CD.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
}
}
