import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import edu.du.dudraw.*;

public class Project4Driver 
{
	//arrays of hashmaps for all the names and frequencies and the normalized version of those
	private static HashMap<String, Double>[] allNames = new HashMap[142];
	private static HashMap<String, Double>[] allNormalizedNames = new HashMap[142];
	
	//populations of every year
	private static int[] populations = new int[142];
	
	//an array of arraylists for easier sorting later
	private static ArrayList<Entry<String, Double>>[] query2Data;
	
	public static void main(String[] args) throws FileNotFoundException
	{
		//comparator object
		SortFrequencies obj = new SortFrequencies();
		
		//initialize array of arraylists
		query2Data = new ArrayList[142];
		for(int i = 0; i < 142; i++)
		{
			query2Data[i] = new ArrayList<>();
		}
		
		//going year by year for every file
		for(int year = 1880; year<=2021; year++)
		{
			try
			{
				//intialize fileReader and each year/index for both allNames and allNormalizedNames 
				Scanner fileReader = new Scanner(new FileInputStream("names/yob" + year + ".txt"));
				allNames[year - 1880] = new HashMap<>();
				allNormalizedNames[year - 1880] = new HashMap<>();
				
				//temp var for population updates
				int population = 0;
				
				//while there's another name on file
				while(fileReader.hasNext())
				{
					//store the line in an array and get the name and frequency
					String temp = fileReader.next();
					String[] tempArr = temp.split(",");
					String name = tempArr[0];
					double frequency = Integer.parseInt(tempArr[2]);
					
					//add the frequency to the temp var
					population += frequency;
					
					//checks if the name exists in the other gender, if so add to that frequency, else simply add the name
					if(allNames[year - 1880].containsKey(name))
					{
						allNames[year - 1880].put(name, allNames[year - 1880].get(name)+frequency);
					}
					else
					{
						allNames[year - 1880].put(name, frequency);
					}
				}
				
				//add the temp var to the population array
				populations[year - 1880] = population;
				
				//add the names of that year to allNormalizedNames by normalizing the data using the population array data
				for(Entry<String, Double> s : allNames[year - 1880].entrySet())
				{
						allNormalizedNames[year - 1880].put(s.getKey(), s.getValue()/populations[year - 1880]*1000);
				}
				
			//catch if file does not exist
			}catch(FileNotFoundException e)
			{
				throw new FileNotFoundException("File does not exist.");
			}
			
			//copy allNames after it's finished intializing into query2Data for sorting
			for(Entry<String, Double> s : allNames[year - 1880].entrySet())
			{
				query2Data[year - 1880].add(s);
			}
		}
		
		//sort data from largest frequency to smallest frequency in the array of arrayLists
		for(int i = 0; i < query2Data.length; i++)
		{
			Collections.sort(query2Data[i], obj);
		}
		
		//all userinput vars and the scanner
		String userInput = "";
		String queryType = "";
		String userYear = "";
		String userNumOfNames = "";
		Scanner input = new Scanner(System.in);
		
		//as long as the user does not want to stop keep asking the queries
		while(!userInput.equals("Stop") || !queryType.equals("Stop") 
			|| !userInput.equals("Stop") || !userYear.equals("Stop") 
			|| !userNumOfNames.equals("Stop"))
		{
			//ask for which query
			System.out.println("Type 'Query 1' for Query 1 or type 'Query 2' for Query 2: (type 'Stop' to end the program)");
			queryType = input.next();
				
				//check if the user wants to stop, if they do want to stop then return
				if(queryType.equals("Stop"))
				{
					System.out.println("Have a good day!");	
					return;
				}

				//run query 1 
				else if(queryType.equals("1"))
				{
					System.out.println("What name's frequency history would you like to see? (type 'Stop' to end the program)");
					userInput = input.next();
					
					//check if the user wants to stop, if they do want to stop then return
					if(userInput.equals("Stop"))
					{
						System.out.println("Have a good day!");
						return;
					}
					

					//call query1
					query1(userInput);	
				}
				
				//run query 2
				else if(queryType.equals("2"))
				{
					int year = 0;
					while(!(year <= 2021 && year >= 1880))
					{
						System.out.println("What year's most popular names would you like to see?");
						userYear = input.next();

						//convert to int for easier calculations
						year = Integer.parseInt(userYear);
						
						//make sure it's a valid year
						if(year > 2021 || year < 1880)
						{
							System.out.println("Invalid year (1880-2021 are valid years).");
						}
					}
					
					int numOfNames = -1;
					while(numOfNames > query2Data[year - 1880].size() || numOfNames < 0)
					{
						System.out.println("How many names do you want to see?");
						userNumOfNames = input.next();
						
						//convert to int for easier calculations
						numOfNames = Integer.parseInt(userNumOfNames);
						
						//make sure it's a valid amount of names
						if(numOfNames > query2Data[year - 1880].size() || numOfNames < 0)
						{
							System.out.println("Invalid amount of names (" + query2Data[year - 1880].size() + " is the amount of names from " + year + ").");
						}					
					}
					
					//call query 2
					query2(year, numOfNames);
				}
				
				//invalid input checker for query question
				else 
				{
					System.out.println("Invalid input, try again.");
				}
		}	
	}
	
	public static void query1(String n)
	{
		//make a new array with just the frequencies 
		double[] dataToGraph = new double[142];
		
		//var to check if the name doesn't exist
		int zeroCounter = 0;
		
		//copy frequencies into array 
		for(int i = 0; i < allNormalizedNames.length; i++)
		{
			if(allNormalizedNames[i].containsKey(n))
			{
				dataToGraph[i] = allNormalizedNames[i].get(n);
			}
			if(dataToGraph[i] == 0)
			{
				zeroCounter++;
			}
		}
		
		//if the name doesn't exist return 
		if(zeroCounter == 142)
		{
			System.out.println("That name does not exist in the records. Try Again.");
			return;
		}
		
		//set up canvas 
		Draw draw = new Draw();
		draw.setCanvasSize(500, 500);
		draw.setXscale(0,141);
		draw.setYscale(0,500);
		draw.setPenColor(0,0,0);
		draw.enableDoubleBuffering();
			
		//max frequency, the year of the max frequency, and the first year the name was used vars
		double maxFreq = 0;
		int freqYear = 0; 
		int firstYear = 0; 
		
		//find the max frequency and the year that was reached
		for(int j = 0; j < dataToGraph.length; j++)
		{
			if(dataToGraph[j] > maxFreq)
			{
				maxFreq = dataToGraph[j];
				freqYear = j + 1880;
			}
		}
		int length = 0;	

		//find the first year the name was used
		while(firstYear == 0)
		{
			if(dataToGraph[length] > 0 && length < dataToGraph.length)
			{
				firstYear = length + 1880;
			}
			length++;
		}
		
		//set vars for rectangles to draw bars on the graph
		int x = 0;
		double halfWidth = 500/141;
		
		//for the whole array draw rectangles
		for(int i = 0; i < dataToGraph.length; i++)
		{
			//reset y to 0 every time there's a new year
			int y = 0;
			
			//decrementer var to draw that many rectangles, max bar should be at 400 and scaled to 500 relatively
			double bar = dataToGraph[i]*400/maxFreq; 
			
			//while there's more frequency keep drawing rectangles
			while(bar > 0)
			{
				draw.filledRectangle(x, y, .5, .5);
				y++;
				bar--;
			}
			
			//keep going along the bottom of the graph
			x++;
		}
	
		//display info about the first year, max frequency, and year of the max frequency
		draw.text(70.5, 450, "The first " + n + " was born in " + firstYear + " (on record)");
		draw.text(70.5, 425, "Maximum Frequency: " + maxFreq + "% in " + freqYear);
		draw.show();
	}
	
	public static void query2(int year, int numOfNames)
	{
		//display the numOfNames elements of the sorted arraylist of that year
		for(int i = 0; i < numOfNames; i++)
		{
			System.out.println(query2Data[year - 1880].get(i) + ", ");
		}
	}
}
