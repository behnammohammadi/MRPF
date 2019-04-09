package Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
//import mrdp.logging.LogWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by behnam on 1/14/18.
 */
public class MRMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        System.out.println("@@@@@@@Start Map Task.@@@@@@@@");
		context.write(new Text("455"),NullWritable.get());
    }
}
