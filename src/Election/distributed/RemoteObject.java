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
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author manso
 */
public class RemoteObject extends UnicastRemoteObject implements RemoteInterface {

    MiningListener listener;
    MinerP2P myMiner;
    Dictionary<String,VoteList> voteList;

    public Block miningBlock; // block in mining process

    public Dictionary<String,BlockChain> blockchains;

    private String address; // nome do servidor
    private List<RemoteInterface> network; // network of miners

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
            voteList = new Hashtable<>();
             //merkle tree
            MerkleTreeString tree = new MerkleTreeString();
            String root = tree.getRoot();
            this.miningBlock = new Block("dummy", "dummy",root, 1);
            //inicializar blockchains
            blockchains  = new Hashtable<>();

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
    public void startMining(String msg, int dificulty) throws RemoteException {
        //se estivar a minar não faz nada
        if (myMiner.isMining()) {
            return;
        }
        //colocar o mineiro a minar
        myMiner.startMining(msg, dificulty);
        listener.onStartMining(msg, dificulty);
        //mandar a rede minar
        for (RemoteInterface node : network) {
            node.startMining(msg, dificulty);
        }
    }

    @Override
    public void stopMining(int nonce) throws RemoteException {
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
            node.stopMining(nonce);
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
        if (this.voteList.get(blockchain).contains(vote)) {
            listener.onMessage("Duplicated vote", vote);
            return;
        }

        voteList.get(blockchain).addVote(vote);
        listener.onUpdateVotes(vote);
        listener.onMessage("addVote", getClientName());
        
        //se tiver mais de 4 votos e não estiver a minar
        if (voteList.get(blockchain).getList().size() >= voteList.get(blockchain).MAXVOTELIST && !myMiner.isMining()) {
            buildNewBlock(blockchain);
        } else {
            //sincronizar os votos
            for (RemoteInterface node : network) {
                node.synchonizeVoteLists(blockchain,voteList.get(blockchain).getList());
            }
        }

    }

    @Override
    public List<String> getVoteList(String blockchain) throws RemoteException {
        return voteList.get(blockchain).getList();
    }

    @Override
    public void synchonizeAllVoteLists(List<String> list) throws RemoteException{
        //se os tamanhos forem diferentes
        Enumeration<String> keys = voteList.keys();
        while (keys.hasMoreElements()) { 
            String blockchain = keys.nextElement();
            synchonizeVoteLists(blockchain, list);
        }

    }
    @Override
    public void synchonizeVoteLists(String blockchain, List<String> list) throws RemoteException {
        if (list.equals(voteList.get(blockchain).getList())) {
            return;
        }
        for (String string : list) {
            addVote(blockchain, string);
        }
        //mandar sincronizar a rede
        for (RemoteInterface node : network) {
            node.synchonizeVoteLists(blockchain,voteList.get(blockchain).getList());
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
        this.voteList.get(blockchain).removeVotes(lst);
        listener.onUpdateVotes(null);
        //espalhar o bloco pela rede
        for (RemoteInterface node : network) {
            node.startMiningBlock(blockchain,miningBlock);
        }
        //minar o bloco
        startMining(newBlock.getMiningData(), newBlock.getNumberOfZeros());
    }

    @Override
    public void buildNewBlock(String blockchain) throws RemoteException {
        if (voteList.get(blockchain).getList().size() < VoteList.MAXVOTELIST) {
            return;
        }
        listener.onUpdateBlockchain();
        //espalhar o bloco pela rede
        for (RemoteInterface node : network) {
            listener.onMessage("Synchronize blockchain", node.getAdress());
            node.synchonizeBlockchain(blockchain, this);
        }

        //String lastHash = blockchains.getLast().getHash();
        String lastHash = new LastBlock(this).getLastBlock().getHash();

        //dados do bloco são as lista de transaçoes 
        String data = Serializer.objectToBase64(voteList.get(blockchain).getList());
        
        
        //merkle tree
        MerkleTreeString tree = new MerkleTreeString(voteList.get(blockchain).getList());
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
        if (newBlock.isValid()
                && newBlock.getPrevious().equals(blockchains.get(blockchain).getLast().getHash())) {
            try {

                blockchains.get(blockchain).addBlock(newBlock);
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
        return blockchains.get(blockchain).getChain().size();
    }

    @Override
    public BlockChain getBlockchain(String blockchain) throws RemoteException {
        return blockchains.get(blockchain);

    }
    
    @Override
    public void synchonizeBlockchains(RemoteInterface syncNode) throws RemoteException {
        //se os tamanhos forem diferentes
        Enumeration<String> keys = blockchains.keys();
        while (keys.hasMoreElements()) { 
            String name = keys.nextElement();
            synchonizeBlockchain(name,syncNode);
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
                this.blockchains.put(blockchain, syncNode.getBlockchain(blockchain));
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
        myList.add(blockchains.get(blockchain).getLast());
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
