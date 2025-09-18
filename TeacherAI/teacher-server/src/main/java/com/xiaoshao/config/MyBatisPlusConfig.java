package com.xiaoshao.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisPlusConfig {
    /**
     *
     1.自动分页处理
     通过注册 PaginationInnerInterceptor，MyBatis-Plus 在执行分页查询时（如使用 Page<T> 对象），
     会自动在SQL末尾追加 MySQL分页语法（如 LIMIT offset, size），无需手动拼接分页参数。
     2.兼容数据库类型
     指定 DbType.MYSQL 表示当前项目使用 MySQL数据库，分页插件会根据数据库类型生成正确的分页SQL
     （例如Oracle会生成ROWNUM语法，PostgreSQL生成LIMIT OFFSET）。
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}