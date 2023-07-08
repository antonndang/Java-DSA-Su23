package problems;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * See the spec on the website for example behavior.
 */
public class MapProblems {

    /**
     * Returns true if any string appears at least 3 times in the given list; false otherwise.
     */
    public static boolean contains3(List<String> list) {
        HashMap<String, Integer> counter = new HashMap<>();
        for (String s : list) {
            counter.put(s, 1 + counter.getOrDefault(s, 0));
        }

        if (!counter.isEmpty()) {
            int maxCount = 0;
            for (int x : counter.values()) {
                maxCount = Math.max(maxCount, x);
            }
            return maxCount >= 3;
        }
        return false;
    }

    /**
     * Returns a map containing the intersection of the two input maps.
     * A key-value pair exists in the output iff the same key-value pair exists in both input maps.
     */
    public static Map<String, Integer> intersect(Map<String, Integer> m1, Map<String, Integer> m2) {
        HashMap<String, Integer> result = new HashMap<>();
        for (String s : m1.keySet()) {
            if (m2.containsKey(s) && m1.get(s) == m2.get(s)) {
                result.put(s, m1.get(s));
            }
        }
        return result;
    }
}
