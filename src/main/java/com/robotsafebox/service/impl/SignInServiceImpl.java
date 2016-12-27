package com.robotsafebox.service.impl;

import com.robotsafebox.dao.SignInMapper;
import com.robotsafebox.entity.SignIn;
import com.robotsafebox.service.SignInService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SignInServiceImpl implements SignInService {

    @Resource
    private SignInMapper signInMapper;

    public int saveSignIn(SignIn signIn) {
        if (signIn.getId() != null) {
            return signInMapper.updateByPrimaryKeySelective(signIn);
        }
        return signInMapper.insertSelective(signIn);
    }

    public List<Map> searchSignInByUserIdAndDate(Long userId, String date) {
        return signInMapper.selectSignInByUserIdAndDate(userId, date+" 00:00:00",date+" 23:59:59");
    }
}
