# Cacio Calculator

1. 文件结构

   文件夹根目录下是源码以及编译并打包好的jar包，HelpDoc文件夹下是计算器的帮助文档。

   **注意!! 由于尚未知的原因，jar包必须和HelpDoc文件夹在同一目录下说明文档才可起作用**

2. 编译说明

   编译命令：

   ```
   javac Cacio.java
   ```

   在windows下编译时可能会出错，如果报错

   ```
   “错误：编码GBK的不可映射字符”
   ```

   请在编译命令后加上`-encoding utf8`选项

3. 运行环境：

   Java JRE8.0及以上版本

   **运行前请确保您已安装JRE！**

4. 运行说明

   运行jar包：双击系统对应jar包，或在命令行运行：

   ```
   java -jar Cacio_$SystemName.jar
   ```

   自编译运行命令：

   ```
   java Cacio
   ```