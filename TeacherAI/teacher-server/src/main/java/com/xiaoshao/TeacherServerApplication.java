package com.xiaoshao;

import com.github.jeffreyning.mybatisplus.conf.EnableKeyGen;
import com.github.jeffreyning.mybatisplus.conf.EnableMPP;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xiaoshao.mapper")
//启动复合主键支持
@EnableMPP
@EnableKeyGen
public class TeacherServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeacherServerApplication.class, args);
    }

}
