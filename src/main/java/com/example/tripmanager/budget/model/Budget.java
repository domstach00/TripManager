package com.example.tripmanager.budget.model;

import com.example.tripmanager.budget.model.category.Category;
import com.example.tripmanager.shared.model.AbstractAuditable;
import jakarta.validation.constraints.NotBlank;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Document(collection = Budget.COLLECTION_NAME)
public class Budget extends AbstractAuditable {
    public static final String COLLECTION_NAME = "Budgets";
    public static final String FIELD_NAME_OWNER_ID = "ownerId";
    public static final String FIELD_NAME_MEMBERS = "members";
    public static final String FIELD_NAME_TEMPLATE_ID = "templateId";
    public static final String FIELD_NAME_NAME = "name";
    public static final String FIELD_NAME_CATEGORIES = "categories";
    public static final String FIELD_NAME_DESCRIPTION = "description";
    public static final String FIELD_NAME_ALLOCATED_BUDGET = "allocatedBudget";
    public static final String FIELD_NAME_START_DATE = "startDate";
    public static final String FIELD_NAME_END_DATE = "endDate";
    public static final String FIELD_NAME_IS_ARCHIVED = "isArchived";

    @NotBlank(message = "Owner Id cannot be empty")
    @Indexed
    private ObjectId ownerId;
    @Indexed
    private List<ObjectId> members;
    @Indexed
    private ObjectId templateId;
    @NotBlank(message = "Budget name cannot be empty")
    private String name;
    @Indexed
    @DBRef
    private List<Category> categories;
    private String description;
    private BigDecimal allocatedBudget;
    @Indexed
    private Instant startDate;
    @Indexed
    private Instant endDate;
    private boolean isArchived;

    public Budget(ObjectId ownerId, List<ObjectId> members, ObjectId templateId, String name, List<Category> categories, String description, BigDecimal allocatedBudget, Instant startDate, Instant endDate, boolean isArchived) {
        this.ownerId = ownerId;
        this.members = members;
        this.templateId = templateId;
        this.name = name;
        this.categories = categories;
        this.description = description;
        this.allocatedBudget = allocatedBudget;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isArchived = isArchived;
    }

    public Budget() {
    }

    public ObjectId getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(ObjectId ownerId) {
        this.ownerId = ownerId;
    }

    public List<ObjectId> getMembers() {
        return members;
    }

    public void setMembers(List<ObjectId> members) {
        this.members = members;
    }

    public ObjectId getTemplateId() {
        return templateId;
    }

    public void setTemplateId(ObjectId templateId) {
        this.templateId = templateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAllocatedBudget() {
        return allocatedBudget;
    }

    public void setAllocatedBudget(BigDecimal allocatedBudget) {
        this.allocatedBudget = allocatedBudget;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}
