package bid.adonis.lau.multithreading;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class HandleFile2 {
    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        long millis1 = System.currentTimeMillis();
        System.out.println(millis1);
        Read3 read = new Read3(millis1);
//        ExecutorService service = Executors.newFixedThreadPool(2);
//        for (int i = 1; i <= 20; i++) {
//            service.execute(new Thread(read, "线程" + i));
//        }

        //设置核心池大小
        int corePoolSize = 5;

        //设置线程池最大能接受多少线程
        int maximumPoolSize = 20;

        //当前线程数大于corePoolSize、小于maximumPoolSize时，超出corePoolSize的线程数的生命周期
        long keepActiveTime = 2;

        //设置时间单位，秒
        TimeUnit timeUnit = TimeUnit.HOURS;

        //设置线程池缓存队列的排队策略为FIFO，并且指定缓存队列大小为5
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(5);

        //创建ThreadPoolExecutor线程池对象，并初始化该对象的各种参数
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepActiveTime, timeUnit,workQueue);

        for (int i = 1; i <= 20; i++) {
            executor.execute(read);
        }

    }

}

class Read3 implements Runnable {
    //    Object o = new Object();
    List<File> filePathsList = new ArrayList<File>();
    int index = 0;
    private long millis;

    public Read3(long millis1) {
        this.millis = millis1;
        File f = new File("E:\\TestFiles\\multithreading\\source");
        getFileList(f);
    }

    private void getFileList(File f) {
        File[] filePaths = f.listFiles();
        for (File s : filePaths) {
            if (s.isDirectory()) {
                getFileList(s);
            } else {
                if (-1 != s.getName().lastIndexOf(".txt")) {
                    filePathsList.add(s);
                }
            }
        }
    }

    @Override
    public void run() {
        File sourceFile = null;
        File outFile = null;
        while (index < filePathsList.size()) {
            //此处，保证了多线程不会交叉读取文件

            synchronized (this) {
                if (index > filePathsList.size()) {
                    return;
                }
                sourceFile = filePathsList.get(index);
                index++;
                //System.out.println("内部index: " + index);
            }
            //    System.out.println("文件: " + file.getName());
            FileReader sourceFileReader = null;
            BufferedReader sourceBufferedReader = null;

            FileWriter outFileWriter = null;
            BufferedWriter outBufferedWriter = null;
            outFile = new File("E:\\TestFiles\\multithreading\\output" + File.separator + sourceFile.getName());

            try {
                sourceFileReader = new FileReader(sourceFile);
                sourceBufferedReader = new BufferedReader(sourceFileReader);

                outFileWriter = new FileWriter(outFile);
                outBufferedWriter = new BufferedWriter(outFileWriter);

                String data = "";
                while ((data = sourceBufferedReader.readLine()) != null) {
                    // sb.append(data + "\r");
                    outBufferedWriter.write(data + "\r");
                    System.out.println(Thread.currentThread().getName());
                }

                outBufferedWriter.write("---------------" + Thread.currentThread().getName() + "---------------");
                System.out.println(Thread.currentThread().getName() + " : " + sourceFile.getName());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    outBufferedWriter.close();
                    outFileWriter.close();
                    sourceBufferedReader.close();
                    sourceFileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

