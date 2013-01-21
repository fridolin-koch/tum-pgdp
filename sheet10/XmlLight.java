package sheet10;

import java.io.StringReader;
import java.io.StringWriter;
/*
 * Author: Frido Koch
 * Email: frido@vresh.net
 * License: CC BY-NC-SA 3.0 DE 
 * http://creativecommons.org/licenses/by-nc-sa/3.0/de/
 */

public class XmlLight {

    final static String name = "FileDemo.dat";

    public static void main(String args[]) {
        //String data = "<xmllight><node1><value=node1.child></node1><value=value></xmllight>";
    	String data = "<xmllight><nodename1><value_name1=3 24 2><a><b><c><value_name1=value></c></b></a><value_name2=value></nodename1><nodename2><value_name1=value><value_name2=value></nodename2><value_name1=value></xmllight>";
        try {
            // Create stream from string rather than file; for testing purposes.
            XmlTree t = XmlTree.load(new StringReader(data));

            XmlTree n1 = t.getNode("nodename1");
            
            System.out.println(n1);
            
            XmlTree n1value = n1.getNode("value_name1");
            String val = n1value.getValue();

            System.out.println("Value:" + val);
            
            StringWriter out = new StringWriter();
            t.safe(out);
            System.out.println(out.toString());

            n1value.getNode("any"); // Should cause an exception.
            
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
}