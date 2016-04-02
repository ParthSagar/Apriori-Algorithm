import java.io.BufferedReader;  
import java.io.BufferedWriter;
import java.io.FileReader;  
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;  
import java.util.Date;
import java.util.Hashtable;  
import java.util.Scanner;  
import java.util.StringTokenizer;  

  

public class apriori 
{  
	String outputFile="output.txt";
	Vector<String> itemsData=null;     
	public static String[] items={"a","b","c","d","e","f"};
    public static String splitkey=",";  
    public static String fileName="file.txt";  
        
    private static double min_sup=0;  
    private static int itemcounter[];  
    private double min_conf=1.0;  
    private static int num_trans=0;
    int count;
    private static Scanner input=new Scanner(System.in);  
    Vector<Hashtable<String,Integer>> supportData=new Vector<Hashtable<String,Integer>>();  
    Hashtable<String,Integer> confidence = new Hashtable<String,Integer>();  
    Vector<Vector<String>> strongRules=null;  
    Date d;
	long start;
    public static void main(String[] args)throws Exception  
    {  
    	apriori ai=new apriori();  
    	  
    	ai.initialise();  
    	System.out.println("How many files data is read");  
    	int numofFiles=input.nextInt();  
    	for(int i=0;i<numofFiles;i++)  
    	{  
    		ai.processData();  
    	}  
    }  
    public void initialise()  
    {  
    	System.out.println("Enter the min_sup value");  
    	min_sup=input.nextDouble();  
    	System.out.println("Enter the confidence value");  
    	min_conf=input.nextDouble();  
    }  
    public void processData()throws Exception  
    {  
    	strongRules=new Vector<Vector<String>>();  
    	System.out.println("Enter the Trnascations FileName");  
    	//override when user given value here  
    	fileName=input.next();  
    	System.out.println("filename:"+fileName+"\n");  
    	itemsData=new Vector<String>();      
    	count=0;  
    	num_trans=0;
   
    	d=new Date();
    	start=d.getTime();
    	do  
    	{  
    		count++;  
    		itemesProcessed(count);  
    		getFrequentItems(fileName);  
    		calculateFrequentItems();  
    		Vector<String> tempData=new Vector<String>(itemsData);  
    		strongRules.add(tempData);  
    	}while(itemsData.size()>1);  
    	
    	calculateCofidence(); 
    	//itemsData.clear();  
    }  
    public void calculateCofidence() throws IOException  
    {  
    	String[] spliter=null; 
    	String s = null,filename;
    	FileWriter fw;
    	BufferedWriter file_out=null;
    	System.out.println("Do You Want To Save Output to File?(y/n)");
    	s=input.next();
    	long end;
    	if(s.compareToIgnoreCase("y")==0)
    	{
    		System.out.println("Enter the file name(.txt): ");
    		filename=input.next();
    		fw= new FileWriter(filename,false);
    		file_out = new BufferedWriter(fw);
    	}
    	
    	for(int i=0;i<strongRules.size();i++)  
    	{  
    			itemsData=strongRules.get(i);  
    			System.out.println("Frequent " + (i+1) + "-itemsets");  
    			for(int items=0;items<itemsData.size();items++)  
    			{  
    				String key=itemsData.get(items);  
    				Hashtable<String,Integer> confidence=supportData.get(i);  
    				int data=confidence.get(key);  
    				String rule=itemsData.get(items);  
    				spliter=rule.split("\\"+" ");
    				double calsupport=(itemcounter[i]/(double)num_trans);
    				if(spliter.length>1)  
    				{  
    					String topKey=spliter[0];  
    					int topValue=confidence.get(topKey);  
    					double calconfidence=((double)topValue/data);  
    					if(calconfidence>=min_conf)  
    					{  
    						if(s.compareToIgnoreCase("y")==0)
    						{
    							file_out.write(key+", "+calsupport+"\n");
    							System.out.println(key +", Minimum Support: "+ calsupport+ ", Minimum Cofidence: "+calconfidence);
    						}
    						else
    						{
    							System.out.println(key +", Minimum Support: "+ calsupport+ ", Minimum Cofidence: "+calconfidence);
    						}
    					}  
    				}  
    				else  
    				{  
    					if(data>=min_conf)  
    					{  
    						if(s.compareToIgnoreCase("y")==0)
    						{
    							file_out.write(key+", "+calsupport+"\n");
    							System.out.println(key +", Minimum Support: "+ calsupport+ ", Minimum Cofidence: "+min_conf);
    							
    						}
    						else
    						{
    							System.out.println(key +", Minimum Support: "+ calsupport+ ", Minimum Cofidence: "+min_conf);
    						}  
    					}  
    				}  
    			}  
    		}  
    	if(s.compareToIgnoreCase("y")==0)
    	{
    		file_out.close();
    	}
    	
    	d=new Date();
    	end=d.getTime();
    	System.out.println("Execution Time is: "+(double)((end-start)/1000)+" Seconds");
    }  
  
    public Vector<String> itemesProcessed(int count)  
    {  
    	Vector<String> tempItemsData=new Vector<String>();  
    	StringTokenizer tokenizer1=null;  
    	StringTokenizer tokenizer2=null;  
    	String stringA=null;  
    	String stringB=null;  
    	if(count==1)  
    	{  
    		for(int i=0;i<items.length;i++)  
    		{  
    			
    			tempItemsData.add(items[i]);  
    		}  
    	}  
    	else if(count==2)  
    	{  
    			for(int i=0;i<items.length;i++)
    			{
    				for(int j=(i+1);j<items.length;j++)
    				{
    					if(i!=j)
    						tempItemsData.add(items[i]+" "+items[j]);
    				}
    			}
    		
    	}  
    	else  
    	{  
    		tempItemsData=new Vector<String>();  
    		for(int items=0; items<itemsData.size(); items++)  
    		{  
    			for(int temp=items+1; temp<itemsData.size(); temp++)  
    			{              
    				stringA = "";  
    				stringB = "";  
    				tokenizer1 = new StringTokenizer(itemsData.get(items));  
    				tokenizer2 = new StringTokenizer(itemsData.get(temp));  	
    				for(int s=0; s<count-2; s++)  
    				{  
    					stringA = stringA + " " + tokenizer1.nextToken();  
    					stringB = stringB + " " + tokenizer2.nextToken();  
    				}  
    				if(stringB.compareToIgnoreCase(stringA)==0)  
    					tempItemsData.add((stringA + " " +tokenizer1.nextToken() + " " + tokenizer2.nextToken()).trim());  
    			}  
    		}  
    	}  
    	itemsData.clear();  
    	itemsData = new Vector<String>(tempItemsData);  
    	tempItemsData.clear();  
    	return itemsData;  
    }  

    public void getFrequentItems(String fileName)throws Exception  
    {  
    	BufferedReader br=null;  
    	br=new BufferedReader(new FileReader(fileName));  
    	String stline=null;  
    	splitkey=",";  
    	StringTokenizer fileToken=null;  
    	itemcounter = new int[itemsData.size()];  
    	Hashtable<String, String> hashdata=new Hashtable<String, String>();  
    	while((stline=br.readLine())!=null)  
    	{  
    		hashdata=new Hashtable<String, String>();  
    		fileToken = new StringTokenizer(stline, splitkey);   
    		for(int j=0; j<items.length; j++)  
    		{  
    			if(fileToken.hasMoreTokens())  
    			{  
    				String token=fileToken.nextToken();  
    				hashdata.put(token, token);  
    			}  
    		}  
    		for(int items=0; items<itemsData.size(); items++)  
    		{  
    			boolean match = false;   
    			StringTokenizer st = new StringTokenizer(itemsData.get(items));  
    			int count=0;  
    			int xcount=0;  
    			while(st.hasMoreTokens())  
    			{  
    				String token=st.nextToken();  
    				if(hashdata.get(token)!=null)  
    				{  
    					match=true;  
    				}  
    				else  
    					match=false;               
    				if(!match)   
    					break;  
    			}  
    			if(match)   
    			{               
    				itemcounter[items]++;  
    			}  
    		}  
    		num_trans++;  
    	}  
    }  
    public void calculateFrequentItems()  
    {  
    	Vector<String> frequentItems=new Vector<String>();
    	for(int i=0; i<itemsData.size(); i++)  
    	{  
    		if((itemcounter[i]/(double)num_trans)>=min_sup)  
    		{  
    			frequentItems.add(itemsData.get(i));  
    			confidence.put(itemsData.get(i),itemcounter[i]);  
    		}  
    	}  
    	supportData.add(confidence);  
    	itemsData.clear();  
    	itemsData = new Vector<String>(frequentItems);  
    	frequentItems.clear();  
    }  
}  

