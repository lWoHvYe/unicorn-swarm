package com.lwohvye.springcloud.springcloudlwohvyeapi.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 实体类
 *
 * @author author
 * @since 2019-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MpCustom对象", description="")
public class MpCustomEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @Id
    @GeneratedValue
    private Integer customId;

    @ApiModelProperty(value = "企业名")
    @NotBlank(message = "企业名不可为空")
    private String customName;

    @ApiModelProperty(value = "全名")
    private String fullName;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "所有人")
    private String ownerName;

    @ApiModelProperty(value = "所有人电话")
    private String ownerTel;

    @ApiModelProperty(value = "企业状态")
    private Integer status;

    @ApiModelProperty(value = "简介")
    private String note;


}
