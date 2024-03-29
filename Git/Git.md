[GitHub Git Cheat Sheet](https://training.github.com/downloads/github-git-cheat-sheet/)

深入理解git（她也写了Gitlet）[Git from the inside out](https://www.youtube.com/watch?v=fCtZWGhQBvo)

```bash
git init
git status
git add/stage * # 暂时保存
git commit
git mv src dst # 重命名
git checkout # 去任何一个branch的任何一个commit
git branch
git log --show-signature --pretty=fuller

gitk.exe # git Visualizer
git cat-file -p [hashID] # 打印object文件
git ls-files -st # 打印文件列表以及状态

git fsck # 检查数据一致性
git prune # stage的中间状态依旧会保留在objects中，通过这个自动去除unreachable object
git repack
git gc # 全套维护，Github里会自动运行这个命令，所以pull下来的不会有unreachable object
```

`Git\bin\bash.exe` 模拟linux环境的工具

# Remote

一般的仓库有working directory，作为合作的中心仓库不应该有这些working directory，所以要用bare仓库

```bash
git init --bare # 新建bare
git clone --bare [src] [dst] # 从已有的仓库clone出bare仓库

git remote add [remote name] [src]
git push [remote name]
# 如果指定branch的话，一定要对准，可以push到任意remote branch的
git push [remote name] [local branch name]:[remote branch name]
git push [remote name] [local branch name]: # 在remote中创建branch
git push [remote name] :[remote branch name] # 在remote中删除branch
```

# 小细节

每次add入stage的都会存入objects，中间过程的文件会变为dangling object

```bash
git fsck # 查看dangling object
git prune # 立即去除
```

`branch -D`、`reset --hard`，不会删除commit以及对应的Blob，commit会变为dangling commit

```bash
git branch -D [branchname]
# 删除branch，只会删除指针，不会删除commit
# 那些commit都无法被直接访问了，叫dangling commit，会在默认30天后被删除
git fsck --no-reflogs # 查看dangling commit
# 设置expire进行立刻删除
git reflog expire --expire-unreachable=all --all
git prune
```

# Config

```bash
git config --list --show-origin --show-scope # 所有已设置的config
```

|         | Local              | Global                   | System              |
| ------- | ------------------ | ------------------------ | ------------------- |
| Scope   | Current Repo       | Current User             | All User            |
| Linux   | [Repo]/.git/config | ~/.gitconfig             | [Git]/etc/gitconfig |
| Windows | [Repo]/.git/config | %USERPROFILE%/.gitconfig | [Git]/etc/gitconfig |

优先级 `Local > Global > System`

```bash
git config --global core.quotepath off # 正确显示utf-8
git config --global safe.directory *
git config --global core.autocrlf true # 自动转换Windows\Linux换行符，不然的话把仓库复制到另一个环境下会出现一堆whitespace change
```

# gpg

git的记录只有用户名和邮箱，在GitHub多人合作的仓库中，很容易伪造记录，可以用PGP签名来防伪。

gpg的功能不只是Sign，还可以Encrypt文件、Authenticate，不过后者在今天的用处不多了。

> 参考阅读：
>
> - [2021年，用更现代的方法使用PGP](<https://ulyc.github.io/2021/01/13/2021年-用更现代的方法使用PGP-上/>)
> - [「译」PGP的问题](https://ulyc.github.io/2022/09/05/tr-pgp-problem-1/)

```bash
bash
gpg --full-generate-key

# list public keys
gpg --fingerprint -k --keyid-format long
# list private keys
gpg --fingerprint -K --keyid-format long
```

导出、导入private key即可备份

```bash
# export
gpg -ao private.asc --export-options backup --export-secret-keys UID

# import
gpg --import-options restore --import private.asc
gpg --edit-key UID
gpg> trust
5 = I trust ultimately
Your decision? 5
gpg> quit
```

建议为每个事务建立专用的subkey，比如git的commit sign，只需要导出这个subkey的public key放到Github的setting里，导出这个subkey的private key到写代码的电脑里并导入到gpg里，并配置git

```bash
# export（感叹号表示仅导出选定的key）
gpg -ao sign-public-key.asc --export [SUBKEY ID]!
gpg -ao sign-private-key.asc --export-secret-subkeys [SUBKEY ID]!

# import
gpg --import sign-private-key.asc
# edit trust
gpg --edit-key UID
> gpg trust

# 指定signingkey
git config --global user.signingkey [SUBKEY ID]!
# allway sign
git config --global commit.gpgsign true
```
