package com.robotsafebox.service.impl;


import com.robotsafebox.dao.AdminUserMapper;
import com.robotsafebox.entity.AdminUser;
import com.robotsafebox.service.AdminUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Resource
    private AdminUserMapper adminUserMapper;

    public AdminUser getAdminUser(String username) {
        List<AdminUser> adminUsers = adminUserMapper.queryByUserName(username);
        return adminUsers != null & adminUsers.size() > 0 ? adminUsers.get(0) : null;
    }

}
