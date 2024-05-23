# 设计说明

## 📄 代码结构

```text
    │  ├─control      与协助方（控制方相关）
    │  ├─obj          常用对象
    │  ├─resort       求助方
    │  │  └─thread      求助方线程
    │  ├─screen       捕获屏幕工具类
    │  ├─UI           可复用的UI界面
    │  │  └─StarComponents
    │  └─Util         工具类
    └─melloware	     （windows 快捷键-注册表相关）
        └─jintellitype
```


## 源码调试运行

- [运行 `com.lym.UI.StartUi` 的 `main` 方法](../src/com/lym/UI/StartUi.java)

## 环境依赖

- 操作系统：不限（`windows`/`linux`/`mac`）
- JDK：`1.8`，

# 关于星控

设计上分为两种使用者: 求助者 + 协助方。协助方可以根据IP 和 port 或者是 求助方发来的`星控令` 直连对方。

设计初衷在于协助，而不是监控，也就是说求助者有权控制协助方的动作。

## UI 说明

星星离我们很远，占卜师却可以洞察远方，此为名称由来。

因此UI采用占卜扑克为初始画面，界面为深色风格、透明风格，与星星相符。又加上该程序是初学者所编写，此为难看UI由来。
