package team.stephen.sunshine.util;

import java.util.*;

/**
 * Created by stephen on 2018/3/7.
 */
public class MapUtils {
    public static <V extends Comparable, T> Map<V, T> sortMapByKey(Map<V, T> oriMap) {

        Map<V, T> sortMap = new TreeMap<>((o1, o2) -> o1.compareTo(o2));

        sortMap.putAll(oriMap);
        return sortMap;
    }

    /**
     * 使用 Map按value进行排序
     *
     * @return
     */
    public static <V, T extends Comparable> Map<V, T> sortMapByValue(Map<V, T> oriMap, boolean reverse) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<V, T> sortedMap = new LinkedHashMap<V, T>();
        List<Map.Entry<V, T>> entryList = new ArrayList<Map.Entry<V, T>>(
                oriMap.entrySet());
        Collections.sort(entryList, (o1, o2) ->
                reverse ? o2.getValue().compareTo(o1.getValue()) :
                        o1.getValue().compareTo(o2.getValue())
        );

        Iterator<Map.Entry<V, T>> iter = entryList.iterator();
        Map.Entry<V, T> tmpEntry = null;
        while (iter.hasNext())

        {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }
}
