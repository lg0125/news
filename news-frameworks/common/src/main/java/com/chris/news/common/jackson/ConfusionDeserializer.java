package com.chris.news.common.jackson;

import com.chris.news.utils.common.IdsUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class ConfusionDeserializer extends JsonDeserializer<Object> {
    JsonDeserializer<Object>  deserializer;
    JavaType type;

    public  ConfusionDeserializer(JsonDeserializer<Object> deserializer, JavaType type){
        this.deserializer = deserializer;
        this.type = type;
    }

    @Override
    public  Object deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        try {
            if(type!=null){
                if(type.getTypeName().contains("Long")){
                    return Long.valueOf(p.getValueAsString());
                }
                if(type.getTypeName().contains("Integer")){
                    return Integer.valueOf(p.getValueAsString());
                }
            }
            return IdsUtil.decryptLong(p.getValueAsString());
        }catch (Exception e){
            if(deserializer!=null){
                return deserializer.deserialize(p,ctxt);
            }else {
                return p.getCurrentValue();
            }
        }
    }
}
