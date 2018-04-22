package wekatest;

import java.io.BufferedReader;  
import java.io.BufferedWriter;  
import java.io.FileNotFoundException;  
import java.io.FileReader;  
import java.io.FileWriter;  
import java.io.IOException;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;  
import weka.core.converters.ConverterUtils.DataSource;

public class KMCluster {
	
	private Instances insdata;
	private String sourceFile;
	private String targetFile;
	private int numCluster;
	private int[] assignment;
	
	public KMCluster(String sourceFile,String targetFile,int numCluster){
		this.sourceFile=sourceFile;
		this.targetFile=targetFile;
		this.numCluster=numCluster;	
	}
	
	public void loadData() throws Exception{
		DataSource dataSource=new DataSource(sourceFile);
		insdata=dataSource.getDataSet();
	}
	
	public void cluster() throws Exception{
		SimpleKMeans KM=new SimpleKMeans();
		
		//set options
		String[] options=new String[4];
		options[0]="-N";
		options[1]=Integer.toString(numCluster);
		options[2]="-S";
		options[3]="10";
		
		KM.setOptions(options);
		KM.buildClusterer(insdata);
		
		//collect results
		assignment=new int[insdata.numInstances()];
		for(int j=0;j<insdata.numInstances();j++){
			assignment[j]=KM.clusterInstance(insdata.instance(j));
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
		   KMCluster cluster1=new KMCluster("YOUR_INPUT_PATH"+i+".csv","YOUR_OUTPUT_PATH"+i+".csv",3);
         	   cluster1.process();
		}
	}

}
