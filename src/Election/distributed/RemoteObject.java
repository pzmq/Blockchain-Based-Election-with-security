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

import Election.distributed.utils.Serializer;
import Election.blockchain.Block;
import Election.blockchain.BlockChain;
import Election.blockchain.BlockchainException;
import Election.blockchain.LastBlock;
import Election.blockchain.MerkleTreeString;
import Election.core.ElectionCore;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author manso
 */
public class RemoteObject extends UnicastRemoteObject implements RemoteInterface {

    MiningListener listener;
    MinerP2P myMiner;
    //Map<String,VoteList> voteList;
    
    public Block miningBlock; // block in mining process
    
    //Blockchains
    //public Dictionary<String,BlockChain> blockchains;
    
    
    Map<String,ElectionCore> elections;

    private String address; // nome do servidor
    private List<RemoteInterface> network; // network of miners
    private Dictionary<String,List<String>> candidates;

    /**
     * creates a object listening the port
     *
     * @param port port to listen
     * @param listener listener do objeto
     * @throws RemoteException
     */
    public RemoteObject(int port, MiningListener listener) throws RemoteException {
        super(port);
        try {
            this.listener = listener;
            this.myMiner = new MinerP2P(listener);
            //atualizar o endereço do objeto remoto
            address = "//" + InetAddress.getLocalHost().getHostAddress() + ":" + port + "/" + RemoteInterface.OBJECT_NAME;
            //inicializar nova rede
            network = new CopyOnWriteArrayList<>();
            //inicializar novas transações
            //voteList = new Hashtable<>();
             //merkle tree
            MerkleTreeString tree = new MerkleTreeString();
            String root = tree.getRoot();
            this.miningBlock = new Block("dummy", "dummy",root, 1);
            //inicializar blockchains
            listener.onStartServer(Election.distributed.utils.RMI.getRemoteName(port, RemoteInterface.OBJECT_NAME));
        } catch (Exception e) {
            address = "unknow" + ":" + port;

        }
    }

    @Override
    public String getAdress() throws RemoteException {
        return address;
    }

    public String getClientName() {
        //informação do cliente
        try {
            return RemoteServer.getClientHost();
        } catch (ServerNotActiveException ex) {
        }
        return "Anonimous";
    }
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::               M I N E I R O 
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @Override
    public String getHash(int nonce, String msg) throws RemoteException {
        //informação do cliente       
        System.out.println("Hashing to " + getClientName());
        //calcular hash      
        return MinerP2P.getHash(nonce + msg);
    }

    @Override
    public void startMining(String blockchain, String msg, int dificulty) throws RemoteException {
        //se estivar a minar não faz nada
        if (myMiner.isMining()) {
            // lista de espera para minerar
            return;
        }
        //colocar o mineiro a minar
        myMiner.startMining(msg, dificulty);
        listener.onStartMining(msg, dificulty);
        //mandar a rede minar
        for (RemoteInterface node : network) {
            node.startMining(blockchain, msg, dificulty);
        }
    }

    @Override
    public void stopMining(String blockchain, int nonce) throws RemoteException {
        //Se estiver parado
        if (!myMiner.isMining()) {
            return; // sair
        }
        
        //parar o mineiro  
        myMiner.stopMining(nonce);
        //atualizar o bloco com o nonce
        this.miningBlock.setNonce(nonce);
        //mandar parar a rede
        for (RemoteInterface node : network) {
            listener.onMessage("Stop Miner", node.getAdress());
            node.stopMining(blockchain, nonce);
        }
    }

    @Override
    public int getNonce() throws RemoteException {
        return myMiner.getNonce();
    }

    @Override
    public int getTicket() throws RemoteException {
        return myMiner.getTicket();
    }

    @Override
    public boolean isMining() throws RemoteException {
        return myMiner.isMining();
    }

    @Override
    public int mine(String msg, int dificulty) throws RemoteException {
        try {
            //informação do cliente
            System.out.println("Mining to " + getClientName());
            //começar minar o bloco
            myMiner.startMining(msg, dificulty);
            //esperar que termine
            return myMiner.waitToNonce();
        } catch (Exception ex) {
            throw new RemoteException(ex.getMessage(), ex.getCause());
        }

    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::                R E D E   M I N E I R A 
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    @Override
    public void addNode(RemoteInterface node) throws RemoteException {
        //se a rede não tiver o no
        if (!network.contains(node)) {
            listener.onAddNode(node);
            //adicionar o mineiro à rede
            network.add(node);
            //adicionar o nosso no a rede do remoto remoto
            node.addNode(this);
            //espalhar o mineiro pela rede            
            for (RemoteInterface remote : network) {
                //adicionar o novo no ao remoto ao nos da rede
                remote.addNode(node); // pedir para adiconar o novo nó
            }
            //notificar o listener
            listener.onAddNode(node);
        }
    }

    @Override
    public List<RemoteInterface> getNetwork() throws RemoteException {
        //transformar a network num arraylist
        return new ArrayList<>(network);
    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::               VOTES
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    @Override
    public void addVote(String blockchain,String vote) throws RemoteException {
        //se já tiver o voto não faz nada
        //if (this.voteList.get(blockchain).contains(vote)) {
        VoteList voteList = this.elections.get(blockchain).voteList;
        if (voteList.contains(vote)) {
            listener.onMessage("Duplicated vote", vote);
            return;
        }

        voteList.addVote(vote);
        listener.onUpdateVotes(vote);
        listener.onMessage("addVote", getClientName());
        
        //se tiver mais de 4 votos e não estiver a minar
        if (voteList.getList().size() >= voteList.MAXVOTELIST && !myMiner.isMining()) {
            buildNewBlock(blockchain);
        } else {
            //sincronizar os votos
            for (RemoteInterface node : network) {
                node.synchronizeVoteList(blockchain,voteList.getList());
            }
        }

    }
    
    @Override
    public Map<String, VoteList> getAllVoteLists() throws RemoteException {
        
        Map<String, VoteList> ret  =new HashMap<>(); 
        
        for (Map.Entry<String, ElectionCore> entry : elections.entrySet()) {
            ret.put(entry.getKey(), entry.getValue().voteList);
        }
        ;
        return ret;
    }
    @Override
    public List<String> getVoteList(String blockchain) throws RemoteException {
        
        return elections.get(blockchain).voteList.getList();
    }

    @Override
    public void synchronizeAllVoteLists(Map<String, VoteList> list)  throws RemoteException{
        
        if(list.equals(this.getAllVoteLists())){
            return;
        }
        
        for (Map.Entry<String, VoteList> entry : list.entrySet()) {
            // Synchronize the vote lists
            synchronizeVoteList(entry.getKey(),entry.getValue().getList());

        }
    }
    @Override
    public void synchronizeVoteList(String blockchain, List<String> list) throws RemoteException {
        VoteList voteList = elections.get(blockchain).voteList;
        if (list.equals(voteList.getList())) {
            return;
        }
        for (String string : list) {
            addVote(blockchain, string);
        }
        //mandar sincronizar a rede
        for (RemoteInterface node : network) {
            node.synchronizeVoteList(blockchain,voteList.getList());
        }
        listener.onMessage("synchonizeTransactions", getClientName());

    }
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::               B L O C K 
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @Override
    public void startMiningBlock(String blockchain,Block newBlock) throws RemoteException {
        //se já tiver o bloco
        if (miningBlock.equals(newBlock)) {
            return;
        }
        listener.onMessage("New Mining Block", newBlock.getHash() + "");
        this.miningBlock = newBlock;
        //Remover as transacoes
        List<String> lst = (List<String>) Serializer.base64ToObject(newBlock.getData());
        this.elections.get(blockchain).voteList.removeVotes(lst);
        listener.onUpdateVotes(null);
        //espalhar o bloco pela rede
        for (RemoteInterface node : network) {
            node.startMiningBlock(blockchain,miningBlock);
        }
        //minar o bloco
        startMining(blockchain, newBlock.getMiningData(), newBlock.getNumberOfZeros());
    }

    @Override
    public void buildNewBlock(String blockchain) throws RemoteException {
        VoteList voteList = elections.get(blockchain).voteList;
        if (voteList.getList().size() < VoteList.MAXVOTELIST) {
            return;
        }
        listener.onUpdateBlockchain();
        //espalhar o bloco pela rede
        for (RemoteInterface node : network) {
            listener.onMessage("Synchronize blockchain", node.getAdress());
            node.synchonizeBlockchain(blockchain, this);
        }

        //String lastHash = blockchains.getLast().getHash();
        String lastHash = new LastBlock(this).getLastBlock(blockchain).getHash();

        //dados do bloco são as lista de transaçoes 
        String data = Serializer.objectToBase64(voteList.getList());
        
        
        //merkle tree
        MerkleTreeString tree = new MerkleTreeString(voteList.getList());
        String root = tree.getRoot();

        //Construir um novo bloco logado ao último
        Block newBlock = new Block(data, lastHash,root, Block.DIFICULTY);
        //Começar a minar o bloco
        startMiningBlock(blockchain, newBlock);
    }

    @Override
    public void updateMiningBlock(String blockchain, Block newBlock) throws RemoteException {
        // se o novo bloco for válido
        // e encaixar na minha blochain
        BlockChain bc = elections.get(blockchain).getSecureLedger();
        if (newBlock.isValid()
                && newBlock.getPrevious().equals(bc.getLast().getHash())) {
            try {

                bc.addBlock(newBlock);
                this.miningBlock = newBlock;

                listener.onUpdateBlockchain();
                //espalhar o bloco pela rede
                for (RemoteInterface node : network) {
                    node.updateMiningBlock(blockchain,newBlock);
                }
            } catch (BlockchainException ex) {
                throw new RemoteException("Update mining Block", ex);
            }
        }
    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::               B L O C K C H A I N
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    @Override
    public int getBlockchainSize(String blockchain) throws RemoteException {
        return elections.get(blockchain).getSecureLedger().getChain().size();
    }

    @Override
    public BlockChain getBlockchain(String blockchain) throws RemoteException {
        return elections.get(blockchain).getSecureLedger();

    }
    
    @Override
    public void synchonizeBlockchains(RemoteInterface syncNode) throws RemoteException {
        //se os tamanhos forem diferentes
        for (Map.Entry<String, ElectionCore> entry : elections.entrySet()) {
            
            synchonizeBlockchain(entry.getKey(),syncNode);
        }
    }

    @Override
    public void synchonizeBlockchain(String blockchain, RemoteInterface syncNode) throws RemoteException {
        //se os tamanhos forem diferentes
        if (syncNode.getBlockchainSize(blockchain) != this.getBlockchainSize(blockchain)) {
            //se o meu tamnho for menor
            if (syncNode.getBlockchainSize(blockchain) > this.getBlockchainSize(blockchain)) {
                //atualizar aminha blockchains
                listener.onUpdateBlockchain();          //RECONFIRMAAARRR
                // colocar a blockchain obtida na nossa
                elections.get(blockchain).updateSecureLedger(syncNode.getBlockchain(blockchain));
                //this.blockchains.put(blockchain, syncNode.getBlockchain(blockchain));
            } else // Se a do no for menor
            if (syncNode.getBlockchainSize(blockchain) < this.getBlockchainSize(blockchain)) {
                //sincronizar com a minha
                syncNode.synchonizeBlockchain(blockchain,this);
            }
            //sincronizar a blockchains pela rede
            for (RemoteInterface node : network) {
                node.synchonizeBlockchain(blockchain,this);
            }
        }

    }
    
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::               E L E C T I O N
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    public List<String> getElections () throws RemoteException{
        return new ArrayList<>(elections.keySet());
    }
            
    
    public List<String> getCandidates (String election) throws RemoteException{
        
        return candidates.get(election);
    }
    
    
    @Override
    public void StartNewElection(String electionName, List<String> candidates) {
        ElectionCore election = new ElectionCore(candidates);
        elections.put(electionName, election);
        //
    }

    
    
        //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::               C O N S E N S U S
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    Map<Long, Boolean> flagLastBlock = new ConcurrentHashMap<>();

    @Override
    public List getLastBlock(String blockchain, long timeStamp, int dept, int maxDep) throws RemoteException {
       //codigo com acesso exclusivo  para a thread
        synchronized (this) {
            //verificar se ja respondi
            if (flagLastBlock.get(timeStamp) != null) {
                return null;
            }
            //verificar se cheguei ao limite de profundidade
            if (dept > maxDep) {
                return null;
            }

            //responder
            flagLastBlock.put(timeStamp, Boolean.TRUE);
        }
        listener.onConsensus("Last Block", address);
        //calcular o ultimo bloco
        List myList = new CopyOnWriteArrayList();
        myList.add(elections.get(blockchain).getSecureLedger().getLast());
        //perguntar à rede
        //sincronizar a blockchains pela rede
        for (RemoteInterface node : network) {
            listener.onConsensus("Get Last Block Lisr", node.getAdress());
            List resp = node.getLastBlock(blockchain, timeStamp, dept + 1, maxDep);
            if (resp != null) {
                myList.addAll(resp);
            }
        }
        return myList;

    }

}
