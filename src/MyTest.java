import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
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

/**
 * Created by behnam on 4/18/19.
 */
public class MyTest {

    public static void main(String[] arguments) throws Exception
    {
      /*  try {
            System.out.println(CheckAccess(Utilities.ReadFileAsString("data/AC.data"),"A/D[1]/G"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/



            /*Collections.sort(accessList, new Comparator<>() {
                public int compare(AccessControlDto o1, AccessControlDto o2) {
                    if (o1.getPath().length() == o2.getPath().length())
                        return 0;
                    return o1.getPath().length() < o2.getPath().length() ? -1 : 1;
                }
            });*/


        InputStream is = new ByteArrayInputStream(Utilities.ReadFileAsString("data/dcam.xml").toString()
                .getBytes());
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(is);

        doc.getDocumentElement().normalize();

        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();


        //NodeList nList = (NodeList) xpath.compile("A").evaluate(doc, XPathConstants.NODESET);
        Object nLists =  xpath.compile("A").evaluate(doc, XPathConstants.);
        NodeList nList = (NodeList) xpath.compile("A").evaluate(doc, XPathConstants.NODESET);


        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);
            // System.out.println("==> "+nNode.getPreviousSibling().gef);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;

                String res=eElement.getTextContent();
                System.out.println("Result: \n" + res);
        }
    }
}}
