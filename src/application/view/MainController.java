package application.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.DBManager;
import application.Main;
import application.ToDoItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;

public class MainController implements Initializable {

	private Main main;

	@FXML
	private TextField newTaskContents;

	@FXML
	private ListView<String> listView;

	@FXML
	private ListView<String> completedListView;

	private ObservableList<ToDoItem> completedItems = FXCollections.observableArrayList();

	private ObservableList<ToDoItem> items = FXCollections.observableArrayList();
	
	private int currentListViewItemSelected = 0;

	private List<String> completedTasks = new ArrayList<>();

	private List<ToDoItem> unfinishedItems = new ArrayList<>();

	private List<ToDoItem> finishedItems = new ArrayList<>();

	/* Adding a task button */
	@FXML
	private void addTaskClicked() {

		String newTask = newTaskContents.getText();

		ToDoItem newItem = new ToDoItem(newTask);

		DBManager.saveTask(newItem);

		newTaskContents.setText("");

		updateApplication();
	}
	
	/* Clicking on an uncompleted task */
	@FXML
	private void handleMouseClick() {
		currentListViewItemSelected = listView.getSelectionModel().getSelectedIndex();
	}

	/* Initializs the application with data */
	@Override
	public void initialize(URL url, ResourceBundle rb) {

		listView.setContextMenu(getUncompletedTasksContextMenu());

		completedListView.setContextMenu(getCompletedTasksContextMenu());

		updateApplication();
	}

	/* Updates the applications with newest changes from DB */
	public void updateApplication() {

		ObservableList<ToDoItem> tempFinishedItems = FXCollections.observableArrayList();

		ObservableList<ToDoItem> tempItems = FXCollections.observableArrayList();

		unfinishedItems = DBManager.getUncompletedTasks();
		for (ToDoItem curr : unfinishedItems) {
			tempItems.add(curr);
		}

		finishedItems = DBManager.getCompletedTasks();
		for (ToDoItem curr : finishedItems) {
			tempFinishedItems.add(curr);
		}

		listView.setItems(convertToStrings(tempItems));

		completedListView.setItems(convertToStrings(tempFinishedItems));
	}

	/* Converts all To Do Item's content (String) into an observable list */
	public static ObservableList<String> convertToStrings(ObservableList<ToDoItem> list) {

		ObservableList<String> strings = FXCollections.observableArrayList();

		for (ToDoItem current : list) {
			strings.add(current.getContents());
		}

		return strings;
	}

	/* Content menu options for the uncompleted to do list */
	public ContextMenu getUncompletedTasksContextMenu() {
		final ContextMenu contextMenu = new ContextMenu();

		MenuItem completed = new MenuItem("Mark Completed");
		MenuItem edit = new MenuItem("Edit");
		MenuItem delete = new MenuItem("Delete");

		contextMenu.getItems().addAll(completed, edit, delete);

		completed.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				ToDoItem newlyCompleted = unfinishedItems.get(currentListViewItemSelected);

				DBManager.updateToDoCompleted(newlyCompleted);

				updateApplication();
			}
		});

		edit.setOnAction(new EventHandler<ActionEvent>() {
			@SuppressWarnings("unchecked")
			@Override
			public void handle(ActionEvent event) {
				listView.setEditable(true);
				
			    listView.setCellFactory(TextFieldListCell.forListView());

				listView.layout();

				listView.edit(currentListViewItemSelected);

				listView.setOnEditCommit(t -> {
					ToDoItem currentDoItem = unfinishedItems.get(t.getIndex());

					listView.getItems().set(t.getIndex(), t.getNewValue());

					DBManager.updateContents(currentDoItem, t.getNewValue());
				});

				updateApplication();
			}
		});

		delete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				ToDoItem toBeDeleted = unfinishedItems.get(currentListViewItemSelected);

				DBManager.deleteToDoItem(toBeDeleted);

				listView.getItems().remove(currentListViewItemSelected);

				updateApplication();
			}
		});

		return contextMenu;
	}
	
	/* Context Menu options for the completed to do list */
	public ContextMenu getCompletedTasksContextMenu() {
		final ContextMenu contextMenu = new ContextMenu();

		MenuItem uncomplete = new MenuItem("Mark Uncompleted");
		MenuItem delete = new MenuItem("Delete");

		contextMenu.getItems().addAll(uncomplete, delete);

		uncomplete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				ToDoItem newlyCompleted = finishedItems.get(completedListView.getSelectionModel().getSelectedIndex());

				DBManager.updateToDoUnCompleted(newlyCompleted);

				updateApplication();
			}
		});

		delete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				ToDoItem toBeDeleted = finishedItems.get(completedListView.getSelectionModel().getSelectedIndex());

				DBManager.deleteToDoItem(toBeDeleted);

				completedListView.getItems().remove(completedListView.getSelectionModel().getSelectedIndex());

				updateApplication();
			}
		});

		return contextMenu;
	}
}
