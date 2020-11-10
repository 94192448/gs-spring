# websocket ssh

## Features
- 通过URL参数打开ssh web终端,URL参数可控制终端行列等属性便于iframe嵌入
- REST API查询session列表,批量操作指定终端
- 多终端并行操作
- 支持全屏终端
- 终端窗口可调整大小
## URL Arguments
通过URL参数打开终端
``` 
http://localhost:8080/wssh?host=yourIP&username=yourName&password=yourPassword[&rows=40&cols=600]
```
查询终端session列表
``` 
http://localhost:8080/wssh/api/session
```
查询终端sessionIds列表
``` 
http://localhost:8080/wssh/api/session/ids
```
通过sessionIds执行命令
``` 
curl -X POST http://localhost:8080/wssh/api/command -d '{"command":"ll", "sessionIds":["282cede890c440afbbfadb3846f53ef5","ff2acfb4bdad44e1969346aa99b925dc","3371d822df3145faa18f87a426b69137"]}' -H "Content-Type: application/json" 

```

### How it works
```
+---------+     http     +--------+    ssh    +-----------+
| browser | <==========> | webssh | <=======> | ssh server|
+---------+   websocket  +--------+    ssh    +-----------+
```

## jsch

```
<!--SSHJ-->
		<dependency>
			<groupId>com.hierynomus</groupId>
			<artifactId>sshj</artifactId>
			<version>0.30.0</version>
		</dependency>

		<!--JSCH-->
		<dependency>
			<groupId>com.jcraft</groupId>
			<artifactId>jsch</artifactId>
			<version>0.1.55</version>
		</dependency>
		
```

## 参考

- spring-boot集成 
https://www.jianshu.com/p/6115af3c72cf
https://github.com/nivance/ssh2-spring-boot 

- 功能 https://github.com/huashengdun/webssh