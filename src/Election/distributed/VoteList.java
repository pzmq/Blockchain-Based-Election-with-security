package Election.distributed;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class VoteList{ 

    public static int MAXVOTELIST = 4;

    private final List<String> list;

    public VoteList() {
        list = new CopyOnWriteArrayList<>();
    }

    public List<String> getList() {
        return list;
    }

    public boolean contains(String vote) {
        return list.contains(vote);
    }

    public void addVote(String newVote) {
        if (!list.contains(newVote)) {
            list.add(newVote);
        }
    }

    public void removeVotes(List<String> lst) {
        list.removeAll(lst);
    }

    public void synchronize(List<String> other) {
        for (String vote : other) {
            addVote(vote);
        }
    }
    
}
