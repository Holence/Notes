package DisjointSet;

public interface DisjointSet<T> {
    public boolean isConnect(T a, T b);

    public void connect(T a, T b);

    public int size();
}
