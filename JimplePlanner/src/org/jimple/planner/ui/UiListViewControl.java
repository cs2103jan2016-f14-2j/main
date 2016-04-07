package org.jimple.planner.ui;
//@@author A0122498
import java.io.IOException;
import java.util.logging.Level;

import org.jimple.planner.constants.Constants;
import org.jimple.planner.task.Task;

import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;

public class UiListViewControl extends UiController{
	protected UiController controller;
	
	public UiListViewControl(UiController controller) {
		this.controller = controller;
	}
	
	protected void selectTaskAtIndex(int index){
		for(int i=0; i<getActiveListView().getItems().size(); i++)
			if(getActiveListView().getItems().get(i).getTaskId() == index){
				selectIndex(i);
				return;
			}
	}
	public void selectFirstIncompleteTask(){
		for(Task task : getActiveListView().getItems()){
			if(task.getToTime() != null && UiFormatter.timeDifference(task.getToTime())>0 && task.getType().equals(Constants.TYPE_STATIC)){
				getActiveListView().requestFocus();
				getActiveListView().getSelectionModel().select(task);
				getActiveListView().scrollTo(getActiveListView().getSelectionModel().getSelectedIndex());
				return;
			}
		}
	}
	public String getCurrentTabName() {
		return controller.tabPanes.getSelectionModel().getSelectedItem().getText();
	}

	public Tab getCurrentTab() {
		return controller.tabPanes.getSelectionModel().getSelectedItem();
	}

	public void updatePointer(int num) {
		System.out.println(getActiveListView().getItems().size());
		if (num >= getActiveListView().getItems().size())
			num -= 1;
		getActiveListView().requestFocus();
		getActiveListView().getSelectionModel().select(num);
		getActiveListView().scrollTo(getActiveListView().getSelectionModel().getSelectedIndex());
	}

	public int getCurrentTabItemIndex() {
		return getActiveListView().getSelectionModel().getSelectedIndex();
	}


	protected boolean isListViewSelectionEmpty() {
		return getActiveListView().getSelectionModel().isEmpty();
	}
	
	public void deselectTaskItem() {
		getActiveListView().getSelectionModel().clearSelection();
	}

	protected void deleteSelectedTask() {
		try {
			log.log(Level.INFO, "attempting to delete selected task");
			int selectedIndex = getCurrentTabItemIndex();
			if (selectedIndex == -1)
				return;
			if(getSelectedListItem().getType().equals("static"))
				return;
			controller.logic.execute("DELETE " + getSelectedListItem().getTaskId());
			updatePointer(selectedIndex);
		} catch (IOException e) {
			log.log(Level.WARNING, "IO exception. delete not successful", e);
		}
	}

	protected Task getSelectedListItem() {
		if(getActiveListView().getSelectionModel().isEmpty())
			return null;
		return getActiveListView().getSelectionModel().getSelectedItem();
	}
	
	protected void addAndReload(Tab tab, int index) {
		if(controller.overlay.isVisible()){
			controller.overlay.setVisible(false);
		}
		controller.loadDisplay();
		controller.tabPanes.getSelectionModel().select(tab);
		selectTaskAtIndex(index);
		controller.commandBox.requestFocus();
	}

	protected void selectIndex(int num) {
		getActiveListView().requestFocus();
		getActiveListView().getSelectionModel().select(num);
		getActiveListView().scrollTo(getActiveListView().getSelectionModel().getSelectedIndex());
	}

	@SuppressWarnings("unchecked")
	protected ListView<Task> getList(Tab tab) {
		if(((Pane) tab.getContent()).getChildren().isEmpty())
			return new ListView<Task>();
		if(((Pane) tab.getContent()).getChildren().get(0).getClass() == ListView.class)
			return (ListView<Task>) ((Pane) tab.getContent()).getChildren().get(0);
		return new ListView<Task>();
	}

	@SuppressWarnings("unchecked")
	protected ListView<Task> getActiveListView() {
		if(((Pane) controller.tabPanes.getSelectionModel().getSelectedItem().getContent()).getChildren()
				.isEmpty())
			return new ListView<Task>();

		if(((Pane) controller.tabPanes.getSelectionModel().getSelectedItem().getContent()).getChildren()
				.get(0).getClass() == ListView.class)
			return (ListView<Task>) ((Pane) controller.tabPanes.getSelectionModel().getSelectedItem().getContent()).getChildren().get(0);
		return new ListView<Task>();
	}
}
