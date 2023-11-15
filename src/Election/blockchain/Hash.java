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
//::                                                               (c)2022   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package Election.blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Election.utils.SecurityUtils;
import static Election.utils.SecurityUtils.calculateHash;

/**
 * Created on 28/09/2022, 11:02:33
 *
 * @author IPT - computer
 * @version 1.0
 */
public class Hash {

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String toHexString(int n) {
        return Integer.toHexString(n).toUpperCase();
    }

    public static String getHash(String data) {
        byte[] hash = null;
        try {
            hash= calculateHash(data.getBytes());
        } catch (Exception ex) {
            Logger.getLogger(Hash.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hash.toString();
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 202209281102L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2022  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
