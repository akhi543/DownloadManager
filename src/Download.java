import java.io.*;
import java.io.RandomAccessFile;
import java.net.*;
import java.util.*;
import sun.rmi.log.ReliableLog;

public class Download extends Observable implements Runnable {
    
    private URL url;
    private int status;
    private int downloaded;
    private int size;
    private URL newurl=null;
    
    public static final String statuses[]={"Downloading","Error","Paused","Cancelled","Complete"};
    
    public static final int Downloading=0;
    public static final int Error=1;
    public static final int Paused=2;
    public static final int Cancelled=3;
    public static final int Complete=4;
    
    final int MAX_BUFFER_SIZE=50*1024;
    
    Download(URL ur)
    {
        url=ur;
        size=-1;
        downloaded=0;
        status=Downloading;
        download();
    }
    
    public URL getURL()
    {
        return url;
    }
    
    public int getSize()
    {
        return size;
    }
    
    public int getStatus()
    {
        return status;
    }
    
    public float getProgress()
    {
        return ((float)downloaded*100)/size;
    }
    
    private void download()
    {
        Thread th=new Thread(this);
        th.start();
    }
    
    void pause()
    {
        status=Paused;
        stateChanged();
    }
    
    void resume()
    {
        status=Downloading;
        stateChanged();
        download();
    }
    
    void cancel()
    {
        status=Cancelled;
        stateChanged();
    }
    
    void error()
    {
        status=Error;
        stateChanged();
    }
    @Override
    public void run()
    {
        RandomAccessFile file=null;
        InputStream stream=null;
        try
        {
            HttpURLConnection connect=(HttpURLConnection)url.openConnection();
            connect.setRequestProperty("Range","bytes"+downloaded+"-");
            connect.connect();
            System.out.println(connect.getURL());
            if (connect.getResponseCode()/100!=2)
            {
                error();
            }
            printheader(connect);
            if (size==-1)
            {
                System.out.println("here");
                HttpURLConnection sizecon=(HttpURLConnection)url.openConnection();
                sizecon.setRequestMethod("HEAD");
                sizecon.getInputStream();
                size=sizecon.getContentLength();
                System.out.println(connect.getResponseCode());
                System.out.println(size);
                stateChanged();
            }
            
            HttpURLConnection con=(HttpURLConnection)url.openConnection();
            con.setRequestProperty("Range","bytes"+downloaded+"-");
            
            if (size==-1)
            {
                newurl=connect.getURL();
                System.out.println(String.valueOf(newurl));
                String searchurl=String.valueOf(url);
                
                //System.out.println(searchurl);
                //Crawler cr=new Crawler(newurl,searchurl);
                
                //if(cr.search()==1)
                //{
                    con.setRequestProperty("Referer",String.valueOf(newurl)); 
                    printheader(con);
                //}
                
            }
            con.connect();
            printheader(con);
            if (size==-1)
            {
                
                HttpURLConnection sizecon=(HttpURLConnection)url.openConnection();
                sizecon.setRequestMethod("HEAD");
                sizecon.setRequestProperty("Referer",String.valueOf(newurl)); 
                sizecon.getInputStream();
                size=sizecon.getContentLength();
                System.out.println(connect.getResponseCode());
                System.out.println(size);
                stateChanged();
            }
            String filename=url.getFile();
            filename=filename.substring(filename.lastIndexOf("/")+1);
            file = new RandomAccessFile(filename, "rw");
            file.seek(downloaded);
            stream = con.getInputStream();
            
            System.out.println(filename+"   "+size+"   "+downloaded+"   "+statuses[status]);
            while (status==Downloading)
            {
                byte buffer[];
                if (size-downloaded>MAX_BUFFER_SIZE)
                {
                    buffer=new byte[MAX_BUFFER_SIZE];
                }
                else
                    buffer=new byte[size-downloaded];
                
                int c=stream.read(buffer);
                if (c==-1)
                    break;
                file.write(buffer,0,c);
                downloaded+=c;
                System.out.printf("%d\n",downloaded);
                stateChanged();
            }
            System.out.println("here3");
            if (status==Downloading)
            {
                status=Complete;
                stateChanged();
            } 
        }
        catch(Exception E)
        {
            error();
            E.printStackTrace();
        }
        finally
        {
            if (file!=null)
            {
                try{file.close();}catch(Exception E){}
            }
            if (stream!=null)
            {
                try{stream.close();}catch(Exception E){}
            }
        }
    }
    private void stateChanged()
    {
        setChanged();
        notifyObservers();
    }
    
    public static void main(String args[]) throws Exception
    {
        URL u=new URL("http://mp3light.net/assets/songs/393000-393999/393375-see-you-again-feat-charlie-puth--1428288074.mp3");
        //URL u=new URL("http://hqwallbase.com/images/big/colours_of_nature-1531327.jpg");
        Download d=new Download(u);
    }
    
    void printheader(HttpURLConnection conn)
    {
        Map<String, List<String>> map = conn.getHeaderFields();
	for (Map.Entry<String, List<String>> entry : map.entrySet()) {
		System.out.println("Key : " + entry.getKey() + 
                 " ,Value : " + entry.getValue());
        }
    }

}

