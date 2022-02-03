package com.broadwave.toppos.common;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-23
 * Remark :
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {
    private static final String CLASSPATH_RESOURCE_LOCATIONS ="classpath:/static/";

    @Value("${toppos.aws.s3.access.id}")
    private String AWSS3ACCESSID;

    @Value("${toppos.aws.s3.access.key}")
    private String AWSS3ACCESSKEY;

    @Value("${toppos.aws.region}")
    private String AWSREGION;

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS).setCachePeriod(31536000);
    }

    @Bean
    public BasicAWSCredentials AwsCredentianls(){
        return new BasicAWSCredentials(AWSS3ACCESSID,AWSS3ACCESSKEY);
    }

    @Bean
    public AmazonS3 AwsS3Client(){
        return AmazonS3ClientBuilder.standard()
                .withRegion(AWSREGION)
                .withCredentials(new AWSStaticCredentialsProvider(this.AwsCredentianls()))
                .build();
    }
    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
        super.addArgumentResolvers(argumentResolvers);
    }

}
