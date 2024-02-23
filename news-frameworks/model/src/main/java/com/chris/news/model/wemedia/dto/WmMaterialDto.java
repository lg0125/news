package com.chris.news.model.wemedia.dto;

import com.chris.news.model.common.dto.PageRequestDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WmMaterialDto extends PageRequestDto {
    /**
     * 1 收藏
     * 0 未收藏
     */
    private Short isCollection;
}
