package Test;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DataOutputBuffer;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

import java.io.IOException;

public class MRInputFormat extends TextInputFormat {
	public static final String START_TAG_KEY = "<employee>";
	public static final String END_TAG_KEY = "</employee>";

	@Override
	public RecordReader<LongWritable, Text> createRecordReader(
			InputSplit split, TaskAttemptContext context) {
		return new XmlRecordReader();
	}

	public static class XmlRecordReader extends
			RecordReader<LongWritable, Text> {
		private byte[] startTag;
		private byte[] endTag;
		private long start;
		private long end;
		private FSDataInputStream fsin;
		private DataOutputBuffer buffer = new DataOutputBuffer();
		private LongWritable key = new LongWritable();
		private Text value = new Text();

		@Override
		public void initialize(InputSplit is, TaskAttemptContext tac)
				throws IOException, InterruptedException {
			System.out.println("@@@@@@@Start XML INPUT.@@@@@@@@");
			FileSplit fileSplit = (FileSplit) is;
			//String START_TAG_KEY = "<employee>";
			//String END_TAG_KEY = "</employee>";
			startTag = START_TAG_KEY.getBytes("utf-8");
			endTag = END_TAG_KEY.getBytes("utf-8");

			start = fileSplit.getStart();
			end = start + fileSplit.getLength();
			Path file = fileSplit.getPath();

			FileSystem fs = file.getFileSystem(tac.getConfiguration());
			fsin = fs.open(fileSplit.getPath());
			fsin.seek(start);
			System.out.println("@@@@@@@End XML INPUT.@@@@@@@@");
		}

		@Override
		public boolean nextKeyValue() throws IOException, InterruptedException {
			long fsinPos=fsin.getPos();

			if (fsinPos < end) {
				if (readUntilMatch(startTag, false)) {
					try {
						buffer.write(startTag);
						if (readUntilMatch(endTag, true)) {

							value.set(buffer.getData(), 0, buffer.getLength());
							key.set(fsin.getPos());
							return true;
						}
					} finally {
						buffer.reset();
					}
				}
			}
			return false;
		}

		@Override
		public LongWritable getCurrentKey() throws IOException,
				InterruptedException {
			return key;
		}

		@Override
		public Text getCurrentValue() throws IOException, InterruptedException {
			return value;

		}

		@Override
		public float getProgress() throws IOException, InterruptedException {
			return (fsin.getPos() - start) / (float) (end - start);
		}

		@Override
		public void close() throws IOException {
			fsin.close();
		}

		private boolean readUntilMatch(byte[] match, boolean withinBlock)
				throws IOException {
			int i = 0;
			while (true) {
				int b = fsin.read();

				if (b == -1)
					return false;

				if (withinBlock)
					buffer.write(b);

				if (b == match[i]) {
					i++;
					if (i >= match.length)
						return true;
				} else
					i = 0;

				if (!withinBlock && i == 0 && fsin.getPos() >= end)
					return false;
			}
		}

	}

}