package xrate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//Based on this example: https://gist.github.com/NicMcPhee/7131454

public class XML_Parser {

	/**
	 * This method is called in ExchangeRateReader class to provide the rate value 
	 */
	public float processDocument(String urlString, String currencyCode) throws IOException, ParserConfigurationException, SAXException {
		URL url = new URL(urlString);
		InputStream xmlStream = url.openStream();
		Document doc = createDocument(xmlStream);
		return printRateInfo(doc, currencyCode);
	}

	/**
	 *  This method checks for the right currency and calls getRate() to get the rate of that certain currency
     */
	private float printRateInfo(Document doc, String currencyCode) {
		NodeList nodeList = doc.getElementsByTagName("fx"); //get the xml element "fx" for base currency
		float result = 0;
		String resultToConvert = "";
		Node node;
		for (int i = 0; i < nodeList.getLength(); i++) { //The loop traverses through these xml elements which are represented as nodes 
			node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element baseCurrency = (Element) node;
				NodeList elements = baseCurrency.getElementsByTagName("currency_code");
				Element firstElement = (Element) elements.item(0);
				NodeList children = firstElement.getChildNodes();
				String componentText = children.item(0).getNodeValue();
				if(componentText.compareTo(currencyCode)==0){  //Once we find the element of the currency which we are looking for we call getRate()
					resultToConvert = getRate(baseCurrency, "rate");	
				}
			}
		}
		result = new Float(resultToConvert);
		return result;
	}


	/**
	 * This method gets the rate of a the currency which we are looking for against base currency
	 */
	private String getRate(Element baseCurrency, final String component) {
		NodeList elements = baseCurrency.getElementsByTagName(component);
		Element firstElement = (Element) elements.item(0);
		NodeList children = firstElement.getChildNodes();
		String componentText = children.item(0).getNodeValue();
		return componentText;
	}
	

	/**
	 * This method creates a Document based the provided InputStream 
	 */
	private Document createDocument(InputStream in) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(in);
		doc.getDocumentElement().normalize();
		return doc;
	}
}