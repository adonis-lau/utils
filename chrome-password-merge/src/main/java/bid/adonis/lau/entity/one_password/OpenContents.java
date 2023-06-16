package bid.adonis.lau.entity.one_password;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author adonis lau
 * @date 2023/6/16 12:01
 */
@Data
public class OpenContents implements Serializable {
    private List<String> tags;
}
