
/**************************************************************************
 * @author Satya,Arash
 */
package graph.simulation;

import graph.common.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class TightSimulation {

	/******************************************************************************************************
	 * Performs tight simulation when data graph is of type Graph
	 * @param dGraph - The Data Graph.
	 * @param query - The Query graph.
	 * @return All the subgraphs; i.e., the set of balls in this case
	 */
	public static Set<Ball> getTightSimulation(Graph dataGraph, SmallGraph query) {
		Set<Ball> resultBalls = new HashSet<Ball>();
		
		//********** FINDING THE "DUAL SIMULATION" STEP ********** //
		Map<Integer,Set<Integer>> dualSimSet = DualSimulation.getDualSimSet(dataGraph, query);
		if(dualSimSet.isEmpty()){
			System.out.println("No Dual Match"); 
			return resultBalls;		
		}

		// ********** FINDING THE MATCH GRAPH STEP **************//
		SmallGraph newGraph = DualSimulation.getResultMatchGraph(dataGraph, query, dualSimSet);	

		// ********** FINDING QUERY Radius and selected center ********** //
		int qRadius = query.getRadius();
		int qCenter = query.getSelectedCenter();

		// ****** BALL CREATION STEP ********* //
		Set<Integer> matchCenters = dualSimSet.get(qCenter);

		for(int center : matchCenters){
			Ball ball = new Ball(newGraph, center, qRadius); // BALL CREATION
			// ******** DUAL FILTER STEP  **********
			boolean found = ball.dualFilter(query, dualSimSet);
			if(found)
				resultBalls.add(ball);
		} //for

		return resultBalls;
	}

	/******************************************************************************************************
	 * Performs tight simulation when data graph is of type SmallGraph
	 * @param dGraph - The Data Graph.
	 * @param query - The Query graph.
	 * @return All the subgraphs i.e., the balls, in this case a Map where K is the center and V is the Ball.
	 */
	public static Set<Ball> getTightSimulation(SmallGraph dataGraph, SmallGraph query) {
		Set<Ball> resultBalls = new HashSet<Ball>();
		
		//********** FINDING THE "DUAL SIMULATION" STEP ********** //
		Map<Integer,Set<Integer>> dualSimSet = DualSimulation.getDualSimSet(dataGraph, query);
		if(dualSimSet.isEmpty()){
			System.out.println("No Dual Match"); 
			return resultBalls;		
		}

		// ********** FINDING THE MATCH GRAPH STEP **************//
		SmallGraph newGraph = DualSimulation.getResultMatchGraph(dataGraph, query, dualSimSet);	

		// ********** FINDING QUERY Radius and selected center ********** //
		int qRadius = query.getRadius();
		int qCenter = query.getSelectedCenter();

		// ****** BALL CREATION STEP ********* //
		Set<Integer> matchCenters = dualSimSet.get(qCenter);

		for(int center : matchCenters){
			Ball ball = new Ball(newGraph, center, qRadius); // BALL CREATION
			// ******** DUAL FILTER STEP  **********
			boolean found = ball.dualFilter(query, dualSimSet);
			if(found)
				resultBalls.add(ball);
		} //for

		return resultBalls;
	}

	/********************************************************************************
	 * This method filters any ball which is superset of any other ball
	 * @param matchGraphs the result of tight simulation in the center->ball format
	 * @return filtered set of results
	 */
	public static Set<Ball> filterMatchGraphs(Set<Ball> matchGraphs) {
		Set<Ball> filteredGraphs = new HashSet<Ball>();
		
		Iterator<Ball> it = matchGraphs.iterator();
		while(it.hasNext()) {
			Ball theBall = it.next();
			it.remove();
			matchGraphs.remove(it);
			boolean good = true;
			for(Ball aBall : matchGraphs) {
				if(theBall.contains(aBall)) {
					good = false;
					break;
				}
			} //for
			if(good)
				filteredGraphs.add(theBall);
		} //while
		return filteredGraphs;
	}

	/*************************************************************************************************
	 * A method to print the match centers. Just for debugging purposes. 
	 */
	public static void printMatch(Set<Ball> matchGraphs){
		System.out.println("Number of Match Graphs: " + matchGraphs.size());
		for(Ball b : matchGraphs){
			System.out.println(b);
		}
		System.out.println("---------------------");
	}
	
	/**
	 * Test main method
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Graph g = new Graph("exampleGraphs/G_tight1.txt");
		SmallGraph q = new SmallGraph("exampleGraphs/Q_tight1.txt");
		
		System.out.println("Result of tight simulation without post-processing:");
		long startTime = System.currentTimeMillis();
		Set<Ball> tightSim = getTightSimulation(g, q);
		long stopTime = System.currentTimeMillis();
		printMatch(tightSim);
		System.out.println("Spent time: " + (stopTime - startTime) + "ms");
		
		System.out.println("Result of tight simulation after post-processing:");
		startTime = System.currentTimeMillis();
		Set<Ball> filteredTightSim = filterMatchGraphs(tightSim);
		stopTime = System.currentTimeMillis();
		printMatch(filteredTightSim);
		System.out.println("Spent time: " + (stopTime - startTime) + "ms");
	} //main

} //class
