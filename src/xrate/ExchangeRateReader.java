package xrate;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * Provide access to basic currency exchange rate services.
 *
 * @author lab-6b-shawn-brain
 * Shawn Saliyev and Brian Caravantes
 */
public class ExchangeRateReader {

	//will be assigned when constructor is called.
	String baseUrl;
	
	/**
	 * Construct an exchange rate reader using the given base URL. All requests
	 * will then be relative to that URL. If, for example, your source is Xavier
	 * Finance, the base URL is http://api.finance.xaviermedia.com/api/ Rates
	 * for specific days will be constructed from that URL by appending the
	 * year, month, and day; the URL for 25 June 2010, for example, would be
	 * http://api.finance.xaviermedia.com/api/2010/06/25.xml
	 *
	 * @param baseURL
	 *            the base URL for requests
	 * @throws IOException 
	 */
	public ExchangeRateReader(String baseURL) throws IOException {
		this.baseUrl = baseURL; 
	}

	/**
	 * Get the exchange rate for the specified currency against the base
	 * currency (the Euro) on the specified date.
	 *
	 * @param currencyCode
	 *            the currency code for the desired currency
	 * @param year
	 *            the year as a four digit integer
	 * @param month
	 *            the month as an integer (1=Jan, 12=Dec)
	 * @param day
	 *            the day of the month as an integer
	 * @return the desired exchange rate
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public float getExchangeRate(String currencyCode, int year, int month, int day) {
		float result;
		try{
			String url = urlBuilder(year, month, day);
			XML_Parser parser  = new XML_Parser();
			result = parser.processDocument(url, currencyCode);
		}catch (UnsupportedOperationException | IOException | ParserConfigurationException | SAXException e){
			throw new UnsupportedOperationException();
		}		
		return result;
	}

	/**
	 * Get the exchange rate of the first specified currency against the second
	 * on the specified date.
	 *
	 * @param currencyCode
	 *            the currency code for the desired currency
	 * @param year
	 *            the year as a four digit integer
	 * @param month
	 *            the month as an integer (1=Jan, 12=Dec)
	 * @param day
	 *            the day of the month as an integer
	 * @return the desired exchange rate
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public float getExchangeRate(String fromCurrency, String toCurrency, int year, int month, int day) { 
		float resultOne;
		float resultTwo;
		float result;
		try{
			String url = urlBuilder(year, month, day);
			XML_Parser parser  = new XML_Parser();
			resultOne = parser.processDocument(url, fromCurrency);
			resultTwo = parser.processDocument(url, toCurrency);
			result = (resultOne/resultTwo); //Calculating the exchange rate
		}catch (UnsupportedOperationException | IOException | ParserConfigurationException | SAXException e){
			throw new UnsupportedOperationException();
		}		
		return result;
	}

	
	/**
	 * Builds the url based on date.
	 */
	public String urlBuilder(int year, int month, int day){
		StringBuilder url = new StringBuilder(baseUrl);
		url.append(year);
		url.append("/");
		if(month<10)url.append('0');//if month is one digit we pad zero to url
		url.append(month);
		url.append("/");
		if(day<10)url.append('0');//if day is one digit we pad zero to url
		url.append(day);
		url.append(".xml");
		return url.toString();
	}
}
