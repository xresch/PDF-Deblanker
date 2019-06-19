package com.peng.internal.pdfdeblanker;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DoneModal extends Stage {
	
	private Text messageText = new Text();
	private Font headerFont = Font.font("Tahoma", FontWeight.NORMAL, 20);
	private String folderPath;
		
	public DoneModal(Scene parent) {

		GridPane grid = new GridPane();
		
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        col1.setHgrow(Priority.ALWAYS);
        
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        col2.setHgrow(Priority.ALWAYS);
        
        
        grid.getColumnConstraints().addAll(col1, col2);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        int rowIndex = 0;
        
        //------------------------------------
        // Title
        //------------------------------------
        Text doneTitle = new Text("All Done!");
        doneTitle.setFont(headerFont);
        grid.add(doneTitle, 0, rowIndex++, 2, 1);
        
        //------------------------------------
        // Message
        //------------------------------------
        grid.add(messageText, 0, rowIndex++,2,1);
        messageText.setText("The blank pages are removed. Do you want to open the result folder?");
        messageText.prefWidth(400);     
        
        //------------------------------------
        // Yes Button
        //------------------------------------
        Button yesButton = new Button("Yes");
        yesButton.setPrefWidth(200);
        yesButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				closeModal();
				openFolder();
			}
        	
        });
        
        grid.add(yesButton, 1, rowIndex);
        
        //------------------------------------
        // Yes Button
        //------------------------------------
        Button noButton = new Button("No");
        noButton.setPrefWidth(200);
        noButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				closeModal();
			}
        	
        });
        
        grid.add(noButton, 0, rowIndex++);
        
        //------------------------------------
        // Scene
        //------------------------------------
		this.setScene(new Scene(grid));
		this.setWidth(500);
		this.setTitle("Progress");
		this.initModality(Modality.WINDOW_MODAL);
		this.initOwner(parent.getWindow());

	}
		
	public void closeModal() {
		this.close();
	}
	
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}
	
	public void openFolder() {

		try {
			Desktop.getDesktop().open(new File(folderPath));
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
	    	alert.setTitle("Error");
	    	alert.setHeaderText("An Error occured");
	    	alert.setContentText(e.getMessage());
	    	
	    	alert.showAndWait();
			
		}
	}

}
