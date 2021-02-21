# simple-rpc-framework
![Build Status](https://travis-ci.org/crwen/simple-rpc-framework.svg?branch=master&status=passed)

中文|[English](./README-EN.md)

本项目是一个简单的 RPC 框架，基于 Netty 和 Nacos 实现
## 模块

- **rpc-api**	——	通用接口
- **rpc-common**	——	实体对象、工具类等公用类
- **rpc-core**	——	框架的核心实现
- **sample-client**	——	测试用消费侧
- **sample-server**	——	测试用提供侧

## 传输协议

```html
+------------------------------------------------+
| Magic Number | Version | Package Type | Status |
|   4byte      |  1byte  |     1byte    |  1byte |
+------------------------------------------------+
| reserve |   Serializer Type  |   Data length   | 
| 4byte   |      1byte         |     4byte       |
+------------------------------------------------+
|                  Date Content                  |
+------------------------------------------------+
```

| 字段            | 解释                                                 |
| :-------------- | :------------------------------------------------- |
| Magic Number    | 魔数，标识一个协议包，0xCAFEBABE                       |
| Version         | 版本号                                              | 
| Package Type    | 包类型，标明这是一个调用请求还是调用响应                  |
| Status          | 状态                                               |
| reserve         | 保留字段                                           |
| Serializer Type | 序列化器类型，标明数据的序列化方式               |
| Data length     | 数据长度                                          |
| Data Content    | 传输的对象，通常是一个`RpcRequest`或`RpcClient`对象  |
