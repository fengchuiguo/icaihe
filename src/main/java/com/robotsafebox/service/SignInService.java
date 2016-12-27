package com.robotsafebox.service;

import com.robotsafebox.entity.SignIn;

import java.util.List;
import java.util.Map;

public interface SignInService {

    int saveSignIn(SignIn signIn);

    List<Map> searchSignInByUserIdAndDate(Long userId, String date);

}
