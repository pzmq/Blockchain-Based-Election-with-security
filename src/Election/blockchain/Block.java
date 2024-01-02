package Election.blockchain;

import Election.distributed.MinerP2P;
import java.io.Serializable;

public class Block implements Serializable {
    public static int DIFICULTY = 3;

    String previous;        // hash do no anterior
    String data;            // dados 
    int numberOfZeros = DIFICULTY;  // número de zeros do hash
    String mkRoot;
    String hash = "000";     // hash do bloco actual
    int nonce = 0;          // numero de validação

    /**
     * cria um bloco
     *
     * @param message dados do bloco
     * @param previous hash do bloco anterior
     * @param zeros numero de zeros
     */
    public Block(String data, String previous,String mkRoot , int zeros) {
        try {
            this.data = data;
            this.previous = previous;
            this.mkRoot = mkRoot;
            this.numberOfZeros = zeros;
            this.nonce = 0;
            this.hash = MinerP2P.getHash(getMiningData());
        } catch (Exception ex) {
        }
    }

    /**
     * para mineração
     *
     * @return previous + data + nonce
     */
    public final String getMiningData() {
        return previous + mkRoot + data + numberOfZeros;
    }

    public String getPrevious() {
        return previous;
    }

    public int getNonce() {
        return nonce;
    }

    public int getNumberOfZeros() {
        return numberOfZeros;
    }

    public String getHash() {
        return hash;
    }

    /**
     * mensagem do no
     *
     * @return
     */
    public String getData() {
        return data;
    }

    /**
     * actualiza o nonce do no
     *
     * @param nonce novo nonce
     *
     */
    public void setNonce(int nonce) {
        this.nonce = nonce;
        //calcular o hash
        this.hash = MinerP2P.getHash(nonce + getMiningData());
    }

    /**
     * verify id the block is valid
     *
     * @return
     */
    public boolean isValid() {
        try {
            //zeros do prefix
            String prefix = String.format("%0" + numberOfZeros + "d", 0);
            if (!hash.startsWith(prefix)) {
                throw new BlockchainException("Wrong prefix Hash" + hash);
            }
            //comparar o hash da mensagem com o hash actual
            String realHash = MinerP2P.getHash(nonce + getMiningData());
            if (!realHash.equals(hash)) {
                throw new BlockchainException("Corrupted data : " + data);
            }
            //OK
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
      public String getInfo() {
        return "Previous:" + previous
                + "\nData    :" + data
                + "\nNonce   :" + nonce
                + "\nHash    :" + hash
                + "\nNº Zeros:" + numberOfZeros
                + "\nValid   :" + isValid();
    }
      

    @Override
    public String toString() {
        return data;
    }

  

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Block) {
            return hash.equals(((Block) obj).hash);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.hash.hashCode();
    }

    public String getTree() {
        return mkRoot;
    }

}
