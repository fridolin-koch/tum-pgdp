package sheet10;

import java.io.*;

abstract class XmlTree {

    // Exceptions that might arise during tree load/access.
    public static class XmlException extends Exception {

        public String item;

        public XmlException(String item) {
            this.item = item;
        }

        public String toString() {
            return this.getClass().getName() + " : " + item;
        }
    }

    public static class MisformatedInputException extends XmlException {
        public MisformatedInputException(String item) {
            super(item);
        }
    }

    public static class NoValueException extends XmlException {
        public XmlTree node;

        public NoValueException(XmlTree node) {
            super("node is not a value : " + node.toString());
            this.node = node;
        }
    }

    public static class NoNodeException extends XmlException {
        public XmlTree node;

        public NoNodeException(XmlTree node, String item) {
            super("'" + item + "' does not exist inside " + node.toString());
            this.node = node;
        }
    }

    public static class NotAvailable extends XmlException {
        public XmlTree node;

        public NotAvailable(XmlTree node, String item) {
            super("'" + item + "' does not exist inside " + node.toString());
            this.node = node;
        }
    }

	public static class AlreadyAvailable extends XmlException {
        public XmlTree node;

        public AlreadyAvailable(XmlTree node, String item) {
            super("'" + item + "' already exist inside " + node.toString());
            this.node = node;
        }
    }

    // Default implementations (node has no value and value has no nodes):
    public String getValue() throws NoValueException {
        //FILL IN
    	return "";
    }
    public XmlTree getNode(String name) throws NoNodeException, NotAvailable {
        //FILL IN
    	return null;
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
    	
    	int data;
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
			} else if(data == 47)	{
				//ignore /
			} else	{
				if(!(name instanceof StringBuilder))	{
					throw new MisformatedInputException(sr.toString());
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
    	//sr.close();
		
		//something went really wrong
		if(!nameComplete)	{
			throw new MisformatedInputException(sr.toString());
		}
		
		//build return array
		String[] ret;
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
    	
    	private XmlTree node;
    	
    	private String tagName;
    	
        //data structure to store children
        public XmlNode(StringReader sr, String outer) throws IOException, MisformatedInputException, AlreadyAvailable    {
            //FILL IN
            //read this and children from sr
        	String[] currentNode = getNode(sr);
        	this.tagName = outer;
        	if(currentNode.length == 2)	{
        		this.node = new XmlValue(currentNode[1]);
        	} else if(currentNode.length == 1)	{
        		this.node = new XmlNode(sr, currentNode[0]);
        	}
        }
        
        public XmlTree getNode()	{
        	return this.node;
        }
        
        public String getValue()	{
        	return "";
        }

		@Override
		protected String toString(String name, String indent) {
			// TODO Auto-generated method stub
			return null;
		}
    }

    static class XmlValue extends XmlTree {
        
    	private String value;

		//FILL IN
        //data structure to store value
        public XmlValue(String value) {
            //FILL IN
            //init value
        	this.value = value;
        }

        public XmlNode getNode()	{
			return null;
        	//throw new NoNodeException(node, item)
        }
        
        public String getValue()	{
        	return this.value;
        }
        
		@Override
		protected String toString(String name, String indent) {
			// TODO Auto-generated method stub
			return null;
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

        /*
         * names/values must not contain '>'
         *
         *<xmllight> //root, must be there
         *    <node_name1> //node must not have values
         *        <value_name1=value> //values must not have children
         *        <value_name2=value>
         *    </nodename1>
         *    <node_name2>
         *        <value_name1=value>
         *        <value_name2=value>
         *    </nodename2>
         *  <value_name1=value>
         *</xmllight>
         */
    }

    public void safe(StringWriter sw) // Write tree to stream.
    {
        sw.write(toString());
    }
}

