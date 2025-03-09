import { BaseModel } from "../../shared/_model/base-models.interface";
import { Account } from "../../account/_model/account";

export interface Budget extends BaseModel {
	owner: Account;
	members?: string[];
	templateId?: string;
	name: string;
	categories?: Category[];
	description?: string;
	allocatedBudget?: string;
	startDate?: string;
	endDate?: string;
	isArchived: boolean;
}

export interface BudgetCreateForm {
	membersToInvite?: string[];
	name: string;
	description?: string;
	allocatedBudget?: number;
	startDate: string;
	endDate: string;
}

export interface Category extends BaseModel {
	name: string;
	type?: 'EXPENSE' | 'INCOME';
	allocatedAmount?: string;
	subCategories?: SubCategory[];
}

export interface CategoryDialogData {
	budgetId: string;
	categoryType?: 'EXPENSE' | 'INCOME';
	category?: {
		id: string;
		name: string;
		allocatedAmount: string;
	};
}

export interface SubCategory {
	id: string;
	name: string;
}

export interface CategoryCreateForm {
	name: string;
	type?: 'EXPENSE' | 'INCOME';
	allocatedAmount?: string;
}

export interface SubCategoryCreateForm {
	name: string;
}
