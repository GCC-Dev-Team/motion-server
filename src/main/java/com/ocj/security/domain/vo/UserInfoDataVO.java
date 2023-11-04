package com.ocj.security.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDataVO {

    private String userName;

    private String email;

    private String avatar;

//    private String gender;

}
