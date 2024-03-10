package MST;

/**
 * MST
 */
public interface MST<T> {
    double getMST();

    void addEdge(T vertexA, T vertexB, double length);
}