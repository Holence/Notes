package DisjointSet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DisjointSetTest {
    @Test
    public void Test() {
        DisjointSet<String> d1 = new TreeDisjointSet<>();
        d1.connect("a", "b");
        d1.connect("c", "d");
        d1.connect("a", "d");
        d1.connect("A", "B");
        d1.connect("C", "D");
        d1.connect("A", "D");
        d1.connect("E", "F");

        DisjointSet<String> d2 = new HashingDisjointSet<>();
        d2.connect("a", "b");
        d2.connect("c", "d");
        d2.connect("a", "d");
        d2.connect("A", "B");
        d2.connect("C", "D");
        d2.connect("A", "D");
        d2.connect("E", "F");

        assertEquals(d1.size(), 10);
        assertEquals(d1.size(), d2.size());

        assertEquals(d1.isConnect("A", "P"), false);
        assertEquals(d1.isConnect("A", "A"), true);
        assertEquals(d1.isConnect("B", "C"), true);
        assertEquals(d1.isConnect("C", "F"), false);
        assertEquals(d1.isConnect("A", "a"), false);
        assertEquals(d1.isConnect("b", "d"), true);

        assertEquals(d1.isConnect("A", "P"), d2.isConnect("A", "P"));
        assertEquals(d1.isConnect("A", "A"), d2.isConnect("A", "A"));
        assertEquals(d1.isConnect("B", "C"), d2.isConnect("B", "C"));
        assertEquals(d1.isConnect("C", "F"), d2.isConnect("C", "F"));
        assertEquals(d1.isConnect("A", "a"), d2.isConnect("A", "a"));
        assertEquals(d1.isConnect("b", "d"), d2.isConnect("b", "d"));
    }
}
