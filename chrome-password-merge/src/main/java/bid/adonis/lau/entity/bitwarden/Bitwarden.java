package bid.adonis.lau.entity.bitwarden;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author adonis lau
 * @date 2023/6/16 11:54
 */
@Data
public class Bitwarden implements Serializable {
    private boolean encrypted;
    private List<String> folders;
    private List<Items> items;

}
