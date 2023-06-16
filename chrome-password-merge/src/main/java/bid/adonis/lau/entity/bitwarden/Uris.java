package bid.adonis.lau.entity.bitwarden;

import lombok.Data;

import java.io.Serializable;

/**
 * @author adonis lau
 * @date 2023/6/16 11:55
 */
@Data
public class Uris implements Serializable {
    private String match;
    private String uri;
}
