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

int Partition(int A[], int low, int high)
{
    if (A[low] > A[high])
    {
        int temp = A[high];
        A[high] = A[low];
        A[low] = temp;
    }

    int pivot = A[low]; // 选择初始枢轴元素

    while (low < high)
    {
        while (low < high && A[high] >= pivot)
            high--;
        A[low] = A[high];
        while (low < high && A[low] <= pivot)
            low++;
        A[high] = A[low];
    }
    A[low] = pivot;
    return low;
}

void QuickSort(int A[], int low, int high)
{
    int pivot_index;
    if (low < high)
    {
        pivot_index = Partition(A, low, high);
        QuickSort(A, low, pivot_index - 1);
        QuickSort(A, pivot_index + 1, high);
    }
}

void print(int *A)
{
    for (int i = 0; i < _msize(A) / sizeof(int); i++)
    {
        cout << A[i] << ",";
    }
    cout << endl;
    cout << endl;
}

int main(int argc, char const *argv[])
{
    int N = 200;
    int *A = (int *)malloc(sizeof(int) * N);
    rng.seed(114514);

    for (int i = 0; i < N; i++)
    {
        A[i] = random();
    }

    print(A);
    QuickSort(A, 0, N - 1);
    print(A);

    free(A);

    return 0;
}
