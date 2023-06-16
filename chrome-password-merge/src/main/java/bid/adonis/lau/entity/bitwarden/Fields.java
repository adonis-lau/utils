package bid.adonis.lau.entity.bitwarden;

import lombok.Data;

/**
 * @author adonis lau
 * @date 2023/6/16 13:45
 */
@Data
public class Fields {
    private String name;
    private String value;
    private int type;
    private String linkedId;
}
