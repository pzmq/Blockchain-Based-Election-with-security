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
package Election.blockchain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created on 19/09/2023, 21:22:39
 *
 * @author manso - computer
 */
public class Converter {

    /**
     *
     * @param data
     * @return
     */
    public static String byteArrayToHex(byte[] data) {
        // return new BigInteger(data).toString(16).toUpperCase();
        final StringBuilder builder = new StringBuilder();
        for (byte b : data) {
            builder.append(String.format("%02X", b));
        }
        return builder.toString();
    }

    public static byte[] hexToByteArray(String hex) {
        byte[] b = new byte[hex.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(hex.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
        //return new BigInteger(data, 16).toByteArray();
    }

    public static byte[] objectToByteArray(Object data) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(data);
            return bos.toByteArray();
        }
    }

    public static Object byteArrayToObject(byte[] bytes) throws Exception {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInputStream in = new ObjectInputStream(bis)) {
            return in.readObject();
        }
    }

    public static String objectToHex(Object obj) {
        try {
            return byteArrayToHex(objectToByteArray(obj));
        } catch (IOException ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
            return "ERROR objectToHex";
        }
    }

    public static Object hexToObject(String hex) {
        try {
            return byteArrayToObject(hexToByteArray(hex));
        } catch (Exception ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
            return "ERROR hexToObject";
        }
    }

    public static void main(String[] args) {
        String txt = "Hello Hex world 123";
        String hex = byteArrayToHex(txt.getBytes());
        System.out.println("HEX   = " + hex);
        System.out.println("PLAIN = " + new String(hexToByteArray(hex)));

        String hex2 = objectToHex(txt);
        System.out.println("HEX   = " + hex2);
        String obj = (String) hexToObject(hex2);
        System.out.println("OBJ = " + obj);
    }

}
