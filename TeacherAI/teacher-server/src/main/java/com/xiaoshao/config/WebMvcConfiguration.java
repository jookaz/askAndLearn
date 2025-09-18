//package com.xiaoshao.config;
//
//import com.xiaoshao.interceptor.JwtTokenAdminInterceptor;
//import com.xiaoshao.interceptor.JwtTokenUserInterceptor;
//import com.xiaoshao.json.JacksonObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//
//import java.util.List;
//
///**
// * 配置类，注册web层相关组件
// */
//@Configuration
//@Slf4j
////WebMvcConfigurationSupport
//public class WebMvcConfiguration extends WebMvcConfigurationSupport {
//
//    @Autowired
//    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;
//    @Autowired
//    private JwtTokenUserInterceptor jwtTokenUserInterceptor;
//    /**
//     * 注册自定义拦截器
//     *
//     * @param registry
//     */
////    protected void addInterceptors(InterceptorRegistry registry) {
////        log.info("开始注册自定义拦截器...");
////        registry.addInterceptor(jwtTokenAdminInterceptor)
////                .addPathPatterns("/admin/**")
////                .excludePathPatterns("/admin/employee/login");
////        registry.addInterceptor(jwtTokenUserInterceptor)
////                .addPathPatterns("/user/**")
////                .excludePathPatterns("/user/user/login")
////                .excludePathPatterns("/user/shop/status");
////    }
//
//    /**
//     * 设置消息转换器
//     *
//     */
//    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        log.info("开始进行消息转换器配置...");
//        //创建消息转换器对象
//        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
//        //设置对象转换器，底层使用Jackson将Java对象转为json
//        messageConverter.setObjectMapper(new JacksonObjectMapper());
//        //将上面的消息转换器对象追加到mvc框架的转换器集合中
//        converters.add(0, messageConverter);
//    }
//}
//
