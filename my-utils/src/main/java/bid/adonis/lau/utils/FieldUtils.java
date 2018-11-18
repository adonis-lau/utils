package bid.adonis.lau.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

/**
 * 实体类字段工具
 *
 * @author: Adonis Lau
 * @date: 2018/10/26 10:50
 */
public class FieldUtils extends org.apache.commons.lang3.reflect.FieldUtils {

    private static String LIST = "java.util.List";
    private static String LIST_FILE = "java.util.List<java.io.File>";
    private static String STRING = "java.lang.String";

    /**
     * 字段属性是否是 java.util.List<java.io.File>
     *
     * @param field
     * @return
     */
    public static boolean whetherListFile(Field field) {
        field.setAccessible(true);
        if (LIST.equals(field.getType().getName())) {
            // 获取参数化类型（泛型）
            ParameterizedType listGenericType = (ParameterizedType) field.getGenericType();
            // 获取泛型名称，去判断是否为LIST_FILE
            return LIST_FILE.equals(listGenericType.getTypeName());
        }
        return false;
    }


    public static boolean whetherString(Field field) {
        field.setAccessible(true);
        // 获取字段类型，去判断是否为 STRING
        return STRING.equals(field.getType().getName());
    }
}
