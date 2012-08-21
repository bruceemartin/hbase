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
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;

public class Blog {
	public String keyStr;
	public String blogText;
	public String blogTitle;
	public Date dateOfPost;

	private HTable table;
	private String commentAuthor = null;
	private Date dateOfComment = null;
	private String commentBody = null;
	private ResultScanner scanner = null;
	private Iterator<Result> resultIterator = null;

	private byte[] key;

	public Blog(String blogid) throws IOException {

		Configuration conf = HBaseConfiguration.create();
		table = new HTable(conf, "blogs");

		// 1. Get the row whose row key is blogid from above
		Get g = new Get(Bytes.toBytes(blogid));
		Result r = table.get(g);

		// 2. Extract the rowkey, blog text (column "body") and blog title
		// (column "meta:title")
		key = r.getRow();
		keyStr = Bytes.toString(key);
		blogText = Bytes.toString(r.getValue(Bytes.toBytes("body"),
				Bytes.toBytes("")));
		blogTitle = Bytes.toString(r.getValue(Bytes.toBytes("meta"),
				Bytes.toBytes("title")));
		Long reverseTimestamp = Long.parseLong(keyStr.substring(4));
		Long epoch = Math.abs(reverseTimestamp - Long.MAX_VALUE);
		dateOfPost = new Date(epoch);

		// Get an iterator for the comments
		Scan s = new Scan();
		s.addFamily(Bytes.toBytes("comment"));
		// Use a PrefixFilter
		PrefixFilter filter = new PrefixFilter(key);
		s.setFilter(filter);
		scanner = table.getScanner(s);
		resultIterator = scanner.iterator();
	}

	public boolean hasNextComment() {
		if ((resultIterator == null) || (!resultIterator.hasNext())) {
			if (scanner != null) {
				scanner.close();
			}
			return false;
		} else {
			Result row = resultIterator.next();
			NavigableMap<byte[], byte[]> map = row.getFamilyMap(Bytes
					.toBytes("comment"));
			keyStr = Bytes.toString(row.getRow());
			commentAuthor = Bytes.toString(map.get(Bytes
					.toBytes("comment_author")));
			commentBody = Bytes
					.toString(map.get(Bytes.toBytes("comment_body")));
			Long timeOfComment = Long.parseLong(keyStr.substring(keyStr
					.lastIndexOf(",") + 1));
			dateOfComment = new Date(timeOfComment);
			return true;
		}
	}

	public String nextCommentAuthor() {
		return commentAuthor;
	}

	public String nextCommentBody() {
		return commentBody;
	}

	public Date nextDateOfComment() {
		return dateOfComment;
	}

}
