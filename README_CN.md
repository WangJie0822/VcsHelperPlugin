# VcsHelperPlugin

![Build](https://github.com/WangJie0822/VcsHelperPlugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/cn.wj.plugin.vcs.svg)](https://plugins.jetbrains.com/plugin/cn.wj.plugin.vcs)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/cn.wj.plugin.vcs.svg)](https://plugins.jetbrains.com/plugin/cn.wj.plugin.vcs)

<!-- Plugin description -->

<p>
<b>
<a href="https://github.com/WangJie0822/VcsHelperPlugin">GitHub</a> |
<a href="https://github.com/WangJie0822/VcsHelperPlugin/issues">Issues</a> |
<a href="mailto:w15555650921@gmail.com">Gmail</a> |
<a href="mailto:15555650921@163.com">163</a>
</b>
</p>

你可用通过这个插件按照以下模板创建 commit 信息:

```
<type>(<scope>): <subject>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```

你可以使用Commit弹窗中提交信息历史按钮前面的按钮 `Create Commit Message` ![](https://raw.githubusercontent.com/WangJie0822/VcsHelperPlugin/develop/src/main/resources/icons/load.svg) 
或者使用工具栏中的按钮 `Show Create Template Dialog` ![](https://raw.githubusercontent.com/WangJie0822/VcsHelperPlugin/develop/src/main/resources/icons/commit.svg) 或者默认快捷键 <kbd>Alt K</kbd>来使用此功能。

![使用示例](SAMPLE_USE.png)

你还可以通过 <kbd>Settings/Preferences</kbd> > <kbd>Tools</kbd> > <kbd>VcsHelper</kbd> 修改配置

![设置示例](SAMPLE_SETTINGS.png)

或者添加像下面格式的 `*.json` 文件来自定义配置:
```json
{
  "keywords": {
    "wrapWords": true,
    "maxLineLength": 70,
    "scopeWrapperStart": "(",
    "scopeWrapperEnd": ")",
    "descriptionSeparator": ": ",
    "breakingChanges": "BREAKING CHANGES: ",
    "breakingChangesEmpty": "",
    "closedIssues": "Closes: ",
    "closedIssuesSeparator": ",",
    "closedIssuesEmpty": ""
  },
  "changeTypes": [
    {
      "title": "Styles",
      "action": "STYLE",
      "description": "Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc)"
    },
    {
      "title": "Code Refactoring",
      "action": "REFACTOR",
      "description": "A code change that neither fixes a bug nor adds a feature"
    }
  ]
}
```
<!-- Plugin description end -->

## 安装

- 使用 IDEA 插件系统:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>搜索 "VcsHelper"</kbd> >
  <kbd>Install Plugin</kbd>
  
- 手动:

  下载 [最新释放版本](https://github.com/WangJie0822/VcsHelper/releases/latest) 并使用
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd> 进行安装


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
