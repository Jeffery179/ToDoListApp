package application;

import java.util.Date;

public class ToDoItem {

	private String contents;

	Date creationDate;

	Date completionDate;

	boolean completed = false;

	String id;

	public ToDoItem(String contents) {
		this.contents = contents;
		creationDate = new Date();
		completionDate = null;
		completed = false;
	}

	public ToDoItem(String contents, Date addedDate, Date completedDate, boolean status, String id) {
		this.contents = contents;
		this.creationDate = addedDate;
		this.completionDate = completedDate;
		this.completed = status;
		this.id = id;
	}

	public void setContents(String newContents) {
		this.contents = newContents;
	}

	public String getContents() {
		return this.contents;
	}

	public void markCompleted() {
		completionDate = new Date();
		completed = true;
	}

	public Date getDateAdded() {
		return this.creationDate;
	}

	public Date getDateCompleted() {
		return this.completionDate;
	}

	public boolean getIsCompleted() {
		return this.completed;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return this.contents;
	}
}
