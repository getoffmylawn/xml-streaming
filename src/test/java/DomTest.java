import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class DomTest {
	
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

	private DocumentBuilderFactory builderFactory;
	private DocumentBuilder builder;
	private XPathFactory xpathFactory;
	
	@Before
	public void setUp() throws Exception {
		builderFactory = DocumentBuilderFactory.newInstance();
		builder = builderFactory.newDocumentBuilder();
		xpathFactory = XPathFactory.newInstance();
	}

	@Test
	public void testDom() throws Exception {
		String expression = "/employees/employee[@emplid='3333']/email"; // also works with /text()
		// Do I need a new XPath object for every XPathExpression or can I reuse it?
		XPathExpression xPath = xpathFactory.newXPath().compile(expression);

		Document document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
		String email = xPath.evaluate(document);
		
		assertEquals("jim@sh.com", email);
	}

	@Test
	public void testDomNode() throws Exception {
		String expression = "/employees/employee[@emplid='3333']/email/text()";
		XPathExpression xPath = xpathFactory.newXPath().compile(expression);
		Document document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
		
		Node node = (Node) xPath.evaluate(document, XPathConstants.NODE);

		assertEquals("jim@sh.com", node.getNodeValue());
	}
	
	@Test
	public void testContextual () throws Exception {
		String expression = "/employees/employee[@emplid='3333']";
		XPathExpression xPath = xpathFactory.newXPath().compile(expression);
		Document document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
		
		Node employeeNode = (Node) xPath.evaluate(document, XPathConstants.NODE);

		String innerExperssion = "email/text()";
		XPathExpression innerXpath = xpathFactory.newXPath().compile(innerExperssion);
		Node emailNode = (Node) innerXpath.evaluate(employeeNode, XPathConstants.NODE);
		
		assertEquals("jim@sh.com", emailNode.getNodeValue());
	}

	@Test
	@Ignore
	public void testDomPerformance() throws Exception {
		String expression = "/employees/employee[@emplid='3333']/email";
		XPathExpression xPath = xpathFactory.newXPath().compile(expression);

		// completed 10000 in 2 seconds so 5 parses per millisecond on my laptop, pretty good
		for (int ii = 0; ii < 10000; ii++) {
			Document document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
			xPath.evaluate(document);
		}
	}
}
