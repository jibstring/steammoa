package com.ssafy.backend.api.controller;

import com.ssafy.backend.api.response.UserRes;
import com.ssafy.backend.api.service.UserService;
import com.ssafy.backend.common.auth.SsafyUserDetails;
import com.ssafy.backend.db.entity.User;
import com.ssafy.backend.api.response.UserDto;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 * 유저 관련 API 요청 처리를 위한 컨트롤러 정의.
 * http://localhost:8080/swagger-ui.html
 **/
@Api(value = "유저 API", tags = {"User"})
@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;


    @GetMapping("/me")
    @ApiOperation(value = "회원 조회", notes = "<strong>사용자 아이디</strong>를 통해 회원 정보 조회.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<? extends UserRes> getUserInfo(@ApiIgnore Authentication authentication) {
        /**
         * 요청 헤더 액세스 토큰이 포함된 경우에만 실행되는 인증 처리이후, 리턴되는 인증 정보 객체(authentication) 통해서 요청한 유저 식별.
         * 액세스 토큰이 없이 요청하는 경우, 403 에러({"error": "Forbidden", "message": "Access Denied"}) 발생.
         */
//        if(authentication == null){
////            System.out.println("아무것도 전달되지 않음");
////        }

        Map<String, Object> result = new HashMap<>();
        try {
            SsafyUserDetails userDetails = (SsafyUserDetails) authentication.getDetails();
            User user = userDetails.getUser();
            String userServiceId = user.getUserServiceId();
            // 생각해야할 부분 : body에 유저정보 + 파티정보 + 팔로워 정보 + 글 목록 받아와야함  / 이 데이터를 어떻게 묶어서 보낼것인지.
            /*
             * 1. Map<String, Object> 형식으로
             * 2. 모든 Entity 의 toString을 재정의해라 (json방식)
             * */
            System.out.println(userDetails.toString());
            System.out.println("userServiceId : " + userServiceId);
            System.out.println("user 정보 : " + result.get("user"));
            result = userService.getUserInfoByUserId(userServiceId);
        } catch (Exception e) { // 에러코드 정리해서 처리해야할 부분

        }


//      List<Follow> fList = userService.getFollowByUserId(userId);
        User user = (User)result.get("user");
        user.setUserPoint(0.0);
        UserDto userDto = UserDto.builder().userServiceId(user.getUserServiceId()).userId(user.getUserId()).userPoint(user.getUserPoint()).build();
        System.out.println(userDto.toString());

        UserRes res = new UserRes();
        res.setUserDto(userDto);

        return ResponseEntity.ok(UserRes.of(200,"회원 정보 조회 성공", userDto));
        }

    }
