package search;

public interface Frontier {
	int size();
	void addNode (Node node);
	void clear();
	boolean isEmpty();
	Node removeNode();
	int maxNodes();
}
