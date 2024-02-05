# SMB

NTFS文件系统设置，security和share里都添加自己账号的完全控制

用户名为登录微软的邮箱，密码为微软账号的密码

若在微软网页更新了微软密码，则需要在电脑上手动更新密码

> runas /user:email@gmail.com cmd.exe

否则得用旧的密码才能登录SMB分享

# WebDAV Server

BaseAuthentication的账户到Computer->Manage->Local Users and Groups里查，密码是微软账号的密码，不是PIN码

目录的Security需要设置为Everyone或者是ISS的用户组IIS_IUSRS

# WebDAV Client

Windows自带的Map Network Driver

如果WebDAV Server是http的，可能会报错

The network name cannot be found.
Network Error: 0x80070043

到regedit中，修改
Computer\HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\WebClient\Parameters 的 BasicAuthLevel 为 2，让它允许http的连接，记得要重启

另外由于WebClient\Parameters 的 FileSizeLimitInBytes 约为 4GB，所以大电影就没法传……

# Win7装机

用Ventoy制作系统启动USB

去找人家打包好的有USB、网络驱动的iso“不忘初心”

> 自己整纯净iso还要自己装驱动
>
> win7 纯净的iso里没有USB Driver，要用下面的工具添加driver
>
> Windows USB Installation Tool www.gigabyte.com

MBR\GPT分区不支持

```cmd
# shitf+f10打开终端
diskpart.exe
list disk
select disk 0
clean
convert mbr或gpt
```

