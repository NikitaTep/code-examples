package com.knok16.test;

import java.io.IOException;

import com.knok16.GeneticFramework.GeneticEngine;

public class Main {

	public static void main(String[] args) throws IOException{
		MyFitnessFunction.generateRandomFile("matrix.txt", 32);
		int countGeneration = 0;
		MyFitnessFunction ff = new MyFitnessFunction("matrix.txt");
		MyFitnessFunction ff2 = new MyFitnessFunction("matrix.txt");
		MyFitnessFunction ff3 = new MyFitnessFunction("matrix.txt");
		MyFitnessFunction ff4 = new MyFitnessFunction("matrix.txt");
		GeneticEngine ge = new GeneticEngine(ff);
		GeneticEngine ge2 = new GeneticEngine(ff2);
		GeneticEngine ge3 = new GeneticEngine(ff3);
		GeneticEngine ge4 = new GeneticEngine(ff4);
		long time = System.currentTimeMillis();

		long[] better1 = null;
		long[] better2 = null;
		long[] better3 = null;
		long[] better4 = null;
		long [][] genomList1 = null;
		long [][] genomList2 = null;
		long [][] genomList3 = null;
		long [][] genomList4 = null;

		int loopCount = 0;
		int swapGeneration = 50;  //промежуток коколений между обменами островов

		//run islandGA in thread
		while (countGeneration < 100000 && better1 == null && better2 == null && better3 == null && better4 == null) {
			//property 1 island
			ge = new GeneticEngine(ff);
			ge.setIndividualCount(60);
			ge.setMaxResult(7);            //count edges+1
			ge.setGenerationCount(swapGeneration);
			ge.setSelectionType(GeneticEngine.SelectionType.ROULETTE_WHEEL);
			ge.setCrossingType(GeneticEngine.CrossingType.TWO_POINT_RECOMBINATION);
			ge.setUseMutation(true);
			ge.setMutationPercent(0.5d);
			ge.setInputGenomList(genomList1);

			//property 2 island
			ge2 = new GeneticEngine(ff2);
			ge2.setIndividualCount(60);
			ge2.setMaxResult(7);            //count edges+1
			ge2.setGenerationCount(swapGeneration);
			ge2.setSelectionType(GeneticEngine.SelectionType.TOURNEY);
			ge2.setCrossingType(GeneticEngine.CrossingType.ELEMENTWISE_RECOMBINATION);
			ge2.setUseMutation(true);
			ge2.setMutationPercent(0.5d);
			ge2.setInputGenomList(genomList2);

			//property 3 island
			ge3 = new GeneticEngine(ff3);
			ge3.setIndividualCount(60);
			ge3.setMaxResult(31);            //count edges+1
			ge3.setGenerationCount(swapGeneration);
			ge3.setSelectionType(GeneticEngine.SelectionType.ROULETTE_WHEEL);
			ge3.setCrossingType(GeneticEngine.CrossingType.TWO_POINT_RECOMBINATION);
			ge3.setUseMutation(true);
			ge3.setMutationPercent(0.5d);
			ge3.setInputGenomList(genomList3);

			//property 4 island
		//	ge4 = new GeneticEngine(ff4);
		//	ge4.setIndividualCount(60);
		//	ge4.setMaxResult(31);            //count edges+1
		//	ge4.setGenerationCount(swapGeneration);
		//	ge4.setSelectionType(GeneticEngine.SelectionType.ROULETTE_WHEEL);
		//	ge4.setCrossingType(GeneticEngine.CrossingType.TWO_POINT_RECOMBINATION);
		//	ge4.setUseMutation(true);
		//	ge4.setMutationPercent(0.5d);
		//	ge4.setInputGenomList(genomList4);

			ge.start();
			ge2.start();
			ge3.start();
		//	ge4.start();
			try {
				ge.join();
				ge2.join();
				ge3.join();
		//		ge4.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			genomList1 = ge.genomListOffsprings;
			genomList2 = ge2.genomListOffsprings;
			genomList3 = ge3.genomListOffsprings;
		//	genomList4 = ge4.genomListOffsprings;
			better1 = ge.bestGenom;
			better2 = ge2.bestGenom;
			better3 = ge3.bestGenom;
		//	better4 = ge4.bestGenom;
			//migration
			genomList1[1] = ge3.missioner;
			genomList2[1] = ge2.missioner;
			genomList3[1] = ge.missioner;
		//	genomList4[1] = ge.missioner;

			countGeneration += swapGeneration;
			loopCount ++;
		}
		
		long timeToFF = ge.timeToFF;
		long timeToSelection = ge.timeToSelection;
		long timeToCrossing = ge.timeToCrossing;
		long timeToMutate = ge.timeToMutate;

	//	System.out.println("\nloop:\t"+loopCount);
		System.out.println("\nRunning:\t"+(System.currentTimeMillis()-time)/1000 + " secs");
		System.out.println("FitnessFunc:\t"+timeToFF/1000 + " secs");
		System.out.println(" - FF Prepare:\t"+ff.prepareTime/1000 + " secs");
		System.out.println(" - FF QSort:\t"+ff.sortingTime/1000 + " secs");
		System.out.println(" - FF Check: \t"+ff.checkTime/1000 + " secs");
		System.out.println("Selection:\t"+(timeToSelection-timeToFF)/1000 + " secs");
		System.out.println("Crossing:\t"+timeToCrossing/1000 + " secs");
		System.out.println("Mutate: \t"+timeToMutate/1000 + " secs");
		//System.out.println("\ntotal generation :\t"+(loopCount-1)*swapGeneration);
		//System.out.println(Long.MAX_VALUE-ff.run(better));
		if (better1 != null){
			ff.printPath(better1);
		}
		if (better2 != null) {
			ff.printPath(better2);
		}
		if (better1==null && better2==null) {
			System.out.println("solution not found");
		}


		//System.out.println("");

		//int[] counter1 = new int[100];

		//for (int i = 0; i < array.length; i++) {
		//	for (int j = i + 1; j < array.length; j++) {
		//		if (array[i] == array[j]) {
		//			counter1[i] = array[i];
		//		}
		//	}
		//}
		//for (int q=0; q<counter1.length; q++)
		//	System.out.print(counter1[q]+ " ");
	}
	
	private static void printLongInBin(long l, int last){
		if (last>0){
			int p = (int)(l & 1);
			printLongInBin(l>>1,--last);
			System.out.print(p);
		}
	}
	
}