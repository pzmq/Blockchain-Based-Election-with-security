//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     I N S T I T U T O    P O L I T E C N I C O   D E   T O M A R        ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                               (c)2021   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package Election.distributed;


import Election.core.Vote;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import Election.distributed.utils.GuiUtils;
import Election.distributed.utils.RMI;


/**
 *
 * @author IPT
 */
public class TemplarCoin extends javax.swing.JFrame {

    RemoteInterface remote;

    /**
     * Creates new form Test03_GUI_miner
     */
    public TemplarCoin() {
        initComponents();
        setLocationRelativeTo(null);
    }
    
    public TemplarCoin(String adress){
        this();
        txtAdress.setText(adress);
        btConnectActionPerformed(null);
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tpMain = new javax.swing.JTabbedPane();
        pnServerNetwork = new javax.swing.JPanel();
        pnServer = new javax.swing.JPanel();
        btConnect = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txtField3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtAdress = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextPane();
        pnTemplarCoin = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        tpTransaction = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        pnTransaction = new javax.swing.JPanel();
        txtFrom = new javax.swing.JTextField();
        candidatos = new javax.swing.JComboBox<>();
        btRegister = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Miner (c)2023");

        pnServerNetwork.setLayout(new java.awt.BorderLayout());

        pnServer.setLayout(new java.awt.BorderLayout());

        btConnect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Election/multimedia/Connect_to_Server.png"))); // NOI18N
        btConnect.setText("Connect to Server");
        btConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btConnectActionPerformed(evt);
            }
        });
        pnServer.add(btConnect, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new java.awt.BorderLayout());

        txtField3.setLayout(new java.awt.BorderLayout(0, 5));

        jLabel5.setText("Adress");
        txtField3.add(jLabel5, java.awt.BorderLayout.NORTH);

        txtAdress.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        txtAdress.setText("//localhost:10010/RemoteMiner");
        txtAdress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAdressActionPerformed(evt);
            }
        });
        txtField3.add(txtAdress, java.awt.BorderLayout.CENTER);

        jPanel1.add(txtField3, java.awt.BorderLayout.CENTER);

        pnServer.add(jPanel1, java.awt.BorderLayout.CENTER);

        pnServerNetwork.add(pnServer, java.awt.BorderLayout.NORTH);

        jScrollPane2.setPreferredSize(new java.awt.Dimension(64, 200));

        txtLog.setBackground(new java.awt.Color(0, 0, 0));
        txtLog.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtLog.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        jScrollPane2.setViewportView(txtLog);

        pnServerNetwork.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        tpMain.addTab("Client Remote Miner", pnServerNetwork);

        pnTemplarCoin.setLayout(new java.awt.BorderLayout());

        jPanel8.setLayout(new java.awt.BorderLayout());
        pnTemplarCoin.add(jPanel8, java.awt.BorderLayout.NORTH);

        tpTransaction.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tpTransactionStateChanged(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setLayout(new java.awt.BorderLayout());

        pnTransaction.setLayout(new java.awt.GridLayout(4, 1, 5, 5));

        txtFrom.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        txtFrom.setText("Ana");
        txtFrom.setBorder(javax.swing.BorderFactory.createTitledBorder("Eleitor"));
        pnTransaction.add(txtFrom);

        candidatos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Joao", "Toze", "Manuel" }));
        pnTransaction.add(candidatos);

        btRegister.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Election/multimedia/voting-box.png"))); // NOI18N
        btRegister.setLabel("Votar");
        btRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRegisterActionPerformed(evt);
            }
        });
        pnTransaction.add(btRegister);

        jPanel3.add(pnTransaction, java.awt.BorderLayout.NORTH);

        tpTransaction.addTab("Transaction", new javax.swing.ImageIcon(getClass().getResource("/Election/multimedia/templar.png")), jPanel3); // NOI18N

        pnTemplarCoin.add(tpTransaction, java.awt.BorderLayout.CENTER);

        tpMain.addTab("Templar Coin", pnTemplarCoin);

        getContentPane().add(tpMain, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConnectActionPerformed
        try {
            remote = (RemoteInterface) RMI.getRemote(txtAdress.getText());
            setTitle(txtAdress.getText());
            onMessage("Connected to ", txtAdress.getText());
            tpMain.setSelectedComponent( pnTemplarCoin);
        } catch (Exception e) {
            onException("Connect to server", e);
        }
    }//GEN-LAST:event_btConnectActionPerformed

    private void txtAdressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAdressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAdressActionPerformed

    private void btRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRegisterActionPerformed

        try {
            Vote t = new Vote(
                    txtFrom.getText(),
                    candidatos.getSelectedItem().toString()
            );
            remote.addTransaction(t.toText());
        } catch (Exception ex) {
            onException("Add Transaction", ex);
        }


    }//GEN-LAST:event_btRegisterActionPerformed

    private void tpTransactionStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tpTransactionStateChanged

    }//GEN-LAST:event_tpTransactionStateChanged

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
            java.util.logging.Logger.getLogger(TemplarCoin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TemplarCoin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TemplarCoin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TemplarCoin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TemplarCoin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btConnect;
    private javax.swing.JButton btRegister;
    private javax.swing.JComboBox<String> candidatos;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel pnServer;
    private javax.swing.JPanel pnServerNetwork;
    private javax.swing.JPanel pnTemplarCoin;
    private javax.swing.JPanel pnTransaction;
    private javax.swing.JTabbedPane tpMain;
    private javax.swing.JTabbedPane tpTransaction;
    private javax.swing.JTextField txtAdress;
    private javax.swing.JPanel txtField3;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextPane txtLog;
    // End of variables declaration//GEN-END:variables

    public void onException(String title, Exception ex) {
        GuiUtils.addText(txtLog, title, ex.getMessage(), Color.RED, Color.MAGENTA);
        JOptionPane.showMessageDialog(this, ex.getMessage(),
                title, JOptionPane.ERROR_MESSAGE);
        Logger.getAnonymousLogger().log(Level.SEVERE, null, ex);
    }

    public void onMessage(String title, String msg) {
        GuiUtils.addText(txtLog, title, msg, Color.GREEN, Color.WHITE);
    }

}
