import { BaseModel } from "./base-models.interface";
import { Account } from "./account";

export interface Trip extends BaseModel {
	name: string;
	description?: string;
	owner?: Account;
	membersId?: string[];
	isPublic?: boolean;
	isClosed?: boolean;
	isArchived?: boolean;
	isDeleted?: boolean;
}
