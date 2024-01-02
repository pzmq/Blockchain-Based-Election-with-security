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
//::                                                               (c)2020   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package Election.core;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import Election.blockchain.Block;
import Election.blockchain.BlockChain;
import Election.wallet.User;
import static Election.wallet.User.getUserFileName;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author manso
 */
public class Election implements Serializable {

    private BlockChain secureLedger;
    public int dificulty = 1;
    
    public BlockChain getSecureLedger(){
        return secureLedger;
    }
    
    public Election() {
        secureLedger = new BlockChain();    
    }

    public List<Vote> getLedger() {
        List<Vote> lst = new ArrayList<>();
        
        for( Block b : secureLedger.getChain()){
            //b.Tree devolve a mkroot
            //ArrayList<Vote> votes = (ArrayList<Vote>) b.getTree().getElements();
            //lst.addAll(votes);
        }
        return lst;
    }
    
    /*
    Converter Função para retornar o número de votos de cada Candidato
    */
    public List<String> getCandidateVotes() {
        // Criar um mapa para armazenar a contagem de votos para cada candidato
        Map<String, Integer> contagemVotos = new HashMap<>();

        // Iterar sobre os votos na lista
        for (Vote vote : getLedger()) {
            String candidato = vote.getCandidate();

            // Atualizar a contagem de votos para o candidato
            contagemVotos.put(candidato, contagemVotos.getOrDefault(candidato, 0) + 1);
        }

        // Construir a lista com o nome de cada candidato e a contagem de votos
        List<String> resultados = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : contagemVotos.entrySet()) {
            resultados.add("Candidato: " + entry.getKey() + ", Votos: " + entry.getValue());
        }

        return resultados;
    }
    
    @Override
    public String toString() {
        List<String> resultados = getCandidateVotes();
        // Juntar os resultados em uma única string, separados por quebras de linha
        return String.join("\n", resultados);
    }

    public void save(String fileName) throws Exception {
        secureLedger.save(fileName);
    }

    public static Election load(String fileName) throws Exception {
        Election tmp = new Election();
        tmp.secureLedger.load(fileName);
        return tmp;
    }

    /*
    Condições de validação
    
    Eleitor ser válido (LogIn)
    Candidato ser válido
    Eleitor não ter votado
    Eleitor ter votado em 1 e só 1 Candidato
    
    FUNÇÃO INCOMPLETA
    */
    public void add(Vote t, User user) throws Exception {
        if (t.isValid(user)) {
            t.SignVote(user);
            secureLedger.add(t,dificulty);  
        } else {
            throw new Exception("Voto nao valido");
        }
    }

    

    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    public static long serialVersionUID = 123;
}
