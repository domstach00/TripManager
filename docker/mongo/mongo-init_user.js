const appDb = process.env.MONGO_APP_DB;
const appUser = process.env.MONGO_APP_USER;
const appPass = process.env.MONGO_APP_PASSWORD;

db = db.getSiblingDB(appDb);

if (db.getUser(appUser)) {
	db.updateUser(appUser, {
		pwd: appPass,
		roles: [
			{ role: "readWrite", db: appDb }
		]
	});
	print(`Updated app user: ${appUser} in DB: ${appDb}`);
} else {
	db.createUser({
		user: appUser,
		pwd: appPass,
		roles: [
			{ role: "readWrite", db: appDb }
		]
	});
	print(`Created app user: ${appUser} in DB: ${appDb}`);
}
