package com.robotsafebox.dao;

import com.robotsafebox.entity.Suggestion;

public interface SuggestionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Suggestion record);

    int insertSelective(Suggestion record);

    Suggestion selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Suggestion record);

    int updateByPrimaryKey(Suggestion record);
}