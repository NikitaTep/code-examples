package com.knok16.test;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import com.knok16.GeneticFramework.FitnessFunction;

public class MyFitnessFunction implements FitnessFunction {

	public long prepareTime = 0;
	public long checkTime = 0;
	public long sortingTime = 0;
	
	private static final int BIT_TO_INT = 8;
	private int pathLength = 0;
	private int[] path = null;
	private int[] seq = null;
	private int vertexCount;
	private int[][] matrix;

	private int[][] edges;
	private  long border =4000;
	
	
	public MyFitnessFunction(String filename) throws FileNotFoundException{
		super();
		Scanner in = new Scanner(new FileReader(filename));
		this.vertexCount = in.nextInt();
		this.matrix = new int[this.vertexCount][this.vertexCount];
		for(int i=0;i<this.vertexCount;i++){
			for(int j=0;j<this.vertexCount;j++){
				this.matrix[i][j] = in.nextInt();
			}
		}
		in.close();
		this.pathLength = vertexCount;
		this.path = new int[this.pathLength];
		this.seq = new int[this.pathLength];

		this.edges = new int[][]{{2,1},{7,18},{6,19},{3,8},{5,14}, {15,16}}; //selected edges
	}
	
	@Override
	public int getArity() {
		return this.pathLength*BIT_TO_INT;
	}

	@Override
	public int run(long[] genom) {

		long old = System.currentTimeMillis(); //time
		
		int offset=0;
		int vertexNumber=0;
		int index = 0;
		long tmp = 0;
				
		for (int i=0;i<this.pathLength/BIT_TO_INT;i++){
			offset = i<<3;
			tmp = genom[i];
			for (int j=0;j<BIT_TO_INT;j++){
				vertexNumber = (int)(tmp & 255);
				index = offset+j;
				this.path[index] = vertexNumber;
				this.seq[index] = index;
				tmp >>= 8;
			}
		}

		this.prepareTime += (System.currentTimeMillis()-old); //time
		old = System.currentTimeMillis(); //time
		
		qsort(this.path,this.seq,0,this.pathLength-1);
		
		this.sortingTime += (System.currentTimeMillis()-old); //time
		old = System.currentTimeMillis(); //time
		
		int res = this.checkPath(this.seq);
		
		this.checkTime += (System.currentTimeMillis()-old); //time
		
		return (res);
	}

	public long getTrackLenght(long[] genom){

		int offset=0;
		int vertexNumber=0;
		int index = 0;
		long tmp = 0;

		for (int i=0;i<this.pathLength/BIT_TO_INT;i++){
			offset = i<<3;
			tmp = genom[i];
			for (int j=0;j<BIT_TO_INT;j++){
				vertexNumber = (int)(tmp & 255);
				index = offset+j;
				this.path[index] = vertexNumber;
				this.seq[index] = index;
				tmp >>= 8;
			}
		}

		qsort(this.path,this.seq,0,this.pathLength-1);

		int maxIndex = 0, minIndex = path.length-1;
		int trackLenght = 0;
		int[] pathTemp = this.seq;
		int predVertex = pathTemp[0];
		int nextVertex = 0;

		//find the start and end
		for (int i=0; i<this.edges.length-1; i++)
			for (int j=0; j<pathTemp.length-1; j++)
				if ((this.edges[i][0] == path[j] && this.edges[i][1] == path[j+1]) || (this.edges[i][1] == path[j] && this.edges[i][0] == path[j+1])) {
					if(minIndex > j) minIndex = j;
					if(maxIndex < j+1) maxIndex = j+1;
				}
		//Calculate the coast way
		predVertex = pathTemp[minIndex];
		for (int i=minIndex+1; i<maxIndex; i++){
			nextVertex = pathTemp[i];
			trackLenght += this.matrix[predVertex][nextVertex];
			predVertex = nextVertex;
		}
		trackLenght += this.matrix[maxIndex][minIndex];

		return (trackLenght);
	}
	
	private void qsort(int[] arrayToSort, int[] arrayToMix,int l, int r){
		int i = l;
		int j = r;
		int tmp = 0;
		int pivot = arrayToSort[(l+r)>>1];

		while (i <= j) {
			while (arrayToSort[i] < pivot) {i+=1;}
			while (arrayToSort[j] > pivot) {j-=1;}

			if (i <= j) {
				tmp = arrayToSort[i];
				arrayToSort[i] = arrayToSort[j];
				arrayToSort[j] = tmp;
				tmp = arrayToMix[i];
				arrayToMix[i] = arrayToMix[j];
				arrayToMix[j] = tmp;
				i+=1;
				j-=1;
			}
		}
		if (l < j){
			qsort(arrayToSort, arrayToMix, l, j);
		}
		if (i < r){
			qsort(arrayToSort, arrayToMix, i, r);
		}
	}

	public void printPath(long[] genom){

		int offset=0;
		int vertexNumber=0;
		int index = 0;
		long tmp = 0;

		for (int i=0;i<this.pathLength/BIT_TO_INT;i++){
			offset = i<<3;
			tmp = genom[i];
			for (int j=0;j<BIT_TO_INT;j++){
				vertexNumber = (int)(tmp & 255);
				index = offset+j;
				this.path[index] = vertexNumber;
				this.seq[index] = index;
				tmp >>= 8;
			}
		}

		qsort(this.path,this.seq,0,this.pathLength-1);

		int maxIndex = 0, minIndex = seq.length-1;
		int[] pathTemp = this.seq;

		//find the start and end
		for (int i=0; i<this.edges.length; i++)
			for (int j=0; j<pathTemp.length-1; j++)
				if ((this.edges[i][0] == pathTemp[j] && this.edges[i][1] == pathTemp[j+1]) || (this.edges[i][1] == pathTemp[j] && this.edges[i][0] == pathTemp[j+1])) {
					if(minIndex > j) minIndex = j;
					if(maxIndex < j+1) maxIndex = j+1;
				}

		System.out.print("final path: " );
		for (int i=minIndex;i<maxIndex+1;i++){
			System.out.print(" "+pathTemp[i] );
		}

		int predVertex = 0;
		int nextVertex = 0;
		int trackLenght = 0;

		predVertex = pathTemp[minIndex];
		for (int i=minIndex+1; i<maxIndex; i++){
			nextVertex = pathTemp[i];
			trackLenght += this.matrix[predVertex][nextVertex];
			predVertex = nextVertex;
		}
		trackLenght += this.matrix[maxIndex][minIndex];
		System.out.println("\nTrack Lenght: \t"+trackLenght );
	}
	
	public int checkPath(int[] path){
		
		int result = 0;
		int maxIndex = 0, minIndex = path.length-1;
		int trackLenght = 0;
		int predVertex = path[0];
		int nextVertex = 0;

		for (int i=0; i<this.edges.length; i++)
			for (int j=0; j<path.length-1; j++)
				if ((this.edges[i][0] == path[j] && this.edges[i][1] == path[j+1]) || (this.edges[i][1] == path[j] && this.edges[i][0] == path[j+1])) {
					result++;
					if(minIndex > j) minIndex = j;
					if(maxIndex < j+1) maxIndex = j+1;
					j = pathLength-1;
				}

		//System.out.print(" "+result );
		if (result == this.edges.length) {
			//System.out.println(" \n i find the way with all selected edges!");
			//System.out.print("minIndex: \t"+minIndex );
			//System.out.println(" maxIndex: \t"+maxIndex );

		//	for (int i=minIndex;i<maxIndex+1;i++){		//печатает путь в котором вс ребра из Е'
		//		System.out.print(" "+path[i] );
		//	}

			predVertex = path[minIndex];
			for (int i=minIndex+1; i<maxIndex; i++){
				nextVertex = path[i];
				trackLenght += this.matrix[predVertex][nextVertex];
				predVertex = nextVertex;
			}
			trackLenght += this.matrix[maxIndex][minIndex];
			//System.out.println("\nTrack Lenght: \t"+trackLenght );
			//System.out.println("Border: \t"+this.border );
			if (trackLenght < 3072) result++;		//проверка длины цикла с границей В
		}

		return result;
	}
	
	public static void generateRandomFile(String filename,int n) throws IOException{
		Random random = new Random();
		BufferedWriter out = new BufferedWriter(new FileWriter(filename));
		out.write(n+"\n");
		
		int[][] matrix = new int[n][n];
		
		for(int i=0;i<n-1;i++){
			for(int j=i+1;j<n;j++){
				matrix[i][j] = random.nextInt(256);
				matrix[j][i] = matrix[i][j];
			}
		}
		
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				out.write(matrix[i][j]+" ");
			}
			out.write("\n");
		}
		
		out.flush();
		out.close();
	}
}
