package sheet10;

import java.io.*;
import java.util.ArrayList;

/**
 * When I started writing this class, I had no information that the tags on each level are unique... so this class supports also duplicate tags. 
 * 
 * 
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

	protected ArrayList<XmlTree> childNodes;
	
	protected String tagName;
	
	public String getTagName()	{
		return this.tagName;
	}
	
    // Default implementations (node has no value and value has no nodes):
    public String getValue() throws NoValueException {
    	//method is overwritten by child class and outer xmllight has no value
    	throw new NoValueException(this);
    }
    
    /**
     * Returns the first XmlNode matching the given tag name
     * @param name The tag name
     * @throws NotAvailable 
     */
    public XmlTree getNode(String name) throws NotAvailable, NoNodeException	{
    	for(XmlTree tree : this.childNodes)	{
    		if( tree.getTagName().equals(name) )	{
    			return tree;
    		}
		}
    	throw new NotAvailable(this, name);
    }
    
    /**
     * Returns a list with all nodes matching the tag
     * 
     * @param name The tag name
     * @return An ArrayList with nodes matching the given name
     * @throws NotAvailable
     */
    public ArrayList<XmlTree> getNodes(String name) throws NotAvailable, NoNodeException	{
    	ArrayList<XmlTree> nodes = new ArrayList<XmlTree>();
    	for(XmlTree tree : this.childNodes)	{
    		if( tree.getTagName().equals(name) )	{
    			nodes.add(tree);
    		}
		}
    	//if no node was found 
    	if(nodes.size() == 0)	{
    		throw new NotAvailable(this, name);
    	}
    	return nodes;
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
    	int data = sr.read();
    	if( data == -1 || (char)data != character)	{
    		throw new MisformatedInputException(sr.toString());
    	}
    }

    // Get chars until character is encountered; character is excluded from result.
    private static String getUntil(StringReader sr, char character) throws IOException, MisformatedInputException {
		
    	int data;
    	boolean read = true;
    	StringBuilder ret = new StringBuilder();
    	while( (data = getNextCharacter(sr)) != -1 && read)	{
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
		
    	StringBuilder name = null;
    	StringBuilder value = null;
    	
    	boolean nameComplete = false;
    	boolean readString = true;
    	
    	
    	
    	int data = 0;
    	while( readString && (data = getNextCharacter(sr)) != -1)	{
    		
			//found opening char '<'
			if( data == 60)	{
				name = new StringBuilder();
			} else if( data == 61 )	{ //found '='
				//tag name reading complete
				nameComplete = true;
				//init value builder
				value = new StringBuilder();
			} else if( data == 62)	{ // found >
				//tag name reading complete
				nameComplete = true;
				readString=false;
			} else	{
				if(!(name instanceof StringBuilder))	{
					throw new MisformatedInputException( String.valueOf( (char)data ) );
				}
    			//build name and value
    			if(!nameComplete)	{
    				name.append( (char)data );
    			} else	{
    				if(value instanceof StringBuilder)	{
    					value.append( (char)data );
    				}
    			}
			}
		}
    	
    	String[] ret;
    	if(data == -1)	{
    		//return empty array
    		ret = new String[0];
    		return ret;
    	}
    	
    	//sr.close();
		
		//something went really wrong
		if(!nameComplete && data != -1)	{
			throw new MisformatedInputException( name.toString() );
		}
		
		//build return array
		
		if(value instanceof StringBuilder)	{
			ret = new String[2];
			ret[0] = name.toString();
			ret[1] = value.toString();
		} else	{
			ret = new String[1];
			ret[0] = name.toString();
		}
		return ret;
		
    }

    static class XmlNode extends XmlTree {

        //data structure to store children
        public XmlNode(StringReader sr, String outer) throws IOException, MisformatedInputException, AlreadyAvailable    {
            
        	//set tag name
        	this.tagName = outer;
        	//init HashMap for child maps
        	this.childNodes = new ArrayList<XmlTree>();
        	//get current node
        	String[] currentNode = getNode(sr);
        	while( currentNode.length > 0)	{
        		//don't add a new element if this is the end node
        		if( currentNode[0].charAt(0) == '/' )	{
        			//its the end of the element so we need to jump one level back up
        			break; //in this particular case using break makes the code clearer and easier to understand than working with flags.
        		} else	{
        			//check if this is a value-node or a node-node
            		if(currentNode.length == 2)	{
                		this.childNodes.add( new XmlValue(currentNode[1], currentNode[0]) );
                	} else if(currentNode.length == 1)	{
                		this.childNodes.add( new XmlNode(sr, currentNode[0]) );
                	}
        		}
        		//get next node
        		currentNode = getNode(sr);
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
			for(XmlTree tree : this.childNodes)	{
				b.append( tree.toString(tree.getTagName(), indent + "\t") );
			}
			b.append( String.format("%s</%s>\n",indent, name) );
			return b.toString();
		}
    }

    static class XmlValue extends XmlTree {
        
    	private String value;

		//I've had to modify the constructor slightly to get a consistent data structure (tagName)
        public XmlValue(String value, String tagName) {
        	//set properties
        	this.value = value;
        	this.tagName = tagName;
        }

        @Override
        public XmlTree getNode(String name) throws NoNodeException	{
        	throw new NoNodeException(this, name);
        }
        
        @Override
        public ArrayList<XmlTree> getNodes(String name) throws NoNodeException	{
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

