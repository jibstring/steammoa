package com.ssafy.backend.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 파티 생성 API ([POST] /api/moazone/) 요청에 필요한 리퀘스트 바디 정의.
 */
@Data
@ApiModel("PartyPostRequest")
public class PartyPostReq {
	@ApiModelProperty(name="게임 ID", example = "gameId")
	Long gameId;

	@ApiModelProperty(name="파티 이름", example="partyTitle")
	String partyTitle;

	@ApiModelProperty(name="파티 최대 인원", example="maxPlayer")
	String maxPlayer;

	@ApiModelProperty(name="파티 설명", example="partyDescription")
	String partyDescription;

	@ApiModelProperty(name="게임 플레이 약속 시간", example="startTime")
	String startTime;

	@ApiModelProperty(name="외부 소통 링크(디스코드 등)", example="chatLink")
	String chatLink;

	@ApiModelProperty(name="파티 성향 태그", example="partyTags")
	String[] partyTags;

	@ApiModelProperty(name="파티장 유저 ID", example="userId")
	String userId;


}
