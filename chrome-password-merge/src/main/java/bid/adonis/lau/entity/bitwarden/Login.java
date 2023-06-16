package bid.adonis.lau.entity.bitwarden;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author adonis lau
 * @date 2023/6/16 11:55
 */
@Data
public class Login implements Serializable {
    private List<Uris> uris;
    private String username;
    private String password;
    private String totp;

}
