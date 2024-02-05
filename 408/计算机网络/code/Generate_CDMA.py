# 寻找与已知向量A互相正交的向量组
# 发现只有长度为4的倍数的A向量，才能有最大长度为length的正交向量组，其他偶数情况的正交向量组最大长度都只能为2？
# 按理来说，n位的向量空间里，一组互相正交的向量最多只可能有n个向量
# 也就是说能服务的设备只有n个，而可能性空间有2^n个，n=128后就不太容易破译了

# target_vec = [1 for i in range(8)]
target_vec = [-1, 1, -1, 1, -1, -1, 1, -1, 1, 1]
target_vec = [-1, -1, -1, 1, 1, -1, 1, 1]
# target_vec = [-1, 1, -1, 1]

length = len(target_vec)

def trans(x):
    return 1 if int(x) else -1


def mul(a, b):
    c = 0
    for i in range(length):
        c += a[i]*b[i]
    return c


group_list = []
for i in range(2**length):
    current_vec = list(map(trans, f"{i:0{length}b}"))
    if mul(target_vec, current_vec) == 0:
        if not group_list:
            group_list.append([current_vec])
        else:
            fflag = False
            for group_index in range(len(group_list)):
                flag = True
                for vec in group_list[group_index]:
                    if mul(vec, current_vec) != 0:
                        flag = False
                        break
                if flag:
                    group_list[group_index].append(current_vec)
                    fflag = True
                    break
            if fflag == False:
                group_list.append([current_vec])

for group in group_list:
	print(len(group)+1)
	print("A:", target_vec)
	for vec in group:
		print(vec)
	print("-"*10)

print("==="*30)

max_length=max([len(group) for group in group_list])
print("最大正交组的长度：", max_length+1)
if max_length>1:
	max_length_groups=[group for group in group_list if len(group)==max_length]
	for group in max_length_groups:
		print(len(group)+1)
		print("A:", target_vec)
		for vec in group:
			print(vec)
		print("-"*10)