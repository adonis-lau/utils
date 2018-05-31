package bid.adonis.lau.multithreading;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;


public class HandleFile {
    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取全部文件
     *
     * @param source
     * @return
     */
    private static void getAllFilePath(File source, List<File> filePathsList) {
        //如果文件不存在
        if (!source.exists()) {
            System.out.println(source.getAbsolutePath() + "文件或目录不存在");
            return;
//            return filePathsList;
        }
        //如果路径不是目录
        if (!source.isDirectory()) {
            filePathsList.add(source);
            return;
//            return filePathsList;
        }
        File[] files = source.listFiles();
        for (File file : Objects.requireNonNull(files)) {
            getAllFilePath(file, filePathsList);
        }

    }

    public static void main(String[] args) {
        File source = new File("E:\\TestFiles\\multithreading\\source\\multithreading.txt");

        final List<File> filePathsList = new ArrayList<File>();
        getAllFilePath(source, filePathsList);

        int fileNum = filePathsList.size();

        CountDownLatch latch = new CountDownLatch(fileNum);
        ExecutorService pool = Executors.newFixedThreadPool(fileNum > 10 ? 10 : fileNum);

        BlockingQueue<Future<Map<String, FileInputStream>>> queue =
                new ArrayBlockingQueue<Future<Map<String, FileInputStream>>>(100);

        System.out.println("-------------文件读、写任务开始时间：" + sdf.format(new Date()));
        for (File temp : filePathsList) {
            Future<Map<String, FileInputStream>> future = pool.submit(new MyCallableProducer(latch, temp));
            queue.add(future);

            pool.execute(new MyCallableConsumer(queue));
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("-------------文件读、写任务结束时间：" + sdf.format(new Date()));
        pool.shutdownNow();
    }


    // 文件读线程
    static class MyCallableProducer implements Callable<Map<String, FileInputStream>> {
        private CountDownLatch latch;
        private File file;
        private FileInputStream fis = null;
        private Map<String, FileInputStream> fileMap = new HashMap<String, FileInputStream>();

        MyCallableProducer(CountDownLatch latch, File file) {
            this.latch = latch;
            this.file = file;
        }

        @Override
        public Map<String, FileInputStream> call() throws Exception {
            System.out.println(Thread.currentThread().getName() + " 线程开始读取文件 ：" + file.getName() + " ,时间为 " + sdf.format(new Date()));
            fis = new FileInputStream(file);
            fileMap.put(file.getName(), fis);
            doWork();
            System.out.println(Thread.currentThread().getName() + " 线程读取文件 ：" + file.getName() + " 完毕" + " ,时间为 " + sdf.format(new Date()));
            latch.countDown();
            return fileMap;
        }

        private void doWork() {
            //此方法可以添加一些业务逻辑，如何包装一些pojo等，返回值可以是任何类型
            System.out.println(Thread.currentThread().getName() + " 线程开始对文件 ：" + file.getName() + " 进行解密");
        }
    }


    // 文件写线程
    static class MyCallableConsumer implements Runnable {
        private String fileName = "";
        private BlockingQueue<Future<Map<String, FileInputStream>>> queue;
        private FileInputStream fis = null;
        private File dirFile = null;

        private BufferedReader br = null;
        private InputStreamReader isr = null;
        private FileWriter fw = null;
        private BufferedWriter bw = null;

        MyCallableConsumer(BlockingQueue<Future<Map<String, FileInputStream>>> queue2) {
            this.queue = queue2;
        }

        @Override
        public void run() {
            try {
                Future<Map<String, FileInputStream>> future = queue.take();
                Map<String, FileInputStream> map = future.get();

                Set<String> set = map.keySet();
                for (String aSet : set) {
                    fileName = aSet.toString();
                    fis = map.get(fileName);

                    System.out.println(Thread.currentThread().getName() + " 线程开始写文件 ：" + fileName + " ,时间为 " + sdf.format(new Date()));
                    try {
                        isr = new InputStreamReader(fis, "utf-8");
                        br = new BufferedReader(isr);

                        dirFile = new File("E:\\TestFiles\\multithreading\\output" + File.separator + fileName);
                        fw = new FileWriter(dirFile);
                        bw = new BufferedWriter(fw);

                        String data = "";
                        bw.write("+++++++++++++" + Thread.currentThread().getName() + " 线程开始写文件++++++++++++");
                        while ((data = br.readLine()) != null) {
                            bw.write(data + "\r");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            bw.close();
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

}
