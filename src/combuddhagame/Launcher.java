/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Launcher.java
 *
 * Created on 23/08/2014, 3:35:57 PM
 */
package combuddhagame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author andreas
 */
public class Launcher extends javax.swing.JFrame {

    /** Creates new form Launcher */
    public Launcher() {
        load();
        initComponents();
        try {
            jLabel1.setSize(0,0);
            jLabel1.setIcon(new ImageIcon(ImageIO.read(new File("character/1.png"))));
        } catch (IOException ex) {
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
        }
            if(loaded){
                jLabel2.setText("HP: "+maxhp+" / "+maxhp);
                jLabel3.setText("Level: "+lvl);
                jLabel4.setText("XP: "+xp+" / "+maxxp);
                jLabel5.setText("Ability Power: "+abilityPower);
                jLabel6.setText("Damage: "+dmg);
                jLabel7.setText("Bullet Speed: "+bulletspeed);
        }
    }
    int maxhp,abilityPower,lvl,maxxp,xp;
    double dmg,speed,bulletspeed;
    long personalhigh;
    int chosenSpell;
    float crit;
    boolean loaded=false;
 public void load() {
        File f = new File(System.getProperty("user.home") + "/.game/save.dat");
        try {
            if (f.exists()) {
                BufferedReader in = new BufferedReader(new FileReader(f));
                try {
                    String[] kit = in.readLine().split(";");
                    if (kit[0].contains("OLOL") && kit[0].split(":")[1].equals("5")) {
                        loaded=true;
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
                        
                        abilityPower = (int) lvl + lvl * 2;
                        
                        } else {
                            if (kit[0].contains("OLOL") && kit[0].split(":")[1].equals("4")) {
                            loaded=true;
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
                                
                                abilityPower = (int) lvl + lvl * 2;

                                dmg = 1 + (0.1 + (0.01 * lvl)) * lvl;
                                
                            } else {
                                f.delete();
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
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jCheckBox2 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jCheckBox1.setText("Fullscreen");

        jButton1.setText("Start Over");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Continue");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Mouse Movement");

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("WASD");

        jLabel2.setText("HP:");

        jLabel3.setText("Level:");

        jLabel4.setText("XP:");

        jLabel5.setText("Ability Power:");

        jLabel6.setText("Damage:");

        jLabel7.setText("Bullet Speed:");

        jCheckBox2.setText("Hardcore");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2))
                            .addComponent(jCheckBox2)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBox2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addGap(3, 3, 3)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2)))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
boolean fullscreen,wasd;
if(jRadioButton1.isSelected()){
    wasd=false;
}else{// TODO add your handling code here:
    wasd=true;
}
if(jCheckBox1.isSelected())fullscreen=true;
else fullscreen=false;
this.setVisible(false);
Game g=new Game(fullscreen,wasd,jCheckBox2.isSelected());
g.setVisible(true);
g.show();
}//GEN-LAST:event_jButton2ActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
File saveFile=new File(System.getProperty("user.home")+"/.game/save.dat");
if(saveFile.exists())while(!saveFile.delete());
boolean fullscreen,wasd;
if(jRadioButton1.isSelected()){
    wasd=false;
}else{// TODO add your handling code here:
    wasd=true;
}
if(jCheckBox1.isSelected())fullscreen=true;
else fullscreen=false;
this.setVisible(false);
Game g=new Game(fullscreen,wasd,jCheckBox2.isSelected());
g.setVisible(true);
g.show();
}//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Launcher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Launcher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Launcher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Launcher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Launcher().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    // End of variables declaration//GEN-END:variables
}
