/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slideviewer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class Main extends javax.swing.JFrame {

    private String receiveMessage, sendMessage;
    private ImageViewer imageViewer;
    private int next = 0;
    private int numOfSlide = 0;

    /**
     * Creates new form NewJFrame
     */
    public Main() {
        initComponents();
        showSlideButton.setVisible(false);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        openButton = new javax.swing.JButton();
        showSlideButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        openButton.setText("Open");
        openButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openButtonActionPerformed(evt);
            }
        });

        showSlideButton.setText("SliedShow");
        showSlideButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showSlideButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(133, 133, 133)
                        .addComponent(showSlideButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(151, 151, 151)
                        .addComponent(openButton)))
                .addContainerGap(143, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(openButton)
                .addGap(18, 18, 18)
                .addComponent(showSlideButton)
                .addContainerGap(181, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openButtonActionPerformed
        SlideViewer slideViewer = new SlideViewer();
        try {
            numOfSlide = slideViewer.OpenSlide();
            showSlideButton.setVisible(true);
            imageViewer = new ImageViewer(numOfSlide);
            server();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_openButtonActionPerformed

    private void showSlideButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showSlideButtonActionPerformed
        
        try {
            imageViewer.slideShow("/root/NetBeansProjects/SlideViewer/images/ppt_image0.png", 0);
            next = 1;
        } catch (MalformedURLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_showSlideButtonActionPerformed

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
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton openButton;
    private javax.swing.JButton showSlideButton;
    // End of variables declaration//GEN-END:variables

    private void server() {

        new Thread() {
            public void run() {
                try {
                    ServerSocket sersock = new ServerSocket(12345);
                    System.out.println("Server  ready for chatting");
                    Socket sock = sersock.accept();

                    // receiving from server ( receiveRead  object)
                    InputStream istream = sock.getInputStream();
                    BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));
                    while (true) {

                        if ((receiveMessage = receiveRead.readLine()) != null) {
                            System.out.println(receiveMessage);
                                                                                  
                            if (receiveMessage.equals(".next")) {
                                if (next < numOfSlide) {
                                    next = next + 1;
                                    imageViewer.slideShow("/root/NetBeansProjects/SlideViewer/images/ppt_image" + next + ".png", next);
                                }

                            } else if (receiveMessage.equals(".previous")) {
                                if (next >= 0) {
                                    next = next - 1;
                                    imageViewer.slideShow("/root/NetBeansProjects/SlideViewer/images/ppt_image" + next + ".png", next);
                                }
                            } else if(receiveMessage.equals(".close")){
                                imageViewer.close();
                            } else if(receiveMessage.equals(".exit")){
                                
                                imageViewer.exit();
                            }
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();
    }
}