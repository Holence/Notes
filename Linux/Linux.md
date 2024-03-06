# Linux历史

UNIX闭源了

`GNU = GNU's not UNIX`没做出成功的操作系统，但诞生了很多软件，以及GPL (GNU Public License)

- gcc: GNU的C和C++编译器
- gdb: GNU程序调试器
- gzip: gz格式压缩与解压缩工具
- GNOME: 隶属于GNU项目的桌面环境
- gimp: GNU图像编辑工具

Linux内核

## 分发版

Distribution

- Debian 社群最大

  - Ubuntu

- Red Hat 后来停止维护了

  - RHEL: Red Hat Enterprise Linux

    自由软件并不一定免费

  - CentOS

  - Fedora 社群继承RedHat

- Arch Linux 优雅构建结构的理念，难用

  - manjaro

- Android Google

  - AncientOS
  - MIUI

- 其他

  - SUSE
  - 红旗

## 著作传

Copyright著作权 vs Copyleft著作传

> 要让程序变成自由软件的最简单方法，就是放弃著作权 (copyright)，使之成为公有领域的公版著作 (public domain)。这样人们就能分享程序和相关改善，只要他们有这样的想法。然而，这种作法却也会让不想合作的人将程序转为专有软件 (proprietary software)。他们能做出改动，也许多也许少，再将成品以专有产品的形式散布。收到修改后程序的人便失去原先作者给予人们的自由；中间人夺去了自由。
>
> 在 GNU 专案中，我们的宗旨是要给予所有使用者再次散布、与修改 GNU 软件的自由。如果中间人可以将自由夺走，我们的程序也许“会有不少使用者”，但是却未能给予他们自由。与其让 GNU 软件成为公版著作，我们更希望它成为“著作传 (copyleft)”。著作传的概念是任何人都能再次散布该软件，不管有无更动，都必须把自由传递下去，使之能进一步复制和修改。著作传能保障任何使用者都拥有自由。

# tty

Virtual Terminal

`Ctrl + Alt + F1~F7`

修改默认启动target：`systemd get-default` `systemd set-default multi-user.target`

# Shell

登录即新建session

开始运行`bash`的过程：《Shell in the Shell》，可以在shell中建立一层层的`bash`、`zsh`等各种shell等，然后再连续`exit`跳出一次次的shell

<u>先有Login Shell，在Login Shell里面才能再创建Login Shell和Non-Login Shell</u>

Login Shell：如tty、SSH、`sudo login`、`su - USERNAME`（`su --login USERNAME`）的Shell

会先运行`/etc/profile`，然后按顺序寻找并运行`~/.bash_profile`或`~/.bash_login`或`~/.profile`中的一个

可以看到`~/.profile`中有：

1. 调用`~/.bashrc`，里面才是bash的配置
2. 将`~/bin`和`~/.local/bin`加入`$PATH`

---

Non-Login Shell：如`bash`、`su USERNAME`（虽然它也要你输密码）、在GUI桌面Open Terminal的Shell

运行`/etc/bash.bashrc`，调用`~/.bashrc`，并且继承了外层Login Shell的环境变量等

---

所以想在登录的时候暗藏私货，可以在`/etc/profile`中搞

## Shell基本操作

Logout `Ctrl+D`

环境变量 `$PWD` `$PATH` `$SHELL`

`which`: 定位可执行文件

清屏 `Ctrl+L = clear`

结束 `Ctrl+C`

挂起 `Ctrl+Z` Traced/Stopped

### navigate

`ls` `pwd` `cd` `.` `..`

`~`: `/home/username`

`-`: 之前所在的路径

### stream

`>`: 前面的输出 流向 后面的指令/文件

> stdin/stdout/stderr: 0/1/2
>
> `>`或`1>`: stdout
>
> `2>`: stderr
>
> `2>&1>`: (stderr + stdout)>

`<`: 后面的输出/文件 流向 前面的指令

`>>`: append入文件

`|`: 作为下个指令的输入

`xargs`: 作为下个指令的参数

### file/directory

`mkdir`: -p自动创建中间路径

`cp`: 复制、-l硬链接、-s软连接

`ln`: 硬链接、软连接

`mv`: 移动、重命名

`rm`: 

`rmdir`

---

`touch`: 新建文件（修改文件属性）

`cat`: 

`tee`: 将输入流写入文件

`tar`: 

`find . -name FILENAME`

### alias

> `alias check_battery="upower -e | grep BAT | xargs upower -i"`
>
> `alias ll="ls -alhF --color --hyperlink --group-directories-first"`	

`alias`指令的定义是在`~/.bashrc`

可以看到它已经定义了几个有用的

想添加自己的可以直接加在里面，也可以加在`~/.bashrc`指定的`~/.bash_aliased`中

## Bash Script

待看https://101.lug.ustc.edu.cn/Ch06/#bash-arithmetic

### 变量

#### 自定义变量

`a=STR`

整数定义、运算需要`let a=1`，`let a=a*2`，或

没有浮点类型，只有整数

#### 环境变量

`env`

定义临时环境变量`export A=1`

#### 位置变量

- `$0`: 脚本名
- `$1 - $9`: 脚本的参数。`$1`是第一个参数，依此类推。

#### 特殊变量

- `$@`: 所有参数
- `$#`: 参数个数
- `$?`: 前一个命令的返回值
- `$$`: 当前脚本的进程识别码
- `!!`: 完整的上一条命令，包括参数。常见应用：当你因为权限不足执行命令失败时，可以使用`sudo !!`再尝试一次。
- `$_`: 上一条命令的最后一个参数。如果你正在使用的是交互式shell，你可以通过按下`Esc`之后键入`.`来获取这个值。

### 特殊字符

`"$PWD"` 会替换，输出当前工作路径

`'$PWD'` 不替换，输出`$PWD`

`\\` 转义字符

`$(CMD)` 或 `` `CMD` `` 替换为CMD的输出

`<(CMD)` CMD的结果先存入一个临时文件，然后指令中替换为这个临时文件

> 如`diff <(ls ./a) <(ls ./b)`比较两个目录的差别

### 输入输出

`read`

`echo`

### 组合

`true` `false`

`&&` 与

`||` 或

`( CMD1; CMD2; )` 建立subshell执行

`{ CMD1; CMD2; }` 就在原本的shell中执行

### 通配符

`*`和`?`

组合`mkdir -p {a,b}/{1..15}`（会创建a/1、a/2到a/15以及b/1、b/2到b/15的路径）

### 执行脚本

- `bash script.sh`

- `chmod +x script.sh`，`./script.sh`

- `source script.sh`或`. script.sh`

  > <u>会把脚本中的变量、函数等读到当前shell的内存中</u>
  >
  > 有些情况下，使用`.`执行脚本是有必要的，例如在激活Python的虚拟环境时：`. venv/bin/activate`
  >
  > 绝大多数时候，不建议使用这种方式执行脚本。

## 其他工具

### 字符处理

`wc`: 统计行数、词数、byte(B)数

`diff`: 

`head`/`tail`: 最前/后的`-n`几行或`-c`几个字符

> `tail -f /var/log/syslog` 动态追踪文件的更新

`grep`: 查找行

`sed`: 

# 用户

`/etc/passwd`中查看用户

`/etc/shadow`中存了密码的hash

`/etc/group`中查看用户组

1. `sudo -u USERNAME` 以另一个用户的身份执行指定的命令，默认切换到root

2. `sudo -i` interactive

3. `su USERNAME` 切换用户

   > `su` 什么都不加的话，默认切换到root，但Ubuntu等Linux发行版默认禁止了root用户的密码登录，只允许通过`sudo`提高权限。我们可以用`sudo su`，来得到一个为root用户权限的shell

> 用`sudo echo hello > file`写入系统文件的时候会依旧缺少权限，因为`sudo`只作用在`echo`上，而`>`是由shell运行的，需要一个有root权限的shell，后两条就行

- `users` 查看当前用户名
- `groups` 查看当前所属的用户组

> 你安装系统的用户应该默认在`sudo`用户组中，所以才可以用`sudo`提升权限

- `adduser USERNAME` 添加新用户
- `adduser USERNAME GROUPNAME` 把用户加入组

> `adduser` `deluser` `addgroup` `delgroup`是Debian中更高层的指令
>
> 不建议用`useradd` `userdel` `groupadd` `groupdel`

# 权限

`ls -l`

| 文件类型                      | 文件所属user的权限 | 文件所属user-group的权限 | others的权限 | 文件所属user | 文件所属user-group |
| ----------------------------- | ------------------ | ------------------------ | ------------ | ------------ | ------------------ |
| -/d/l<br />文件/文件夹/软连接 | rwx                | rwx                      | rwx          | root         | root               |

> 对于e[x]ecute位，对应三种人，有三种特殊权限位
>
> - user: rw[s] 不论执行命令的是谁，都以文件所属的user的身份执行
>
>   如`ls -l /usr/bin/passwd`
>
> - group: rw[s] 不论执行命令的是谁，都以文件所属的user-group的身份执行
>
> - others: rw[t] 作用在文件夹上，规定该文件夹中的文件（夹）只能由文件夹的所属user（和root）删除和移动
>
>   如`ls -ld /tmp/`

`chmod [ugoa][+-=][rwx] FILE` 修改权限

`chown` 修改文件所属用户

# FHS

Filesystem Hierachy Standard

文件系统层次结构标准

![FHS](<./img/FHS.png>)

`man hier`

- /bin：放置执行文件/命令的目录，尤其是放置在单用户维护模式下还能够被操作执行的命令（是/usr/bin的软链接）

- /boot：放置开机会使用到的文件，包括Linux内核文件以及开机菜单与开机所需配置文件等

- /dev：以文件的形式保存Linux所有的设备及接口设备

  > 在Linux的哲学中，存在着「一切皆文件」这样的思想。设备文件就是计算机设备抽象成文件的形式，程序和用户可以以读写普通文件的方式向这些文件输入内容，或者从文件中获取内容。系统驱动程序会相应处理用户对对应设备文件的输入和输出。
  >
  > 有一些常用的设备文件如：
  >
  > - `/dev/null`：总是返回空（EOF）数据。
  > - `/dev/zero`：总是返回零数据。
  > - `/dev/urandom`：输出随机数据。

- /etc：系统主要配置文件基本上都在这个目录下，包括人员账号密码文件、多种服务的起始文件等

  > Windows系统与其上很多程序都使用注册表，而非文件的形式存储配置信息。注册表是一个数据库，拥有数据库的优点（如原子性），集中地管理配置项。而Linux下的程序更喜欢将配置以文件的形式存储，保持配置简单，便于用户编辑与备份。
  >
  > 当然，这不是绝对的。现在Windows下的.NET程序更偏向于将配置存储在XML文件中，而Linux下的GNOME桌面环境也采取了类似于注册表的形式`GConf`，存储自己的配置信息。

- /home：系统默认的普通用户的主文件夹

- /lib：主要放置系统开机使用的、/bin和/sbin目录下的命令使用的库函数（是/usr/lib的软链接）

- /media：挂载可移除的设备

- /mnt：用于暂时挂载某些额外设备

- /opt：*self-contained*的软件目录（不依赖/bin，/lib），一般是大型的、商业的应用程序

- /proc：proc是一个虚拟文件系统，挂载在/proc，记载着系统以及进程的信息，例如进程、外部设备状态、网络状态等

  > /proc/[pid]/ 等众多文件夹是一个个进程的信息
  >
  > 其他文件/文件夹是系统的信息，例如
  >
  > - /proc/bus/...
  > - /proc/meminfo
  > - /proc/mounts
  > - /proc/dma
  > - /pro/net/arp

- /root：root用户的主目录

- /sbin：该目录下放置开机过程中需要的命令，包括开机、修复、还原系统等（是/usr/sbin的软链接）

- /srv：该目录放置与网络服务相关的文件数据（非系统的，初始为空）

- /sys：和/proc一样也是一个虚拟文件系统，主要记录与内核相关的信息，与/proc目录十分相似

  > /sys比/proc的结构更新、更优雅，改系统应该改/sys里的文件，而不是/proc里的文件

- /tmp：让一般用户或者正在执行程序暂时放置文件的地方

- /usr：FHS建议所有软件开发者都应该将数据合理地放置到这个目录的子目录下

  > /bin /usr/bin /sbin /usr/sbin /lib /usr/lib 软连接的历史
  >
  > 因为最初写UNIX系统时/bin、/sbin、/lib超过了一个存储盘的空间上限，不得不把多出来的东西放到另一个存储盘，存在用户文件夹/usr下。后来本末倒置，干脆称/usr为**U**NIX **S**ource **R**epository或是**U**NIX **S**ystem **R**esources或是**U**NIX **S**oftware **R**esource随便什么能解释过去的词，新建个/home成为用户文件夹。现在完全没有存储上限的问题了，发行版才渐渐merge了/bin和/usr/bin的东西，现在用软连接互相等价就行了（两个地方都保留着，可能是向前兼容的考虑）
  >
  > [filesystem - What is the rationale for the `/usr` directory? - Ask Ubuntu](<https://askubuntu.com/a/135679>)
  >
  > [Understanding the bin, sbin, usr/bin , usr/sbin split](<http://lists.busybox.net/pipermail/busybox/2010-December/074114.html>)

  - /usr/include：通用C头文件
  - /usr/local：自行安装的程序，不受系统软件管理机制（`apt`）控制
  - /usr/share：
  - /usr/src

- /var：存储会发生变化的程序相关文件，例如缓存、登录日志文件、软件运行产生的文件等

---

对于软件安装位置的总结：

> [filesystem - What is the rationale for the `/usr` directory? - Ask Ubuntu](<https://askubuntu.com/a/135679>)
>
> Currently, regarding install directories, your the best way to understand is to think this way:
>
> - `/usr` - all system-wide, read-only files installed by (or provided by) the OS
> - `/usr/local` - system-wide, read-only files installed by the local administrator (usually, you). *And that's why most directory names from `/usr` are duplicated here.*
> - `/opt` - an atrocity meant for system-wide, read-only *and self-contained* software. That is, software that does not split their files over `bin`, `lib`, `share`, `include` like well-behaved software should.
> - `~/.local` - the per-user counterpart of `/usr/local`, that is: software installed by (and for) each user. Like `/usr`, it has its own `~/.local/share`, `~/.local/bin`, `~/.local/lib`.
> - `~/.local/opt` - the per-user counterpart of `/opt`
>
> **So where to install software?**
>
> The above list is already half the answer of you Oracle JDK question, at least it gives several clues. The checklist to *"Where should I install software X?"* goes by:
>
> - Is it a completely self-contained, single directory software, like Eclipse IDE and other downloaded java apps, and you want it to be available to all users? Then install in `/opt`
> - Same as above, but you don't care about other users and I want to install for your user alone? Then install in `~/.local/opt`
> - Its files split over multiple dirs, like `bin` and `share`, like traditional software compiled and installed with `./configure && make && sudo make install`, and should be available to all users? Then install in `/usr/local`
> - Same as above, but for your user only? Then install in `~/.local`
> - Software installed by the OS, or via package managers (like Software Center), and, most importantly, *which any local modification might be overwritten when update manager upgrades it to a new version*? It goes to `/usr`

# 进程

`ps`

`htop` 进程管理器

| 进程状态         | 缩写表示 | 说明                                        |
| ---------------- | -------- | ------------------------------------------- |
| Running          | R        | 正在运行/可以立刻运行<br />（运行、就绪态） |
| Sleeping         | S        | 可以被中断的睡眠<br />（阻塞态）            |
| Disk Sleep       | D        | 不可被中断的睡眠                            |
| Traced / Stopped | T        | 被跟踪/被挂起的进程<br />（挂起态）         |
| Zombie           | Z        | 僵尸进程                                    |

> 父子关系引出了两种特殊的运行情况
>
> - 父进程先退出，它的子进程成为**孤儿进程** (orphan)；孤儿进程由操作系统回收，交给 init「领养」。
> - 子进程先退出，而父进程未作出回应 (wait)，子进程则变为**僵尸进程** (zombie)。僵尸进程的进程资源大部分已释放，但占用一个 PID，并保存返回值。系统中大量僵尸进程的存在将导致无法创建进程。

`jobs` `fg` `bg` 只能管理当前session中的工作

信号：

`kill`可以发送任意类型的信号

- SIGSTOP/SIGTSTP: 用`Ctrl+z`使之进入`Traced / Stopped`挂起态
- SIGINT (interrupt): `Ctrl+C` 结束
- SIGTERM (terminate): `kill`（不加指定SIGNAL默认SIGTERM）
- SIGKILL (kill): `kill -9`
- SIGCONT (continue): 把挂起态的放入前台`fg`/后台`bg`运行

指令最后加`&`使之直接运行在后台，以`nohup`开头可以避免退出session导致的SIGHUP退出

# 服务

也叫 守护进程/daemon

`.service`文件

独立于用户的登录的进程（由系统维护的自启动的进程）

systemd？

用`service` / `systemctl (systemd)`指令控制系统服务

---

定时任务

`crontab` `systemd timer`

# Server

`SysRq` 进行紧急的系统维护操作：PrtScr键

## nano

改为正常的快捷键

`sudo nano /etc/nanorc` 在最下面把那些bind都取消注释

## Battery

`upower -e | grep BAT | xargs upower -i`

睡觉/休眠 `systemctl suspend/hibernate`

## Mount

1. `lsblk -fm` List Block Devices

2. `sudo mount /dev/sdb1 /mnt/MOUNTNAME`

   `sudo mkdir /mnt/MOUNTNAME`自己建一个空文件夹

3. `sudo umount /dev/sdb1`

   或`sudo umount /mnt/MOUNTNAME`

## mirror

`sudo sed -i 's|//.*archive.ubuntu.com|//mirrors.ustc.edu.cn|g' /etc/apt/sources.list`

## DNS

```
sudo nano /etc/systemd/resolved.conf
DNS=8.8.8.8 1.1.1.1

sudo systemctl restart systemd-resolved
resolvectl status
```

## disable ipv6

```
sudo nano /etc/default/grub
GRUB_CMDLINE_LINUX_DEFAULT="ipv6.disable=1 ...others..."
sudo update-grub
```

## Turn off Screen

Create a file:

```
sudo nano /etc/systemd/system/enable-console-blanking.service
```

And put this into the file:

```
[Unit]
Description=Enable virtual console blanking

[Service]
Type=oneshot
Environment=TERM=linux
StandardOutput=tty
TTYPath=/dev/console
ExecStart=/usr/bin/setterm -blank 1

[Install]
WantedBy=multi-user.target
```

Then change the file rights and enable the service:

```
sudo chmod 664 /etc/systemd/system/enable-console-blanking.service
sudo systemctl enable enable-console-blanking.service
```

And reboot the server. Now the screens blanks after 1 minute without keypresses, even before the login.

## Cockpit

远程web端控制

`sudo apt install cockpit`

[File Browser for Cockpit](https://github.com/45Drives/cockpit-navigator)

## docker

```
curl -fsSL https://get.docker.com | sudo sh
```

删除所有

```bash
# Stop all containers
docker stop $(docker container ls -qa)

# Remove all containers
docker container rm $(docker container ls -qa)

# Remove all images
docker image rm -f $(docker image ls -qa)

# Remove all volumes
docker volume rm $(docker volume ls -q)

# Remove all networks
docker network rm $(docker network ls -q)
```

## samba

```bash
sudo apt update
sudo apt install samba
sudo nano /etc/samba/smb.conf
##########在最后添加##########
[sambashare]
    comment = Samba on Ubuntu
    path = /home/[username]/path
    read only = no
    browsable = yes
##############################

sudo service smbd restart
sudo ufw allow samba
sudo smbpasswd -a [username] # 为用户名设置密码

# 在其他电脑上用\\ip-address\sambashare以及用户名和密码进行连接
```

## WireGuard

安装
sudo apt install wireguard

报错/usr/bin/wg-quick: line 31: resolvconf: command not found
ln -s /usr/bin/resolvectl /usr/local/bin/resolvconf

用sudo cp指令在/ext/wireguard/中放入jp.conf

开启
sudo wg-quick up jp

查看状态
sudo wg show

关闭
sudo wg-quick down jp

??

- wireguard 同一账号两个设备设定同一个子网地址
- server设定子网地址和client设置子网地址不同会怎样

## FUN

1. `tldr` too long dont read
2. `cmatrix` 黑客屏保
3. `nnn` 终端文件浏览器
4. `tmux` 单屏多终端，并且作为“虚拟机”型的终端，退出后能保存session（仅仅是驻留在内存中，所以关机后就没了）