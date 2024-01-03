package Election.core;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import Election.blockchain.Block;
import Election.blockchain.BlockChain;
import Election.distributed.VoteList;
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


public class ElectionCore implements Serializable {

    private BlockChain secureLedger;
    public int dificulty = 1;
    public List<String> candidates;
    public VoteList voteList;
    
    public BlockChain getSecureLedger(){
        return secureLedger;
    }
    
    public ElectionCore() {
        secureLedger = new BlockChain();  
        candidates = new ArrayList<>();
        voteList = new VoteList();
    }
    
    public ElectionCore(List<String> candidates) {
        secureLedger = new BlockChain();  
        candidates = candidates;
        voteList = new VoteList();
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
    
    
    public boolean voteListContains(String vote) {
        return voteList.contains(vote);
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

    public static ElectionCore load(String fileName) throws Exception {
        
        ElectionCore tmp = new ElectionCore();
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


    public void updateSecureLedger(BlockChain blockchain) {
        this.secureLedger = blockchain;
    }


    

}
