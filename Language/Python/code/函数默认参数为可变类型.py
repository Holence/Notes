dd={}
name=["A","S","D"]

def generateContainer(id:int,detail:str="",List:list=[]):
    d={
        "id":id,
        "detail":detail,
        "List":List
    }
    return d

index=0
for i in name:
    
    dd[i]=generateContainer(index)
    index+=1

dd["last"]=generateContainer(index,"another last")
dd["another last"]=generateContainer(index,"another last",[1,2])


dd["S"]["List"].append("你大爷的")

for key,value in dd.items():
    print(key,value)


print("################################################################################")

dd={}
name=["A","S","D"]

index=0
for i in name:
    def generateContainer(id:int,detail:str="",List:list=[]):
        d={
            "id":id,
            "detail":detail,
            "List":List
        }
        return d
    dd[i]=generateContainer(index)
    index+=1

dd["last"]=generateContainer(index,"another last")
dd["another last"]=generateContainer(index,"another last",[1,2])


dd["S"]["List"].append("你大爷的")

for key,value in dd.items():
    print(key,value)