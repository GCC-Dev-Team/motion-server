package com.ocj.security.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -6928715001184572156L;

    /**
     * 当前页（默认第1页）
     */

    private Integer currentPage;

    /**
     * 每页记录数（默认10条）
     */

    private Integer pageSize;
}
