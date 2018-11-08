package application;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class JavaMongoConnection {

	private static MongoDatabase database;

	public static MongoDatabase getDB(String dbName) {

		if (database == null) {
			// Create connection to Mongo client
			MongoClient mongoDBClient = new MongoClient("localhost", 27017);

			// access DB
			MongoDatabase fetchedDB = mongoDBClient.getDatabase(dbName);

			database = fetchedDB;
		}

		return database;
	}

}
