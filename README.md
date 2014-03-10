官方文档
===================

http://subchen.github.io/jetbrick-template/


编译方法
===================

1. 先安装 apache-ant 1.9.x

    ```
    wget http://mirrors.cnnic.cn/apache//ant/binaries/apache-ant-1.9.2-bin.zip
    ```

2. 设置好 JDK, ANT 环境变量

    ```
    set JAVA_HOME=/path/jdk_1.6.x
    set ANT_HOME=/path/apache-ant-1.9.x
    set PATH=%JAVA_HOME%/bin;%ANT_HOME%/bin;%PATH%
    ```

3. 编译

    ```
    git clone https://github.com/subchen/jetbrick-template-webmvc-samples.git
    cd jetbrick-template-webmvc-samples
    ant dist
    ```

4. 编译后的文件存放在每个 project 的目录中

    ```
    jetx-samples-<project>.war
    jetx-samples-<project>.zip
    ```


下载
===================

* [jetx-samples-servlet.zip](http://subchen.github.io/jetbrick-template/demo/jetx-samples-servlet.zip)
* [jetx-samples-jfinal.zip](http://subchen.github.io/jetbrick-template/demo/jetx-samples-jfinal.zip)
* [jetx-samples-springmvc.zip](http://subchen.github.io/jetbrick-template/demo/jetx-samples-springmvc.zip)
* [jetx-samples-struts.zip](http://subchen.github.io/jetbrick-template/demo/jetx-samples-struts.zip)
* [jetx-samples-jodd.zip](http://subchen.github.io/jetbrick-template/demo/jetx-samples-jodd.zip)

下载的 zip 包中包含完整的源代码和可直接运行的 war 包。

更多范例请查看官方文档下载区： http://subchen.github.io/jetbrick-template/download.html


作者 Author
===================

* Author: Guoqiang Chen, Shanghai, China
* Email: subchen&#64;gmail.com
* WebSite: http://subchen.github.io/
* Blog: http://my.oschina.net/sub/blog


开源许可 License
===================

jetbrick-template
http://subchen.github.io/jetbrick-template/

Copyright 2010-2014 Guoqiang Chen. All rights reserved.
Email: subchen@gmail.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

