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
package Election.distributed;

import java.io.Serializable;
import Election.distributed.utils.Serializer;

/**
 *
 * @author manso
 */
public class Transfer implements Serializable {

    private String from;
    private String to;
    private double value;
    

    public Transfer(String from, String to, double value) {
        this.from = from;
        this.to = to;
        this.value = value;    
    }

    public Transfer(String from, String to, double value, String sign) {
        this.from = from;
        this.to = to;
        this.value = value;
    }

    public double getValue() {
        return value;
    }


    public void setValue(double value) {
        this.value = value;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
         return     "From : "+from
                + "\nTo   : " + to
                + "\nValue: " + value;                
    }

    public String toText() {
        return Serializer.objectToBase64(this);
    }

    public static Transfer fromText(String obj) {
        return (Transfer)Serializer.base64ToObject(obj);
    }

    @Override
    public int hashCode() {
        return toText().hashCode();
    }

    @Override
    public boolean equals(Object t) {
        if (t instanceof Transfer) {
            return this.toText().equals(((Transfer) t).toText());
        }
        return false;
    }
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 202312050910L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2023  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
