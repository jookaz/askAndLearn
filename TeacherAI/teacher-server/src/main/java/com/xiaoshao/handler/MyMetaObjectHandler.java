package com.xiaoshao.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xiaoshao.context.BaseContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
//mp的字段填充类
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("createUser", getCurrentUserId(), metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateUser", getCurrentUserId(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateUser", getCurrentUserId(), metaObject);
    }
//    TODO 获取当前用户ID，这个功能需要在登录功能完成后实现
    private Long getCurrentUserId() {
        // 从 ThreadLocal 或其他方式获取当前用户ID

        return BaseContext.getCurrentId();

    }

}