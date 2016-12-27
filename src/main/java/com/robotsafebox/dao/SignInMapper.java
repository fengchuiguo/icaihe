package com.robotsafebox.dao;

import com.robotsafebox.entity.SignIn;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SignInMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SignIn record);

    int insertSelective(SignIn record);

    SignIn selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SignIn record);

    int updateByPrimaryKey(SignIn record);

    //new add
    List<Map> selectSignInByUserIdAndDate(@Param("userId") Long userId,@Param("startDate") String startDate,@Param("endDate")String endDate);

}