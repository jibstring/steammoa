package com.ssafy.backend.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 유저 모델 정의.
 */
@Entity
@Getter
@Setter
public class User {
    // 유저 식별자 PK
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_steam_id", nullable = false)
    private String steamId;

    @Column(name = "user_service_id", nullable = false)
    private String userId;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // 왜 쓰는지 -> https://eglowc.tistory.com/28
    @Column(name = "user_service_pw")
    private String password;

    @Column(name = "user_name", nullable = false)
    private String name;

    // Enum 타입으로 바꿀지 고민중임.
    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;

    @Column(name = "user_point")
    private Double userPoint;


    @Column(name = "user_img_path")
    private String imgPath;

    @Column(name = "user_img_nm")
    private String imgName;

    @OneToMany(mappedBy = "noticeList")
    private List<NoticeList> nLists = new ArrayList<>();

    @OneToMany(mappedBy = "userTag")
    private List<UserTag> uTagLists = new ArrayList<>();

}
