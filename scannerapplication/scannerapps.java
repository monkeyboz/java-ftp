/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scannerapplication;

import java.util.Vector;
import java.util.List;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import uk.co.mmscomputing.device.scanner.Scanner;
import uk.co.mmscomputing.device.scanner.ScannerDevice;
import uk.co.mmscomputing.device.scanner.ScannerIOException;
import uk.co.mmscomputing.device.scanner.ScannerIOMetadata;
import uk.co.mmscomputing.device.scanner.ScannerListener;

/**
 *
 * @author user1
 */
@SuppressWarnings("serial")
public class scannerapps extends javax.swing.JFrame implements java.awt.event.FocusListener, java.awt.event.ActionListener, uk.co.mmscomputing.device.scanner.ScannerListener {

    String localhost;
    String username;
    String password;
    String uploadDirectory;
    String[] files;
    FTPClient ftp;
	List<String> fileUpload;
    Scanner scanner;
    /**
     * Creates new form scannerapps
     */
    public scannerapps() {
        initComponents();
        localhost = jTextField1.getText();
        username = jTextField2.getText();
        password = jTextField3.getText();

        jList1.addFocusListener(this);
        jTextField1.addFocusListener(this);
        jTextField2.addFocusListener(this);
        jTextField3.addFocusListener(this);

        jButton1.addActionListener(this);
        jButton2.addActionListener(this);
        jButton3.addActionListener(this);
        jButton4.addActionListener(this);

        jButton3.setEnabled(false);

        File filename = new File("./");
        String currentPath = filename.getAbsolutePath();
        currentPath = currentPath.substring(0, currentPath.length()-2);
        uploadDirectory = currentPath;

        Vector holder = new Vector();
        for(File fileEntry : filename.listFiles()){
            if(!fileEntry.isDirectory()){
                holder.add(fileEntry.getName());
                System.out.println(fileEntry.getName());
            }
        }
        jList1.setListData(holder);

        ftp = new FTPClient();
        scanner = Scanner.getDevice();
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == jButton1){
            System.out.println("Setting FTP");
            getFtp();
        } else if(e.getSource() == jList1) {
            fileUpload = jList1.getSelectedValuesList();
        } else {
            System.out.println("Setting Scanner");
            getScanner(e);
        }
    }

    public void getScanner(ActionEvent e){
        try{
            if(e.getSource() == jButton2){
                jButton3.setEnabled(true);
                this.scanner.select();

                this.scanner.addListener(this);
            } else if(e.getSource() == jButton3) {
                this.scanner.acquire();
            } else {
                this.scanner.setCancel(true);
            }
        } catch(ScannerIOException ex) {
            ex.printStackTrace();
        }
    }

    public void focusGained(FocusEvent e){
        System.out.println(e.getComponent().getName());
        String source = e.getComponent().getName();
        //source.replaceAll("/\d+/", username);
        switch(source){
            case "jList1":
                fileUpload = jList1.getSelectedValuesList();
                break;
            case "jTextField":
                break;
            case "jButton":
                break;
        }
    }

    public void focusLost(FocusEvent e){

    }

    public void update(ScannerIOMetadata.Type type, ScannerIOMetadata metadata){
        if(type.equals(ScannerIOMetadata.ACQUIRED)){
            BufferedImage image=metadata.getImage();
            System.out.println("Have an image now!");
            try{
                File file = new File(uploadDirectory);
                System.out.println("Image Path: "+file.getAbsolutePath());
                ImageIO.write(image, "jpg", file);
            }catch(Exception e){
                e.printStackTrace();
            }
        }else if(type.equals(ScannerIOMetadata.NEGOTIATE)){
          ScannerDevice device=metadata.getDevice();
        /*
          try{
        //        device.setShowUserInterface(false);
            device.setShowProgressBar(true);
            device.setRegionOfInterest(20,40,300,200);
            device.setResolution(100.0);
          }catch(Exception e){
            e.printStackTrace();
          }
        */
        }else if(type.equals(ScannerIOMetadata.STATECHANGE)){
          System.err.println(metadata.getStateStr());
        }else if(type.equals(ScannerIOMetadata.EXCEPTION)){
          metadata.getException().printStackTrace();
        }
    }

    private void getFtp(){
        System.out.println("Get Upload Files");
        try{
            localhost = jTextField1.getText();
            username = jTextField2.getText();
            password = jTextField1.getText();

            ftp.connect(localhost);
            ftp.login(username,password);

            System.out.print(ftp.getReplyString());

            ftp.enterLocalPassiveMode();
            
            for(String i : fileUpload){
                System.out.println(i.toString());
                try{
                    System.out.println(uploadDirectory+"/"+i.toString());
                    InputStream fileU = new FileInputStream(uploadDirectory+"/"+i.toString());
                    ftp.storeFile(i.toString(), fileU);
                } catch(IOException ex){
                    ex.printStackTrace();
                }
            }

            ftp.disconnect();

            System.out.println("Working Great");

        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    //
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TWinc. - Scanner Upload");

        jLabel1.setText("Localhost");

        jLabel2.setText("Username");

        jLabel3.setText("Password");

        jLabel4.setText("Scanned Documents");

        jButton1.setText("Upload");
        jButton1.setName("Select Scanner"); // NOI18N

        jButton2.setText("Select Scanner");
        jButton2.setName("jButton1"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextField1.setName("jTextField1"); // NOI18N

        jTextField2.setName("jTextField2"); // NOI18N

        jTextField3.setName("jTextField3"); // NOI18N

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.setName("jList1"); // NOI18N
        jScrollPane1.setViewportView(jList1);

        jButton3.setText("Scan Document");
        jButton3.setName("Scan Document"); // NOI18N

        jButton4.setText("Remove Scanner");
        jButton4.setName("Remove Scanner"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane1)
                        .addComponent(jLabel4)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jButton1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton4)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                            .addComponent(jTextField1)
                            .addComponent(jTextField2))))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }//                        

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }                                        

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //
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
            java.util.logging.Logger.getLogger(scannerapps.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(scannerapps.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(scannerapps.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(scannerapps.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new scannerapps().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration
}