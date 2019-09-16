package search;
import java.util.*;

public class IterativeDeepeningTreeSearch implements Search{
    private int depth=0;
    private int maxi=0;
    public int maximumsize() { return maxi;}
    Frontier frontier= new DepthFirstFrontier();

    public Node DFS_limited(Node root, GoalTest goal, int level)
    {
        frontier.addNode(root);
        depth+=1;
        while (!frontier.isEmpty())
        {
            Node node=frontier.removeNode();
            if (goal.isGoal(node.state)) {maxi=frontier.maxNodes(); return node;}
            if (node.depth<level) {
                for (Action action : node.state.getApplicableActions()) {
                    State newState = node.state.getActionResult(action);
                    //		if (!explored.contains(newState))

                    int s = frontier.size();
                    frontier.addNode(new Node(node, action, newState));
                    int p = frontier.size();
                    if (p == s + 1) depth++;
                }
            }
        }
        return null;
    }
    public Node findSolution(Node root, GoalTest goal) {
        int level=0;
        boolean ok=true;
        while (ok)
        {
            Node x=DFS_limited(root, goal, level);
            if (x!=null ) return x;
            level+=1;
        }
        return null;
    }
    public int nodesGen()
    {
        return depth;
    }

}
