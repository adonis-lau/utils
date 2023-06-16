package bid.adonis.lau;

import bid.adonis.lau.entity.bitwarden.Bitwarden;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author adonis lau
 * @date 2023/6/16 11:20
 */
@Slf4j
public class BitwardenTo1Password {

    public static void main(String[] args) throws IOException {
        String fileName = "/Users/adonis/Library/CloudStorage/Dropbox/Applications/Syncs/Password/bitwarden_export_20230616111555.json";
        Bitwarden bitwarden = JSONObject.parseObject(new String(Files.readAllBytes(Paths.get(fileName))), Bitwarden.class);
        System.out.println(bitwarden.getItems().size());
    }
}
