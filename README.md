# websocket ssh

## Features
- SSH password授权支持,通过URL参数打开ssh web终端,URL参数可控制终端行列等属性便于iframe嵌入
- SSH public-key授权支持,包括DSA RSA ECDSA Ed25519 keys
- REST API查询session列表,批量操作指定终端
- 多终端并行操作
- 支持全屏终端
- 终端窗口可调整大小
- 文件传输支持
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
通过sessionIds批量执行命令
``` 
curl -X POST http://localhost:8080/wssh/api/command -d '{"command":"ll", 
"sessionIds":["282cede890c440afbbfadb3846f53ef5","ff2acfb4bdad44e1969346aa99b925dc"]}' -H "Content-Type: application/json" 

```
通过sessionIds文件批量传输 , webssh不同于xshell桌面ssh工具能直接操作本地文件

- scp-ext-put [source] [destination]  //将terminal server上[source]文件上传到远程ssh server [destination]
- scp-ext-get [source] [destination]  //从远程ssh server上[source]文件下载到terminal server [destination]
``` 

curl -X POST http://localhost:8080/wssh/api/command -d '{"command":"scp-ext-put mytmps-a tmp/test-a", 
"sessionIds":["cc9c3b8f56774737a036ec4f8db49e77"]}' -H "Content-Type: application/json" 
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