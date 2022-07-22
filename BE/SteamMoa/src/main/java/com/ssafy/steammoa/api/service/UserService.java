package com.ssafy.steammoa.api.service;

import com.ssafy.steammoa.api.request.UserRegisterPostReq;
import com.ssafy.steammoa.db.entity.User;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface UserService {
    User createUser(UserRegisterPostReq userRegisterInfo);
    User getUserByUserId(String userId);
}
