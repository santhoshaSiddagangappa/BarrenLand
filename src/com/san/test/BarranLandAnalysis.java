package com.san.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

public class BarranLandAnalysis {
			
	LinkedList<Integer[]> allRectangles; 
	LinkedList<Integer[]> queue;
	HashMap<Integer, Integer> areasMap;
	int mColor[][];
	final static int XLIM = 400;
	final static int YLIM = 600;
 
	public void readInput(String input){	
			
		String[] parts = input.split(",");
		for(String s:parts){
			s = s.replace("\"", "");
			s = s.replaceAll("“|”", "");
		    s = s.replaceAll("\\{|\\}", "");
		    s = s.replaceAll("^ ", "");
		    
		    if(!s.isEmpty()){
		    	String[] coord = s.split(" ");
		    
		    	Integer[] temp = {Integer.parseInt(coord[0]), Integer.parseInt(coord[1]), 
		    					  Integer.parseInt(coord[2]), Integer.parseInt(coord[3])};
		    
		    	allRectangles.add(temp);
		    }
		}
		
	}
	
	// Set as 1 all nodes inside a barren rectangle
	public void colorBarrenRectangles(){
		
		ListIterator<Integer[]> iterator = allRectangles.listIterator();		
		while(iterator.hasNext()){
			
			Integer[] rectangle = iterator.next();
			
			for(int i = rectangle[0]; i <= rectangle[2]; i++)
				for(int j = rectangle[1]; j <= rectangle[3]; j++)
					mColor[i][j] = 1;							
		}
	}
	
	// Zero Matrix
	public void clearColoMatrix() {
		for(int i = 0; i < XLIM; i++)
			  for(int j = 0; j < YLIM; j++)
				  mColor[i][j] = 0;
	}
 
	// Add node to the queue to be be inspected
	public void addQueue(int i, int j){
		if(mColor[i][j] == 0){	
			queue.add(new Integer[] {i, j});
		}
	}
	
	public String printOutput(){
		int[] result = new int[areasMap.values().size()];
		int i = 0;	
		
		for (Map.Entry<Integer, Integer> entry : areasMap.entrySet()){
		     result[i] = entry.getValue();
		     i++;
		}
		
		Arrays.sort(result);		
		return (Arrays.toString(result)).replaceAll("\\[|\\]|,", "");

	}
	
	public void readFromSTDIN() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s = br.readLine();		
		readInput(s);
	}
	
	// Main work is done here. This is a type of BFS for disconnected components.
	// We will traverse through the "Graph" giving the same color to all connected components.
	// We will keep track of how many nodes each component has, and this will be the final area.
	public void getFertileLands(){
		int land = 1;
		int i = 0;
		int j = 0;
		
		while(i < XLIM && j < YLIM){
			
			if(queue.isEmpty()) {
				Integer node[] = {i, j};
				
				// If node[i][j] has not been visited add to queue
				// As the queue was empty, this is a new fertile land
				if(mColor[i][j] == 0) {		  
					land++;
					areasMap.put(land, 0);
					queue.add(node);
				}
				// Make sure we pass through all the Land
				if(i == (XLIM-1)){
					i = 0;
					j++;
				}
				else 
					i++;
			}
			
			if(!queue.isEmpty()) {
				Integer node[] = queue.pop();
			
				int x = node[0];
				int y = node[1];
						
				if(mColor[x][y] == 0){
					if(x > 0)
						addQueue(x-1, y);
					if(x < (XLIM - 1))
						addQueue(x+1, y);
					if(y > 0) 
						addQueue(x, y-1);
					if(y < (YLIM - 1))
						addQueue(x, y+1);
					
					mColor[x][y] = land;
					areasMap.put(land, (areasMap.get(land) + 1));
				}
			}
		}
		
	}
		
	BarranLandAnalysis(){
		allRectangles = new LinkedList<Integer[]>();
		queue = new LinkedList<Integer []>();
		areasMap = new HashMap<Integer, Integer>();
		mColor = new int[XLIM][YLIM];	
	}
	
	public static void main(String[] args) {
		
		BarranLandAnalysis test;
        
		test = new BarranLandAnalysis();			
		//String input = new String("{“”}");
		String input = new String("{“0 292 399 307”}");
		//String input = new String("{“48 192 351 207”, “48 392 351 407”, “120 52 135 547”, “260 52 275 547”}");
		test.readInput(input);
		
		//from standardIn
		//test.readFromSTDIN();
		
		test.clearColoMatrix();
		test.colorBarrenRectangles();
		test.getFertileLands();		
		System.out.println(test.printOutput());
    }

}