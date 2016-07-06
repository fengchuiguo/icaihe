
--数据库初始化脚本

--创建数据库
CREATE DATABASE seckill;

--使用数据库
use seckill;

CREATE TABLE seckill(
`seckill_id` BIGINT NOT NUll AUTO_INCREMENT COMMENT '商品库存ID',
`name` VARCHAR(120) NOT NULL COMMENT '商品名称',
`number` int NOT NULL COMMENT '库存数量',
`start_time` TIMESTAMP  NOT NULL COMMENT '秒杀开始时间',
`end_time`   DATETIME   NOT NULL COMMENT '秒杀结束时间',
`create_time` TIMESTAMP NOT NULL COMMENT '创建时间',
PRIMARY KEY (seckill_id),
key idx_start_time(start_time),
key idx_end_time(end_time),
key idx_create_time(create_time)
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

--初始化工具
INSERT INTO seckill
  (name,number,start_time,end_time)
VALUE
  ('1000元秒杀iphone6',100,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
  ('500元秒杀ipad2',200,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
  ('300元秒杀小米4',300,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
  ('200元秒杀红米note',400,'2015-11-01 00:00:00','2015-11-02 00:00:00');

INSERT INTO seckill(name,number,start_time,end_time)
VALUE ('100元秒杀哈哈哈',400,'2016-7-31 00:00:00','2016-7-31 00:00:00');

INSERT INTO seckill (name,number,start_time,end_time)
VALUE ('20元秒杀ing',600,'2016-6-1 00:00:00','2016-8-31 00:00:00');

update seckill set create_time =sysdate();

-- 秒杀成功明细表
-- 用户登录认证相关信息(简化为手机号)
CREATE TABLE `success_killed`(
`seckill_id` BIGINT NOT NULL COMMENT '秒杀商品ID',
`user_phone` BIGINT NOT NULL COMMENT '用户手机号',
`state` TINYINT NOT NULL DEFAULT -1 COMMENT '状态标识:-1:无效 0:成功 1:已付款 2:已发货',
`create_time` TIMESTAMP NOT NULL COMMENT '创建时间',
PRIMARY KEY(seckill_id,user_phone),/*联合主键*/
KEY idx_create_time(create_time)
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';
