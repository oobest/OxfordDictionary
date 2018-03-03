# OxfordDictionary
####英译字典

###使用DexClassLoader实现代码动态加载，和隐藏关键代码

单词的查找方式是解析牛津字典的网页
Model:app完成了单词的英译查找功能，缺点是如果牛津字典的网页格式发生变化，将会解析失败

于是设计了一个动态更新的方式：
###见Model：dictkit,和Model：endict；
（没有完成功能包括：dex文件加密，朗文英语翻译的Dex;中文翻译的Dex;DexClassLoader根据用户不同翻译，加载不同的Dex）
其中dictkit是字典书籍提供者（这里主要是解析牛津网页，如果想使用其他地方的数据，直接实现DictCallback.java和DictImpl接口即可）
Endict是展示层，用户安装后，不能动态更新。

主要的知识点是DexClassLoader的动态加载。

将dictkit打包后使用dex2jar工具，去掉资源文件，资源id文件，MainActivity,DictImpl,DictCallback文件后生成dictkit-release-dex2jar-jar2dex.dex
<img src="https://github.com/oobest/DoubanTop250/blob/master/device-2018-03-01-170303.png" width="540" height="960" alt="截图"/>

将dictkit-release-dex2jar-jar2dex.dex放入Model:endict
<img src="https://github.com/oobest/DoubanTop250/blob/master/device-2018-03-01-170303.png" width="540" height="960" alt="截图"/>

