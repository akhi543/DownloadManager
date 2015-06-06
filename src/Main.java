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
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.cell.PropertyValueFactory;

public class Main extends Application {
    TextField urlTextBox;
    Button addDownload;
    TableView<DownloadEntry> downloadsTable;
    TableColumn<DownloadEntry, String> fileNameColumn;
    TableColumn<DownloadEntry, String> urlColumn;
    TableColumn<DownloadEntry, Integer> fileSizeColumn;
    TableColumn<DownloadEntry, ProgressBar> progressColumn;
    TableColumn<DownloadEntry, String> statusColumn;
    Button pauseResumeButton;
    Button cancelButton;
    Button deleteButton;
    
    HBox box1,box2,box3;
    String fullUrl;
    String fileName;
    URL url;
    URLConnection uc;
    Integer size;
    int c;
    RandomAccessFile file;
    InputStream stream;
    ArrayList<String> statuses = new ArrayList<>();
    
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public Main() {
        statuses.add("Downloading");
        statuses.add("Paused");
        statuses.add("Completed");
        statuses.add("Cancelled");
        statuses.add("Error");
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        urlTextBox = new TextField("Put URL here..");
        addDownload = new Button("Add Download");
        downloadsTable = new TableView();
        fileNameColumn = new TableColumn<>("File Name");
        urlColumn = new TableColumn<>("URL");
        fileSizeColumn = new TableColumn<>("Size");
        progressColumn = new TableColumn<>("Progress");
        statusColumn = new TableColumn<>("Status");
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
        
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        fileSizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        progressColumn.setCellValueFactory(new PropertyValueFactory<>("progress"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        addDownload.setOnAction(e -> addDownloadClicked());
        
        FlowPane root = new FlowPane();
        root.getChildren().addAll(box1,box2,box3);
        
        Scene scene = new Scene(root, 500,500);
        
        primaryStage.setTitle("Download manager");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.centerOnScreen();
//        primaryStage.setOnCloseRequest(e->primaryStage.close());
    }

    public void addDownloadClicked() {
        fullUrl = urlTextBox.getText();
        fileName = fullUrl.substring(fullUrl.lastIndexOf('/')+1);
        try {
            file = new RandomAccessFile(fileName, "rw");
            url = new URL(fullUrl);
            uc = url.openConnection();
            uc.connect();
            size = new Integer(uc.getContentLength());
            downloadsTable.setItems(getDownloadEntry());
            stream = uc.getInputStream();
            while((c = stream.read()) != -1) {
                file.write(c);
            }
            new AlertBox("sdf", "done");
        }
        catch(Exception ex) {}
        finally {
            try {
                if(stream!=null)
                    stream.close();
                if(file!=null) 
                    file.close();
            }
            catch(Exception ex) {}
        }
    }
    
    public ObservableList<DownloadEntry> getDownloadEntry() {
        System.out.println(size);
        ObservableList<DownloadEntry> download = FXCollections.observableArrayList();
        download.add(new DownloadEntry(fileName, fullUrl, size, new ProgressBar(0.5), statuses.get(0)));
        return download;
    }
}
