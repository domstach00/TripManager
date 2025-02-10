import { BaseModel } from "../../shared/_model/base-models.interface";

export interface Transaction extends BaseModel {
	budgetId: string;
	categoryId?: string;
	subCategoryId?: string;
	description?: string;
	amount: string;
	transactionDate: string;
}

export interface TransactionCreateForm {
	budgetId: string;
	categoryId?: string;
	subCategoryId?: string;
	description?: string;
	amount: string;
	transactionDate: string;
}
