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

import Election.core.Vote;
import static Election.wallet.User.getUserFileName;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created on 22/08/2022, 10:09:17
 *
 * @author IPT - computer
 * @version 1.0
 */
public class BlockChain implements Serializable {

    ArrayList<Block> chain = new ArrayList<>();
    ArrayList<Vote> tmpVotes = new ArrayList<>();
    public static String DEFAULT_FILENAME = "blockchain.bl";
    
    Integer maxVotesPerBlock = 5;
    String filename;
    
    public ArrayList<Block> getChain() {
        return chain;
    }

    /**
     * gets the last block hash of the chain
     *
     * @return last hash in the chain
     */
    
    public String getLastBlockHash() {
        //Genesis block
        if (chain.isEmpty()) {
            return String.format("%08d", 0);
        }
        //hash of the last in the list
        return chain.get(chain.size() - 1).hash;
    }

    /**
     * adds MerkleTreeRoot to the blockChain
     *
     * @param vote
     * @param dificulty dificulty of block to miners (POW)
     */
    public void add(Vote vote, int dificulty) {
       
        try {
            //Adicionar sempre o voto ao temporario
            tmpVotes.add(vote);
            
            if(tmpVotes.size() == maxVotesPerBlock){
                
                //Inicializa o bloco
                //addNewBlock(dificulty);
                
                //limpa votos
                tmpVotes.clear();
                PrintWriter pw = new PrintWriter(filename.split(",")[0]+".votes");
                pw.close();
                
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BlockChain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addBlock(Block newBlock) throws BlockchainException {
      //se bão for válido
        if (!newBlock.isValid()) {
            throw new BlockchainException("Block corrupted");
        }
        //se não encaixar no último
        if (!newBlock.getPrevious().equals(getLast().getHash())) {
            throw new BlockchainException("Block does not fit in the last");
        }
        //adicionar o bloco
        this.chain.add(newBlock);
    }

    public Block get(int index) {
        return chain.get(index);
    }

    @Override
    public String toString() {
        StringBuilder txt = new StringBuilder();
        txt.append("Blochain size = ").append(chain.size()).append("\n");
        for (Block block : chain) {
            txt.append(block.toString()).append("\n");
        }
        return txt.toString();
    }

    public void save(String fileName) throws Exception {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(chain);
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName.split(",")[0]+".votes"))) {
            out.writeObject(tmpVotes);
        }
    }

    private void loadChain(String fileName){
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            this.chain = (ArrayList<Block>) in.readObject();
        } catch (IOException ex) {
            Logger.getLogger(BlockChain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BlockChain.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void loadTmpVotes(String fileName){
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName.split(",")[0]+".votes"))) {
            this.tmpVotes = (ArrayList<Vote>) in.readObject();
        } catch (IOException ex) {
            Logger.getLogger(BlockChain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BlockChain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void load(String fileName){
        this.filename = fileName;
        loadChain(fileName);
        loadTmpVotes(fileName);
    }

    public boolean isValid() {
        //Validate blocks
        for (Block block : chain) {
            if (!block.isValid()) {
                return false;
            }
        }
        //validate Links
        //starts in the second block
        for (int i = 1; i < chain.size(); i++) {
            if (!chain.get(i).isValid() 
            || !chain.get(i).previous.equals(chain.get(i - 1).hash)) {
             //blockchain invalida
                return false;
            }
        }
        return true;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 202208221009L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2022  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

    public Block getLast() {
        if(chain.size() > 0){
            return chain.get(chain.size()-1);
        }
        return null;
    }
}
