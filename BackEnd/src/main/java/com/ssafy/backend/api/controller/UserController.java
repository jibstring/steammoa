package com.ssafy.backend.api.controller;

import com.ssafy.backend.api.request.UserUpdatePutReq;
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
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;


    @GetMapping("/profile/{user_service_id}")
    @ApiOperation(value = "회원 조회", notes = "<strong>사용자 아이디</strong>를 통해 회원 정보 조회.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 403, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<? extends Map<String,Object>> getUserInfo(@PathVariable("user_service_id") String userServiceId) {

        Map<String, Object> result = new HashMap<>();

        try {
            User user = (User) userService.getUserInfoByUserId(userServiceId).get("user");
//            System.out.println(user.toString());
            UserDto userDto = new UserDto();
            //builder 패턴 적용해야함
            userDto.setUserId(user.getUserId());
            userDto.setUserServiceId(user.getUserServiceId());
            userDto.setUserPoint(user.getUserPoint());
            result.put("user",userDto);
            result.put("message","Success");
        } catch (Exception e) { // 에러코드 정리해서 처리해야할 부분
            result.put("message","Fail");
            return ResponseEntity.status(403).body(result);
        }
        return ResponseEntity.status(200).body(result);
    }

    @PutMapping("/profile")
    @ApiOperation(value = "회원 정보 수정", notes = "<strong>회원 정보 수정 </strong>")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<? extends UserRes> updateUserInfo(@ApiIgnore Authentication authentication, @RequestBody UserUpdatePutReq userUpdatePutReq) {
        /**
         * 요청 헤더 액세스 토큰이 포함된 경우에만 실행되는 인증 처리이후, 리턴되는 인증 정보 객체(authentication) 통해서 요청한 유저 식별.
         * 액세스 토큰이 없이 요청하는 경우, 403 에러({"error": "Forbidden", "message": "Access Denied"}) 발생.
         */
        Map<String, Object> result = new HashMap<>();

        try {
            SsafyUserDetails userDetails = (SsafyUserDetails) authentication.getDetails();
            User user = userDetails.getUser();
            String userServiceId = user.getUserServiceId();
            // userName 수정
            user.setUserName(userUpdatePutReq.getUser_name());
            userService.updateUser(user);
            result = userService.getUserInfoByUserId(userServiceId);
        } catch (Exception e) {
            return (ResponseEntity<? extends UserRes>) ResponseEntity.badRequest();
        }

        User user = (User) result.get("user");

        return ResponseEntity.ok(UserRes.of(200, "회원 정보 조회 성공", user.getUserId(), user.getUserServiceId(), user.getUserPoint()));

    }
}

