import re
import hashlib


def get_references_and_footnote(file):
    with open(file, 'r', encoding="utf-8") as f:
        text = f.readlines()

    references = set()
    footnotes = dict()
    for i in range(len(text)):
        s = text[i].strip()
        r = re.match("^\[\^(.*?)\]: (.*)", s)
        if r != None:
            footnotes[r.group(1)] = r.group(2)
        else:
            for r in re.findall("\[\^(.*?)\]", s):
                references.add(r)
    return references, footnotes


def check_footnote_integrity(references, footnotes: dict):
    footnotes = set(footnotes.keys())
    print(len(references), references)
    print(len(footnotes), footnotes)

    r1 = set.difference(references, footnotes)
    if r1:
        print("References找不到", r1)
    r2 = set.difference(footnotes, references)
    if r2:
        print("Footnote多出", r2)

    if r1 or r2:
        return False
    else:
        return True


def hash_string(s):
    r = hashlib.sha1(bytes(s, encoding="utf-8"))
    return r.digest().hex()[:6]


def randomize_footnote(file):
    references, footnotes = get_references_and_footnote(file)
    # Github自动按照reference在正文中出现的顺序对footnote进行排序渲染
    # 并且会自动去除多余的footnote，多余的reference警告一下就行
    check_footnote_integrity(references, footnotes)

    # 直接按照footnotes，把所有使用到的随机化一下就行了
    with open(file, 'r+', encoding="utf-8") as f:
        text = f.read()
        f.seek(0)
        new_footnotes = set()
        for key, value in footnotes.items():
            footnote = f"[^{key}]"
            new_footnote = f"[^{hash_string(value)}]"
            while new_footnote in new_footnotes:
                print("哦吼，竟然撞了", new_footnote)
                new_footnote = f"[^{hash_string(new_footnote)}]"

            print(footnote, new_footnote)
            text = text.replace(footnote, new_footnote)
        f.write(text)
        f.truncate()


# randomize_footnote("./AI/CL/Continual Learning.md")
# randomize_footnote("./AI/NLP/NLP.md")
