import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class MyHandler extends DefaultHandler {
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("address")) {
            //add Employee object to list
            System.err.println("uri: " + uri);
            System.err.println("localName: " + localName);
            System.err.println("qName: " + qName);
        }
    }
}

public class Main {
	public static void main(String[] args) throws Exception {
	    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
	    try {
	        SAXParser saxParser = saxParserFactory.newSAXParser();
	        if (saxParser.isValidating()) {
	        	System.err.println("It Validates!");
	        }
	        MyHandler handler = new MyHandler();
	        saxParser.parse(System.in, handler);
	    } catch (ParserConfigurationException | SAXException | IOException e) {
	        e.printStackTrace();
	    }

	}

	// This doesnt work because unmarshaller.unmarshal will block forever.
	// It expects "end of stream" (EOS) before it returns.
	// It will also close the stream, which would close our socket...
	private static void jaxbWayThatDoesntWork() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Address.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		Address address = new Address();
		address.setCity("st louis");
		address.setStreet("foo street");
		marshaller.marshal(address, System.out);

		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		Source source = new StreamSource(System.in);
		while (true) {
			address = (Address) unmarshaller.unmarshal(source, Address.class).getValue();
			System.out.println("Address street: " + address.getStreet());
		}
	}
}

// <?xml version="1.0" encoding="UTF-8" standalone="yes"?><address><street>foo street</street><city>st louis</city></address>
