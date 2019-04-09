import java.io.File;

//import mrdp.logging.LogWriter;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.commons.io.FileUtils;

public class XMLDriver {
	private static final String dataDatabase ="data/full-Shakespeare.xml";
	private static final String accessControlDatabase="data/db2.xml";
	private static final String query="/PLAYS/PLAY[@name='2']/TITLE";
	private static final String outputPath="data/output";

	public static void main(String[] arguments) throws Exception {

		String[] args=new String[2];
		args[0] = dataDatabase;
		args[1] = outputPath;

		File output = new File(args[1]);
		if (output.exists())
			FileUtils.forceDelete(output);

		try {
			Configuration conf = new Configuration();
			String[] arg = new GenericOptionsParser(conf, args)
					.getRemainingArgs();

			String accessControlList=Utilities.ReadFileAsString(accessControlDatabase);
			conf.set("accessControlList",accessControlList);
			conf.set("xpathQuery",query);
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