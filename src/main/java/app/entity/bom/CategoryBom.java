package app.entity.bom;

import lombok.Data;

import java.util.List;
@Data
public class CategoryBom {
    private Long id;
    private String name;
    private String description;
    private List<CriterionBom> criteria;
}
