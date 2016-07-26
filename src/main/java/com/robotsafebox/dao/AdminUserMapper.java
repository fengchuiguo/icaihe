package com.robotsafebox.dao;

import com.robotsafebox.entity.AdminUser;

import java.util.List;

public interface AdminUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdminUser record);

    int insertSelective(AdminUser record);

    AdminUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdminUser record);

    int updateByPrimaryKey(AdminUser record);


//    new add
    List<AdminUser> queryByUserName(String username);

}