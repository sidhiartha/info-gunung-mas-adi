package engines;

import java.util.ArrayList;
import java.util.List;

import models.OntoBahasa;

public class Tree
{
	private TreeNode root;
	private Stack<List<OntoBahasa>> stack;

	public Tree()
	{
		// TODO Auto-generated constructor stub
		root = new TreeNode();
		stack = new Stack<List<OntoBahasa>>();
	}

	public void constructTree(TreeNode parent, String kalimat,
			List<List<OntoBahasa>> bahasas)
	{
		if (kalimat.equals(""))
		{
			return;
		}

		if (parent == null)
		{
			parent = root;
		}

		for (List<OntoBahasa> list : bahasas)
		{
			OntoBahasa bahasa = list.get(0);
			String nilai =
					bahasa.getShadowNilai() != null ? bahasa.getShadowNilai()
							: bahasa.getNilai();
			// System.out.println("kalimat sekarang = " + kalimat);
			// System.out.println("nilai = " + nilai);
			if (!kalimat.endsWith(nilai))
			{
				continue;
			}
			// System.out.println("nilai lewat = " + nilai);
			TreeNode node = new TreeNode();
			node.setData(list);
			parent.addNode(node);

			if (kalimat.lastIndexOf(nilai) > 0)
			{
				constructTree(node,
						kalimat.substring(0, kalimat.lastIndexOf(nilai) - 1),
						bahasas);
			}
		}
	}

	public void kunjungi(TreeNode parent, List<OntoBahasa> resolusi)
	{
		if (parent == null)
		{
			return;
		}

		if (parent != root)
		{
			if (resolusi.isEmpty())
			{
				resolusi.addAll(parent.getData());
			} else
			{
				boolean cocok = false;
				List<OntoBahasa> checker = new ArrayList<OntoBahasa>(resolusi);
				resolusi.clear();
				for (OntoBahasa bahasa : checker)
				{
					for (OntoBahasa cek : parent.getData())
					{
						if (SyntaxChecker.isValidSyntax(cek, bahasa))
						{
							cocok = true;
							break;
						}
					}

						if (cocok)
						{
							break;
						}
					}

				if (cocok)
				{
					for (OntoBahasa bahasa : checker)
					{
						for (OntoBahasa cek : parent.getData())
						{
							OntoBahasa hasil =
									SyntaxChecker.generateAccepted(cek, bahasa);
							// System.out.println("generated = " + hasil);
							resolusi.add(hasil);
						}
					}
				} else
				{
					if (stack.isEmpty())
					{
						stack.push(checker);
					} else
					{
						List<OntoBahasa> temp = new ArrayList<OntoBahasa>();
						processStack(resolusi, temp);
						if (resolusi.size() <= 0)
						{
							stack.push(temp);
							stack.push(checker);
						}
					}
				}
			}
		}

		List<TreeNode> nodes = parent.getNodes();
		for (TreeNode node : nodes)
		{
			kunjungi(node, resolusi);
		}
	}

	public void
			processStack(List<OntoBahasa> resolusi, List<OntoBahasa> parent)
	{
		while (!stack.isEmpty())
		{
			if (parent == null)
			{
				parent = stack.pop();
			} else
			{
				parent.addAll(stack.pop());
			}

			// System.out.println("parent = " + parent);

			boolean cocok = false;
			List<OntoBahasa> checker = new ArrayList<OntoBahasa>(resolusi);
			resolusi.clear();
			for (OntoBahasa bahasa : checker)
			{
				for (OntoBahasa cek : parent)
				{
					if (SyntaxChecker.isValidSyntax(bahasa, cek))
					{
						cocok = true;
						break;
					}
				}

				if (cocok)
				{
					break;
				}
			}

			if (cocok)
			{
				for (OntoBahasa bahasa : checker)
				{
					for (OntoBahasa cek : parent)
					{
						OntoBahasa hasil =
								SyntaxChecker.generateAccepted(bahasa, cek);
						// System.out.println("generated = " + hasil);
						resolusi.add(hasil);
					}
				}
			}
		}
	}

	public TreeNode getRoot()
	{
		return root;
	}

	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		return "tree : " + root;
	}
}
