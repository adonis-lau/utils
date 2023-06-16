package bid.adonis.lau.entity.bitwarden;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author adonis lau
 * @date 2023/6/16 11:56
 */
@Data
public class Items implements Serializable {
    private String id;
    private String organizationId;
    private String folderId;
    private int type;
    private int reprompt;
    private String name;
    private String notes;
    private boolean favorite;
    private List<Fields> fields;
    private Login login;
    private String collectionIds;

}
