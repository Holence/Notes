import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuickSort<Type extends Comparable<Type>> {
    int Partition(List<Type> A, int low, int high) {
        Type pivot = A.get(low); // 选择初始枢轴元素
        while (low < high) {
            while (low < high && A.get(high).compareTo(pivot) >= 0)
                high--;
            A.set(low, A.get(high));
            while (low < high && A.get(low).compareTo(pivot) < 0)
                low++;
            A.set(high, A.get(low));
        }
        //此处low==high，就是枢轴元素应该存放的位置
        A.set(low, pivot);
        return low;
    }

    void sort(List<Type> A, int low, int high) {
        int pivot_index;
        if (low < high) {
            pivot_index = Partition(A, low, high);
            sort(A, low, pivot_index - 1);
            sort(A, pivot_index + 1, high);
        }
    }

    public static void main(String[] args) {
        List<Integer> l = new ArrayList<>();
        for (int i = 1000000; i > 0; i--) {
            l.add(i);
        }
        Collections.shuffle(l);
        // System.out.println(l);
        QuickSort<Integer> q = new QuickSort<>();
        q.sort(l, 0, l.size() - 1);
        // System.out.println(l);
    }
}
