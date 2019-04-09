package Test;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.File;
import java.io.IOException;

/**
 * Created by behnam on 1/14/18.
 */
public class MRDriver {
    public static void main(String[] arguments) throws IOException {

        String[] args = new String[2];
        args[0] = "data/db2.xml";
        args[1] = "data/output-test";

        File output = new File(args[1]);
        if (output.exists())
            FileUtils.forceDelete(output);


        Configuration conf = new Configuration();
        String[] arg = new GenericOptionsParser(conf, args).getRemainingArgs();

        Job job = new Job(conf, "XML Processing Processing");
        //Job job = Job.getInstance(conf, "XML Processing Processing");

        job.setJarByClass(MRDriver.class);
        job.setMapperClass(MRMapper.class);
        job.setNumReduceTasks(0);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        job.setInputFormatClass(MRInputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        try {
            job.waitForCompletion(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
