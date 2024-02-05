```bash
git init
git status
git add * # 
git mv src dst # 重命名
git checkout # 去任何一个branch的任何一个commit
git branch 
git log --show-signature --pretty=fuller
gitk.exe # Visualizer
```

`Git\bin\bash.exe` 模拟linux环境的工具

## Config

```cmd
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
```
