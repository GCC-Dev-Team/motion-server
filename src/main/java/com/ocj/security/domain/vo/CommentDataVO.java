package com.ocj.security.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDataVO implements Serializable {

    private Integer commentCount;

    private List<CommentVO> list;

}
