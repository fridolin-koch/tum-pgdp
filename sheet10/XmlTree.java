/*
 * Author: Frido Koch
 * Email: frido@vresh.net
 * License: CC BY-NC-SA 3.0 DE 
 * http://creativecommons.org/licenses/by-nc-sa/3.0/de/
 * 
 * 
 */

package sheet10;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Frido Koch
 * 
 */
abstract class XmlTree {

    // Exceptions that might arise during tree load/access.
    @SuppressWarnings("serial")
	public static class XmlException extends Exception {

        public String item;

        public XmlException(String item) {
            this.item = item;
        }

        public String toString() {
            return this.getClass().getName() + " : " + item;
        }
    }

    @SuppressWarnings("serial")
	public static class MisformatedInputException extends XmlException {
        public MisformatedInputException(String item) {
            super(item);
        }
    }

    @SuppressWarnings("serial")
	public static class NoValueException extends XmlException {
        public XmlTree node;

        public NoValueException(XmlTree node) {
            super("node is not a value : " + node.toString());
            this.node = node;
        }
    }

    @SuppressWarnings("serial")
	public static class NoNodeException extends XmlException {
        public XmlTree node;

        public NoNodeException(XmlTree node, String item) {
            super("'" + item + "' does not exist inside " + node.toString());
            this.node = node;
        }
    }

    @SuppressWarnings("serial")
	public static class NotAvailable extends XmlException {
        public XmlTree node;

        public NotAvailable(XmlTree node, String item) {
            super("'" + item + "' does not exist inside " + node.toString());
            this.node = node;
        }
    }

	@SuppressWarnings("serial")
	public static class AlreadyAvailable extends XmlException {
        public XmlTree node;

        public AlreadyAvailable(XmlTree node, String item) {
            super("'" + item + "' already exist inside " + node.toString());
            this.node = node;
        }
    }

	protected HashMap<String, XmlTree> childNodes;
	
    // Default implementations (node has no value and value has no nodes):
    public String getValue() throws NoValueException {
    	//method is overwritten by child class and outer xmllight has no value
    	throw new NoValueException(this);
    }
    
    /**
     * Returns the XmlNode matching the given tag name
     * @param name The tag name
     * @throws NotAvailable 
     */
    public XmlTree getNode(String name) throws NotAvailable, NoNodeException	{
    	
    	for (Map.Entry<String, XmlTree> entry : this.childNodes.entrySet())	{
    		if( entry.getKey().equals(name) )	{
    			return entry.getValue();
    		}
    	}
    	throw new NotAvailable(this, name);
    }

    // For parsing. Returns next visible character (or -1 for EOF)
    private static int getNextCharacter(StringReader sr) throws IOException {
    	int data;
    	while( (data = sr.read()) != -1)	{
    		//ignore any whitespace, tabs and control codes
    		if( data > 32 && data < 127)	{
    			return data;
    		}
    	}
		return data;
    }

    // Consume next char, throw exception if it is unexpected.
    private static void eat(StringReader sr, char character) throws IOException, MisformatedInputException {
        //FILL IN
    	int data = getNextCharacter(sr);
    	if( data == -1 || (char)data != character)	{
    		throw new MisformatedInputException( String.format("Expected %c found %c", character, (char)data) );
    	}
    }

    // Get chars until character is encountered; character is excluded from result.
    private static String getUntil(StringReader sr, char character) throws IOException, MisformatedInputException {
		
    	int data;
    	boolean read = true;
    	StringBuilder ret = new StringBuilder();
    	while( read && (data = getNextCharacter(sr)) != -1)	{
    		if( (char)data == character)	{
    			read = false;
    		} else	{
    			ret.append( (char)data );
    		}
    	}
		return ret.toString();
    }

    // Read a node: "< ... >", if there is a node <name = value>, return {name,value}, else return {name}.
    private static String[] getNode(StringReader sr) throws IOException, MisformatedInputException {
    	//we expect a opening tag here
    	XmlTree.eat(sr, '<');
    	//read text until >
    	String name = XmlTree.getUntil(sr, '>');
    	
    	//no lets make some error checking
    	//check if the tag name is empty
    	if(name.length() < 1)	{
    		throw new MisformatedInputException("Found empty tag (<>).");
    	}
    	//check if name contains < or > if yes the XML is malformed
    	if(name.indexOf("<") != -1 || name.indexOf(">") != -1)	{
    		throw new MisformatedInputException("Found < or > inside tag name! Check your xml if there are unclosed tags.");
    	}
    	//count equals signs
    	if( countOccurrencesInString(name, '=') > 1 )	{
    		throw new MisformatedInputException( String.format("The tagName %s is invalid because two or more equals signs were found!", name) );
    	}
    	if( name.endsWith("=") )	{
    		throw new MisformatedInputException( String.format("The tagName %s is invalid because its value is empty!", name) );
    	}
    	//split name at the equals sign and return array
    	String ret[] = name.split("=");
    	
    	if(ret.length == 2)	{
    		
    	}
    	return ret;
    }

    private static int countOccurrencesInString(String str, char c)	{
    	int occurrences = 0;
    	for(int i = 0; i < str.length(); i++)	{
    		if( c == str.charAt(i) )	{
    			occurrences++;
    		}
    	}
    	return occurrences;
    }
    
    static class XmlNode extends XmlTree {

        //data structure to store children
        public XmlNode(StringReader sr, String outer) throws IOException, MisformatedInputException, AlreadyAvailable    {
            
        	//init HashMap for child maps
        	this.childNodes = new HashMap<String, XmlTree>();
        	//get current node
        	String[] currentNode = getNode(sr);
        	String openingTag = outer;
        	String closingTag = "";
        	while( currentNode.length > 0)	{
        		//don't add a new element if this is the end node
        		if( currentNode[0].charAt(0) == '/' )	{
        			closingTag = currentNode[0].substring(1);
        			//its the end of the element so we need to jump one level back up
        			break; //in this particular case, using break makes the code clearer and easier to understand than working with flags.
        		} else	{
        			//check if the node is a duplicate
        			if( this.childNodes.get(currentNode[0]) != null )	{
        				throw new AlreadyAvailable(this, currentNode[0]);
        			}
        			//check if this is a value-node or a node-node
            		if(currentNode.length == 2)	{
                		this.childNodes.put(currentNode[0], new XmlValue(currentNode[1]) );
                	} else if(currentNode.length == 1)	{
                		this.childNodes.put(currentNode[0], new XmlNode(sr, currentNode[0]) );
                	}
        		}
        	
        		//get next node
        		currentNode = getNode(sr);
        	}
        	//check if opening tag matches closing tag
    		if(!openingTag.equals(closingTag))	{
    			throw new MisformatedInputException( String.format("Opening tag <%s> mismatches closing tag </%s>",openingTag,closingTag) );
    		}
        	
        }
        
        @Override
        public String getValue() throws NoValueException	{
        	throw new NoValueException(this);
        }

		@Override
		protected String toString(String name, String indent) {
			StringBuilder b = new StringBuilder();
			b.append( String.format("%s<%s>\n",indent, name) );
			for (Map.Entry<String, XmlTree> entry : this.childNodes.entrySet())	{
				b.append( entry.getValue().toString(entry.getKey(), indent + "\t") );
			}
			b.append( String.format("%s</%s>\n",indent, name) );
			return b.toString();
		}
    }

    static class XmlValue extends XmlTree {
        
    	private String value;

        public XmlValue(String value) {
        	//set properties
        	this.value = value;
        }

        @Override
        public XmlTree getNode(String name) throws NoNodeException	{
        	throw new NoNodeException(this, name);
        }
        
        @Override
        public String getValue()	{
        	return this.value;
        }
        
		@Override
		protected String toString(String name, String indent) {
			//just return a single tag, because XmlValue must have no child nodes
			return String.format("%s<%s=%s>\n", indent, name, this.value);
		}
    }

    protected abstract String toString(String name, String indent);

    public String toString() {
        return toString("xmllight", "");
    }

    // Parse a simple file.
    public static XmlTree load(StringReader sr) throws IOException, MisformatedInputException, AlreadyAvailable
    {
        // Check outermost node.
        String[] outer = getNode(sr);

        if (outer.length != 1 || !outer[0].equals("xmllight")) {
            throw new MisformatedInputException("expected  <xmllight>, found </" + outer + ">");
        }

        return new XmlNode(sr, "xmllight");
    }

    public void safe(StringWriter sw) // Write tree to stream.
    {
        sw.write(toString());
    }
}

