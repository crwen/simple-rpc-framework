# simple-rpc-framework
![Build Status](https://travis-ci.org/crwen/simple-rpc-framework.svg?branch=master&status=passed)

English|[中文](./README.md)

本项目是一个简单的 RPC 框架，基于 Netty 和 Nacos 实现
## Module

- **rpc-api**	——	General interface
- **rpc-common**	——	Common classes such as entity objects, utility classes, etc
- **rpc-core**	——	The core implementation of the framework
- **sample-client**	——	Test of consumer
- **sample-server**	——	Test of provider

## Transport Protocol

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

| fields          | description                                                      |
| :-------------- | :---------------------------------------------------------------- |
| Magic Number    | Magic，to identify a package.0xCAFEBABE                           |
| Version         | version number                                                    | 
| Package Type    | To specify a request or a response                                |
| Status          | Status of package                                                 |
| reserve         | reserve field                                                     |
| Serializer Type | To tell how to serialize or deserialize                           |
| Data length     | Length of data                                                    |
| Data Content    | Object that is transported.General be `RPCRequest`or`RPCResponse` |
