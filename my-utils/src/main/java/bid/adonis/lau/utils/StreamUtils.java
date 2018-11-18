package bid.adonis.lau.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 内存流操作
 *
 * @author: Adonis Lau
 * @date: 2018/10/25 17:07
 */
public class StreamUtils {

    final static int BUFFER_SIZE = 4096;

    /**
     * 将InputStream转换成String
     *
     * @param in InputStream
     * @return String
     * @throws Exception
     */
    public static String InputStreamTOString(InputStream in) {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        String string = null;
        int count = 0;
        try {
            while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
                outStream.write(data, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        data = null;
        try {
            string = new String(outStream.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string;
    }

    /**
     * 将InputStream转换成某种字符编码的String
     *
     * @param in
     * @param encoding
     * @return
     * @throws Exception
     */
    public static String InputStreamTOString(InputStream in, String encoding) {
        String string = null;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        try {
            while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
                outStream.write(data, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        data = null;
        try {
            string = new String(outStream.toByteArray(), encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string;
    }

    /**
     * 将String转换成InputStream
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static InputStream StringTOInputStream(String in) throws Exception {

        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("UTF-8"));
        return is;
    }

    /**
     * 将String转换成InputStream
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static byte[] StringTObyte(String in) {
        byte[] bytes = null;
        try {
            bytes = InputStreamTOByte(StringTOInputStream(in));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 将InputStream转换成byte数组
     *
     * @param in InputStream
     * @return byte[]
     * @throws IOException
     */
    public static byte[] InputStreamTOByte(InputStream in) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
            outStream.write(data, 0, count);
        }

        data = null;
        return outStream.toByteArray();
    }

    /**
     * 将byte数组转换成InputStream
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static InputStream byteTOInputStream(byte[] in) throws Exception {

        ByteArrayInputStream is = new ByteArrayInputStream(in);
        return is;
    }

    /**
     * 将byte数组转换成String
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static String byteTOString(byte[] in) {

        String result = null;
        InputStream is = null;
        try {
            is = byteTOInputStream(in);
            result = InputStreamTOString(is, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据文件路径创建文件输入流处理
     * 以字节为单位（非 unicode ）
     *
     * @param filepath
     * @return
     */
    public static FileInputStream getFileInputStream(String filepath) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filepath);
        } catch (FileNotFoundException e) {
            System.out.print("错误信息:文件不存在");
            e.printStackTrace();
        }
        return fileInputStream;
    }

    /**
     * 根据文件对象创建文件输入流处理
     * 以字节为单位（非 unicode ）
     *
     * @param file
     * @return
     */
    public static FileInputStream getFileInputStream(File file) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.print("错误信息:文件不存在");
            e.printStackTrace();
        }
        return fileInputStream;
    }

    /**
     * 根据文件对象创建文件输出流处理
     * 以字节为单位（非 unicode ）
     *
     * @param file
     * @param append true:文件以追加方式打开,false:则覆盖原文件的内容
     * @return
     */
    public static FileOutputStream getFileOutputStream(File file, boolean append) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file, append);
        } catch (FileNotFoundException e) {
            System.out.print("错误信息:文件不存在");
            e.printStackTrace();
        }
        return fileOutputStream;
    }

    /**
     * 根据文件路径创建文件输出流处理
     * 以字节为单位（非 unicode ）
     *
     * @param filepath
     * @param append   true:文件以追加方式打开,false:则覆盖原文件的内容
     * @return
     */
    public static FileOutputStream getFileOutputStream(String filepath, boolean append) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filepath, append);
        } catch (FileNotFoundException e) {
            System.out.print("错误信息:文件不存在");
            e.printStackTrace();
        }
        return fileOutputStream;
    }

    /**
     * 根据文件对象创建文件字节数组
     *
     * @param file
     * @return
     */
    public static byte[] getFileByte(File file) {
        byte[] bytes = null;
        try {
            bytes = InputStreamTOByte(getFileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 根据文件路径创建文件字节数组
     *
     * @param filepath
     * @return
     */
    public static byte[] getFileByte(String filepath) {
        byte[] bytes = null;
        try {
            bytes = InputStreamTOByte(getFileInputStream(filepath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * inputStream转outputStream
     *
     * @param in
     * @return
     */
    public static ByteArrayOutputStream parse(InputStream in) {
        ByteArrayOutputStream swapStream = null;
        try {
            swapStream = new ByteArrayOutputStream();
            int ch;
            while ((ch = in.read()) != -1) {
                swapStream.write(ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return swapStream;
    }

    /**
     * outputStream转inputStream
     *
     * @param out
     * @return
     */
    public static ByteArrayInputStream parse(OutputStream out) {
        ByteArrayOutputStream baos = (ByteArrayOutputStream) out;
        return new ByteArrayInputStream(baos.toByteArray());
    }

    public static ByteArrayOutputStream getByteArrayOutputStream() {
        return new ByteArrayOutputStream();
    }

    /**
     * 将文件map压缩为ByteArrayOutputStream
     *
     * @param map <filename, fileByte>
     * @return
     */
    public static ByteArrayOutputStream compressToOutput(Map<String, byte[]> map) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOut = null;
        try {
            zipOut = new ZipOutputStream(byteArrayOutputStream);
            Set<Map.Entry<String, byte[]>> entries = map.entrySet();
            for (Map.Entry<String, byte[]> entry : entries) {
                zipOut.putNextEntry(new ZipEntry(entry.getKey()));
                zipOut.write(entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (zipOut != null) {
                    zipOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return byteArrayOutputStream;
    }

    /**
     * 将文件map压缩为ByteArrayInputStream
     *
     * @param map <filename, fileByte>
     * @return
     */
    public static ByteArrayInputStream compressToInput(Map<String, byte[]> map) {
        return parse(compressToOutput(map));
    }

    /**
     * 解压zip文件为内存流
     *
     * @param inputStream
     * @return
     */
    public static Map<String, byte[]> uncompress(InputStream inputStream) {
        ZipInputStream zis = new ZipInputStream(inputStream);
        Map<String, byte[]> map = null;
        try {
            map = new HashMap<>();
            ZipEntry ze = null;
            while (((ze = zis.getNextEntry()) != null) && !ze.isDirectory()) {
                String name = ze.getName();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[10240];
                int length = -1;
                while ((length = zis.read(buffer, 0, buffer.length)) > -1) {
                    byteArrayOutputStream.write(buffer, 0, length);
                }
                map.put(name, byteArrayOutputStream.toByteArray());
                byteArrayOutputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                zis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
