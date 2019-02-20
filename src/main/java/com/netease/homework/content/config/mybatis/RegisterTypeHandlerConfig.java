package com.netease.homework.content.config.mybatis;

import com.netease.snailreader.common.component.enumeration.IntEnum;
import com.netease.snailreader.common.component.enumeration.StringEnum;
import com.netease.snailreader.common.ext.mybatis.typehandler.RegisterTypeHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @Description 用于注册自定义类型转换器，如枚举类型
 * @Auther ctl
 * @Date 2019/1/15
 */
@org.springframework.context.annotation.Configuration
public class RegisterTypeHandlerConfig implements ConfigurationCustomizer {

    /**
     * 用于注册snailreader包提供的IntEnum,StringEnum等
     * 如果使用CustomSqlSessionFactoryBean则无法使用springboot对Mybatis的自动配置
     * 为了仍能使用自动配置，将CustomSqlSessionFactoryBean中的scanHandledTypes逻辑配置在这
     */
    @Override
    public void customize(Configuration configuration) {
        // 使用springboot打成jar包后运行必须使用SpringBootVFS才能扫描到.class文件资源
        VFS.addImplClass(SpringBootVFS.class);
        // 出于简便，下述两个参数暂时不配置到外部配置中
        String handledTypesPackages = "com.netease.homework.content.config.constant";
        List<Class<?>> handledTypesBaseClasses = Arrays.asList(IntEnum.class, StringEnum.class);

        String[] packages = StringUtils.split(handledTypesPackages, ",; ");
        TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();

        for (Class<?> baseClass: handledTypesBaseClasses) {
            RegisterTypeHandler defaultTypeHandler = baseClass.getAnnotation(RegisterTypeHandler.class);
            for (String pk: packages) {
                ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<>();
                resolverUtil.find(new ResolverUtil.IsA(baseClass), pk);
                Set<Class<? extends Class<?>>> typeSet = resolverUtil.getClasses();
                for (Class<?> type: typeSet) {
                    RegisterTypeHandler typeHandler = type.getAnnotation(RegisterTypeHandler.class);
                    if (typeHandler == null)
                        typeHandler = defaultTypeHandler;
                    registry.register(type, typeHandler.value());
                }
            }
        }

    }
}
