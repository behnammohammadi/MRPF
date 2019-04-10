import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
//import mrdp.logging.LogWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MyMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

	private static final Log LOG = LogFactory.getLog(MyMapper.class);

	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		System.out.println("*-*-*-*-*-*- Start Map Task *-*-*-*-*-*-");
		System.out.println(value);

		try {
			Configuration conf=context.getConfiguration();
			String accessControlList = conf.get("accessControlList");
			String xpathQuery = conf.get("xpathQuery");

/*
			boolean access=AccessControlXmlProvider.CheckAccess(accessControlList,xpathQuery);
			if(access==false)
				return;
*/
			InputStream is = new ByteArrayInputStream(value.toString()
					.getBytes());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);

			doc.getDocumentElement().normalize();

			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();

			XPathExpression expr = xpath.compile(xpathQuery);
			NodeList nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					String res=eElement.getTextContent();
					//System.out.println("Result: \n" + res);
					context.write(new Text(res),NullWritable.get());
				}
			}
		} catch (Exception e) {
			 System.out.println("Error: " +e.getMessage());

		}
		finally {
			System.out.println("*-*-*-*-*-*- End Map Task *-*-*-*-*-*-");
		}
	}


}