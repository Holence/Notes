#include <iostream>
#include <random>

using namespace std;
std::uniform_int_distribution<uint32_t> uint_dist10(-100, 100);
std::mt19937 rng;

// 产生 [a,b) 范围内到随机数
int random()
{
    return uint_dist10(rng);
}

void print(int *A, int length)
{
    for (int i = 1; i <= length; i++)
    {
        cout << A[i] << ",";
    }
    cout << endl;
    cout << endl;
}

void swap(int &a, int &b)
{
    int temp = a;
    a = b;
    b = temp;
}

void HeadAdjust(int A[], int k, int length)
{
    // 第一位为空，利于算二叉树的左右孩子，也用来暂存要下沉的数
    A[0] = A[k];
    for (int i = 2 * k; i <= length; i *= 2)
    {
        // 一开始选的是左孩子，如果右孩子更大，选右孩子
        if (i + 1 <= length && A[i + 1] > A[i])
            i = i + 1; // 选右孩子
        // 孩子比根节点大的话，换位
        if (A[i] > A[0])
        {
            A[k] = A[i]; // 让孩子上来
            k = i;       // 让根节点下去
        }
        else // 否则退出
            break;
    }
    A[k] = A[0];
}

void HeapSort(int A[], int length)
{
    // 建立大根堆，按照二叉树的编号
    // 从最后一个分叉节点开始，到顶部
    for (int i = length / 2; i >= 1; i--)
        // 根节点小的话就下沉，孩子节点上升
        HeadAdjust(A, i, length);

    cout << "初始化大根堆：";
    print(A, length);

    // 取下大顶A[1]，让编号最后的元素上去
    // 再下沉，换上另一个最大的
    // 循环往复
    for (int i = length; i > 1; i--)
    {
        swap(A[i], A[1]);        // 取下大顶A[1]，让编号最后的元素上去
        HeadAdjust(A, 1, i - 1); // 下沉，换上另一个最大的
    }
}

void HeadAdjust_reverse(int A[], int k, int length)
{
    // 第一位为空，利于算二叉树的左右孩子，也用来暂存要下沉的数
    A[0] = A[k];
    for (int i = 2 * k; i <= length; i *= 2)
    {
        // 一开始选的是左孩子，如果右孩子更小，选右孩子
        if (i + 1 <= length && A[i + 1] < A[i])
            i = i + 1; // 选右孩子
        // 孩子比根节点小的话，换位
        if (A[i] < A[0])
        {
            A[k] = A[i]; // 让孩子上来
            k = i;       // 让根节点下去
        }
        else // 否则退出
            break;
    }
    A[k] = A[0];
}

void HeapSort_reverse(int A[], int length)
{
    // 建立小根堆，按照二叉树的编号
    // 从最后一个分叉节点开始，到顶部
    for (int i = length / 2; i >= 1; i--)
        // 根节点小的话就下沉，孩子节点上升
        HeadAdjust_reverse(A, i, length);

    cout << "初始化小根堆：";
    print(A, length);

    // 取下小顶A[1]，让编号最后的元素上去
    // 再下沉，换上另一个最小的
    // 循环往复
    for (int i = length; i > 1; i--)
    {
        swap(A[i], A[1]);                // 取下小顶A[1]，让编号最后的元素上去
        HeadAdjust_reverse(A, 1, i - 1); // 下沉，换上另一个最小的
    }
}

int main(int argc, char const *argv[])
{
    int N = 25;
    int length = 0;
    int *A = (int *)malloc(sizeof(int) * N);

    rng.seed(114514);
    for (int i = 1; i < N; i++)
    {
        A[i] = random();
        length++;
    }

    // int temp[] = {53, 17, 78, 9, 45, 65, 87, 32};
    // for (int i = 1; i < 9; i++)
    // {
    //     A[i] = temp[i - 1];
    //     length++;
    // }

    cout << "初始：";
    print(A, length);
    HeapSort_reverse(A, length);
    cout << "小根堆排序：";
    print(A, length);

    cout << "初始：";
    print(A, length);
    HeapSort(A, length);
    cout << "大根堆排序：";
    print(A, length);

    free(A);

    return 0;
}
