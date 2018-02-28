package edu.pitt.cs.cs1631.group4.voteapp;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by Daniel Rowe on 2/25/2018.
 */

public class TallyTable {
    private Map<Contestant, Integer> tbl = new HashMap<Contestant, Integer>();

    public boolean addContestant(String name, int id){
        Contestant contestant = new Contestant(name, id);
        if(!this.tbl.containsKey(contestant)){
            this.tbl.put(contestant, 0);
            return true;
        }
        return false;
    }

    public boolean castVote(int ContestantId){
        for(Contestant currCont : this.tbl.keySet()){
            if(currCont.getId() == ContestantId){
                Integer votes = this.tbl.get(currCont);
                this.tbl.put(currCont, ++votes);
                return true;
            }
        }
        return false;
    }

    public Map<Contestant, Integer> getResult(int topNumber){
        return MapUtil.sortByValue(this.tbl, topNumber);
    }

    private static class MapUtil {
        public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, int topNumber) {
            List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
            Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
                public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                    return (o2.getValue()).compareTo( o1.getValue() );
                }
            });

            Map<K, V> result = new LinkedHashMap<K, V>();
            int count = 0;
            for (Map.Entry<K, V> entry : list) {
                if(count <= topNumber) {
                    result.put(entry.getKey(), entry.getValue());
                    count++;
                }
            }
            return result;
        }
    }
}
