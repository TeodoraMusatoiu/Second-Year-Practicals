package search;

import java.util.LinkedList;
import java.util.Queue;

public class BredthFirstFrontier implements Frontier {
	
	Queue<Node> fifoQueue = new LinkedList<Node>();
	int current = 0;
	int max = 0;
	
	public void addNode (Node node) {
		fifoQueue.add(node);
		current+=1;
		if(max<current)
			max = current;
	}
	
	public void clear() {
		fifoQueue.clear();
		current = 0;
	}
	
	public boolean isEmpty() {
		return fifoQueue.isEmpty();
	}
	
	public Node removeNode() {
		current-=1;
		return fifoQueue.remove();
	}

	public int maxNodes() {
		return max;
	}
    public int size()
    {
        return current;
    }
}
