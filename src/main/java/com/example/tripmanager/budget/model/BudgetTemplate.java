package com.example.tripmanager.budget.model;

import com.example.tripmanager.budget.model.category.Category;
import com.example.tripmanager.shared.model.AbstractAuditable;
import jakarta.validation.constraints.NotBlank;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Document(collection = BudgetTemplate.COLLECTION_NAME)
public class BudgetTemplate extends AbstractAuditable {
    public static final String COLLECTION_NAME = "BudgetTemplates";
    public static final String FIELD_NAME_OWNER_ID = "ownerId";
    public static final String FIELD_NAME_IS_PUBLIC = "isPublic";
    public static final String FIELD_NAME_NAME = "name";
    public static final String FIELD_NAME_DESCRIPTION = "description";
    public static final String FIELD_NAME_CATEGORIES = "categories";
    public static final String FIELD_NAME_ALLOCATED_BUDGET = "allocatedBudget";
    public static final String FIELD_NAME_BUDGET_PERIOD = "budgetPeriod";
    public static final String FIELD_NAME_IS_ARCHIVED = "isArchived";

    @NotBlank(message = "Owner Id cannot be empty")
    @Indexed
    private ObjectId ownerId;
    private boolean isPublic;
    @Indexed
    @NotBlank(message = "Budget name cannot be empty")
    private String name;
    private String description;
    private List<Category> categories;
    private BigDecimal allocatedBudget;
    private BudgetPeriod budgetPeriod;
    private boolean isArchived;

    public BudgetTemplate(ObjectId ownerId, boolean isPublic, String name, String description, List<Category> categories, BigDecimal allocatedBudget, BudgetPeriod budgetPeriod, boolean isArchived) {
        this.ownerId = ownerId;
        this.isPublic = isPublic;
        this.name = name;
        this.description = description;
        this.categories = categories;
        this.allocatedBudget = allocatedBudget;
        this.budgetPeriod = budgetPeriod;
        this.isArchived = isArchived;
    }

    public BudgetTemplate() {
    }

    public enum BudgetPeriod {
        WEEKLY("Weekly") {
            @Override
            public Instant calculateEndDate(@Nullable Instant startDate) {
                return startDate == null
                        ? null
                        : startDate.atZone(DEFAULT_ZONE).plusDays(6).toInstant();
            }
        },
        MONTHLY("Monthly") {
            @Override
            public Instant calculateEndDate(@Nullable Instant startDate) {
                return startDate == null
                        ? null
                        : startDate.atZone(DEFAULT_ZONE).plusMonths(1).toInstant();
            }
        },
        QUARTERLY("Quarterly") {
            @Override
            public Instant calculateEndDate(@Nullable Instant startDate) {
                return startDate == null
                        ? null
                        : startDate.atZone(DEFAULT_ZONE).plusMonths(3).toInstant();
            }
        },
        YEARLY("Yearly") {
            @Override
            public Instant calculateEndDate(@Nullable Instant startDate) {
                return startDate == null
                        ? null
                        : startDate.atZone(DEFAULT_ZONE).plusYears(1).toInstant();
            }
        },
        CUSTOM("Custom") {
            @Override
            public Instant calculateEndDate(@Nullable Instant startDate) {
                return null;
            }
        };

        private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();
        private String displayName;

        BudgetPeriod(String displayName) {
            this.displayName = displayName;
        }

        public abstract Instant calculateEndDate(@Nullable Instant startDate);

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
    }

    public ObjectId getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(ObjectId ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public BigDecimal getAllocatedBudget() {
        return allocatedBudget;
    }

    public void setAllocatedBudget(BigDecimal allocatedBudget) {
        this.allocatedBudget = allocatedBudget;
    }

    public BudgetPeriod getBudgetPeriod() {
        return budgetPeriod;
    }

    public void setBudgetPeriod(BudgetPeriod budgetPeriod) {
        this.budgetPeriod = budgetPeriod;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}
