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
package Election.distributed;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created on 05/12/2023, 09:10:56
 *
 * @author IPT - computer
 * @version 1.0
 */
public class Transactions {

    public static int MAXTRANSACTIONS = 2;

    private final List<String> list; // list not mined

    public Transactions() {
        list = new CopyOnWriteArrayList<>();
    }

    public List<String> getList() {
        return list;
    }

    public boolean contains(String trans) {
        return list.contains(trans);
    }

    public void addTransaction(String newTrans) {
        if (!list.contains(newTrans)) {
            list.add(newTrans);
        }
    }

    public void removeTransactions(List<String> lst) {
        list.removeAll(lst);
    }

    public void synchronize(List<String> other) {
        for (String trans : other) {
            addTransaction(trans);
        }
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 202312050910L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2023  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
