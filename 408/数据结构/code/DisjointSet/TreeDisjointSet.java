package DisjointSet;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * groupList中用负数表示为根节点，负数的绝对值表示group中总共有几个member
 */
public class TreeDisjointSet<T> {

    HashMap<T, Integer> indexMap; // 每个元素所属的下标
    ArrayList<Integer> groupList; // 每个下标的group信息

    TreeDisjointSet() {
        indexMap = new HashMap<>();
        groupList = new ArrayList<>();
    }

    public boolean isConnect(T a, T b) {
        if (!indexMap.containsKey(a) || !indexMap.containsKey(b)) {
            return false;
        }
        return getGroup(indexMap.get(a)) == getGroup(indexMap.get(b));
    }

    private int getGroup(int index) {
        int value = groupList.get(index);
        if (value < 0) {
            // 是根节点，返回根节点的index
            return index;
        } else {
            // 不是根节点
            value = getGroup(value); // 深入
            groupList.set(index, value); // 路径压缩
            return value;
        }
    }

    public void connect(T a, T b) {
        if (a.equals(b)) {
            return;
        }

        int indexA, indexB;
        if (indexMap.containsKey(a)) {
            indexA = indexMap.get(a);
        } else {
            indexA = indexMap.keySet().size();
            indexMap.put(a, indexA);
            groupList.add(-1); // 动态增加
        }

        if (indexMap.containsKey(b)) {
            indexB = indexMap.get(b);
        } else {
            indexB = indexMap.keySet().size();
            indexMap.put(b, indexB);
            groupList.add(-1); // 动态增加
        }

        int groupA = getGroup(indexA);
        int membersInA = groupList.get(groupA);

        int groupB = getGroup(indexB);
        int membersInB = groupList.get(groupB);
        // 小树并入大树
        if (membersInA < membersInB) {
            groupList.set(groupA, membersInA + membersInB);
            groupList.set(groupB, groupA);
        } else {
            groupList.set(groupB, membersInA + membersInB);
            groupList.set(groupA, groupB);
        }
    }

    public static void main(String[] args) {
        TreeDisjointSet<String> d = new TreeDisjointSet<>();
        d.connect("a", "b");
        d.connect("c", "d");
        d.connect("a", "d");

        d.connect("A", "B");
        d.connect("C", "D");
        d.connect("A", "D");

        System.out.println("isConnect(\"A\", \"P\") = " + d.isConnect("A", "P"));
        System.out.println("isConnect(\"A\", \"A\") = " + d.isConnect("A", "A"));
        System.out.println("isConnect(\"B\", \"C\") = " + d.isConnect("B", "C"));
        System.out.println("isConnect(\"A\", \"a\") = " + d.isConnect("A", "a"));
        System.out.println("isConnect(\"b\", \"d\") = " + d.isConnect("b", "d"));
        System.out.println(d.indexMap);
        System.out.println(d.groupList);
    }
}
