import java.io.InputStream;
import java.io.RandomAccessFile;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.net.*;

public class Main extends Application {
    
    TextField urlTextBox;
    Button addDownload;
    TableView downloadsTable;
    TableColumn fileNameColumn;
    TableColumn urlColumn;
    TableColumn fileSizeColumn;
    TableColumn progressColumn;
    TableColumn statusColumn;
    Button pauseResumeButton;
    Button cancelButton;
    Button deleteButton;
    
    HBox box1;
    HBox box2;
    HBox box3;
    
    public static void main(String[] args) throws Exception {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        urlTextBox = new TextField("Put URL here..");
        addDownload = new Button("Add Download");
        downloadsTable = new TableView();
        fileNameColumn = new TableColumn("File Name");
        urlColumn = new TableColumn("URL");
        fileSizeColumn = new TableColumn("Size");
        progressColumn = new TableColumn("Progress");
        statusColumn = new TableColumn("Status");
        pauseResumeButton = new Button("Pause");
        cancelButton = new Button("Cancel");
        deleteButton = new Button("Delete");
        downloadsTable.getColumns().addAll(fileNameColumn, urlColumn, fileSizeColumn, progressColumn, statusColumn);
        box1 = new HBox(2);
        box2 = new HBox(2);
        box3 = new HBox(2);
        
        box1.getChildren().addAll(urlTextBox, addDownload);
        box2.getChildren().add(downloadsTable);
        box3.getChildren().addAll(pauseResumeButton, deleteButton, cancelButton);
        
        addDownload.setOnAction(e -> {
            String t = urlTextBox.getText();
            URL url;
            URLConnection uc;
            try {
                url = new URL(t);
                uc = url.openConnection();
                uc.connect();
//                int size = uc.getContentLength();
                String fileName = url.getFile();
                fileName = fileName.substring(fileName.lastIndexOf('/')+1);
                RandomAccessFile file = new RandomAccessFile(fileName, "rw");
                int c;
                int count=0;
                InputStream stream = uc.getInputStream();
                while((c = stream.read()) != -1) {
                   {
                       file.write(c);
                       count++;
                       if (count%1024==0)
                           System.out.printf("%d\n",count);
                   }
                  
                }
                 file.close();
            }
            catch(Exception ex) {}
        });
        
        FlowPane root = new FlowPane();
        root.getChildren().addAll(box1,box2,box3);
        
        Scene scene = new Scene(root, 500,500);
        
        primaryStage.setTitle("Download manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
