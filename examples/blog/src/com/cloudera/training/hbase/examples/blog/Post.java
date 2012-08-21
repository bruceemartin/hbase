package com.cloudera.training.hbase.examples.blog;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

public class Post {
	public static void savePost(String newPostTitle, String newPostBody)
			throws IOException {

		Configuration conf = HBaseConfiguration.create();
		HTable table = new HTable(conf, "blogs");

		// 1. Create a Put object with a key that looks like
		// "123,9223370761422928544"
		// 123 represents the blogger id
		// The other value is a reverse timestamp, which should be computed
		// using System.currentTimeMillis()
		Put newrow = new Put(Bytes.toBytes("123,"
				+ (Long.MAX_VALUE - System.currentTimeMillis())));

		// 2. Add the value of newPostTitle to column "meta:title" and
		// the value of newPostBody to the column "body:"
		newrow.add(Bytes.toBytes("meta"), Bytes.toBytes("title"),
				Bytes.toBytes(newPostTitle));
		newrow.add(Bytes.toBytes("body"), Bytes.toBytes(""),
				Bytes.toBytes(newPostBody));
		table.put(newrow);

	}
}
