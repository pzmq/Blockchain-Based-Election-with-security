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
package Election.core;

import java.io.Serializable;
import java.util.Locale;
import Election.utils.Converter;
import static Election.utils.Converter.objectToByteArray;
import Election.wallet.User;
import Election.utils.SecurityUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Ricardo Saúde e Pedro Martinho 
 * com base no código de António Manso
 */
public class Vote implements Serializable {

    private String voter;
    private String candidate;
    private byte[] Signature;

    public byte[] getSignature() {
        return Signature;
    }

    public Vote(String voter, String candidate) {
        this.voter = voter;
        this.candidate = candidate;
    }

    public String getCandidate() {
        return candidate;
    }
    
    public void SignVote(User user){
        //Converte o voto em objeto
        byte[] voteObject = null;
        
        //Tenta converter para byteArray
        try {
            voteObject = objectToByteArray(this.toString());
        } catch (IOException ex) {
            Logger.getLogger(Vote.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Tenta assinar
        try {
            Signature = SecurityUtils.sign(voteObject,user.getPrivKey());
        } catch (Exception ex) {
            Logger.getLogger(Vote.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isValid(User user){
        //Se votante e/ou votado forem nulos nao ha voto
        if (voter.trim().isEmpty() || candidate.trim().isEmpty()) {
            return false;
        }
        
        //Converte o voto em objeto
        byte[] voteObject = null;
        try {
            voteObject = objectToByteArray(this.toString());
        } catch (IOException ex) {
            Logger.getLogger(Vote.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Verify Sign
        try {
            return SecurityUtils.verifySign(voteObject,Signature,user.getPubKey());
        } catch (Exception ex) {
            Logger.getLogger(Vote.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return true;
    }
    
    @Override
    public String toString() {
        //format values candidate english notation

        return String.format("%-10s -> %s (Sign %s) ", voter,  candidate, Arrays.toString(Signature));
        //return voter + "\t : " + candidate + "\t -> " + value;
    }
    
    public String toText(){
       return Converter.objectToHex(this);
    }
    
    public List toList(){
        List<String> l = new ArrayList<String>();
        l.add(voter);
        l.add(candidate);
        return l;
    }
    
    public static Vote fromText(String obj){
       return (Vote) Converter.hexToObject(obj);
    }
    
    public static long serialVersionUID = 123;

}
