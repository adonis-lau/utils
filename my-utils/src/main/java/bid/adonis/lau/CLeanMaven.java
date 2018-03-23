package bid.adonis.lau;


import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Adonis Lau
 * @eamil adonis.lau.dev@gmail.com
 * @date Created in 2018/3/16 19:26
 */
public class CLeanMaven {

    public static void main(String[] args) {
        CLeanMaven cm = new CLeanMaven();
        cm.clear();
    }

    public void clear() {
        File file = new File("C:\\Users\\Adonis\\.m2\\repository");
        if (file.exists()) {
            clearFile(file);
        }
    }

    public void clearFile(File file) {
        File[] files = file.listFiles();
        boolean flag = false;
        for (int i = 0; i < files.length; i++) {
            File temp = files[i];
            if (temp.isDirectory()) {
                clearFile(temp);
            } else {
                if (temp.getName().endsWith("jar")) {
                    flag = true;
                }
            }
        }
        Pattern pt = Pattern.compile("^[/d.]*$");
        Matcher matcher = pt.matcher(file.getName());
        if (!flag && matcher.find()){
            boolean rs = deleteDir(file);
            if (rs) {
                System.out.println("删除:" + file.getAbsolutePath());
            }
        }
    }

    public boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
