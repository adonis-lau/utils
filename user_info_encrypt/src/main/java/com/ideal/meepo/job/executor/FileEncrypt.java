package com.ideal.meepo.job.executor;

import com.ideal.encryptor.EncodeManager;
import com.ideal.meepo.commponent.HadoopVFS;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * @author: Adonis Lau
 * @email: adonis.lau.dev@gmail.com
 * @date: 2018/5/30 10:26
 */
public class FileEncrypt {

    /**
     * HDFS帮助类
     */
    private static HadoopVFS hadoopVFS = new HadoopVFS();
    private static FileSystem fs = hadoopVFS.getFs();

    public static void main(String[] args) throws Exception {
        boolean success = true;
        if (args.length < 2) {
            throw new Exception("作业参数配置错误，请检查传入的参数");
        }
        //源文件路径
        String sourcePath = args[0];
        //新生成文件路径
        String targetPath = args[1];

        List<String> children = new ArrayList<>();
        if (!hadoopVFS.isDirectory(sourcePath)) {
            //如果不是目录
            if (fs.isFile(new Path(sourcePath))) {
                //如果是文件
                children.add(sourcePath);
            }
        } else {
            children = hadoopVFS.getChildrenPath(sourcePath, new String[]{"_SUCCESS", "_FAILURE"}, new String[]{"_logs"});
        }

        System.out.println("一共需要处理 " + children.size() + " 个文件");

        for (int i = 0; i < children.size(); i++) {
            String inPath = children.get(i);
            System.out.println("正在处理第 " + (i + 1) + " 个文件,文件名: " + inPath);
            try (InputStream hdfsInputStream = hadoopVFS.getInputStream(inPath)) {
                if (hdfsInputStream == null) {
                    System.out.println("获取文件: " + inPath + " 输入流为空，检索下一个文件");
                    success = false;
                    continue;
                }
                //对获取的文件流进行处理，
                if (!doEncrypt(hdfsInputStream, targetPath)) {
                    System.out.println("文件 " + inPath + " ,处理失败");
                    success = false;
                }
            } catch (Exception e) {
                System.out.println("第 " + (i + 1) + " 个文件,文件名: " + inPath + " 处理失败");
                System.out.println(e);
            }
        }
        if (!success) {
            throw new Exception("文件加密失败");
        }
    }

    private static boolean doEncrypt(InputStream ins, String targetPath) {
        boolean success = true;
        //输出流写入到远程文件目录
        FSDataOutputStream fsDataOutputStream = null;
        StringBuilder builder = new StringBuilder();
        try {
            if (!fs.exists(new Path(targetPath))) {
                fsDataOutputStream = fs.create(new Path(targetPath));
                fs.setPermission(new Path(targetPath), new FsPermission("755"));
            } else {
                fsDataOutputStream = fs.append(new Path(targetPath));
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins));
            String line;
            long acount = 0;
            while ((line = bufferedReader.readLine()) != null) {
                acount++;
                String encryptValue = getEncryptValue(line);
                if (StringUtils.isNotBlank(encryptValue)) {
                    builder.append(encryptValue).append("\n");
                }
                if (acount % 100 == 0) {
                    //数据分批写入
                    fsDataOutputStream.writeUTF(builder.toString());
                    builder.setLength(0);
                    fsDataOutputStream.flush();
                }
            }
            fsDataOutputStream.writeUTF(builder.toString());
            builder.setLength(0);
            fsDataOutputStream.flush();
        } catch (Exception e) {
            success = false;
            System.out.println(FileEncrypt.class.getName() + " line 114 :" + e);
        } finally {
            try {
                if (fsDataOutputStream != null) {
                    fsDataOutputStream.close();
                }
            } catch (IOException e) {
                System.out.println(FileEncrypt.class.getName() + " line 121 :" + e);
            }
        }

        return success;
    }

    private static String getEncryptValue(String line) {
        StringBuilder builder = new StringBuilder();
        String[] array = line.split("\u0005", -1);

        //对第四列数据进行校验
        String ad = array[3].trim();
        if (StringUtils.isNotBlank(ad)) {
            ad = ad.replace("\"", "");
            if (ad.length() == 8) {
                ad = "ad" + ad;
            }
        }

        //加密第四列数据
        array[3] = EncodeManager.EncodeMessageUTF8(ad, "Lvo9vdyX83z9mQL9", "wb_ad", "Xng5iULk");

        for (int i = 0; i < array.length - 1; i++) {
            builder.append(array[i]);
            String separatorTmp = StringEscapeUtils.unescapeJava("\u0005");
            builder.append(separatorTmp);
        }
        builder.append(array[array.length - 1]);

        return builder.toString();
    }
}