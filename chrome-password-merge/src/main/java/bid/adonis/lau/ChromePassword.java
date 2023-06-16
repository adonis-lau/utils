package bid.adonis.lau;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author adonis lau
 * @date 2023/6/9 18:33
 */
@Slf4j
public class ChromePassword {
    public static void main(String[] args) {
        String init = "/Users/adonis/Library/CloudStorage/Dropbox/Applications/Syncs/1Password/ChromePassword.csv";
        String output = "/Users/adonis/Downloads/ChromePassword.xlsx";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(init));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                // 处理每一行数据
                log.info(JSONObject.toJSONString(fields));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
