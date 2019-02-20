/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.20-log : Database - content
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`content` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `content`;

/*Table structure for table `content` */

DROP TABLE IF EXISTS `content`;

CREATE TABLE `content` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `Title` varchar(80) NOT NULL COMMENT '商品标题',
  `Price` double NOT NULL COMMENT '商品价格',
  `ImgUrl` varchar(2083) NOT NULL COMMENT '商品图片url',
  `ImgType` tinyint(4) NOT NULL COMMENT '商品图片上传方式，1：外部url，2：本地上传',
  `SalesAmount` bigint(20) DEFAULT '0' COMMENT '已销售量',
  `Summary` varchar(140) NOT NULL COMMENT '商品摘要',
  `DetailText` varchar(1000) NOT NULL COMMENT '商品正文',
  `UserId` bigint(20) NOT NULL COMMENT '商品所属卖家ID',
  `Status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态，0：正常，-1：删除',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

/*Data for the table `content` */

insert  into `content`(`Id`,`Title`,`Price`,`ImgUrl`,`ImgType`,`SalesAmount`,`Summary`,`DetailText`,`UserId`,`Status`) values (1,'First Blood',1314.23,'http://static.tongjilab.cn/image/20170215/142547700.jpg',1,5,'第一滴血','佛光普照',2,0),(2,'Flower',11.11,'/img/12/2/632b596ed4ba6f9d5144891223a3d04f.jpg',2,0,'花花','好看的花花',2,0),(5,'Java编程思想',108,'http://img14.360buyimg.com/n1/jfs/t2191/111/699154754/198998/32d7bfe0/5624b582Nbc01af5b.jpg',1,1,'thinking in java','《计算机科学丛书：Java编程思想（第4版）》赢得了全球程序员的广泛赞誉，即使是晦涩的概念，在BruceEckel的文字亲和力和小而直接的编程示例面前也会化解于无形。从Java的基础语法到高级特性（深入的面向对象概念、多线程、自动项目构建、单元测试和调试等），本书都能逐步指导你轻松掌握。 从《计算机科学丛书：Java编程思想（第4版）》获得的各项大奖以及来自世界各地的读者评论中，不难看出这是一本经典之作。本书的作者拥有多年教学经验，对C、C++以及Java语言都有独到、深入的见解，以通俗易懂及小而直接的示例解释了一个个晦涩抽象的概念。本书共22章，包括操作符、控制执行流程、访问权限控制、复用类、多态、接口、通过异常处理错误、字符串、泛型、数组、容器深入研究、JavaI/O系统、枚举类型、并发以及图形化用户界面等内容。这些丰富的内容，包含了Java语言基础语法以及高级特性，适合各个层次的Java程序员阅读，同时也是高等院校讲授面向对象程序设计语言以及Java语言的好教材和参考书。 《计算机科学丛书：Java编程思想（第4版）》特点： 适合初学者与专业人员的经典的面向对象叙述方式，为更新的JavaSE5/6增加了新的示例和章节。 测验框架显示程序输出。 设计模式贯穿于众多示例中：适配器、桥接器、职责链、命令、装饰器、外观、工厂方法、享元、点名、数据传输对象、空对象、代理、单例、状态、策略、模板方法以及访问者。 为数据传输引入了XML，为用户界面引入了SWT和Flash。 重新撰写了有关并发的章节，有助于读者掌握线程的相关知识。 专门为第4版以及JavaSE5/6重写了700多个编译文件中的500多个程序。 支持网站包含了所有源代码、带注解的解决方案指南、网络日志以及多媒体学习资料。 覆盖了所有基础知识，同时论述了高级特性。 详细地阐述了面向对象原理。 在线可获得Java讲座CD，其中包含BruceEckel的全部多媒体讲座。 在网站上可以观看现场讲座、咨询和评论。 专门为第4版以及JavaSE5/6重写了700多个编译文件中的500多个程序。 支持网站包含了所有源代码、带注解的解决方案指南、网络日志以及多媒体学习资料。 覆盖了所有基础知识，同时论述了高级特性。 详细地阐述了面向对象原理。',2,0),(6,'baby',520,'http://h.hiphotos.baidu.com/image/pic/item/4610b912c8fcc3ce441fcaf69f45d688d53f2013.jpg',1,2,'baby','cute',2,0),(7,'美景',10.12,'/img/8/10/5bc73caddc6ccb91a88c3fe14cd52384.jpg',2,0,'aaabbb','rrrrrrbbb',2,0),(8,'鲲鹏',999999,'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546851194123&di=175bb332f593d8d9c4601f9666eef276&imgtype=0&src=http%3A%2F%2Fimg.mp.sohu.com%2Fupload%2F20170629%2F94c199a320a74c36af19925e69f61f81_th.png',1,10,'神兽鲲鹏','会飞的大鱼',2,0),(9,'虫草',88888,'http://pic30.nipic.com/20130614/5750750_182806065390_2.jpg',1,4,'上好的虫草','包治百病',2,0),(10,'教室',10000,'/img/5/7/af6af918d7dbc2a4c082388c56bb438f.jpg',2,0,'长教室','亮晶晶教室',2,0);

/*Table structure for table `order` */

DROP TABLE IF EXISTS `order`;

CREATE TABLE `order` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ContentId` bigint(20) NOT NULL COMMENT '购买商品ID',
  `TradeAmount` bigint(20) NOT NULL COMMENT '购买量',
  `UserId` bigint(20) NOT NULL COMMENT '购买的买家ID',
  `TradePrice` double NOT NULL COMMENT '购买价格',
  `TradeTime` bigint(20) NOT NULL COMMENT '购买时间',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `idx_uid_cid` (`UserId`,`ContentId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `order` */

insert  into `order`(`Id`,`ContentId`,`TradeAmount`,`UserId`,`TradePrice`,`TradeTime`) values (1,1,5,1,12.12,1548739822409),(2,8,10,1,11111,1549550922245),(3,5,1,1,99,1549550922245),(4,9,4,1,88888,1550590690198),(5,6,2,1,520,1550590690198);

/*Table structure for table `shopcart` */

DROP TABLE IF EXISTS `shopcart`;

CREATE TABLE `shopcart` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `UserId` bigint(20) NOT NULL COMMENT '所属买家用户ID',
  `ContentId` bigint(20) NOT NULL COMMENT '商品ID',
  `Amount` bigint(20) NOT NULL COMMENT '数量',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `idx_uid_cid` (`UserId`,`ContentId`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

/*Data for the table `shopcart` */

insert  into `shopcart`(`Id`,`UserId`,`ContentId`,`Amount`,`CreateTime`,`UpdateTime`) values (29,1,7,4,1550592584449,1550592914005),(30,1,2,2,1550592642939,1550592648550);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `Username` varchar(127) NOT NULL COMMENT '用户名，登录账号',
  `Nickname` varchar(50) NOT NULL COMMENT '用户昵称，前台显示',
  `Role` tinyint(4) NOT NULL COMMENT '用户角色，1：buyer，2：seller',
  `PasswordMd5` varchar(50) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`Id`,`Username`,`Nickname`,`Role`,`PasswordMd5`) values (1,'buyer','买家小金',1,'37254660e226ea65ce6f1efd54233424'),(2,'seller','卖家小成',2,'981c57a5cfb0f868e064904b8745766f');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
