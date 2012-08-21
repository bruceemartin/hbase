package com.cloudera.training.hbase.examples.blog;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.NavigableMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class UserBlogs {
	private String key = null;
	private Date postDate = null;
	private String title = null;
	ResultScanner scanner = null;
	private Iterator<Result> resultIterator = null;

	public String nextKey() {
		return key;
	}

	public Date nextPostDate() {
		return postDate;
	}

	public String nextTitle() {
		return title;
	}

	public boolean hasNext() {
		if ((resultIterator == null) || (!resultIterator.hasNext())) {
			if (scanner != null) {
				scanner.close();
			}
			return false;
		} else {
			Result row = resultIterator.next();
			NavigableMap<byte[], byte[]> map = row.getFamilyMap(Bytes
					.toBytes("meta"));
			title = Bytes.toString(map.get(Bytes.toBytes("title")));
			key = Bytes.toString(row.getRow());
			Long reverseTimestamp = Long.parseLong(key.substring(4));
			Long epoch = Math.abs(reverseTimestamp - Long.MAX_VALUE);
			postDate = new Date(epoch);
			return true;
		}
	}

	public UserBlogs() throws IOException {
		ResultScanner scanner = null;
		// 1. Create a Configuration
		Configuration conf = HBaseConfiguration.create();

		// 2. Instantiate the HTable for the table "blogs" using conf
		HTable table = new HTable(conf, "blogs");
		Scan s = new Scan();

		// 3. use s.addFamily to retreive the "meta" column family.
		// // Hint: use the HBase utility method Bytes.toBytes to convert a
		// String to byte[]
		s.addFamily(Bytes.toBytes("meta"));

		// 4. Get the scanner
		scanner = table.getScanner(s);
		resultIterator = scanner.iterator();
	}
}