package com.example.java_final_project_javafx;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainController {

    @FXML
    private Label timerLabel;

    @FXML
    private TextField taskField;

    @FXML
    private TextField durationField;

    @FXML
    public ListView<Task> taskListView;

    //    @FXML
//    private ListView<String> workflowListView;
    @FXML
    public ListView<Task> finishedListView;

    private Timer timer;
    private int timeRemaining;
    public final int TASK = 1;
    public final int FINISH = 2;

    public TaskStorage taskStorage = new TaskStorage();

    @FXML
    public void initialize() {
        taskListView.setItems(FXCollections.observableArrayList());
        taskListView.setCellFactory(param -> new TaskListCell());
        taskStorage.loadTasksFromFile(taskListView.getItems());
        durationField.setText("25");
    }

    @FXML
    protected void startTimer() {
        String durationText = durationField.getText();

        try {
            int duration = Integer.parseInt(durationText) * 60;
            if (duration <= 0)
                new ErrorDialog("Input Error", "Please enter a positive number");

            timeRemaining = duration;
            if (timer != null) {
                timer.cancel();
            }
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    // JavaFX UI操作需要在JavaFX Application Thread中執行
                    javafx.application.Platform.runLater(() -> {
                        timeRemaining--;
                        int minutes = timeRemaining / 60;
                        int seconds = timeRemaining % 60;
                        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
                        if (timeRemaining <= 0) {
                            timer.cancel();
                        }
                    });
                }
            }, 0, 1000);
        } catch (NumberFormatException e) {
            new ErrorDialog("Input Error", "Please enter a valid number");
        }
    }

    @FXML
    protected void showAddTaskDialog() {
        TaskDialog taskDialog = new TaskDialog(this);
        taskDialog.show();
    }

    @FXML
    protected void showEditTaskDialog() {
        // get the selected task
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            new ErrorDialog("Selection Error", "No task selected");
            return;
        }

        // show the dialog
        TaskDialog taskDialog = new TaskDialog(this, selectedTask);
        taskDialog.show();
    }

    public void storeToListView(Task newTask) {
        if (newTask.getCompleted() < 100) {
            taskListView.getItems().add(newTask);
        } else {
            System.out.println("Task already completed");
            finishedListView.getItems().add(newTask);
        }
    }

    @FXML
    protected void deleteTaskElement() {
        int selectedIndex = taskListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            // 刪除該任務並保存進度
            taskListView.getItems().remove(selectedIndex);
            taskStorage.saveTasksToFile(taskListView.getItems());
        } else {
            new ErrorDialog("Selection Error", "No task selected");
        }
    }

    @FXML
    protected void deleteFinishedTaskElement() {
        int selectedIndex = finishedListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            finishedListView.getItems().remove(selectedIndex);
            taskStorage.saveTasksToFile(taskListView.getItems());
        }
    }

    @FXML
    protected void finishTask() {
        int selectedIndex = taskListView.getSelectionModel().getSelectedIndex();
//        if()
    }

    @FXML
    protected void viewTaskDetails() {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            new ErrorDialog("Selection Error", "No task selected");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("task-details-view.fxml"));
            VBox root = loader.load();
            TaskDetailsController controller = loader.getController();
            controller.setTask(selectedTask);

            Stage stage = new Stage();
            stage.setTitle("Task Details");
            stage.setScene(new Scene(root, 400, 300));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveTasksToFile() {
        taskStorage.saveTasksToFile(taskListView.getItems());
    }

    public int getListViewSize(int type) {
        if (type == TASK) {
            return taskListView.getItems().size();
        } else if (type == FINISH) {
            return finishedListView.getItems().size();
        }
        return 0;
    }
}
