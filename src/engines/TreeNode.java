package engines;

import java.util.ArrayList;
import java.util.List;

import models.OntoBahasa;

public class TreeNode
{
	List<OntoBahasa> data;
	List<TreeNode> nodes;

	public TreeNode()
	{
		nodes = new ArrayList<TreeNode>();
	}

	public List<OntoBahasa> getData()
	{
		return data;
	}

	public void setData(List<OntoBahasa> data)
	{
		this.data = data;
	}

	public List<TreeNode> getNodes()
	{
		return nodes;
	}

	public void addNode(TreeNode node)
	{
		nodes.add(node);
	}

	@Override
	public String toString()
	{
		return "TreeNode [data=" + data + ", nodes=" + nodes + "]";
	}

}
