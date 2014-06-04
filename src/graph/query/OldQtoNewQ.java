package graph.query;

import graph.common.SmallGraph;

import java.io.File;
import java.util.Random;

public class OldQtoNewQ {
	
	/*
	 * Randomly extract a few query graphs from a given data graph
	 * args[0] the path to the old queries
	 * args[1] the requested number of output queries from each size
	 * args[2] the requested number of increments in each input query
	 * args[3] the path for storing the output queries
	 */
	public static void main(String[] args) throws Exception {
		File dirIn = new File(args[0]);
		if(!dirIn.isDirectory())
			throw new Exception("The specified path for the input graphs is not a valid directory");
		File[] graphFiles = dirIn.listFiles();
		
		int qPerSize = Integer.parseInt(args[1]);
		int qPerInc =  Integer.parseInt(args[2]);

		Random randLabel = new Random();
		Random randVertex = new Random();
		Random randEdgeDirection = new Random();
		
		for(File inG : graphFiles) {
			SmallGraph oldQ = new SmallGraph(inG.getAbsolutePath());
			
			int version = 0;

			for(int count1 = 0; count1 < qPerSize; count1++) {
				SmallGraph newQ = oldQ.clone();

				for(int count2 = 0; count2 < qPerInc; count2++) {
					int newQ_N = newQ.getNumVertices();
					int newLabel = newQ.getLabel(randLabel.nextInt(newQ_N));
					int connectVertex = randVertex.nextInt(newQ_N);
					int edgeDirection = randEdgeDirection.nextInt(1);

					newQ.connectNewVertex(newQ_N , newLabel, connectVertex, edgeDirection);
					newQ.print2File(args[3] + "/" + inG.getName() + "-V" + version);
					version ++;
				} //for
			} //for
			
		} //for

	}//main
	
}//class