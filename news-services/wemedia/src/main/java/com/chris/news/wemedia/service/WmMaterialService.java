package com.chris.news.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.wemedia.dto.WmMaterialDto;
import com.chris.news.model.wemedia.pojo.WmMaterial;
import org.springframework.web.multipart.MultipartFile;

public interface WmMaterialService extends IService<WmMaterial> {
    /**
     * 图片上传
     */
    ResponseResult uploadPicture(MultipartFile multipartFile);

    /**
     * 素材列表查询
     */
    ResponseResult findList(WmMaterialDto dto);
}
