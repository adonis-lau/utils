package com.ideal.meepo.commponent;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.viewfs.ViewFileSystem;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: Adonis Lau
 * @email: adonis.lau.dev@gmail.com
 * @date: 2018/5/30 12:52
 */
public class HadoopVFS {
    private Logger logger = LoggerFactory.getLogger(HadoopVFS.class);
    private static final String CORE_SITE_LOCATION = "file:///etc/hadoop/conf/core-site.xml";
    private static final String HDFS_SITE_LOCATION = "file:///etc/hadoop/conf/hdfs-site.xml";
    public static final String ENCODING = "UTF-8";
    private Configuration conf;
    private FileSystem fs;

    public HadoopVFS() {
        try {
            conf = new Configuration();
            conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
            conf.set("fs.file.impl", LocalFileSystem.class.getName());

            conf.addResource(new Path(CORE_SITE_LOCATION));
            conf.addResource(new Path(HDFS_SITE_LOCATION));
            UserGroupInformation.setConfiguration(conf);
            String defaultFS = conf.get("fs.defaultFS");
            //兼容跨集群
            if (defaultFS.startsWith("viewfs://")) {
                fs = ViewFileSystem.get(conf);
            } else {
                fs = FileSystem.get(conf);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 根据hdfs文件路径和encoding来获得相应的Reader
     *
     * @param filePath
     * @param encoding
     * @return
     */
    public InputStreamReader getReader(String filePath, String encoding) {
        InputStreamReader reader = null;
        try {
            InputStream is = getInputStream(filePath);
            if (StringUtils.isBlank(encoding)) {
                reader = new InputStreamReader(is);
            } else {
                reader = new InputStreamReader(is, encoding);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
        return reader;
    }

    /**
     * 根据HDFS的文件路径来获取
     *
     * @param filePath
     * @return
     */
    public InputStream getInputStream(String filePath) {
        InputStream is = null;
        try {
            Path file = new Path(filePath);
            CompressionCodec codec = checkCompressionCodec(conf, file);
            FSDataInputStream fsDataInputStream = fs.open(file);
            if (codec == null) {
                is = fsDataInputStream;
            } else {
                is = codec.createInputStream(fsDataInputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return is;
    }

    /**
     * 根据文件路径-->判断文件格式,返回相应的Codec-->以便选择相应的
     *
     * @param conf
     * @param filePath
     * @return
     */
    private CompressionCodec checkCompressionCodec(Configuration conf, Path filePath) {
        CompressionCodecFactory factory = new CompressionCodecFactory(conf);
        //自动根据文件后缀名判断
        CompressionCodec codec = factory.getCodec(filePath);
        return codec;
    }

    /**
     * 根据hdfs路径获得路径的状态
     *
     * @param hdfsPath
     * @return
     * @throws IOException
     */
    public int getFileStatus(String hdfsPath) throws IOException {
        FileStatus[] fileStatus = fs.globStatus(new Path(hdfsPath));
        if (fileStatus == null || fileStatus.length == 0) {
            throw new FileNotFoundException("Cannot access " + hdfsPath + ": No such file or directory.");
        }
        int num = 0;
        for (int i = 0; i < fileStatus.length; i++) {
            num += getFileStatus(fileStatus[i]);
        }
        return num;
    }

    private int getFileStatus(FileStatus fileStatus) throws IOException {
        FileStatus[] fileStatuses = fs.listStatus(fileStatus.getPath());
        if (fileStatuses.length == 1 && !fileStatuses[0].isDirectory()) {
            return 1;
        }
        int num = 0;
        for (int i = 0; i < fileStatuses.length; i++) {
            num += getFileStatus(fileStatuses[i]);
        }
        return num;
    }

    /**
     * 根据hdfsPath和Path过滤器来获得子路径的状态
     *
     * @param hdfsPath
     * @param pathFilter
     * @return
     * @throws IOException
     */
    public List<FileStatus> getChildren(String hdfsPath, PathFilter pathFilter) throws IOException {
        FileStatus[] fileStatuses = fs.globStatus(new Path(hdfsPath), pathFilter);
        List<FileStatus> retList = new ArrayList<FileStatus>();
        for (int i = 0; i < fileStatuses.length; i++) {
            List<FileStatus> list = getChildren(fileStatuses[i], pathFilter);
            retList.addAll(list);
        }
        return retList;
    }

    private List<FileStatus> getChildren(FileStatus fileStatus, PathFilter pathFilter) throws IOException {
        FileStatus[] fileStatuses = fs.listStatus(fileStatus.getPath(), pathFilter);
        if (fileStatuses.length == 1 && !fileStatuses[0].isDirectory()) {
            return Arrays.asList(fileStatuses[0]);
        }
        List<FileStatus> retList = new ArrayList<FileStatus>();
        for (int i = 0; i < fileStatuses.length; i++) {
            List<FileStatus> list = getChildren(fileStatuses[i], pathFilter);
            retList.addAll(list);
        }
        return retList;
    }

    /**
     * 判断hdfs路径是否是目录
     *
     * @param hdfsPath
     * @return
     * @throws IOException
     */
    public boolean isDirectory(String hdfsPath) throws IOException {
        int num = getFileStatus(hdfsPath);
        return num == 1 ? false : true;
    }

    /**
     * 根据hdfsPath和过滤的关键字来获得子路径
     *
     * @param hdfsPath
     * @param filterFileNames
     * @param filterFilePathNames
     * @return
     * @throws IOException
     */
    public List<String> getChildrenPath(String hdfsPath, final String[] filterFileNames, final String[] filterFilePathNames) throws IOException {
        PathFilter pathFilter = new PathFilter() {
            @Override
            public boolean accept(Path path) {
                for (String filterFileName : filterFileNames) {
                    if (path.getName().equals(filterFileName)) {
                        return false;
                    }
                }
                for (String filterFilePathName : filterFilePathNames) {
                    if (path.getParent().getName().equals(filterFilePathName)) {
                        return false;
                    }
                }
                return true;
            }
        };
        List<FileStatus> list = getChildren(hdfsPath, pathFilter);
        List<String> children = new ArrayList<String>();
        for (FileStatus fileStatus : list) {
            if (fileStatus.isDirectory()) {
                continue;
            }
            String path = fileStatus.getPath().toUri().toString();
            children.add(path);
        }
        return children;
    }

    public void closeResource() throws Exception {
        if (fs != null) {
            fs.close();
        }
    }

    public List<String> getChildren(String hdfsPath, String fileType) throws IOException {
        FileStatus[] fileStatuses = fs.globStatus(new Path(hdfsPath + "/*." + fileType));
        List<String> files = new ArrayList<String>();
        for (FileStatus fileStatus : fileStatuses) {
            String path = fileStatus.getPath().toUri().toString();
            files.add(path);
        }
        return files;
    }

    public boolean checkIsExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    public void copyToLocalFile(String src, String dst) throws IOException {
        fs.copyToLocalFile(false, new Path(src), new Path(dst));
    }

    public boolean rename(String src, String dest) {
        File file = new File(src);
        return file.renameTo(new File(dest));
    }

    public void delete(String srcPath) throws IOException {
        fs.delete(new Path(srcPath), false);
    }

    public FileSystem getFs() {
        return fs;
    }


    public FSDataInputStream getInputStreams() {

        FSDataInputStream fsDataInputStream = null;
        try {
            fsDataInputStream = fs.open(new Path("private/kv/test.txt"));
            IOUtils.copyBytes(fsDataInputStream, System.out, 4086, false);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fsDataInputStream;
    }

    /**
     * 根据传入的文件地址，获取文件的大小，单位bites
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public long getFileSize(String filePath) throws IOException {
        long number = fs.getLength(new Path(filePath));
        return number;
    }


}
