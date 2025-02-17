package com.example.tripmanager.budget.repository;

import com.example.tripmanager.budget.model.category.Category;
import com.example.tripmanager.shared.repository.AbstractRepositoryImpl;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class CategoryRepository extends AbstractRepositoryImpl<Category> {
    @Override
    protected Class<Category> getEntityClass() {
        return Category.class;
    }

    @Override
    public <S extends Category> S save(@NotNull S entity) {
        if (entity != null && !CollectionUtils.isEmpty(entity.getSubCategories())) {
            entity.getSubCategories().forEach(subCategory -> {
                if (StringUtils.isBlank(subCategory.getId())) {
                    subCategory.setId(new ObjectId().toHexString());
                }
            });
        }
        return super.save(entity);
    }

    @Override
    public <S extends Category> List<S> saveAll(@NotNull Iterable<S> entities) {
        if (entities != null) {
            entities.forEach(this::generateIdForSubCategoryInCategory);
        }
        return super.saveAll(entities);
    }

    /**
     * Since SubCategory is an embedded document, MongoDB does not generate an ID for it automatically.
     * Therefore, we generate an ID manually before saving.
     *
     * @param category The category entity to update or create
     * @param <S> A subclass of Category
     */
    private <S extends Category> void generateIdForSubCategoryInCategory(S category) {
        if (category != null && !CollectionUtils.isEmpty(category.getSubCategories())) {
            category.getSubCategories().stream()
                    .filter(subCategory -> StringUtils.isBlank(subCategory.getId()))
                    .forEach(subCategory -> subCategory.setId(new ObjectId().toHexString()));
        }
    }
}
