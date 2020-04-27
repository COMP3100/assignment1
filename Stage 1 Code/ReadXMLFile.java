import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.*;

public class ReadXMLFile {
	
	public static List<Server> parseSystemXML(String filePath) {
		List<Server> serverList = new ArrayList<>();
		File systemXML = new File(filePath);
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc;
			doc = dBuilder.parse(systemXML);
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("server");
			for (int count = 0; count < nList.getLength(); count++) {
				Node nNode = nList.item(count);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) nNode;
					
					serverList.add(new Server(e.getAttribute("type"), Integer.parseInt(e.getAttribute("limit")), 
							Integer.parseInt(e.getAttribute("bootupTime")), Double.parseDouble(e.getAttribute("rate")), 
							Integer.parseInt(e.getAttribute("coreCount")), Integer.parseInt(e.getAttribute("memory")), 
							Integer.parseInt(e.getAttribute("disk"))));

				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return serverList;
	}
}
