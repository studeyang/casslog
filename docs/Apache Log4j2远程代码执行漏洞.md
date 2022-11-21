## Apache Log4j2 远程代码执行漏洞（CVE-2021-44228、CVE-2021-45046）

2021-12-10 日，Apache Log4j2 存在一处远程代码执行漏洞（CVE-2021-44228），在引入 Apache Log4j2 处理日志时，会对用户输入的内容进行一些特殊的处理，攻击者可以构造特殊的请求，触发远程代码执行。目前POC已公开，风险较高。

12月16日，官方披露低于 2.16.0 版本除了存在拒绝服务漏洞外，还存在另一处远程代码执行漏洞（CVE-2021-45046）。

## 威胁级别

威胁级别：【严重】

（说明：威胁级别共四级：一般、重要、严重、紧急）

## 漏洞影响范围

影响版本：

2.0-beat9 <= Apache Log4j 2.x < 2.16.0（2.12.2 版本不受影响）

已知受影响的应用及组件：spring-boot-starter-log4j2/Apache Solr/Apache Flink/Apache Druid

安全版本：

Apache Log4j 1.x 不受影响

Apache Log4j 2.16.0

## 漏洞处置

目前官方已发布修复版本修复了该漏洞，请受影响的用户尽快升级Apache Log4j2所有相关应用到安全版本：[https://logging.apache.org/log4j/2.x/download.html](https://logging.apache.org/log4j/2.x/download.html?spm=a2c4g.11174386.n2.3.6b334c076rAmk0)

Java 8（或更高版本）的用户建议升级到 2.16.0 版本；

