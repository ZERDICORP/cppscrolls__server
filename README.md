# cppscrolls__server :star2:
#### A platform where you can solve problems in C++, as well as create your own ([client](https://github.com/ZERDICORP/cppscrolls__client)).
## Deployment Guide :speaker:
> All work will be done on arch linux.
#### 1. Clone the repository
```
$ git clone https://github.com/ZERDICORP/cppscrolls__server.git
```
#### 2. Check dependencies
```
$ java --version
openjdk version "17.0.3" 2022-04-19
OpenJDK Runtime Environment (build 17.0.3+3)
OpenJDK 64-Bit Server VM (build 17.0.3+3, mixed mode)
$ jar --version
jar 17.0.3
$ docker --version
Docker version 20.10.13, build a224086349
$ nginx -version
nginx version: nginx/1.20.2
$ mariadb --version
mariadb  Ver 15.1 Distrib 10.7.3-MariaDB, for Linux (x86_64) using readline 5.1
```
#### 3. Application configuration setting
> Replace all values between <>.
```
$ cd /path/to/cppscrolls__server/src
$ vi resources/app.cfg
```
#### 4. Nginx configuration setting
```
$ vi resources/nginx/nginx.conf
```
> Replace `<user>` with your linux user.  
> Replace `/path/to/images/` with `/path/to/cppscrolls__server/build/images/`.
```
$ sudo cp resources/nginx/nginx.conf /etc/nginx/
```
#### 5. Creating a docker image
```
$ cd resources/docker/ && docker build -t solution . && cd ../../
```
#### 6. MySQL setup
```
$ mysql -u root -p
MariaDB> create database cppscrolls;
MariaDB> exit;
$ mysql -u root -p cppscrolls < resources/mysql/cppscrolls.sql
```
#### 7. Server start
```
$ ./build && cd ../build/ && ./run
```
#### ~ 8. Delete everything except `build` folder
```
$ rm -rf /path/to/cppscrolls__server/{,.[!.],..?}!(build)
```
