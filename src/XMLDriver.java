import java.io.File;
import java.io.IOException;

import javax.xml.stream.XMLInputFactory;
//import mrdp.logging.LogWriter;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.db.BooleanSplitter;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.commons.io.FileUtils;

public class XMLDriver {
	public static void main(String[] arguments) throws Exception {

/*
        String xmlData=Utilities.ReadFileAsString("data/db2.xml");
		boolean res=AccessControl.CheckAccess(xmlData,"/Data/employee/children/employee/name");
        System.out.println(res);
*/



		String[] args=new String[2];
		args[0] = "data/full-Shakespeare.xml";
		args[1] = "data/output";

		File output = new File(args[1]);
		if (output.exists())
			FileUtils.forceDelete(output);

		try {
			Configuration conf = new Configuration();
			String[] arg = new GenericOptionsParser(conf, args)
					.getRemainingArgs();

			String accessControlList = "behnam";

			conf.set("START_TAG_KEY", "<PLAY");
			conf.set("END_TAG_KEY", "</PLAY>");

			conf.set("accessControlList",accessControlList);
			//Job job = new Job(conf, "XML Processing Processing");
			Job job = Job.getInstance(conf, "XML Processing Processing");



			job.setJarByClass(XMLDriver.class);
			job.setMapperClass(MyMapper.class);
			job.setNumReduceTasks(0);
			job.setInputFormatClass(XMLInputFormat.class);
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(LongWritable.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(LongWritable.class);
			FileInputFormat.addInputPath(job, new Path(args[0]));
			FileOutputFormat.setOutputPath(job, new Path(args[1]));
			job.waitForCompletion(true);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage().toString());
		}
	}

}