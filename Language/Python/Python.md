# 高阶函数

![Nested Function](<./img/Nested Function.jpg>)

可以用[Python Tutor][PythonTutor]跑跑看

```python
def f(k):
    # counter = counter + k*b
    counter = 0

    def ff(b):
        nonlocal counter
        counter += k*b
        print(counter)
    return ff

a = f(2)

a(1) # 0+2*1=2
a(2) # 2+2*2=6
a(3) # 6+2*3=12
```

---

> [!WARNING]
>
> [Late Binding Closures — The Hitchhiker's Guide to Python](<https://docs.python-guide.org/writing/gotchas/#late-binding-closures>)
>
> 函数body（procedure）的定义是late-binding的，只有在运行到的时候才会去各个父级的frame确定变量的值
>
> 可以用[Python Tutor][PythonTutor]跑跑看
>
> ```python
> def create_multipliers():
>  return [lambda x : i * x for i in range(5)]
> 
> def create_multipliers():
>  multipliers = []
> 
>  for i in range(5):
>      def multiplier(x):
>          # 运行的时候才会到create_multipliers的frame中查i的值
>          # 此时i已经是4了
>          return i * x
>      multipliers.append(multiplier)
> 
>  return multipliers
> 
> # 上面两个写法是等价的，结果都是8 8 8 8 8
> for multiplier in create_multipliers():
>  print(multiplier(2))
> ```
>
> 解决方法是
>
> ```python
> def create_multipliers():
>  return [lambda x, j=i : j * x for i in range(5)]
> 
> def create_multipliers():
>  multipliers = []
> 
>  for i in range(5):
>      def multiplier(i):
>          # append的时候就运行到这里，把i作为参数存进来了
>          def helper(x):
>              return i * x
>          return helper
>      multipliers.append(multiplier(i))
> 
>  return multipliers
> 
> # 上面两个写法是等价的，结果都是0 2 4 6 8
> for i in create_multipliers():
>  print(i(2))
> ```
>

# 指针



所有东西都是Object，所有变量都是指向Object的指针，但根据可变和不可变：

```python
a=东西
b=a
改变a的一些操作
# b变了吗？
# 如果b变了说明a是可变的
# 如果b没变说明a是不可变的
```

- 不可变类型：`int` `float` `bool` `string`

  自身是不可分的，所以无法修改自身，只能进行重新赋值，而重新赋值就指向另外一个东西了

- 可变类型：指向`list/dict/set`的指针，以及指向`Object`的指针，指向带有内部变量的函数（函数也是Object，见[高阶函数的例子](#高阶函数)）的指针

  可以根据指针修改内部。除非重新赋值，否则一直指向这个东西

  > [!WARNING]
  >
  > [Mutable Default Arguments — The Hitchhiker's Guide to Python](<https://docs.python-guide.org/writing/gotchas/#mutable-default-arguments>)
  >
  > 不要把可变类型作为函数的默认参数，Python在解释函数的时候，会把这个可变的东西算作函数的内部变量，然后你每次调用这个函数，默认参数就成了可变的内部变量了
  >
  > 可以用[Python Tutor][PythonTutor]跑跑看
  >
  > ```python
  > def append_to(element, to=[]):
  > to.append(element) # to会成为内部变量，一直被append
  > return to
  > 
  > def append_to(element, to=None): # 这才是正确的方法
  > if to is None:
  >   to = []
  > to.append(element)
  > return to
  > ```

---

赋值操作：

- 不可变：复制值
- 可变：复制地址

传入函数的值，将形参赋值为：

- 不可变：值

- 可变：地址

  - 传一个列表进去，然后修改列表

  - 传一个Instance进去，然后修改Instance的属性

# Hashable

Dict\Set的访存时间均为`O(1)`，因为用的是Hash表，存储的元素必须Hashable

可变和hashble有关系吗？

> 指向`list/dict/set`的指针，不可hashable，不能作为dict的key，或放在set里
>
> 然而“指向对象的指针”、“指向带有内部变量的函数的指针”，都是hashable，也就是说可以作为dict的key，或放在set里

# 迭代器

`iter`: `iter(range(1, 10))`

`map` `filter`

# Build

PyIntaller，`auto-py-to-exe`是个不错的GUI

# 附件

[PythonTutor]: https://pythontutor.com/python-compiler.html	"Python Tutor code visualizer: Visualize code in Python, JavaScript, C, C++, and Java"
