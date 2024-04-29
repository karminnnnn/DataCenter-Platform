//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.freakchick.orange.engine;

import com.github.freakchick.orange.SqlMeta;
import com.github.freakchick.orange.context.Context;
import com.github.freakchick.orange.node.SqlNode;
import com.github.freakchick.orange.tag.XmlParser;
import com.github.freakchick.orange.token.TokenParser;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DynamicSqlEngine {
	Cache cache = new Cache();

	public DynamicSqlEngine() {
	}

	public SqlMeta parse(String text, Map<String, Object> params) {
		text = String.format("<root>%s</root>", text);
		SqlNode sqlNode = this.parseXml2SqlNode(text);
		Context context = new Context(params);
		this.parseSqlText(sqlNode, context);
		this.parseParameter(context);
		return new SqlMeta(context.getSql(), context.getJdbcParameters());
	}

	public Set<String> parseParameter(String text) {
		text = String.format("<root>%s</root>", text);
		SqlNode sqlNode = this.parseXml2SqlNode(text);
		HashSet<String> set = new HashSet<>();
		sqlNode.applyParameter(set);
		return set;
	}

	private SqlNode parseXml2SqlNode(String text) {
		SqlNode node = this.cache.getNodeCache().get(text);
		if (node == null) {
			node = XmlParser.parseXml2SqlNode(text);
			this.cache.getNodeCache().put(text, node);
		}

		return node;
	}

	private void parseSqlText(SqlNode sqlNode, Context context) {
		sqlNode.apply(context);
	}

	private void parseParameter(final Context context) {
		TokenParser tokenParser = new TokenParser("#{", "}", content -> {
			context.addParameter(context.getOgnlValue(content));
			return "?";
		});
		String sql = tokenParser.parse(context.getSql());
		context.setSql(sql);
	}
}
