package search;

public class Node {
	public final Node parent;
	public final Action action;
	public final State state;
	public int depth = 0;
	public int value = 0;
	public int costRoot = 0;
	
	public Node(Node parent, Action action, State state) {
		this.parent = parent;
		this.action = action;
		this.state = state;
		if(this.parent == null ) {
			this.depth = 0;
			this.costRoot = 0;
		}
		else {
			this.depth = this.parent.depth + 1;
			this.costRoot = this.parent.costRoot + this.parent.action.cost();
		}
	}
}
