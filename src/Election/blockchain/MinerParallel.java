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

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created on 28/09/2022, 11:13:39
 *
 * @author IPT - computer
 * @version 1.0
 */
public class MinerParallel {
    //maximum number of Nonce
    public static int MAX_NONCE = (int)1E9;
   
    
    
    public static class MinerThread extends Thread{
        
        AtomicInteger nonce;
        AtomicInteger solution;
        String data;
        int dificulty;

        public MinerThread(AtomicInteger nonce, AtomicInteger solution, String data, int dificulty) {
            this.nonce = nonce;
            this.solution = solution;
            this.data = data;
            this.dificulty = dificulty;
        }
        
        @Override
        public void run(){
            String zeros = String.format("%0" + dificulty + "d", 0);
            nonce.set(0);
            while(solution.get() == 0){
                int n = nonce.getAndIncrement();
                String hash = Hash.getHash(n + data); // MELHORAR
                if(hash.endsWith(zeros)){
                    solution.set(n);
                    
                }
            }
        }
        
        

        
    }
    public static int getNonce(String data, int dificulty) {

                //criar nonce
                AtomicInteger nonce = new AtomicInteger(0);

                //criar solution
                AtomicInteger solution = new AtomicInteger(0);

                //criar threads
                int procs = Runtime.getRuntime().availableProcessors();

                MinerThread threads[] = new MinerThread[procs];

                for (int i = 0; i < procs; i++) {
                    //cria as threads com os limites
                    threads[i] = new MinerThread(nonce, solution, data, dificulty);
                    //executa as threads
                    threads[i].start();
                }

                //join threads
                for (MinerThread th: threads) {
                    try {
                        th.join();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MinerParallel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //returnar solution
                return solution.get();
            }

    
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 202209281113L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2022  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}




