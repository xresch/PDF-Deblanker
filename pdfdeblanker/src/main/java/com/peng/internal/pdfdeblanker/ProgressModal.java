package com.peng.internal.pdfdeblanker;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProgressModal extends Stage {
	
	private Text progressLabel = new Text("Total Progress:");
	private ProgressBar totalProgress = new ProgressBar(0);
	private ProgressBar fileProgress = new ProgressBar(0);
	private Text messageText = new Text();
	
	private Task task;
	
	Thread thread = new Thread();
	
	private int totalFiles = 0;
	
	private ProgressModal instance;
	
	public ProgressModal(Scene parent) {
		
		instance = this;
		GridPane grid = new GridPane();
		
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(80);
        col1.setHgrow(Priority.ALWAYS);
        
        grid.getColumnConstraints().add(col1);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        int rowIndex = 0;
        //------------------------------------
        //Total Progress Bar
        //------------------------------------
        grid.add(progressLabel, 0, rowIndex++);
        grid.add(totalProgress, 0, rowIndex++);
        totalProgress.setPrefWidth(400);
        
        
        //------------------------------------
        //File Progress Bar
        //------------------------------------
        grid.add(new Label("Current File:"), 0, rowIndex++);
        grid.add(fileProgress, 0, rowIndex++);
        fileProgress.setPrefWidth(400);
        
        //------------------------------------
        // Message
        //------------------------------------
        grid.add(messageText, 0, rowIndex++);
        messageText.prefWidth(400);     
        
        //------------------------------------
        // Cancel Button
        //------------------------------------
        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefWidth(400);
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				cancelTask();
			}
        	
        });
        
        grid.add(cancelButton, 0, rowIndex++);
        
        //------------------------------------
        // Scene
        //------------------------------------
		this.setScene(new Scene(grid));
		this.setWidth(500);
		this.setTitle("Progress");
		this.initModality(Modality.WINDOW_MODAL);
		this.initOwner(parent.getWindow());
		

	}
	
	public ProgressModal setTotalFiles(int fileCount) {
		this.totalFiles = fileCount;
		return this;
	}
	
	public int getTotalFiles() {
		return this.totalFiles;
	}
	
	public void setCurrentFile(final int currentFile, final String fileName) {
		Platform.runLater(new Runnable() {
			public void run() {
				progressLabel.setText("Total Progress("+currentFile+"/"+totalFiles+") - "+fileName+":");
			}
		});
	}
	
	public void setTotalProgress(final double percent) {
		Platform.runLater(new Runnable() {
			public void run() {
				totalProgress.setProgress(percent);
			}
		});
	}
	
	public double getTotalProgress() {
		return totalProgress.getProgress();
	}
	
	public void setMessage(final String message) {
		Platform.runLater(new Runnable() {
			public void run() {
				messageText.setText(message);
			}
		});
	}
	
	public void setCurrentFileProgress(final double percent) {
		Platform.runLater(new Runnable() {
			public void run() {
				fileProgress.setProgress(percent);
			}
		});
		
	}
	
	public ProgressModal resetProgress() {
		progressLabel.setText("Total Progress:");
		totalProgress.setProgress(0);
		fileProgress.setProgress(0);
		return this;
	}
	
	public ProgressModal setTask(Task task) {
		this.task = task;
		return this;
	}
	
	public void cancelTask() {
		thread.stop();

		this.close();
	}
	
	public ProgressModal startMonitoredTask() {
		thread = new Thread(task);
		thread.start();
		
		return this;
	}
	

}
