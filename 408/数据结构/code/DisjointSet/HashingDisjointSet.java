package DisjointSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HashingDisjointSet<T> implements DisjointSet<T> {
    private class Group {
        List<T> members;

        Group(T m) {
            members = new ArrayList<>();
            addMember(m);
        }

        public void addMember(T m) {
            members.add(m);
        }

        public int getSize() {
            return members.size();
        }

        @Override
        public String toString() {
            return members.toString();
        }
    }

    private HashMap<T, Group> memberGroup;

    public HashingDisjointSet() {
        memberGroup = new HashMap<>();
    }

    @Override
    public int size() {
        return memberGroup.size();
    }

    public void connect(T a, T b) {
        if (a.equals(b)) {
            return;
        }
        Group ag = memberGroup.getOrDefault(a, new Group(a));
        Group bg = memberGroup.getOrDefault(b, new Group(b));
        // 小树并入大树
        if (!ag.equals(bg)) {
            if (ag.getSize() <= bg.getSize()) {
                for (T m : ag.members) {
                    bg.addMember(m);
                    memberGroup.put(m, bg);
                }
                memberGroup.put(b, bg);
            } else {
                for (T m : bg.members) {
                    ag.addMember(m);
                    memberGroup.put(m, ag);
                }
                memberGroup.put(a, ag);
            }
        }
    }

    public HashMap<T, Group> getMemberGroup() {
        return memberGroup;
    }

    public boolean isConnect(T a, T b) {
        Group ag = memberGroup.get(a);
        Group bg = memberGroup.get(b);
        if (ag == null || bg == null) {
            return false;
        } else {
            return ag.equals(bg);
        }
    }

    public static void main(String[] args) {
        HashingDisjointSet<String> d = new HashingDisjointSet<>();
        d.connect("a", "b");
        d.connect("c", "d");
        d.connect("a", "d");

        d.connect("A", "B");
        d.connect("C", "D");
        d.connect("A", "D");
        d.connect("E", "F");

        System.out.println("isConnect(\"A\", \"P\") = " + d.isConnect("A", "P"));
        System.out.println("isConnect(\"A\", \"A\") = " + d.isConnect("A", "A"));
        System.out.println("isConnect(\"B\", \"C\") = " + d.isConnect("B", "C"));
        System.out.println("isConnect(\"C\", \"F\") = " + d.isConnect("C", "F"));
        System.out.println("isConnect(\"A\", \"a\") = " + d.isConnect("A", "a"));
        System.out.println("isConnect(\"b\", \"d\") = " + d.isConnect("b", "d"));
        System.out.println(d.size());
        System.out.println(d.memberGroup);
    }
}
