import { BaseModel } from "../shared/_model/base-models.interface";
import { Account } from "./account";

export interface Trip extends BaseModel {
	name: string;
	description?: string;
	owner?: Account;
	members?: Member[];
	isPublic?: boolean;
	isClosed?: boolean;
	isArchived?: boolean;
	isDeleted?: boolean;
}

export interface Member {
	accountId: string;
	memberRole: string;
}
