package bid.adonis.lau.entity.one_password;

import lombok.Data;

import java.io.Serializable;

/**
 * @author adonis lau
 * @date 2023/6/16 12:02
 */
@Data
public class SectionsFields implements Serializable {
    private String k;
    private String n;
    private String v;
    private String t;
}
