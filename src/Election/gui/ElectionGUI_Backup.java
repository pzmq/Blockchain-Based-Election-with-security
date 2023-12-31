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
//::                                                               (c)2020   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package Election.gui;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import Election.core.Vote;
import Election.wallet.User;

/**
 *
 * @author manso
 */
public class ElectionGUI_Backup extends javax.swing.JFrame {
    public static String fileElection = "Election.obj";
    // election;
    private User user;
    
    /**
     * Creates new form TemplarCoinGUI
     * @param user
     */
    public ElectionGUI_Backup(User user) {
        initComponents();
        this.user = user;
        try {
        } catch (Exception e) {
        }
        txtFrom.setText(user.getName());
       // txtLeger.setText(election.toString());
        //txtBlochains.setText(election.getSecureLedger().toString());
        setSize(800, 600);
        setLocationRelativeTo(null);        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLeger = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtBlochains = new javax.swing.JTextArea();
        tpTransaction = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        pnTransaction = new javax.swing.JPanel();
        txtFrom = new javax.swing.JTextField();
        txtTo = new javax.swing.JTextField();
        btRegister = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        pnUsersBalance = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstUsers = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Election App");

        txtLeger.setColumns(20);
        txtLeger.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        txtLeger.setRows(5);
        jScrollPane1.setViewportView(txtLeger);

        jTabbedPane1.addTab("Ledger", new javax.swing.ImageIcon(getClass().getResource("/Election/multimedia/blockchain.png")), jScrollPane1); // NOI18N

        txtBlochains.setColumns(20);
        txtBlochains.setRows(5);
        jScrollPane3.setViewportView(txtBlochains);

        jTabbedPane1.addTab("blockchain", jScrollPane3);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        tpTransaction.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tpTransactionStateChanged(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setLayout(new java.awt.BorderLayout());

        pnTransaction.setLayout(new java.awt.GridLayout(4, 1, 5, 5));

        txtFrom.setEditable(false);
        txtFrom.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        txtFrom.setBorder(javax.swing.BorderFactory.createTitledBorder("Eleitor"));
        pnTransaction.add(txtFrom);

        txtTo.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        txtTo.setBorder(javax.swing.BorderFactory.createTitledBorder("Lista"));
        txtTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtToActionPerformed(evt);
            }
        });
        pnTransaction.add(txtTo);

        btRegister.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Election/multimedia/cash-icon.png"))); // NOI18N
        btRegister.setText("Registar o voto");
        btRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRegisterActionPerformed(evt);
            }
        });
        pnTransaction.add(btRegister);

        jPanel1.add(pnTransaction, java.awt.BorderLayout.NORTH);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Election/multimedia/templarCoin.png"))); // NOI18N
        jPanel1.add(jLabel1, java.awt.BorderLayout.CENTER);

        tpTransaction.addTab("Votacao", new javax.swing.ImageIcon(getClass().getResource("/Election/multimedia/templar.png")), jPanel1); // NOI18N

        pnUsersBalance.setLayout(new java.awt.BorderLayout());

        lstUsers.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        lstUsers.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lstUsers);

        pnUsersBalance.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        tpTransaction.addTab("Votos", new javax.swing.ImageIcon(getClass().getResource("/Election/multimedia/users_ledger_24.png")), pnUsersBalance); // NOI18N

        getContentPane().add(tpTransaction, java.awt.BorderLayout.WEST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtToActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtToActionPerformed

    private void btRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRegisterActionPerformed
        try {
            Vote vote = new Vote(
                    txtFrom.getText(),
                    txtTo.getText()
            );
           // election.add(vote,user);
            //txtLeger.setText(election.toString());
            //txtBlochains.setText(election.getSecureLedger().toString());
            //election.save(fileElection);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            Logger.getLogger(ElectionGUI_Backup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btRegisterActionPerformed

    private void tpTransactionStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tpTransactionStateChanged
        if( tpTransaction.getSelectedComponent()== pnUsersBalance){
            DefaultListModel model = new DefaultListModel();
            //model.addAll(election.getCandidateVotes());
            lstUsers.setModel(model);
        }
    }//GEN-LAST:event_tpTransactionStateChanged

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(ElectionGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ElectionGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ElectionGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ElectionGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ElectionGUI().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btRegister;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JList<String> lstUsers;
    private javax.swing.JPanel pnTransaction;
    private javax.swing.JPanel pnUsersBalance;
    private javax.swing.JTabbedPane tpTransaction;
    private javax.swing.JTextArea txtBlochains;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextArea txtLeger;
    private javax.swing.JTextField txtTo;
    // End of variables declaration//GEN-END:variables
}
