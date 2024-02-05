# Nested Higher Order Function that store variable
ENABLE = 0
if ENABLE:
    def f(k):
        # counter = counter + k*b
        counter = 0

        def ff(b):
            nonlocal counter
            counter += k * b
            print(counter)
        return ff

    a = f(2)

    a(1)
    a(2)
    a(3)

    def g(k):
        # counter = counter + k*b
        counter = [0]

        def ff(b):
            # Mutable values can be changed without nonlocal statment
            counter[0] += k * b
            print(counter[0])
        return ff

    a = g(2)

    a(1)
    a(2)
    a(3)

# Self Reference
ENABLE = 0
if ENABLE:
    def f(x):
        print(x)

        def ff(y):
            return f(x + y)
        return ff

    a = f(1)(2)(3)
    a(4)(5)

# Currying
ENABLE = 0
if ENABLE:
    # g(x)(y) == f(x, y)
    def curried_pow(base):
        def g(exp):
            return pow(base, exp)
        return g

    print(curried_pow(2)(6))  # 2^6=64
    power_of_two = curried_pow(2)
    print(power_of_two(6))  # 2^6=64

    curried_pow = lambda base: lambda exp: pow(base, exp)
    print(curried_pow(2)(6))  # 2^6=64
    power_of_two = curried_pow(2)
    print(power_of_two(6))  # 2^6=64

# Decorator is Higher Order Function
ENABLE = 0
if ENABLE:
    def magic_add_114514(f):
        # f(x: int) -> int:
        def magic(x: int) -> int:
            return f(x) + 114514
        return magic

    @magic_add_114514
    def square(x):
        return x * x
    print(square(3))

    def square(x):
        return x * x
    print(magic_add_114514(square)(3))
