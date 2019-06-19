package com.peng.internal.pdfdeblanker;

import java.io.File;
import java.io.FilenameFilter;

import com.peng.internal.pdfdeblanker.utils.PDFUtils;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Main extends Application  {
	
	private final TextField filePathTextField = new TextField();
	private final TextField folderPathTextField = new TextField();
	private final TextField percentageField = new TextField();
	
	FileChooser fileChooser = new FileChooser();
	
	public static ProgressModal progressModal;
	public static DoneModal doneModal;
	
	private Font headerFont = Font.font("Tahoma", FontWeight.NORMAL, 20);
	
	public static void main(String[] args) {
		launch(args);
		//deblank();
	}
	
	@Override
    public void start(final Stage primaryStage) {
		
        primaryStage.setTitle("BJB PDF Deblanker");
        
        GridPane grid = new GridPane();
        

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        col1.setHgrow(Priority.NEVER);
       
        col1.setFillWidth(true);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(20);
        col2.setHgrow(Priority.NEVER);
        col2.setFillWidth(true);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(50);
        col3.setHgrow(Priority.ALWAYS);
        col3.setFillWidth(true);
        //grid.getColumnConstraints().addAll(col1,col2,col3);

        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        
        
        int rowIndex = 0;
        
        //-----------------------------
        // Percentage Controls
        //-----------------------------
        Text deblankerTitle = new Text("PDF Deblanker");
        deblankerTitle.setFont(headerFont);
        grid.add(deblankerTitle, 0, rowIndex, 3, 1);
        
        rowIndex++;
        Text deblankerInfo = new Text();
        deblankerInfo.setText(" The PDF deblanker will remove pages that can be considered blank from PDFs."
        					 + "\n It will determine if a page is blank by how many white it can find on a page."
        					 + "\n If a page reaches a certain amount of white, it will be removed from the PDF."
        					 + "\n The results will be saved as follows:"
        					 + "\n   - PDFs without blank pages will be saved in a subfolder called 'deblanked' at the location of the original file."
        					 + "\n   - The removed pages will be saved in a subfolder called 'removedPages' at the location of the original file. This can be used for ckecking the amount of pages removed."
        					 );
        
        
        grid.add(deblankerInfo, 0, rowIndex, 3, 4);
        rowIndex+=4;
        
        //###########################################################################
        // Percentage
        //###########################################################################
        
        //------------------------
        // Add vertical space
        rowIndex++;
        grid.add(new Label(), 0, rowIndex);
        
        
        //-----------------------------
        // Percentage Controls
        //-----------------------------
        Text percentageTitle = new Text("Percentage");
        percentageTitle.setFont(headerFont);
        grid.add(percentageTitle, 0, rowIndex, 3, 1);
        
        rowIndex++;
        Text percentageInfo = new Text();
        percentageInfo.setText("Define how much white area a page should have in percentage to be considered a blank page."
        					 + "\nAll pages with more white will be removed from the PDF.");
        
        grid.add(percentageInfo, 0, rowIndex, 3, 1);
        
        rowIndex++;
        Label percentageLabel = new Label("Percentage:");
        grid.add(percentageLabel, 0, rowIndex);

        percentageField.setText("99.0");
        
        grid.add(percentageField, 1, rowIndex);
        
        //###########################################################################
        // DeBlank Single File
        //###########################################################################
        
        //------------------------
        // Add vertical space
        rowIndex++;
        grid.add(new Label(), 0, rowIndex);
        
        //-----------------------------
        // Select File Controls
        //-----------------------------
        rowIndex++;
        Text singleTitle = new Text("Deblank Single PDF");
        singleTitle.setFont(headerFont);
        grid.add(singleTitle, 0, rowIndex, 2, 1);

        //-----------------------------
        // Select File Controls
        //-----------------------------
        rowIndex++;
        Label selectLabel = new Label("PDF File:");
        grid.add(selectLabel, 0, rowIndex);
        
        filePathTextField.setDisable(true);
        filePathTextField.setPrefWidth(500);
        grid.add(filePathTextField, 2, rowIndex);
        
        Button fileChooserButton = new Button();
        fileChooserButton.setText("Select PDF ...");
        fileChooserButton.setPrefWidth(200.0);
        fileChooserButton.setOnAction(new EventHandler<ActionEvent>() {
 
            public void handle(ActionEvent event) {
                fileChooser.setTitle("Choose PDF");
                fileChooser.getExtensionFilters().add(new ExtensionFilter("PDF Files", "*.pdf"));
                File pdf = fileChooser.showOpenDialog(primaryStage);
                if(pdf != null) {
                	filePathTextField.setText(pdf.getAbsolutePath());
                }
            } 
        });
        
        
        grid.add(fileChooserButton, 1, rowIndex);
        
        //-----------------------------
        // Deblank Button
        //-----------------------------
        rowIndex++;
        Button deblankButton = new Button();
        deblankButton.setText("Deblank Single PDF");
        deblankButton.setOnAction(new EventHandler<ActionEvent>() {
 
            public void handle(ActionEvent event) {
            	final String path = filePathTextField.getText();
            	
            	if(path == null || path.length() == 0){
	            	Alert alert = new Alert(AlertType.WARNING);
	            	alert.setTitle("Input required");
	            	alert.setHeaderText("PDF not selected.");
	            	alert.setContentText("Please select a PDF file first.");
	
	            	alert.showAndWait();
	            	return;
            	}
            	
            	File file = new File(path);            	
            	if(!file.isFile() && !file.canRead() ){
	            	Alert alert = new Alert(AlertType.WARNING);
	            	alert.setTitle("Warning");
	            	alert.setHeaderText("PDF cannot be read.");
	            	alert.setContentText("Please make sure the selected PDF exists and you have access to the specified location.");
	
	            	alert.showAndWait();
	            	return;
            	}
            	
            	String percentageString = percentageField.getText();
            	final double percentageDouble;
            	try{
            		percentageDouble = Double.parseDouble(percentageString);
            		
            		if(percentageDouble > 100.0 || percentageDouble < 0.0) {
            			Alert alert = new Alert(AlertType.WARNING);
    	            	alert.setTitle("Warning");
    	            	alert.setHeaderText("Enter Valid Percentage");
    	            	alert.setContentText("Please enter a number between 0.0 and 100.0 for the percentage.");
    	
    	            	alert.showAndWait();
    	            	return;
            		}
            		
            		
            	}catch(Exception e){
            		Alert alert = new Alert(AlertType.WARNING);
	            	alert.setTitle("Warning");
	            	alert.setHeaderText("Enter Valid Number");
	            	alert.setContentText("Please enter a number between 0.0 and 100.0 for the percentage.");
	
	            	alert.showAndWait();
	            	return;
            	}
            	
            	
            	//------------------------------------
            	// Start Progress
            	//------------------------------------
            	
        		Task<Boolean> task = new Task<Boolean>() {

					@Override
					protected Boolean call() throws Exception {
						deblankSingleFile(path, percentageDouble);
						Main.progressModal.setMessage("Done! You can close this window.");
						
						Platform.runLater(new Runnable() {
							public void run() {
								Main.progressModal.close();
								Main.doneModal.setFolderPath(new File(path).getParent()+"/deblanked");
								Main.doneModal.show();
							}
						});
						return null;
					}
        		};

            	progressModal.resetProgress()
            		.setTotalFiles(1)
	            	.setTask(task)
	            	.startMonitoredTask()
            		.show();

            }
            
        });
        
        grid.add(deblankButton, 0, rowIndex);
        
        //###########################################################################
        // DeBlank All in Folder
        //###########################################################################
        
        //------------------------
        // Add vertical space
        rowIndex++;
        grid.add(new Label(), 0, rowIndex);
        
        //-----------------------------
        // Select File Controls
        //-----------------------------
        rowIndex+= 2;
        Text multiTitle = new Text("Deblank All PDFs in Folder");
        multiTitle.setFont(headerFont);
        grid.add(multiTitle, 0, rowIndex, 2, 1);

        //-----------------------------
        // Select File Controls
        //-----------------------------
        rowIndex++;
        Label selectFolderLabel = new Label("Folder:");
        grid.add(selectFolderLabel, 0, rowIndex);
        
        folderPathTextField.setDisable(true);
        folderPathTextField.setPrefWidth(500);
        grid.add(folderPathTextField, 2, rowIndex);
        
        Button folderChooserButton = new Button();
        folderChooserButton.setText("Select Folder ...");
        folderChooserButton.setPrefWidth(200.0);
        folderChooserButton.setOnAction(new EventHandler<ActionEvent>() {
 
            public void handle(ActionEvent event) {
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Choose Folder");
                File folder = chooser.showDialog(primaryStage);
                if(folder != null) {
                	folderPathTextField.setText(folder.getAbsolutePath());
                }
            } 
        });
        
        grid.add(folderChooserButton, 1, rowIndex);
        
        //-----------------------------
        // Deblank Button
        //-----------------------------
        rowIndex++;
        Button deblankFolderButton = new Button();
        deblankFolderButton.setText("Deblank All in Folder");
        deblankFolderButton.setOnAction(new EventHandler<ActionEvent>() {
 
            public void handle(ActionEvent event) {
            	String folderPath = folderPathTextField.getText();
            	
            	if(folderPath == null || folderPath.length() == 0){
	            	Alert alert = new Alert(AlertType.WARNING);
	            	alert.setTitle("Input required");
	            	alert.setHeaderText("Folder not selected");
	            	alert.setContentText("Please select a Folder first.");
	
	            	alert.showAndWait();
	            	return;
            	}
            	
            	File folder = new File(folderPath);            	
            	if(!folder.isDirectory() && !folder.canRead()){
	            	Alert alert = new Alert(AlertType.WARNING);
	            	alert.setTitle("Warning");
	            	alert.setHeaderText("Is not a readable folder.");
	            	alert.setContentText("Please make sure the selected folder exists and you have access to the specified location.");
	
	            	alert.showAndWait();
	            	return;
            	}
            	
            	String percentageString = percentageField.getText();
            	double percentageDouble;
            	try{
            		percentageDouble = Double.parseDouble(percentageString);
            		
            		if(percentageDouble > 100.0 || percentageDouble < 0.0) {
            			Alert alert = new Alert(AlertType.WARNING);
    	            	alert.setTitle("Warning");
    	            	alert.setHeaderText("Enter Valid Percentage");
    	            	alert.setContentText("Please enter a number between 0.0 and 100.0 for the percentage.");
    	
    	            	alert.showAndWait();
    	            	return;
            		}
            		
            		
            	}catch(Exception e){
            		Alert alert = new Alert(AlertType.WARNING);
	            	alert.setTitle("Warning");
	            	alert.setHeaderText("Enter Valid Number");
	            	alert.setContentText("Please enter a number between 0.0 and 100.0 for the percentage.");
	
	            	alert.showAndWait();
	            	return;
            	}
            	
                deblankAllInFolder(folderPath, percentageDouble);
            }
            
        });
        
        grid.add(deblankFolderButton, 0, rowIndex);
        //-----------------------------
        // Create Scene
        //-----------------------------
        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("wash99_128px.png")));
        
        progressModal = new ProgressModal(scene);
        doneModal = new DoneModal(scene);
        
        primaryStage.show();
    }

	public void deblankAllInFolder(final String folderPath, final double percentage){
		
		File folder = new File(folderPath);
		
		final File[] pdfFiles = folder.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".pdf");
		    }
		});
		
		if(pdfFiles.length == 0) {
			Alert alert = new Alert(AlertType.WARNING);
        	alert.setTitle("Warning");
        	alert.setHeaderText("No PDFs in Folder");
        	alert.setContentText("No PDFs were found in the specified folder.");

        	alert.showAndWait();
        	return;
		}
		
		//------------------------------------
    	// Start Progress
    	//------------------------------------
    			
		Task<Boolean> task = new Task<Boolean>() {

			@Override
			protected Boolean call() throws Exception {
				for(int i = 0; i < pdfFiles.length; i++) {
					File pdf = pdfFiles[i];
					Main.progressModal.setCurrentFile(i, pdf.getName());
					deblankSingleFile(pdf.getAbsolutePath(), percentage);
				}
				Main.progressModal.setMessage("Done! You can close this window.");

				Platform.runLater(new Runnable() {
					public void run() {
						Main.progressModal.close();
						Main.doneModal.setFolderPath(folderPath+"/deblanked");
						Main.doneModal.show();
					}
				});
				return null;
			}
		};

    	progressModal.resetProgress()
    		.setTotalFiles(pdfFiles.length)
        	.setTask(task)
        	.startMonitoredTask()
    		.show();
		
		
	}
	
	public void deblankSingleFile(String filepath, double percentage){
	
    File origFile = new File(filepath);
    String origFolder = origFile.getParent();
    String origFileName = origFile.getName();
    	    
    try {
	    //-------------------------------------------
	    // Deblank PDF
	    //-------------------------------------------
    	DeblankResults deblanked = null;
    	deblanked = PDFUtils.deblankPDF(origFile, percentage);
    	
    	
    	//-------------------------------------------
	    // Save deblanked File
	    //-------------------------------------------
    	Main.progressModal.setMessage("Save Deblanked File ...");
	    String deblankedFolderPath = origFolder+"/deblanked";
	    
	    File deblankedFolder = new File(deblankedFolderPath);
	    if(!deblankedFolder.isDirectory()) {
	    	deblankedFolder.mkdirs();
	    }
	    
	    deblanked.deblankedPDF.save(deblankedFolderPath+"/"+origFileName);	    
	    deblanked.deblankedPDF.close();
	    
	    //-------------------------------------------
	    // Save removed File
	    //-------------------------------------------
	    Main.progressModal.setMessage("Save Removed Pages ...");
	    
	    String removedFolderPath = origFolder+"/removedPages";
	    
	    File removedFolder = new File(removedFolderPath);
	    if(!removedFolder.isDirectory()) {
	    	removedFolder.mkdirs();
	    }
	    
	    deblanked.removedPagesPDF.save(removedFolderPath+"/"+origFileName);	    
	    deblanked.removedPagesPDF.close();
	    deblanked.originalPDF.close();   	
	} catch (final Exception e) {
		
		Platform.runLater(new Runnable() {
			public void run() {
				Alert alert = new Alert(AlertType.ERROR);
		    	alert.setTitle("Error");
		    	alert.setHeaderText("An Error occured");
		    	alert.setContentText(e.getMessage());
		    	
		    	alert.showAndWait();
		    	
		    	Main.progressModal.cancelTask();
			}
		});


		e.printStackTrace();
	}
}

}
