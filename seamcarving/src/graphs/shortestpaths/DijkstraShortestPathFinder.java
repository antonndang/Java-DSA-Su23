package graphs.shortestpaths;

import priorityqueues.ExtrinsicMinPQ;
import priorityqueues.NaiveMinPQ;
import graphs.BaseEdge;
import graphs.Graph;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new NaiveMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        HashMap<V, E> spt  = new HashMap<>();
        HashMap<V, Double> vertexWeights = new HashMap<>();
        ExtrinsicMinPQ<V> pq = createMinPQ();

        pq.add(start, 0.0);
        vertexWeights.put(start, 0.0);
        while (!pq.isEmpty()) {
            V cur = pq.removeMin();
            if (cur.equals(end)) {
                break;
            }
            double curWeight = vertexWeights.get(cur);
            for (E e: graph.outgoingEdgesFrom(cur)) {
                V nxt = e.to();
                double nxtWeight = (curWeight + e.weight());
                if (!vertexWeights.containsKey(nxt)) {
                    pq.add(nxt, nxtWeight);
                    vertexWeights.put(nxt, nxtWeight);
                    spt.put(nxt, e);
                } else if (nxtWeight < vertexWeights.get(nxt)) {
                    pq.changePriority(nxt, nxtWeight);
                    vertexWeights.put(nxt, nxtWeight);
                    spt.put(nxt, e);
                }
            }
        }


        return spt;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        //ShortestPath<V, E> sp =
        if (start.equals(end)) {
            return new ShortestPath.SingleVertex<>(start);
        }
        // process failure
        if (!spt.containsKey(end)) {
            return new ShortestPath.Failure<>();
        }
        // process the spt in reverse
        ArrayList<E> pathEdges = new ArrayList<>();
        while (!end.equals(start)) {
            E e = spt.get(end);
            pathEdges.add(e);
            end = e.from();
        }
        // reverse the enge list before returingg
        Collections.reverse(pathEdges);
        return new ShortestPath.Success<>(pathEdges);
    }

}
