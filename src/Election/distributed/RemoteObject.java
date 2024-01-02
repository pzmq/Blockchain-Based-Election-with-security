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
    VoteList transactions;

    public Block miningBlock; // block in mining process

    public BlockChain blockchain;

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
            transactions = new VoteList();
             //merkle tree
            MerkleTreeString tree = new MerkleTreeString(transactions.getList());
            String root = tree.getRoot();
            this.miningBlock = new Block("dummy", "dummy",root, 1);
            //inicializar blockchain
            blockchain = new BlockChain();

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
    //:::::               TRANSACTIONS
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    @Override
    public void addTransaction(String transaction) throws RemoteException {
        //se já tiver a transação não faz nada
        if (this.transactions.contains(transaction)) {
            listener.onMessage("Duplicated Transaction", transaction);
            return;
        }

        transactions.addTransaction(transaction);
        listener.onUpdateTransactions(transaction);
        listener.onMessage("addTransaction", getClientName());
        //se tiver mais de 4 transacoes e não estiver a minar
        if (transactions.getList().size() >= transactions.MAXVOTELIST && !myMiner.isMining()) {
            buildNewBlock();
        } else {
            //sincronizar a transacao
            for (RemoteInterface node : network) {
                node.synchonizeTransactions(transactions.getList());
            }
        }

    }

    @Override
    public List<String> getTransactionsList() throws RemoteException {
        return transactions.getList();
    }

    @Override
    public void synchonizeTransactions(List<String> list) throws RemoteException {
        if (list.equals(transactions.getList())) {
            return;
        }
        for (String string : list) {
            addTransaction(string);
        }
        //mandar sincronizar a rede
        for (RemoteInterface node : network) {
            node.synchonizeTransactions(transactions.getList());
        }
        listener.onMessage("synchonizeTransactions", getClientName());

    }
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::               B L O C K 
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @Override
    public void startMiningBlock(Block newBlock) throws RemoteException {
        //se já tiver o bloco
        if (miningBlock.equals(newBlock)) {
            return;
        }
        listener.onMessage("New Mining Block", newBlock.getHash() + "");
        this.miningBlock = newBlock;
        //Remover as transacoes
        List<String> lst = (List<String>) Serializer.base64ToObject(newBlock.getData());
        this.transactions.removeTransactions(lst);
        listener.onUpdateTransactions(null);
        //espalhar o bloco pela rede
        for (RemoteInterface node : network) {
            node.startMiningBlock(miningBlock);
        }
        //minar o bloco
        startMining(newBlock.getMiningData(), newBlock.getNumberOfZeros());
    }

    @Override
    public void buildNewBlock() throws RemoteException {
        if (transactions.getList().size() < VoteList.MAXVOTELIST) {
            return;
        }
        listener.onUpdateBlockchain();
        //espalhar o bloco pela rede
        for (RemoteInterface node : network) {
            listener.onMessage("Synchronize blockchain", node.getAdress());
            node.synchonizeBlockchain(this);
        }

        //String lastHash = blockchain.getLast().getHash();
        String lastHash = new LastBlock(this).getLastBlock().getHash();

        //dados do bloco são as lista de transaçoes 
        String data = Serializer.objectToBase64(transactions.getList());
        
        
        //merkle tree
        MerkleTreeString tree = new MerkleTreeString(transactions.getList());
        String root = tree.getRoot();

        //Construir um novo bloco logado ao último
        Block newBlock = new Block(data, lastHash,root, Block.DIFICULTY);
        //Começar a minar o bloco
        startMiningBlock(newBlock);
    }

    @Override
    public void updateMiningBlock(Block newBlock) throws RemoteException {
        // se o novo bloco for válido
        // e encaixar na minha blochain      
        if (newBlock.isValid()
                && newBlock.getPrevious().equals(blockchain.getLast().getHash())) {
            try {

                blockchain.addBlock(newBlock);
                this.miningBlock = newBlock;

                listener.onUpdateBlockchain();
                //espalhar o bloco pela rede
                for (RemoteInterface node : network) {
                    node.updateMiningBlock(newBlock);
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
    public int getBlockchainSize() throws RemoteException {
        return blockchain.getChain().size();
    }

    @Override
    public BlockChain getBlockchain() throws RemoteException {
        return blockchain;

    }

    @Override
    public void synchonizeBlockchain(RemoteInterface syncNode) throws RemoteException {
        //se os tamanhos forem diferentes
        if (syncNode.getBlockchainSize() != this.getBlockchainSize()) {
            //se o meu tamnho for menor
            if (syncNode.getBlockchainSize() > this.getBlockchainSize()) {
                //atualizar aminha blockchain
                listener.onUpdateBlockchain();
                this.blockchain = syncNode.getBlockchain();
            } else // Se a do no for menor
            if (syncNode.getBlockchainSize() < this.getBlockchainSize()) {
                //sincronizar com a minha
                syncNode.synchonizeBlockchain(this);
            }
            //sincronizar a blockchain pela rede
            for (RemoteInterface node : network) {
                node.synchonizeBlockchain(this);
            }
        }

    }

    Map<Long, Boolean> flagLastBlock = new ConcurrentHashMap<>();

    @Override
    public List getLastBlock(long timeStamp, int dept, int maxDep) throws RemoteException {
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
        myList.add(blockchain.getLast());
        //perguntar à rede
        //sincronizar a blockchain pela rede
        for (RemoteInterface node : network) {
            listener.onConsensus("Get Last Block Lisr", node.getAdress());
            List resp = node.getLastBlock(timeStamp, dept + 1, maxDep);
            if (resp != null) {
                myList.addAll(resp);
            }
        }
        return myList;

    }

}
