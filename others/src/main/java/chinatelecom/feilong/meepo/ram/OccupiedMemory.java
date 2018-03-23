package chinatelecom.feilong.meepo.ram;

/**
 * @author Adonis Lau
 * @eamil adonis.lau.dev@gmail.com
 * @date Created in 2017/11/26 15:24
 */
public class OccupiedMemory {

    public static final Integer G = 1024 * 1024 * 1024;

    public static void main(String[] args) throws Exception {
        Integer num = Integer.valueOf(args[0]);
        Integer minute = Integer.valueOf(args[1]);

        System.out.println("将占用" + num + "G的内存");

        for (int i = 0;i < num; i++){
            byte[] buf = new byte[G];
        }

        Thread.sleep(1000 * 60 *minute);
    }
}
