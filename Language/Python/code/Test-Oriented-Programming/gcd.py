def gcd1(a, b):
    a, b = max(a, b), min(a, b)
    while b:
        a, b = min(a, b), abs(a - b)
    return a


def gcd2(a, b):
    while b:
        a, b = b, a % b
    return a
