package bid.adonis.lau.entity.one_password;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author adonis lau
 * @date 2023/6/16 12:03
 */
@Data
public class SecureContents implements Serializable {
    private List<URLs> URLs;
    private List<Fields> fields;
    private List<Sections> sections;
}
