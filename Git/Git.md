```bash
git init
git status
git add * # add to stage 暂时保存
git commit
git mv src dst # 重命名
git checkout # 去任何一个branch的任何一个commit
git branch 
git log --show-signature --pretty=fuller
gitk.exe # Visualizer
```

`Git\bin\bash.exe` 模拟linux环境的工具

## Config

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
git config --global safe.directory * # 
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
> gpg trust
5 = I trust ultimately
Your decision? 5
> gpg quit
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
