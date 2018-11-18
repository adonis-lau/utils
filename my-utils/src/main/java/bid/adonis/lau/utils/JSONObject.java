package bid.adonis.lau.utils;

import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author: Adonis Lau
 * @date: 2018/10/25 10:38
 */
public class JSONObject extends com.alibaba.fastjson.JSONObject {

    /**
     * 重写 toJSONString 方法
     * <p>
     * SerializerFeature.WriteMapNullValue          是否输出值为null的字段,默认为false
     * SerializerFeature.WriteNullNumberAsZero      数值字段如果为null,输出为0,而非null
     * SerializerFeature.WriteNullListAsEmpty       List字段如果为null,输出为[],而非null
     * SerializerFeature.WriteNullStringAsEmpty     字符类型字段如果为null,输出为"",而非null
     * SerializerFeature.WriteNullBooleanAsFalse    Boolean字段如果为null,输出为false,而非null
     * <p>
     * 属性 clazz 在 JSON 字符串中转换为 class
     *
     * @param object
     * @return
     */
    public static String toJSONString(Object object) {
        String jsonString = com.alibaba.fastjson.JSONObject.toJSONString(object,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullBooleanAsFalse);
        return jsonString.replace("clazz", "class");
    }

}
