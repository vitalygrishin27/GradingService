package app.entity.converter;

import app.entity.Category;
import app.entity.Contest;
import app.entity.bom.CategoryBom;
import app.entity.bom.ContestBom;
import app.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class ContestConverter {
    @Autowired
    ContestService contestService;

    @Autowired
    CategoryConverter categoryConverter;

    public Contest fromBom(ContestBom bom) {
        Contest result = new Contest();
        if (bom.getId() != null && bom.getId() != -1) {
            result = contestService.findById(bom.getId());
        }
        result.setId(bom.getId());
        result.setName(bom.getName());
        result.setPhoto(bom.getPhoto());
        result.setDescription(bom.getDescription());
        List<Category> categoryList = new ArrayList<>();
        for (CategoryBom categoryBom : bom.getCategories()
        ) {
            categoryList.add(categoryConverter.fromBom(categoryBom));
        }
        result.setCategories(categoryList);
        return result;
    }

    public ContestBom toBom(Contest contest) {
        ContestBom bom = new ContestBom();
        bom.setId(contest.getId());
        bom.setName(contest.getName());
        bom.setDescription(contest.getDescription());
        bom.setPhoto(contest.getPhoto());
        List<CategoryBom> categoryBomList = new ArrayList<>();
        for (Category category : contest.getCategories()
        ) {
            categoryBomList.add(categoryConverter.toBom(category));
        }
        bom.setCategories(categoryBomList);
        return bom;
    }

    public List<Contest> fromBom(List<ContestBom> boms) {
        List<Contest> result = new ArrayList<>();
        for (ContestBom bom : boms
        ) {
            result.add(fromBom(bom));
        }
        return result;
    }

    public List<ContestBom> toBom(List<Contest> contests) {
        List<ContestBom> result = new ArrayList<>();
        for (Contest contest : contests
        ) {
            result.add(toBom(contest));
        }
        return result;
    }
}
