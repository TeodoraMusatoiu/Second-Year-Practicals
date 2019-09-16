package search;

import java.util.Comparator;
import java.util.PriorityQueue;

public class BestFirstSearch implements Frontier{
    PriorityQueue<Node> frontier = new PriorityQueue<Node>();

    Comparator<Node> nodeComparator = new Comparator<Node>(){
        @Override
        public int compare (Node n1, Node n2){
            return n1.value - n2.value;
        }
    }
    int current = 0;
    int max = 0;

    NodeFunction nodeFunction = new NodeFunction()

    public BestFirstSearch(NodeFunction nfun) { this.nodeFunction = nfun;}


    public void addNode (Node node) {
        node.value = nodeFunction.desirability(node);
        frontier.add(node);
        current +=1;
        if(max<current)
            max = current;
    }
    public void clear() {
        frontier.clear();
        current = 0;
    }

    public boolean isEmpty() {
        return frontier.isEmpty();
    }

    public Node removeNode() {
        current -=1;
        return frontier.dequeue();
    }

    public int size()
    {
        return current;
    }
    public int maxNodes() {
        return max;
    }


}

