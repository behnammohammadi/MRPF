import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class AccessControlProvider {
    public static boolean CheckAccess(String data,String xpathString) throws  Exception
    {
        if(xpathString.equals(""))
            return  false;

            InputStream is = new ByteArrayInputStream(data.getBytes());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            doc.getDocumentElement().normalize();

            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();


            XPathExpression expr = xpath.compile(xpathString);
            NodeList nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

            if(nList.getLength()==0)
            {
                String newXpathString =xpathString.substring(0,xpathString.lastIndexOf('/'));
                return CheckAccess(data,newXpathString);
            }
            else
            {
                    Node nNode = nList.item(0);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                        Element eElement = (Element) nNode;

                       // String res=eElement.getTextContent();
                        String res=eElement.getAttribute("authorize");
                        if(res.equals(""))
                        {
                            String newXpathString =xpathString.substring(0,xpathString.lastIndexOf('/'));
                            return CheckAccess(data,newXpathString);
                        }
                        if(res.equals("yes"))// if true (allow access)
                            return  true;
                        else// if false (deny access)
                            return  false;
                    }
            }

        return false;
    }
}
