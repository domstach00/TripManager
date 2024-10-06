import { Account } from "./account";

export interface Trip {
	id: string;
	name: string;
	description?: string;
	dayLength?: number;
	summaryCost?: number;
	lastModifiedTime?: Date;
	lastModifiedBy?: Account;
}
