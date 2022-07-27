package com.ssafy.backend.common.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 서버 요청에대한 기본 응답값(바디) 정의.
 */
@Getter
@Setter
@ApiModel("BaseResponseBody")
public class BaseResponseBody {
	@ApiModelProperty(name="응답 코드", example = "200")
	Integer status = null;
	@ApiModelProperty(name="응답 메시지", example = "정상")
	String message = null;
	
	public BaseResponseBody() {}
	
	public BaseResponseBody(Integer statusCode){
		this.status = statusCode;
	}
	
	public BaseResponseBody(Integer statusCode, String message){
		this.status = statusCode;
		this.message = message;
	}
	
	public static BaseResponseBody of(Integer statusCode, String message) {
		BaseResponseBody body = new BaseResponseBody();
		body.status = statusCode;
		body.message = message;
		return body;
	}
}
