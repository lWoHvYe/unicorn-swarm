package com.lwohvye.springcloud.springcloudlwohvyeprovider.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2的接口配置
 * Swagger注解：
 *
 * @Api：修饰整个类，描述Controller的作用
 * @ApiOperation：描述一个类的一个方法，或者说一个接口
 * @ApiParam：单个参数描述
 * @ApiModel：用对象来接收参数
 * @ApiProperty：用对象接收参数时，描述对象的一个字段
 * @ApiResponse：HTTP响应其中1个描述
 */
@Configuration
//开启SwaggerAPI
@EnableSwagger2
//开启Knife4j增强功能
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    @Value("${swagger.enable}")
    private boolean swaggerEnable;

    /**
     * 创建API
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerEnable)
                // 详细定制
                .apiInfo(apiInfo())
                .select()
                // 指定当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.lwohvye.springcloud.springcloudlwohvyeprovider"))
                // 扫描所有 .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 添加摘要信息
     */
    private ApiInfo apiInfo() {
        // 用ApiInfoBuilder进行定制
        return new ApiInfoBuilder()
                .title("标题：Springboot相关系统API文档")
                .description("描述：用于进行系统相关API测试")
                .contact(new Contact("Hongyan Wang", "https://github.com/lWoHvYe/springboot-lwohvye", "lWoHvYe@outlook.com"))
                .version("0.1.2")
                .build();
    }
}