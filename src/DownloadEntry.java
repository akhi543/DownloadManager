import javafx.scene.control.ProgressBar;

public class DownloadEntry {

    String fileName;
    String url;
    Integer size;
    ProgressBar progress;
    String status;

    public DownloadEntry(String fileName, String url, Integer size, ProgressBar progress, String status) {
        this.fileName = fileName;
        this.url = url;
        this.size = size;
        this.progress = progress;
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public ProgressBar getProgress() {
        return progress;
    }

    public void setProgress(ProgressBar progress) {
        this.progress = progress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
