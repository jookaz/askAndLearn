package com.xiaoshao.config;

import com.xiaoshao.interceptor.JwtTokenAdminInterceptor;
import com.xiaoshao.interceptor.JwtTokenStudentInterceptor;
import com.xiaoshao.interceptor.JwtTokenTeacherInterceptor;
import com.xiaoshao.properties.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private JwtTokenStudentInterceptor jwtTokenStudentInterceptor;
    @Autowired
    private JwtTokenTeacherInterceptor jwtTokenTeacherInterceptor;
    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;
    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenStudentInterceptor)
                .addPathPatterns("/student/**")
                .excludePathPatterns("/student/student/login")
                .excludePathPatterns("/student/student/register");
        registry.addInterceptor(jwtTokenTeacherInterceptor)
                .addPathPatterns("/teacher/**")
                .excludePathPatterns("/teacher/teacher/login")
                .excludePathPatterns("/teacher/teacher/register")
                .excludePathPatterns("/teacher/common/**");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/admin/login");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Knife4j资源映射
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
//    配置消息转换器

    /**
     * 1. 替换默认 Jackson 的作用
     * 您的 JacksonObjectMapper 通过继承 ObjectMapper 并覆盖配置，实现了以下核心功能：
     * 日期格式自定义： 将 LocalDateTime、LocalDate、LocalTime 的序列化/反序列化格式统一为 yyyy-MM-dd HH:mm（日期时间）、yyyy-MM-dd（日期）、HH:mm:ss（时间），避免默认的时间戳或 ISO 格式
     * 忽略未知属性： 配置 FAIL_ON_UNKNOWN_PROPERTIES=false，使 JSON 反序列化时遇到未定义的字段不报错，提升接口兼容性
     * 模块化扩展： 通过注册 SimpleModule 添加对 Java 8 时间类型的支持（需依赖 jackson-datatype-jsr310）
     * 2. 不替换默认 Jackson 的影响
     * 若使用默认配置，会有以下行为：
     * 日期格式为时间戳或 ISO 格式： 例如 LocalDateTime 默认序列化为 "2025-04-23T10:15:54.042"，而非您定义的 "2025-04-23 10:15"
     * 严格校验未知属性： 若请求的 JSON 包含未定义的字段，默认会抛出 UnrecognizedPropertyException，导致反序列化失败
     * 缺少 Java 8 时间支持： 未显式注册 JavaTimeModule 时，默认无法正确处理 LocalDateTime 等类型
     * @param converters
     */
//    TODO 这里的消息转换器无法正常使用，后续需要解决
//    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        log.info("开始进行消息转换器配置...");
//        //创建消息转换器对象
//        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
//        //设置对象转换器，底层使用Jackson将Java对象转为json
//        messageConverter.setObjectMapper(new JacksonObjectMapper());
//        //将上面的消息转换器对象追加到mvc框架的转换器集合中
//        converters.add(0, messageConverter);
//    }
}