package app.entity.converter;

import app.entity.Criterion;
import app.entity.bom.CriterionBom;
import app.service.CriterionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CriterionConverter {

    @Autowired
    CriterionService criterionService;

    public Criterion fromBom(CriterionBom bom) {
        Criterion result = new Criterion();
        if (bom.getId() != null && bom.getId() != -1) {
            result = criterionService.findById(bom.getId());
        }
        result.setId(bom.getId());
        result.setName(bom.getName());
        result.setDescription(bom.getDescription());
        return result;
    }

    public CriterionBom toBom(Criterion contest) {
        CriterionBom bom = new CriterionBom();
        bom.setId(contest.getId());
        bom.setName(contest.getName());
        bom.setDescription(contest.getDescription());
        return bom;
    }

    public List<Criterion> fromBom(List<CriterionBom> boms) {
        List<Criterion> result = new ArrayList<>();
        for (CriterionBom bom : boms
        ) {
            result.add(fromBom(bom));
        }
        return result;
    }

    public List<CriterionBom> toBom(List<Criterion> criteria) {
        List<CriterionBom> result = new ArrayList<>();
        for (Criterion criterion : criteria
        ) {
            result.add(toBom(criterion));
        }
        return result;
    }
}
