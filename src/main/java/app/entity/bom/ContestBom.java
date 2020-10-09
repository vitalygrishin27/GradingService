package app.entity.bom;

import lombok.Data;

import java.util.List;
@Data
public class ContestBom {
    private Long id;
    private String name;
    private String description;
    private List<CategoryBom> categories;
    private String photo;
}
