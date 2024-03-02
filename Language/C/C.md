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

## 编译

> gcc的编译其实是四个过程的集合，分别是预处理（preprocessing）、编译（compilation）、汇编（assembly）、链接（linking），分别由cpp、cc1、as、ld这四个程序完成，gcc是它们的封装。
>
> 这四个过程分别完成：处理#开头的预编译指令、将源码编译为汇编代码、将汇编代码编译为二进制代码、组合众多二进制代码生成可执行文件，也可分别调用gcc-E、gcc-S、gcc-c、gcc来完成。
>
> 在这一过程中，文件经历了如下变化：main.c到main.i到main.s到main.o到main。
>
> ---
>
> 默认编译是**动态链接**，即打包出来的可执行文件需要依赖系统的`.so`（windows上就是`.dll`）
>
> 而把所有以来的库都打包到可执行文件，`gcc --static`，就是**静态链接**
>
> 动态链接在别的环境下用可能无法运行，报错“缺失动态链接库”；而静态链接因为全打包了体积会很大。

`make`，搜索Makefile文件的配置，多文件批量编译