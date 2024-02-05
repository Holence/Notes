import unittest
from gcd import gcd1, gcd2


class Test(unittest.TestCase):
    def test_gcd1(self):
        self.assertEqual(gcd1(0, 0), 0)
        self.assertEqual(gcd1(0, 3), 3)
        self.assertEqual(gcd1(3, 0), 3)
        self.assertEqual(gcd1(1, 3), 1)
        self.assertEqual(gcd1(3, 1), 1)
        self.assertEqual(gcd1(3, 2), 1)
        self.assertEqual(gcd1(3, 3), 3)
        self.assertEqual(gcd1(3, 4), 1)
        self.assertEqual(gcd1(3, 5), 1)
        self.assertEqual(gcd1(3, 6), 3)

    def test_gcd2(self):
        self.assertEqual(gcd2(0, 0), 0)
        self.assertEqual(gcd2(0, 3), 3)
        self.assertEqual(gcd2(3, 0), 3)
        self.assertEqual(gcd2(1, 3), 1)
        self.assertEqual(gcd2(3, 1), 1)
        self.assertEqual(gcd2(3, 2), 1)
        self.assertEqual(gcd2(3, 3), 3)
        self.assertEqual(gcd2(3, 4), 1)
        self.assertEqual(gcd2(3, 5), 1)
        self.assertEqual(gcd2(3, 6), 4)


# add below to exec with "python PATH/TO/FOLDER/test.py"
# at any cwd (current working directory)
if __name__ == '__main__':
    unittest.main()

# or cd "PATH/TO/FOLDER" then exec with "python -m unittest test"
