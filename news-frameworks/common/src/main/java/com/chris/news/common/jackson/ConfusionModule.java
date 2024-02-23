package com.chris.news.common.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfusionModule extends Module {
    public final static String MODULE_NAME = "jackson-confusion-encryption";
    public final static Version VERSION = new Version(1,0,0,null,"heima",MODULE_NAME);

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public Version version() {
        return VERSION;
    }

    @Override
    public void setupModule(SetupContext context) {
        context.addBeanSerializerModifier(new ConfusionSerializerModifier());
        context.addBeanDeserializerModifier(new ConfusionDeserializerModifier());
    }

    /**
     * 注册当前模块
     */
    public static ObjectMapper registerModule(ObjectMapper objectMapper){
        //CamelCase策略，Java对象属性：personId，序列化后属性：personId
        //PascalCase策略，Java对象属性：personId，序列化后属性：personId
        //SnakeCase策略，Java对象属性：personId，序列化后属性：person_id
        //KebabCase策略，Java对象属性：personId，序列化后属性：person-id
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.registerModule(new ConfusionModule());
    }
}
