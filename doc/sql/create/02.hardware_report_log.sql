
CREATE TABLE hardware_report_log(
`id` BIGINT NOT NUll AUTO_INCREMENT COMMENT 'ID',
`ich_id` VARCHAR(50)  NOT NULL COMMENT 'ICHID',
`action_type` int NOT NULL COMMENT '上报类型(1：WIFI配置成功；2：报警；3：电量不足；4：开箱；5：关箱。)',
`create_time` TIMESTAMP  COMMENT '创建时间',
`remark` VARCHAR(200)  COMMENT '备注',
PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='硬件上报记录';