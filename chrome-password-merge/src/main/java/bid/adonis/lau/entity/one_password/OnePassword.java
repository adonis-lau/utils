package bid.adonis.lau.entity.one_password;

import lombok.Data;

import java.io.Serializable;

/**
 * @author adonis lau
 * @date 2023/6/16 12:00
 */
@Data
public class OnePassword implements Serializable {
    private String uuid;
    private long updatedAt;
    private String locationKey;
    private String securityLevel;
    private OpenContents openContents;
    private String contentsHash;
    private String title;
    private String location;
    private SecureContents secureContents;
    private long createdAt;
    private String typeName;
}
