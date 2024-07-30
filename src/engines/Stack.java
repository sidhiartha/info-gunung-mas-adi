package engines;

import java.util.ArrayList;
import java.util.List;

public class Stack<T>
{
	List<T> datas;

	public void push(T data)
	{
		if (datas == null)
		{
			datas = new ArrayList<T>();
		}

		datas.add(0, data);
	}

	public T pop()
	{
		if (isEmpty())
		{
			return null;
		}

		return datas.remove(0);
	}

	public T peek()
	{
		if (isEmpty())
		{
			return null;
		}

		return datas.get(0);
	}

	public boolean isEmpty()
	{
		return datas == null || datas.size() == 0;
	}
}
