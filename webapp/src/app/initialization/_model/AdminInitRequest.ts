export interface AdminInitRequest {
	email: string;
	username: string;
	password: string;
	sendActivationEmail?: boolean
}
