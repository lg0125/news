package com.chris.news.common.aliyun.utils;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UploadCredentials implements Serializable {

    private String accessKeyId;
    private String  accessKeySecret;
    private String  securityToken;
    private Long  expiredTime;
    private String  ossEndpoint;
    private String  ossInternalEndpoint;
    private String  uploadBucket;
    private String  uploadFolder;

    public UploadCredentials(String accessKeyId, String accessKeySecret, String securityToken, Long expiredTime, String ossEndpoint, String ossInternalEndpoint, String uploadBucket, String uploadFolder) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.securityToken = securityToken;
        this.expiredTime = expiredTime;
        this.ossEndpoint = ossEndpoint;
        this.ossInternalEndpoint = ossInternalEndpoint;
        this.uploadBucket = uploadBucket;
        this.uploadFolder = uploadFolder;
    }
}
