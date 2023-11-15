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

import java.io.Serializable;

/**
 * Created on 22/08/2022, 09:23:49
 * 
 * Block with consensus of Proof of Work
 *
 * @author IPT - computer
 * @version 1.0
 */
public class Block implements Serializable {

    String previousHash; // link to previous block
    String MerkleTreeRoot;         // MerkleTreeRoot in the block
    int nonce;           // proof of work 
    String currentHash;  // Hash of block
    MerkleTreeString tree; //MT of block

    public MerkleTreeString getTree() {
        return tree;
    }
    
    public String getPreviousHash() {
        return previousHash;
    }

    public String getMerkleTreeRoot() {
        return MerkleTreeRoot;
    }

    public int getNonce() {
        return nonce;
    }

    public String getCurrentHash() {
        return currentHash;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    
    
    public Block(String previousHash, String mkroot, MerkleTreeString tree, int nonce) {
        this.previousHash = previousHash;
        this.MerkleTreeRoot = mkroot;
        this.nonce = nonce;
        this.tree = tree;
        this.currentHash = calculateHash();
    }

    public String calculateHash() {
        return Hash.getHash(nonce + previousHash + MerkleTreeRoot);
    }

    public String toString() {
        return // (isValid() ? "OK\t" : "ERROR\t")+
                 String.format("[ PrevHash : %8s", previousHash) +
                   String.format(" Hash : %8s",currentHash) +  String.format("nonce: %7d ] = ", nonce) + 
                String.format("%-10s", MerkleTreeRoot);

    }

    public boolean isValid() {
        return currentHash.equals(calculateHash());
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 202208220923L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2022  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
