package search;

public class TreeSearch implements Search{
	Frontier frontier;
	private int numberofnodes = 0;
	public TreeSearch(Frontier front){
		this.frontier = front;
	}

	public Node findSolution(Node rootNode, GoalTest goalTest) {
		frontier.addNode(rootNode);
		numberofnodes=1;
		while (!frontier.isEmpty()) {
			Node node = frontier.removeNode();
			if (goalTest.isGoal(node.state))
				return node;
			else {
				for (Action action : node.state.getApplicableActions()) {
					State newState = node.state.getActionResult(action);
					int s=frontier.size();
					frontier.addNode(new Node(node, action, newState));
					int p=frontier.size();
					if (s==p-1) numberofnodes++;
				}
			}
		}
		return null;
	}
	
	public int nodesGen() {
		return numberofnodes;
	}
}
