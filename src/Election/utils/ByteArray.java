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
//::                                                               (c)2023   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package Election.utils;

import java.math.BigInteger;

/**
 * Created on 19/09/2023, 12:20:09
 *
 * @author manso - computer
 */
public class ByteArray {

    public static String toHex(byte[] byteArray) {
        return new BigInteger(1, byteArray).toString(16).toUpperCase();
    }

    public static byte[] fromHex(String hex) {
        return new BigInteger(hex, 16).toByteArray();
    }
    
     public static String toBase64(byte[] byteArray) {
        return new BigInteger(1, byteArray).toString(16).toUpperCase();
    }

    public static byte[] fromBase64(String hex) {
        return new BigInteger(hex, 16).toByteArray();
    }
    
    public static void main(String[] args) {
        String txt = "Hello world 123";
        String hex = toHex(txt.getBytes());
        System.out.println("Hex  = " + hex);
        System.out.println("Plain= " + new String(fromHex(hex)));
        String b64 = toBase64(txt.getBytes());
        System.out.println("Base64  = " + b64);
        System.out.println("Plain= " + new String(fromBase64(b64)));
        
    }

}
