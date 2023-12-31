/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Election.blockchain;


import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author manso
 */
public class BlockchainException extends Exception {

    public BlockchainException(String msg) {
        super(msg);
    }

    public void show() {
        JOptionPane.showMessageDialog(null, getMessage(),
                "Blockchain Exception", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void printStackTrace() {
        Logger.getAnonymousLogger().log(Level.SEVERE, getMessage(), this);
    }

}
