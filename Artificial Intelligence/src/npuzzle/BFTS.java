package npuzzle;

import search.*;
import java.util.Scanner;

public class BFTS {
	public static void main(String[] args) {
		System.out.println("Select one of the following options to solve the puzzle problem:");
		System.out.println("0. BFTS_Demo");
		System.out.println("1. Tree Search with BFS");
		System.out.println("2. Tree Search with DFS");
		System.out.println("3. Graph Search with BFS");
		System.out.println("4. Graph Search with DFS");
		System.out.println("5. Iterative Deepening Tree Search");
		System.out.println("Please insert option: ");
		Scanner input = new Scanner(System.in);
		int number = input.nextInt();
		
		Tiles initialConfiguration = new Tiles(new int[][] {
			{ 7, 4, 2 },
			{ 8, 1, 3 },
			{ 5, 0, 6 }
		});

		GoalTest goalTest = new TilesGoalTest();

		switch (number){
			case 0:{
				System.out.println("This is a demonstration of breadth-first tree search on 8-puzzle");
				System.out.println();
				Node solution = BreadthFirstTreeSearch.findSolution(initialConfiguration, goalTest);
				new NPuzzlePrinting().printSolution(solution);
				break;
			}
			case 1:{
				System.out.println("This is the solution with breadth-first tree search on 8-puzzle");
				System.out.println();
				Node root = new Node(null, null, initialConfiguration);
				BredthFirstFrontier frontier = new BredthFirstFrontier();
				TreeSearch tree = new TreeSearch(frontier);
				Node solution = tree.findSolution(root, goalTest);
				new NPuzzlePrinting().printSolution(solution);
                System.out.println(tree.nodesGen());
                System.out.println(frontier.maxNodes());
				break;
			}
			case 2:{
				System.out.println("This is the solution with depth-first tree search on 8-puzzle");
				System.out.println();
				Node root = new Node(null, null, initialConfiguration);
				DepthFirstFrontier frontier = new DepthFirstFrontier();
				TreeSearch tree = new TreeSearch(frontier);
				Node solution = tree.findSolution(root, goalTest);
				new NPuzzlePrinting().printSolution(solution);
				System.out.println(tree.nodesGen());
				System.out.println(frontier.maxNodes());
				break;
			}
			case 3:{
				System.out.println("This is the solution with breadth-first graph search on 8-puzzle");
				System.out.println();
				Node root = new Node(null, null, initialConfiguration);
				BredthFirstFrontier frontier = new BredthFirstFrontier();
				GraphSearch graph = new GraphSearch(frontier);
				Node solution = graph.findSolution(root, goalTest);
				new NPuzzlePrinting().printSolution(solution);
				System.out.println(graph.nodesGen());
				System.out.println(frontier.maxNodes());
				break;
			}
			case 4:{
				System.out.println("This is the solution with depth-first graph search on 8-puzzle");
				System.out.println();
				Node root = new Node(null, null, initialConfiguration);
				Frontier frontier = new DepthFirstFrontier();
				GraphSearch graph = new GraphSearch(frontier);
				Node solution = graph.findSolution(root, goalTest);
				new NPuzzlePrinting().printSolution(solution);
				System.out.println(graph.nodesGen());
				System.out.println(frontier.maxNodes());
				break;
			}
			case 5:{
				System.out.println("This is a solution using iterative deepening tree search on 8-puzzle");
				System.out.println();
				Node root = new Node(null, null, initialConfiguration);
				Frontier frontier= new DepthFirstFrontier();
				IterativeDeepeningTreeSearch tree= new IterativeDeepeningTreeSearch();

				Node solution = tree.findSolution(root, goalTest);

				new NPuzzlePrinting().printSolution(solution);
				System.out.println(tree.nodesGen());
				System.out.println(tree.maximumsize());
				break;
			}
			default: {
				System.out.println("Invalid test number, run again.");
			}
		}


	}
}
