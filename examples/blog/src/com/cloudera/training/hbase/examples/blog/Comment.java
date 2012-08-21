package com.cloudera.training.hbase.examples.blog;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

public class Comment {
	public static void saveComment(String blogid, String author, String body)
			throws IOException {

		Configuration conf = HBaseConfiguration.create();
		HTable table = new HTable(conf, "blogs");

		// 1. Create a Put object with key: the blogid (from above) + a
		// comma + the current timestamp
		Put newrow = new Put(Bytes.toBytes(blogid + ","
				+ System.currentTimeMillis()));

		// 2. Add the value of author to column "comment:comment_author" and
		// the value of body to the column "comment:comment_body"

		newrow.add(Bytes.toBytes("comment"), Bytes.toBytes("comment_author"),
				Bytes.toBytes(author));
		newrow.add(Bytes.toBytes("comment"), Bytes.toBytes("comment_body"),
				Bytes.toBytes(body));
		table.put(newrow);

	}
}
