package bid.adonis.lau.foreach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 测试forEach方法以及它的lambda
 *
 * @author: Adonis Lau
 * @date: 2019/4/15 10:54
 */
public class ForEach {
    public static void main(String[] args) {
        List<HashMap<Integer, Integer>> myList = new ArrayList<HashMap<Integer, Integer>>();
        for (int i = 0; i < 10; i++) {
            HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
            hashMap.put(i, i);
            myList.add(hashMap);
        }

        System.out.println("<<<<<<<<Java8使用forEach新迭代方式start...>>>>>>>");
        myList.forEach(s -> {
            HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
            hashMap.put(Math.round(88L), Math.round(100L));
            s.putAll(hashMap);
        });

        System.out.println("<<<<<<<<Java8使用forEach新迭代方式end.>>>>>>>");


        System.out.println("使用自定义的消费动作行为处理集合元素:");
        //创建自定义消费行为动作实例
        myList.forEach(integer -> {
            System.out.println("Iterator Value::" + integer);
        });
    }
}
