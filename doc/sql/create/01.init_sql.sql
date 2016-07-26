DROP TABLE IF EXISTS `ich_admin_user`;

CREATE TABLE `ich_admin_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_name` varchar(45) NOT NULL COMMENT '后台用户名',
  `pass_word` varchar(45) NOT NULL COMMENT '后台密码',
  `show_name` varchar(100)  DEFAULT '' COMMENT '名称',
  `head_img`  varchar(100)  DEFAULT '' COMMENT '头像地址',
  `create_time` TIMESTAMP  COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='后台用户表';


INSERT INTO `ich_admin_user` (`id`, `user_name`, `pass_word`, `show_name`, `head_img`, `create_time`)
VALUES(1,'admin','cf3061f0abf1d19a9a168694356f1e26','管理员','',sysdate());