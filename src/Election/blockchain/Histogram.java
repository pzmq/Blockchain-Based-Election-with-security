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
package Election.blockchain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 06/12/2023, 17:59:01
 *
 * @author manso - computer
 */
public class Histogram {

    public Object getMostCommon(List lst) {
        Map<Object, Integer> hist = new HashMap<>();

        //construir o histograma
        for (Object object : lst) {
            if (hist.get(object) == null) {
                hist.put(object, 1);
            } else {
                hist.put(object, hist.get(object) + 1);
            }
        }
        //calcular o mais frequente
        int max = 0;
        Object mostCommon = null;
        for (Object object : hist.keySet()) {
            if (hist.get(object) > max) {
                max = hist.get(object);
                mostCommon = object;
            }
        }
        return mostCommon;

    }
    
    public static void main(String[] args) {
        Histogram h = new Histogram();
        List lst = Arrays.asList(1,1,2,2,2,2,2,2,2,3,4,3,2,4,5,6,1,1);
        System.out.println(h.getMostCommon(lst));
    }

}
