package bid.adonis.lau.test;

import java.net.MalformedURLException;
import java.util.*;

public class Lamda {
    public static void main(String[] args) throws ClassNotFoundException, MalformedURLException {
        List<Map<String, Object>> list = null;
        list = instance(list);

        System.out.println(list);

        System.out.println(grouping(list, "a"));
    }

    private static Map<String, Object> grouping(List<Map<String, Object>> list, String key) {
        // 使用TreeMap存放元素并排序
        Map<String, Object> newMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });

        list.stream().forEach(m -> {
            if (m.containsKey(key)) {
                List<Object> l = new ArrayList<>();
                l.add(m);
                newMap.put(m.get(key).toString(), l);
            }
        });
        ;
        return newMap;
    }

    private static List<Map<String, Object>> instance(List<Map<String, Object>> list) {
        list = new ArrayList() {
            {
                add(new HashMap() {
                    {
                        put("a", "1");
                        put("", "0");
                    }
                });
                add(new HashMap() {
                    {
                        put("a", "a11");
                        put("b", "2");
                    }
                });
                add(new HashMap() {
                    {
                        put("a", "111");
                        put("c", "3");
                    }
                });
                add(new HashMap() {
                    {
                        put("b", "22");
                    }
                });
                add(new HashMap() {
                    {
                        put("a", "1111");
                        put("b", "222");
                    }
                });
            }
        };
        return list;
    }
}
