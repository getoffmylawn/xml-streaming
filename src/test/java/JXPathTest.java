import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.jxpath.JXPathContext;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


public class JXPathTest {

	String xml = "<employees>"
			+ "     <employee emplid='1111' type='admin'>"
			+ "         <firstname>John</firstname>"
			+ "         <lastname>Watson</lastname>"
			+ "         <age>30</age>"
			+ "         <email>johnwatson@sh.com</email>"
			+ "     </employee>"
			+ "     <employee emplid='2222' type='admin'>"
			+ "         <firstname>Sherlock</firstname>"
			+ "         <lastname>Homes</lastname>"
			+ "         <age>32</age>"
			+ "         <email>sherlock@sh.com</email>"
			+ "     </employee>"
			+ "     <employee emplid='3333' type='user'>"
			+ "         <firstname>Jim</firstname>"
			+ "         <lastname>Moriarty</lastname>"
			+ "         <age>52</age>"
			+ "         <email>jim@sh.com</email>"
			+ "     </employee>"
			+ "     <employee emplid='4444' type='user'>"
			+ "         <firstname>Mycroft</firstname>"
			+ "         <lastname>Holmes</lastname>"
			+ "         <age>41</age>"
			+ "         <email>mycroft@sh.com</email>"
			+ "     </employee>"
			+ " </employees>";

	@Test
	public void test() throws Exception {
		InputStream stream = new ByteArrayInputStream(xml.getBytes());
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		JXPathContext jpc = JXPathContext.newContext(builder.parse(stream));

		String expression = "/employees/employee[@emplid='3333']/email/text()";
		Node node = (Node) jpc.selectSingleNode(expression);
		assertEquals("jim@sh.com", node.getNodeValue());
	}

	@Test
	public void testContextual () throws Exception {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		String expression = "/employees/employee[@emplid='3333']";
		Document document = builder.parse(new ByteArrayInputStream(xml.getBytes()));

		JXPathContext jpc = JXPathContext.newContext(document);
		Node node = (Node) jpc.selectSingleNode(expression);

		String innerExperssion = "email/text()";
		jpc = JXPathContext.newContext(node);
		Node emailNode = (Node) jpc.selectSingleNode(innerExperssion);

		assertEquals("jim@sh.com", emailNode.getNodeValue());
	}

	@Test
	public void testPerformance() throws Exception {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

		// completed 10000 in ~1.3 seconds, about as fast as VTD! JXPath is much faster than jdk xpath
		String expression = "/employees/employee[@emplid='3333']/email/text()";
		for (int ii = 0; ii < 10000; ++ii) {
			InputStream stream = new ByteArrayInputStream(xml.getBytes());
			Document doc = builder.parse(stream);
			JXPathContext jpc = JXPathContext.newContext(doc);
			Node node = (Node) jpc.selectSingleNode(expression);
			node.getNodeValue();
			stream.close();
		}
	}

}
