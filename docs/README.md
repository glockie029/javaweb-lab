# docs 说明

该目录用于沉淀当前 JavaWeb 学习项目的学习路线、阶段进度、工程化实践、审计笔记、检查清单和案例复盘模板。

当前文档按“学习主线 + 专题知识 + 审计方法”组织，推荐从路线文档进入，再逐步展开各专题。

## 1. 学习主线

1. [01-JavaWeb前置学习路线.md](/Users/mac/Documents/javaweb-lab/docs/01-JavaWeb前置学习路线.md)
   完整定义 JavaWeb 代码审计学习路径、阶段目标、阶段顺序、产出物和完成标准。
2. [02-阶段进度记录.md](/Users/mac/Documents/javaweb-lab/docs/02-阶段进度记录.md)
   用于记录每个阶段的学习进度、关键结论、调用链理解、风险点和验证结果。

## 2. 专题知识

1. [03-JavaWeb代码审计检查清单.md](/Users/mac/Documents/javaweb-lab/docs/03-JavaWeb代码审计检查清单.md)
   按真实项目排查顺序整理的 JavaWeb 审计清单。
2. [04-Java安全审计前置基础.md](/Users/mac/Documents/javaweb-lab/docs/04-Java安全审计前置基础.md)
   覆盖反射、代理、类加载、序列化、IO、XML、表达式引擎等 Java 审计前置基础。
3. [05-Java工程化实践与构建链.md](/Users/mac/Documents/javaweb-lab/docs/05-Java工程化实践与构建链.md)
   覆盖 Maven、Gradle、多模块、依赖分析、打包方式和配置加载。
4. [06-Spring生态与主流框架审计.md](/Users/mac/Documents/javaweb-lab/docs/06-Spring生态与主流框架审计.md)
   覆盖 Spring Boot、Spring MVC、MyBatis、JPA、Jackson、Fastjson、模板引擎、Spring Security、Shiro 等主流技术栈。该文档只展开 Spring 生态中的实现差异与框架特有风险，不替代阶段六中的通用认证、授权与会话安全主线。
5. [07-JavaWeb漏洞类型与Source-Sink映射.md](/Users/mac/Documents/javaweb-lab/docs/07-JavaWeb漏洞类型与Source-Sink映射.md)
   从漏洞类型出发建立 source、sink、典型组件和审计切入点的对应关系。
6. [08-配置与中间件审计.md](/Users/mac/Documents/javaweb-lab/docs/08-配置与中间件审计.md)
   覆盖配置文件、容器、中间件、日志、外部服务和运行环境的审计要点。
7. [09-JavaWeb审计流程与案例复盘模板.md](/Users/mac/Documents/javaweb-lab/docs/09-JavaWeb审计流程与案例复盘模板.md)
   给出可直接执行的审计步骤、搜索策略、问题记录格式和案例复盘模板。
8. [10-真实项目训练、依赖漏洞与工具化扩展.md](/Users/mac/Documents/javaweb-lab/docs/10-真实项目训练、依赖漏洞与工具化扩展.md)
   补充真实项目训练分级、依赖漏洞识别、补丁 diff 复盘和 SAST / 半自动审计工具使用方向。

## 3. 推荐使用方式

1. 先阅读路线文档，明确当前所处阶段和阶段目标。
2. 学每个阶段时，同时参考对应专题文档，避免只记概念、不建立审计视角。
   传统 JavaWeb 的认证、授权、会话问题先按阶段六主线学习；涉及 `Interceptor`、`Spring Security`、`Shiro`、前后端分离 `Token` 实现时，再进入 Spring 专题文档。
3. 每完成一个阶段，更新进度记录，把关键调用链、风险点、验证结论补齐。
4. 在开始真实项目审计前，先通读检查清单、漏洞映射和审计流程文档。
5. 在进入真实开源项目、历史漏洞复盘和工具化辅助阶段前，再阅读扩展文档，按分级路线补充训练。
6. 做完一个样例或真实项目后，按案例复盘模板沉淀证据链和修复结论。

## 4. 当前文档定位

1. 服务于“JavaWeb 代码审计学习”，而不是完整 Java 开发路线。
2. 重点围绕请求链、参数流、权限链、配置链、依赖链和危险能力调用展开。
3. 同时覆盖传统 JavaWeb 与主流 Spring 生态，适配老项目和现代项目。
4. 目标是形成一套可直接复用到真实审计任务中的知识结构和工作模板。
