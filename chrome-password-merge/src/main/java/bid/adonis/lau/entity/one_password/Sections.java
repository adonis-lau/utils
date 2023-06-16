package bid.adonis.lau.entity.one_password;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author adonis lau
 * @date 2023/6/16 12:02
 */
@Data
public class Sections implements Serializable {
    private String title;
    private String name;
    private List<SectionsFields> fields;
}
