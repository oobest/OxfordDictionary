# OxfordDictionary
 英译字典
### 使用DexClassLoader实现代码动态加载，和隐藏关键代码
单词的查找方式是解析牛津字典的网页

### app
完成了单词的英译查找功能，
缺点是如果牛津字典的网页格式发生变化，应用将不能完成单词查找。   

## 所以将应用分成两部分：一部分为单词翻译核心功能，一部分为单词翻译显示功能和加载Dex功能；

### dictkit
该Model为单词翻译核心功能，为单词翻译应用的展示界面提供数据。此次demo代码只是完成牛津字典翻译的网页进行解析。

### endict
该Model实现应用的页面展示，和使用DexClassLoader对Dex文件的动态加载功能，（此次demo未实现对翻译核心功能dex的更新功能）。


### dictkit和endict功能合并操作提示
将dictkit打包后使用dex2jar工具，去掉资源文件，资源id文件，MainActivity,DictImpl,DictCallback文件后生成dictkit-release-dex2jar-jar2dex.dex
<img src="https://github.com/oobest/OxfordDictionary/blob/master/pic/pic.png"/>

将dictkit-release-dex2jar-jar2dex.dex放入Model:endict 打包生成的APK运行情况如下
<img src="https://github.com/oobest/OxfordDictionary/blob/master/pic/device-2018-03-03-143058.png" width="540" height="960" alt="截图"/>

