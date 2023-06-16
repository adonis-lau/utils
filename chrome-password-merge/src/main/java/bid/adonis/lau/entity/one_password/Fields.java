package bid.adonis.lau.entity.one_password;

import lombok.Data;

import java.io.Serializable;

/**
 * @author adonis lau
 * @date 2023/6/16 12:02
 */
@Data
public class Fields implements Serializable {
    private String value;
    private String name;
    private String type;
    private String designation;
}
