# <center>Cmder安装使用</center>

## 下载cmder

- 下载地址```<https://cmder.net/>```

## 下载Chocolatey

- 管理员身份打开cmder,输入以下命令

  - ```
    @"%SystemRoot%\System32\WindowsPowerShell\v1.0\powershell.exe" -NoProfile -InputFormat None -ExecutionPolicy Bypass -Command "iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))" && SET "PATH=%PATH%;%ALLUSERSPROFILE%\chocolatey\bin"
    ```

- 检查版本信息 ```choco -v```

- 升级Chocolatey ```choco upgrade chocolatey```

- 安装 powershell ```choco install powershell```

- 安装 dotnet4.5.1 ```choco install dotnet4.5.1```

## 配置ssh

-  vim /Users/tushenghong01/.ssh/config,输入以下配置

  - ```
    Host github.com
    User initpassion
    Hostname ssh.github.com
    PreferredAuthentications publickey
    IdentityFile ~/.ssh/initpassion
    Port 443
    
    Host jumper
       #nisp-jumper1.hz.163.org
       Hostname 123.58.180.66
       User tushenghong01
       Port 1046
       ForwardAgent yes
    
    Host *.163.org
       User tushenghong01
       Port 1046
       ProxyCommand ssh jumper exec nc -w 10 %h %p 2>/dev/null
    ```

- mysql 连接

  - ```ssh -fCPN -L 6161:10.172.145.155:6000 -p 1046 tushenghong01@nisp37.dg.163.org```
  - mysql -h127.0.0.1 -P6161 -ung_online -png_online

