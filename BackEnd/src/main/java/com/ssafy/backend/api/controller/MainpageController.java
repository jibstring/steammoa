package com.ssafy.backend.api.controller;

import com.ssafy.backend.api.service.MainpageService;
import com.ssafy.backend.db.entity.IntegratedSearch_ContentDTO;
import com.ssafy.backend.db.entity.MainpageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 메인페이지 API 요청 처리를 위한 컨트롤러 정의.
 * http://localhost:8080/swagger-ui.html
 **/
@Api(value = "메인페이지 API", tags = {"Mainpage"})
@RestController
@RequestMapping("/api/main")
@Slf4j
public class MainpageController {
    @Autowired
    MainpageService mainpageService;

    @GetMapping("")
    @ApiOperation(value = "메인페이지 데이터 전송", notes = "메인페이지 배너, 파티, 기획상품을 조회.")
    public ResponseEntity<?> getMainpage(){
        MainpageDTO result = mainpageService.getMainpage();
        return ResponseEntity.status(200).body(result);
    }


    @GetMapping("/search")
    @ApiOperation(value = "통합 검색", notes = "type: user -> 유저 정보 검색 \n type: content -> '게임 이름'을 받아서 파티, 게임, 공략글을 검색")
    public ResponseEntity<?> getIntegratedSearch(@RequestParam(required = true, defaultValue = "content") String type, @RequestParam(required = false, defaultValue = "lucky") String keyword){
        Map<String,Object> result = mainpageService.getIntegratedSearch(type, keyword);
        return ResponseEntity.status(200).body(result);
    }

}
