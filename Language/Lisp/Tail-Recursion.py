# https://youtu.be/V4HDNg79EXo?list=PL6BsET-8jgYXdYh7kYQ-4HkO_EF_BItoz&t=483

# 因为递归的深入会创建Frame，而Scheme中只能用递归。即使是一个列表遍历的操作，也需要O(n)的空间复杂度。
# 而某些的递归调用是没有后续操作的，只占着茅坑等return值，这类的递归就叫Tail Recusion。
# Scheme中对这种不需等待返回值的递归调用存在优化，可以扔掉中间那些没用的Frame，让空间复杂度只用O(1)。
# 所以希望让你写递归都写成Tail Recusion的递归。
# Python没有对Tail Recusion的优化，所以下面的只是便于理解的示例。

# 比如用递归对列表求和
l = [1, 2, 3, 4, 5]


def bad_recursion(l):
    if not l:
        return 0
    return l[0] + bad_recursion(l[1:])  # 等着返回值做加法


print(bad_recursion(l))


def tail_recursion(l):
    def helper(sublist, total):
        if not sublist:
            return total
        return helper(sublist[1:], total + sublist[0])  # 没有后续操作
    return helper(l, 0)  # 没有后续操作


print(tail_recursion(l))
