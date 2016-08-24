package com.robotsafebox.service.impl;

import com.robotsafebox.dao.SuggestionMapper;
import com.robotsafebox.entity.Suggestion;
import com.robotsafebox.service.SuggestionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SuggestionServiceImpl implements SuggestionService {

    @Resource
    private SuggestionMapper suggestionMapper;

    public int saveSuggestion(Suggestion suggestion) {
        if (suggestion.getId() != null) {
            return suggestionMapper.updateByPrimaryKeySelective(suggestion);
        }
        return suggestionMapper.insertSelective(suggestion);
    }
}
