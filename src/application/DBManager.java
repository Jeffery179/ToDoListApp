package application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class DBManager {

	private static final String DBNAME = "ToDoListAppDB";

	private static final String COLLECTION_NAME = "todo";

	/* Inserting a new to-do list task */
	public static void saveTask(ToDoItem item) {

		Document newDoc = new Document().append("contents", item.getContents()).append("dateAdded", item.getDateAdded())
				.append("dateCompleted", item.getDateCompleted()).append("status", item.getIsCompleted());

		MongoDatabase database = JavaMongoConnection.getDB(DBNAME);

		MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

		collection.insertOne(newDoc);

	}

	/* Retrieving to-do list tasks */
	public static List<ToDoItem> getAllTasks() {

		MongoDatabase database = JavaMongoConnection.getDB(DBNAME);

		MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

		List<ToDoItem> allItems = new ArrayList<>();

		MongoCursor<Document> cursor = collection.find().iterator();

		while (cursor.hasNext()) {

			Document current = cursor.next();

			String contents = current.getString("contents");

			Date dateAdded = current.getDate("dateAdded");

			Date dateCompleted = current.getDate("dateCompleted");

			boolean status = current.getBoolean("status");

			String id = current.get("_id").toString();

			ToDoItem task = new ToDoItem(contents, dateAdded, dateCompleted, status, id);

			allItems.add(task);
		}

		return allItems;
	}

	/* Retrieving un-completed items only */
	public static List<ToDoItem> getUncompletedTasks() {

		MongoDatabase database = JavaMongoConnection.getDB(DBNAME);

		MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

		List<ToDoItem> unfinished = new ArrayList<>();

		BasicDBObject query = new BasicDBObject();
		query.put("status", false);

		MongoCursor<Document> cursor = collection.find(query).iterator();

		while (cursor.hasNext()) {

			Document current = cursor.next();

			String contents = current.getString("contents");

			Date dateAdded = current.getDate("dateAdded");

			Date dateCompleted = current.getDate("dateCompleted");

			boolean status = current.getBoolean("status");

			String id = current.get("_id").toString();

			ToDoItem task = new ToDoItem(contents, dateAdded, dateCompleted, false, id);

			unfinished.add(task);
		}
		return unfinished;
	}

	/* Retrieving completed items only */
	public static List<ToDoItem> getCompletedTasks() {

		MongoDatabase database = JavaMongoConnection.getDB(DBNAME);

		MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

		List<ToDoItem> finished = new ArrayList<>();

		BasicDBObject query = new BasicDBObject();
		query.put("status", true);

		MongoCursor<Document> cursor = collection.find(query).iterator();

		while (cursor.hasNext()) {

			Document current = cursor.next();

			String contents = current.getString("contents");

			Date dateAdded = current.getDate("dateAdded");

			Date dateCompleted = current.getDate("dateCompleted");

			boolean status = current.getBoolean("status");

			String id = current.get("_id").toString();

			ToDoItem task = new ToDoItem(contents, dateAdded, dateCompleted, false, id);

			finished.add(task);
		}
		return finished;
	}

	/* Setting To Do item as completed */
	public static void updateToDoCompleted(ToDoItem item) {
		MongoDatabase database = JavaMongoConnection.getDB(DBNAME);

		MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

		Bson filter = Filters.eq("_id", new ObjectId(item.getId()));

		Bson newValue = new Document("dateCompleted", new Date()).append("status", true);

		Bson updateOperationDocument = new Document("$set", newValue);

		collection.updateMany(filter, updateOperationDocument);
	}
	
	/*
	 * Updating a ToDoItem's contents by re-sending it's information to the DB based
	 * on it's ID
	 */
	public static void updateContents(ToDoItem item, String newContents) {

		MongoDatabase database = JavaMongoConnection.getDB(DBNAME);

		MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

		Bson filter = Filters.eq("_id", new ObjectId(item.getId()));

		Bson newValue = new Document("contents", newContents);

		Bson updateOperationDocument = new Document("$set", newValue);

		collection.updateMany(filter, updateOperationDocument);
	}

	/*Deleting a To Do Item from our DB*/
	public static void deleteToDoItem(ToDoItem item) {
		MongoDatabase database = JavaMongoConnection.getDB(DBNAME);

		MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
	
		collection.deleteOne(new Document("_id", new ObjectId(item.getId())));
	}

	/* Moves the completed to do item back into uncomplete status */
	public static void updateToDoUnCompleted(ToDoItem item) {
		MongoDatabase database = JavaMongoConnection.getDB(DBNAME);

		MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

		Bson filter = Filters.eq("_id", new ObjectId(item.getId()));

		Bson newValue = new Document("dateCompleted", null).append("status", false);

		Bson updateOperationDocument = new Document("$set", newValue);

		collection.updateMany(filter, updateOperationDocument);

	}	
}
