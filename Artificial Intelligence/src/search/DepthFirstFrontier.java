package search;

import java.util.Stack;

public class DepthFirstFrontier implements Frontier{
	Stack<Node> lifoQueue = new Stack<Node>();
	int current = 0;
	int max = 0;
	
	public void addNode (Node node) {
		lifoQueue.push(node);
		current +=1;
		if(max<current)
			max = current;
	}
	
	public void clear() {
		lifoQueue.clear();
		current = 0;
	}
	
	public boolean isEmpty() {
		return lifoQueue.isEmpty();
	}
	
	public Node removeNode() {
		current -=1;
		return lifoQueue.pop();
	}

    public int size()
    {
        return current;
    }
	public int maxNodes() {
		return max;
	}
}
