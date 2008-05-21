package gov.epa.owm.mtb.cwns.fax;

import gov.epa.owm.mtb.cwns.common.CWNSProperties;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.efaxdeveloper.util.inbound.InboundClient;

public class FaxPostSimulator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		String urlStr=CWNSProperties.getProperty("fax.post.url");
		String filename=CWNSProperties.getProperty("fax.post.document");
		URL    url;
	    URLConnection   urlConn;
	    DataOutputStream   printout;
	    DataInputStream    input;
	    FileInputStream fis =null;
	    try {
	    	String xml = getFile(filename);
	    
		    // url of the inbound fax processing servlet
		    url = new URL (urlStr);
		    // URL connection channel.
		    urlConn = url.openConnection();
		    // Let the run-time system (RTS) know that we want input.
		    urlConn.setDoInput (true);
		    // Let the RTS know that we want to do output.
		    urlConn.setDoOutput (true);
		    // No caching, we want the real thing.
		    urlConn.setUseCaches (false);
		    // Specify the content type.
		    urlConn.setRequestProperty
		    ("Content-Type", "application/x-www-form-urlencoded");
		    // Send POST output.	    	
			printout = new DataOutputStream (urlConn.getOutputStream ());
		    String content = "xml=" + URLEncoder.encode(xml);
		    System.out.println (xml);						    
		    printout.writeBytes (content);
		    printout.flush ();
		    printout.close ();
		    // Get response data.
		    input = new DataInputStream (urlConn.getInputStream ());
		    String str;
		    while (null != ((str = input.readLine()))){
		       System.out.println (str);
		    }
		    input.close ();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			
		}
	}
	
	
	private static String getFile(String fileName) throws IOException{
	    File file = new File( fileName ); // create a File object
        FileInputStream fileinputstream = new FileInputStream(file);

        int numberBytes = fileinputstream.available();
        byte bytearray[] = new byte[numberBytes];

        fileinputstream.read(bytearray);
        String xml = new String(bytearray);
        fileinputstream.close();
        return xml;		
	}

}
