package wekatest;

import java.io.BufferedReader;  
import java.io.BufferedWriter;  
import java.io.FileNotFoundException;  
import java.io.FileReader;  
import java.io.FileWriter;  
import java.io.IOException;

import weka.clusterers.HierarchicalClusterer;
import weka.core.Instances;  
import weka.core.converters.ConverterUtils.DataSource;

public class HierarchicalCluster {
	
	private Instances insdata;
	private String sourceFile;
	private String targetFile;
	private int numCluster;
	private int[] assignment;
	
	public HierarchicalCluster(String sourceFile,String targetFile,int numCluster){
		this.sourceFile=sourceFile;
		this.targetFile=targetFile;
		this.numCluster=numCluster;	
	}
	
	public void loadData() throws Exception{
		DataSource dataSource=new DataSource(sourceFile);
		insdata=dataSource.getDataSet();
	}
	
	public void cluster() throws Exception{
		HierarchicalClusterer HC=new HierarchicalClusterer();
		
		//set options
		String[] options=new String[4];
		options[0]="-N";
		options[1]=Integer.toString(numCluster);
		options[2]="-L";
		options[3]="AVERAGE";
		
		HC.setOptions(options);
		HC.buildClusterer(insdata);
		assignment=new int[insdata.numInstances()];
		//System.out.println(HC.toString()); 
		for(int j=0;j<insdata.numInstances();j++){
			assignment[j]=HC.clusterInstance(insdata.instance(j));
		}
	}
	
	public void writeResult() throws Exception{
		FileReader fr=null;
		BufferedReader br=null;
		FileWriter fw=null;
		BufferedWriter bw=null;
		
		String line=null;
		int j=0;
		
		try{
			fr=new FileReader(sourceFile);
			br=new BufferedReader(fr);
			fw=new FileWriter(targetFile);
			bw=new BufferedWriter(fw);
			
			line=br.readLine();
			bw.write(line+",cluster\n");
			while((line=br.readLine())!=null){
				bw.write(line+","+assignment[j++]+"\n");
			}
		}finally{
			if (br!=null){
				br.close();}
			if(bw!=null){
				bw.close();}
		}
	}
	
	public void process() throws Exception{
		loadData();
		cluster();
		writeResult();
	}

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		int i;
		for(i=0;i<100;i++){
		   HierarchicalCluster cluster1=new HierarchicalCluster("YOUR_INPUT_PATH"+i+".csv","YOUR_OUTPUT_PATH"+i+".csv",3);
		   cluster1.process();

		}
	}

}
