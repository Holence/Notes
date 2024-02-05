# Packing / Unpacking
ENABLE = 0
if ENABLE:
    # def f(pos1, pos2, /, pos_or_kwd, *, kwd1, kwd2):
    def f(a, *tuple_of_parameters, b=114514, **dict_of_parameters):
        print(tuple_of_parameters)
        print(*tuple_of_parameters)
        print(dict_of_parameters)
        print(*dict_of_parameters)

    f(1, "我是二号", 3, b="114514", balabala="小魔仙", dilidili=2 + 4j)

# Flip-Flop
ENABLE = 0
if ENABLE:
    def a(n):
        print("a", n)
        return a

    def b(n):
        print("b", n)
        return b

    a, b = b, a
    a(1)(2)(3)

# Recursive List
ENABLE = 0
if ENABLE:
    a = [1, 2]
    a[0] = a  # a[0]存指向a的指针
    print(a)
    print(a[0])
    print(a[0][0])

    # 都一样的
    print(a is a[0], a is a[0][0])

    # 所以这样修改的话，就是对a进行修改
    a[0][0][1] = 114
    print(a)
    print(a[0])
    print(a[0][0])

    a[0][0][0] = 514
    print(a)
