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

import Election.distributed.RemoteObject;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

/**
 * Created on 06/12/2023, 18:06:52
 *
 * @author manso - computer
 */
public class LastBlock {

    RemoteObject myNode;

    public LastBlock(RemoteObject myNode) {
        this.myNode = myNode;
    }

    public Block getLastBlock() throws RemoteException {
//        List<Block> lst = new ArrayList<>();
//        try {
//            lst.add(myNode.blockchain.getLast());
//            for (RemoteInterface node : myNode.getNetwork()) {
//                lst.add(node.getBlockchain().getLast());
//            }
//
//        } catch (Exception e) {
//        }
        List blks = myNode.getLastBlock(new Date().getTime(), 0, 7);

        return (Block) new Histogram().getMostCommon(blks);

    }

}
