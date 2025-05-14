package com.setravel.swifttravel;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

@SpringBootTest
class SwiftTravelApplicationTests {

    @Resource
    DataSource source;

    @Test
    void contextLoads() {
        FastAutoGenerator
                .create(new DataSourceConfig.Builder(source))
                .globalConfig(builder -> {
                    builder.author("Swift_Travel_Team");
                    builder.commentDate("2025-04-23");
                    builder.outputDir("src\\main\\java"); // 设置生成的目录
                    builder.enableSpringdoc();
                })
                .packageConfig(builder -> {
                    builder.parent("com.setravel.swifttravel")
                            .entity("entities");
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder()  // 只生成实体类
                            .enableLombok()  // 启用 Lombok 注解
                            .enableTableFieldAnnotation()  // 启用字段注解（@TableField 等）
                            .enableChainModel()  // 启用链式编程风格
                            .enableRemoveIsPrefix()  // 移除字段的 is 前缀
                            .enableColumnConstant()  // 生成常量字段
                            .enableActiveRecord();  // 启用 ActiveRecord 风格
                    builder.mapperBuilder().disable();  // 禁用 Mapper 生成
                    builder.serviceBuilder().disable();  // 禁用 Service 生成
                    builder.controllerBuilder().disable();  // 禁用 Controller 生成
                })
                .execute();
    }

}
