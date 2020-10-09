package app.entity.converter;

import app.entity.Category;
import app.entity.Criterion;
import app.entity.bom.CategoryBom;
import app.entity.bom.CriterionBom;
import app.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryConverter {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CriterionConverter criterionConverter;

    public Category fromBom(CategoryBom bom) {
        Category result = new Category();
        if (bom.getId() != null && bom.getId() != -1) {
            result = categoryService.findById(bom.getId());
        }
        result.setId(bom.getId());
        result.setName(bom.getName());
        result.setDescription(bom.getDescription());
        if (bom.getCriteria()!=null){
            List<Criterion> criterionList = new ArrayList<>();
            for (CriterionBom criterionBom : bom.getCriteria()
            ) {
                criterionList.add(criterionConverter.fromBom(criterionBom));
            }
            result.setCriteria(criterionList);
        }
        return result;
    }

    public CategoryBom toBom(Category category) {
        CategoryBom bom = new CategoryBom();
        bom.setId(category.getId());
        bom.setName(category.getName());
        bom.setDescription(category.getDescription());
        List<CriterionBom> criterionBomList = new ArrayList<>();
        for (Criterion criterion : category.getCriteria()
        ) {
            criterionBomList.add(criterionConverter.toBom(criterion));
        }
        bom.setCriteria(criterionBomList);
        return bom;
    }
    public List<Category> fromBom(List<CategoryBom> boms) {
        List<Category> result = new ArrayList<>();
        for (CategoryBom bom : boms
        ) {
            result.add(fromBom(bom));
        }
        return result;
    }

    public List<CategoryBom> toBom(List<Category> categories) {
        List<CategoryBom> result = new ArrayList<>();
        for (Category category : categories
        ) {
            result.add(toBom(category));
        }
        return result;
    }
}
