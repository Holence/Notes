package MST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * 无向图最小生成树
 * adjacent list + PriorityQueue
 */
public class Prim<T> implements MST<T> {
    private class Edge implements Comparable<Edge> {
        T vertexA; // adjacent list + PriorityQueue 不知道起始点，要打印路径的话这里得记录一下
        T vertexB;
        double length;

        Edge(T vertexA, T vertexB, double length) {
            this.vertexA = vertexA;
            this.vertexB = vertexB;
            this.length = length;
        }

        @Override
        public int compareTo(Edge o) {
            return this.length - o.length > 0 ? 1 : -1;
        }
    }

    private HashMap<T, ArrayList<Edge>> vertexMap;

    public Prim() {
        vertexMap = new HashMap<>();
    }

    @Override
    public void addEdge(T vertexA, T vertexB, double length) {
        ArrayList<Edge> neighborsA = vertexMap.getOrDefault(vertexA, new ArrayList<>());
        neighborsA.add(new Edge(vertexA, vertexB, length));
        vertexMap.putIfAbsent(vertexA, neighborsA);

        ArrayList<Edge> neighborsB = vertexMap.getOrDefault(vertexB, new ArrayList<>());
        neighborsB.add(new Edge(vertexB, vertexA, length));
        vertexMap.putIfAbsent(vertexB, neighborsB);
    }

    @Override
    public double getMST() {
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        pq.add(new Edge(null, vertexMap.keySet().iterator().next(), 0));
        double sum = 0;
        HashSet<T> visited = new HashSet<>();
        while (!pq.isEmpty()) {
            Edge e = pq.poll();
            T newVertex = e.vertexB;
            if (!visited.contains(newVertex)) {
                sum += e.length;
                for (Edge neighbor : vertexMap.get(newVertex)) {
                    pq.add(neighbor);
                }
                visited.add(newVertex);
                System.out.println(e.vertexA + " -> " + e.vertexB + " = " + e.length);
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        // https://www.geeksforgeeks.org/prims-minimum-spanning-tree-mst-greedy-algo-5/
        MST<Integer> graph = new Prim<>();
        int[][] edgelist = { { 0, 1, 4 }, { 1, 2, 8 }, { 1, 7, 11 }, { 7, 8, 7 }, { 7, 6, 1 }, { 8, 6, 6 }, { 8, 2, 2 },
                { 5, 2, 4 }, { 5, 6, 2 }, { 5, 3, 14 }, { 4, 3, 9 }, { 4, 5, 10 }, { 3, 2, 7 }, { 0, 7, 8 } };
        for (int[] is : edgelist) {
            graph.addEdge(is[0], is[1], is[2]);
        }
        System.out.println(graph.getMST());
    }
}
