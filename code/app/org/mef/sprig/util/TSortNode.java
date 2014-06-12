package org.mef.sprig.util;

import java.util.ArrayList;
import java.util.List;


public class TSortNode
{
	public Object obj;
	private List<TSortNode> deps = new ArrayList<TSortNode>();

	public TSortNode(Object obj)
	{
		this.obj = obj;
	}

	public List<TSortNode> getDependents()
	{
		return deps;
	}

	public void addDep(TSortNode node)
	{
		deps.add(node);
	}
}