[Green Card](https://inst.eecs.berkeley.edu/~cs61c/fa17/img/riscvcard.pdf)

# Data Transfer

`lw` / `sw` 存取字

存取一个byte

- `lb` 以补码模式，把内存的某个字节读入到寄存器的最低字节，前面的3个字节sign extend（全部赋值为读入字节的最高位）

  > 内存中有`00 00 80 11 (H)`，地址存在`s0`寄存器中
  >
  > ```assembly
  > lb t0, 0(s0)
  > # t0 == 00 00 00 11 (H)
  > lb t0, 1(s0)
  > # t0 == FF FF FF 80 (H)
  > ```

- `lbu` 以unsigned模式读入字节，前面的3个字节zero extend（全部赋0）

- `sb` 把寄存器的最低字节写入到内存某个字节处

# Pseudo Instruction

不是能对应上指令码的指令，只是一些便于理解的语法糖，汇编器会翻译成真正的指令。

```assembly
mv rd, rs # 复制rs里的内容到rd
# 其实就是 addi rd, rs, 0

j LABEL # PC设置为LABEL那一行的地址
# 其实就是 jal x0, LABEL （x0不会被修改）

jr rs # PC设置为rs中保存的地址
# 其实就是 jalr x0, rs, 0 （x0不会被修改）

ret # 把ra保存的地址读入PC
# 等于 jr ra
# 其实就是 jalr x0, ra, 0 （x0不会被修改）
```

# Function Call

![Memory Management](<./../C/img/Memory Management.jpg>)

`sp`是栈顶的指针，实时动态变化着：

```assembly
# push
addi sp, sp, -4
sw rs, 0(sp)

# pop
lw rd, 0(sp)
addi sp, sp, 4
```

需要遵循的规范Calling Convention：一个函数，一定是Callee，可能是Caller

- 作为Caller时需要操纵的register

  - `a0 - a7` 作为传入Callee的`args`，可能会被子函数Sub-Callee修改。如果某个`a`在调用子函数后还会被用到，则需要在调用的前后用`sp`对stack存取`a`

  - `ra` 存PC返回的地方，因为Callee的返回需要`ra`，而Caller自己的返回点不能丢失，所以作为Caller，需要在调用子函数Sub-Callee的时候用`sp`对stack存取`ra`

    > 调用函数的时候`jal ra, FUNC`（jump and link），这个命令先把下一行的地址存到`ra`中，再把PC设置为`FUNC`那一行的地址

  - `t0 - t6` 可能会被子函数Sub-Callee修改的register。如果某个`t`在调用子函数后还会被用到，则需要在调用的前后用`sp`对stack存取`t`

- 作为Callee时需要操纵的register

  - `a0 - a7` 作为传回Caller的`return`，只要保证放好了返回值，其他可以随意修改

  - `ra` PC读取返回的地方，不需要改变，只需要读取

    > `ret`自动把`ra`读入PC

  - `s0 - s11` 保证不会被子函数Sub-Callee修改的register。如果本函数的操作会修改某些`s`，则需要在执行函数操作的前后用`sp`对stack存取`s`

---

总结一下写函数：

1. 看会用到哪些`s`，在函数的开头push，在结尾pop
2. 如果有调用子函数，则在函数的开头push `ra`，在结尾pop `ra`，在调用前设定参数`a`，调用函数后取返回值`a`（如果有的话）
3. 如果有`t`在调用子函数后还会被用到，则在调用的前后push和pop
4. 如果是不想被子函数修改的变量，用`s`；如果是随意的变量，用`t`
