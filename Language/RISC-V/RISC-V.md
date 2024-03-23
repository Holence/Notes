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

# Instruction Formats

因为寄存器有32个，所以rs和rd都需要5位

## R-Type

`add rd, rs1, rs2`

| 7     | 5    | 5    | 3     | 5    | 7(opcode) |
| ----- | ---- | ---- | ----- | ---- | --------- |
| func7 | rs2  | rs1  | func3 | rd   | 0110011   |

## I-Type

`addi rd, rs1, imm`

立即数imm是12位补码，会被sign extend

| 12        | 5    | 3     | 5    | 7(opcode) |
| --------- | ---- | ----- | ---- | --------- |
| imm[11:0] | rs1  | func3 | rd   | 0010011   |

`slli\srli\srai rd, rs1, imm`

Shift：imm只需要最后的5位shmat，因为只有32位可以移

| 12                 | 5    | 3     | 5    | 7(opcode) |
| ------------------ | ---- | ----- | ---- | --------- |
| imm[11:5]+imm[4:0] | rs1  | func3 | rd   | 0010011   |

`lb\lh\lw\lbu\lhu rd, imm(rs1)`

Load：M\[rs1+imm\]\[0:width\] -> rd

| 12        | 5    | 3          | 5    | 7(opcode) |
| --------- | ---- | ---------- | ---- | --------- |
| imm[11:0] | rs1  | width type | rd   | 0000011   |

`jalr rd, imm(rs1)`

Jump and Link Register：`rd = PC+4; PC = R[rs1] + imm`

| 12        | 5    | 3     | 5    | 7(opcode) |
| --------- | ---- | ----- | ---- | --------- |
| imm[11:0] | rs1  | func3 | rd   | 1100111   |

## S-Type

`sb\sh\sw rs2, imm(rs1)`

Save: rs2 -> M\[rs1+imm\]\[0:width\]

| 7         | 5    | 5    | 3          | 5        | 7(opcode) |
| --------- | ---- | ---- | ---------- | -------- | --------- |
| imm[11:5] | rs2  | rs1  | width type | imm[4:0] | 0100011   |

## (S)B-Type

`beq rs1, rs2, imm`

if (Condition): `PC += {imm,0}`

| 7             | 5    | 5    | 3          | 5            | 7(opcode) |
| ------------- | ---- | ---- | ---------- | ------------ | --------- |
| imm[12\|10:5] | rs2  | rs1  | width type | imm[4:1\|11] | 1100011   |

![RV32 instruction formats](<./img/RV32 instruction formats.jpg>)

为了支持压缩指令（16bit的指令），需要能跳转到half word的地址，所以offset不是4的倍数，而是2的倍数。那么imm的最低位必定为0，于是就可以省略掉存储这一位，最低位隐含为0。

为了复用S-Type的线路，imm[4:1]和imm[10:5]的部分仍就按原位存，这样就剩去了移位计算imm的操作。imm的最高位是补码的符号位，所以[12]必须存在最高位处，那么剩下的[11]就放在空出来的那个地方。

所以只需要一条额外的datapath❓

> [Why are RISC-V S-B and U-J instruction types encoded in this way? - Stack Overflow](https://stackoverflow.com/questions/58414772/why-are-risc-v-s-b-and-u-j-instruction-types-encoded-in-this-way)
>
> 另外，如果$\pm 2^{10}$个32bit指令的跳转点不够用，可以用`j`指令（(U)J-Type的imm有$\pm 2^{18}$个32bit指令的跳转点）

## U-Type

Upper Immediate

在I-Type中只能用imm存入低12位的数字，若想用imm设定高20位就得额外设计指令

`lui rd, 0xfffff`

Load Upper Imm：`rd = imm<<12`（把imm以sign extend放到rd的高20位，低12位全零）

| 20         | 5    | 7(opcode) |
| ---------- | ---- | --------- |
| imm[31:12] | rd   | 0110111   |

> [!NOTE]
>
> 如果想先用`lui`设定高20位，然后用`addi`设定低12位，可能因为低12位的最高位为1而被sign extend，做加法后不对
>
> ```assembly
> lui x10, 0x11111
> # x10 = 0x11111000
> addi x10, x10, 0x800
> # x10 = 0xFFFFF800 + 0x11111000 = 0x11110800
> ```
>
> 所以当低12位的最高位为1时，需要设定高20位+1的值
>
> 语法糖`li`就帮你实现这个了，用`li`可以直接赋值32位的imm

---

`auipc rd, imm`

Add Upper Imm to PC：`rd = PC + (imm<<12)`

| 20         | 5    | 7(opcode) |
| ---------- | ---- | --------- |
| imm[31:12] | rd   | 0010111   |

> 只是用来取PC值吗❓`auipc rd, 0`
>
> 怎么没有Add Lower Imm to PC❓

## (U)J-Type

`jal rd, imm`

Jump (to Imm) and link：`rd = PC + 4; PC += {imm,0}`

| 20                       | 5    | 7(opcode) |
| ------------------------ | ---- | --------- |
| imm[20\|10:1\|11\|19:12] | rd   | 1101111   |

![RV32 instruction formats](<./img/RV32 instruction formats.jpg>)

和[(S)B-Type](#(S)B-Type)一样，为了支持half word的指令，offset是2的倍数。这里同样imm最低位隐含为0。

[20]同理是符号位在最前，[10:1]复用I-Type的线路，[11]单独处理，[19:12]复用U-Type的线路。

所以只需要一条额外的datapath❓

# Venus

[Venus - Github](https://github.com/ThaumicMekanism/venus)

[Venus Reference](https://inst.eecs.berkeley.edu/~cs61c/sp21/resources/venus-reference)

Java/Web的RISC-V模拟器

```bash
java -jar venus.jar -cc test.s arg1 arg2 arg3
```

System Call `ecall`：用`a0`指定函数，用`a1-a7`传参：[Venus List of System Calls](https://github.com/ThaumicMekanism/venus/wiki/Environmental-Calls)

```assembly
li a0, 1    # syscall number for printing integer
li a1, 1024 # the integer we're printing
ecall       # issue system call
```

