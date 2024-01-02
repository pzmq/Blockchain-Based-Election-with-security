package Election.distributed;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class VoteList{ 

    public static int MAXVOTELIST = 2;

    private final List<String> list;

    public VoteList() {
        list = new CopyOnWriteArrayList<>();
    }

    public List<String> getList() {
        return list;
    }

    public boolean contains(String trans) {
        return list.contains(trans);
    }

    public void addTransaction(String newTrans) {
        if (!list.contains(newTrans)) {
            list.add(newTrans);
        }
    }

    public void removeTransactions(List<String> lst) {
        list.removeAll(lst);
    }

    public void synchronize(List<String> other) {
        for (String trans : other) {
            addTransaction(trans);
        }
    }
    
}
