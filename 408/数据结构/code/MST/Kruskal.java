package MST;

import java.util.HashSet;
import java.util.PriorityQueue;

import DisjointSet.DisjointSet;
import DisjointSet.TreeDisjointSet;

/**
 * 无向图最小生成树
 * DisjointSet + PriorityQueue
 */
public class Kruskal<T> implements MST<T> {
    private class Edge implements Comparable<Edge> {
        T vertexA;
        T vertexB;
        double length;

        public Edge(T vertexA, T vertexB, double length) {
            this.vertexA = vertexA;
            this.vertexB = vertexB;
            this.length = length;
        }

        @Override
        public int compareTo(Edge o) {
            return this.length - o.length > 0 ? 1 : -1;
        }
    }

    PriorityQueue<Edge> edgePQ;
    HashSet<T> vertexSet;

    public Kruskal() {
        edgePQ = new PriorityQueue<>();
        vertexSet = new HashSet<>();
    }

    @Override
    public double getMST() {
        int count = 0;
        double sum = 0;
        // 非破坏性计算
        PriorityQueue<Edge> copyEdgePQ = new PriorityQueue<>(edgePQ);

        DisjointSet<T> disjointSet = new TreeDisjointSet<>();
        while (count < vertexSet.size() - 1) {
            Edge e = copyEdgePQ.poll();
            if (!disjointSet.isConnect(e.vertexA, e.vertexB)) {
                sum += e.length;
                disjointSet.connect(e.vertexA, e.vertexB);
                System.out.println(e.vertexA + " -> " + e.vertexB + " = " + e.length);
                count++;
            }
        }
        return sum;
    }

    @Override
    public void addEdge(T vertexA, T vertexB, double length) {
        edgePQ.add(new Edge(vertexA, vertexB, length));
        vertexSet.add(vertexA);
        vertexSet.add(vertexB);
    }

    public static void main(String[] args) {
        // https://www.geeksforgeeks.org/prims-minimum-spanning-tree-mst-greedy-algo-5/
        MST<Integer> graph = new Kruskal<>();
        int[][] edgelist = { { 0, 1, 4 }, { 1, 2, 8 }, { 1, 7, 11 }, { 7, 8, 7 }, { 7, 6, 1 }, { 8, 6, 6 }, { 8, 2, 2 },
                { 5, 2, 4 }, { 5, 6, 2 }, { 5, 3, 14 }, { 4, 3, 9 }, { 4, 5, 10 }, { 3, 2, 7 }, { 0, 7, 8 } };
        for (int[] is : edgelist) {
            graph.addEdge(is[0], is[1], is[2]);
        }
        System.out.println(graph.getMST());
    }
}
