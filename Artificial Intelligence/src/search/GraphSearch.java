package search;


import java.util.*;

public class GraphSearch implements Search{
	private int depth = 0;
	Frontier frontier;
	Set<State> explored = new HashSet<State>();

	public GraphSearch(Frontier front){
		this.frontier = front;
	}

	public Node findSolution(Node rootNode, GoalTest goalTest) {
		frontier.addNode(rootNode);
		while (!frontier.isEmpty()) {
			Node node = frontier.removeNode();
			if (goalTest.isGoal(node.state))
				return node;
			else {
				for (Action action : node.state.getApplicableActions()) {
					State newState = node.state.getActionResult(action);
					if(!explored.contains(newState)){
						int s=frontier.size();
						explored.add(newState);
						frontier.addNode(new Node(node,action, newState));
						int p=frontier.size();
						if (p==s+1) depth++;
					}
				}
			}
		}
		return null;
	}
	
	public int nodesGen() {
		return depth;
	}
}
