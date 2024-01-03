
package Election.distributed;

public interface MiningListener {

    public void onException(String title, Exception ex);

    public void onMessage(String title, String msg);

    public void onStartServer(String adress);

    public void onAddNode(RemoteInterface node);

    public void onStartMining(String message, int zeros);

    public void onStopMining(int nonce);

    public void onMining(int number);

    public void onNounceFound(int nonce);

    public void onUpdateVotes(String transaction);
    
    public void onUpdateBlockchain();
    
    public void onConsensus(String title, String desc);
    

}
