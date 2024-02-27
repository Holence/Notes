[WinLibs - Windows Pre-built](https://winlibs.com/)

C像个高性能的零件库，造出来的汽车性能极佳，但没有保护壳，各种零件都暴露着。

- 变量不会初始化，一开始拿到手的就是garbage
- array就是首地址，array的长度你自己得记住
- 运行时不会进行安全检查，指针可以随意移动，随意修改任意位置的值

# 高阶函数

```c
#include <stdio.h>

int add(int x, int y) {
    return x + y;
}

int sub(int x, int y) {
    return x - y;
}

int operate(int x, int y, int (*fp)(int, int)) {
    return (*fp)(x, y);
}

int main(int argc, char const *argv[]) {
    printf("%d\n", operate(1, 2, &add));
    int (*fp_add)(int, int) = &add;
    int (*fp_sub)(int, int) = &sub;
    int k = operate(operate(1, 2, fp_add), operate(3, 4, &add), fp_sub);
    printf("%d", k);
    return 0;
}
```

# 结构体

```c
typedef struct {
    int x;
    int y;
    Point *p;
} Point;

Point a;
Point b;
a.x=...;
a.y=...;
a.p=...;

b=a; // 等于memcpy，浅拷贝（b.p和a.p指向同一个）
```

# 细节

## 内存

![Memory Management](<./img/Memory Management.jpg>)

- `local variable`: stack（栈，包括调用栈）

- `malloc() / free()`: heap（只是一堆，不是数据结构中的堆）

  `malloc()` 到空闲分区链中寻找可用的内存块，见[《操作系统》动态分区分配](../../408/操作系统/操作系统.md#动态分区分配)

  `free()` 把今后不用的内存块放入空闲分区链，但并不会对内存块覆写0

  > 同样`realloc()`相当于`malloc()+memcpy()+free()`，并不会对内存块覆写0
  >
  > 担心隐私数据被hack的话，记得在`free()`和`realloc()`之后`memset`为0
  >
  > ```c
  > size = 10;
  > int *p = (int *)malloc(size * sizeof(int));
  > int *new_p = realloc(p, size * 2 * sizeof(int));
  > if (!new_p) {
  >     printf("Cannot realloc");
  >     free(p);
  >     exit(0);
  > }
  > // 有可能realloc后仍在原位
  > if (new_p != p) {
  >     // 不在原位的话，realloc()后覆写0
  >     memset(p, 0, size * sizeof(int));
  > }
  > p = new_p;
  > free(p);
  > // free()后覆写0
  > memset(p, 0, size * sizeof(int));
  > ```

- `global variable`: static data（定义之后大小不可变）

- 程序代码文本: code

## Error

- Segment Fault：访问了不该访问的地址
- Bus Error：没有按字对齐