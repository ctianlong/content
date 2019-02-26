## 内容销售系统
#### 在线预览地址：
- [shop163.tongjilab.cn](http://shop163.tongjilab.cn)
#### 系统简要说明
- 系统有两类用户，买家和卖家，系统可以有多个卖家和多个买家，用户的账号由后台直接注入，不由本系统的功能来注册和维护。卖家可以发布内容，为内容定价，查看购买情况。买家可以浏览已发布的内容，选择购买，查看已购买的内容。
- 主要技术栈：Spring Boot、Spring MVC、Thymeleaf、Mybatis、MySQL、Bootstrap、jQuery、AdminLTE。
- 在工程content目录中，doc目录包含数据库schema和部分测试数据sql文件，img目录为用户上传的图片（上传图片存储目录当前配置为工程根路径下的img目录，该配置可修改，后期可将图片上传至内容存储中；为了防止一个目录下出现太多文件，使用hash算法创建多级目录打散图片存储）。
