package com.robotsafebox.dao;

import com.robotsafebox.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);


    //new add
    User selectByPhone(@Param("phone") String phone);

    User selectCreateUserByBoxId(@Param("boxId") Long boxId);

}