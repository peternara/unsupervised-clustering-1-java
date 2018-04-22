package wekatest;

import java.io.BufferedReader;  
import java.io.BufferedWriter;  
import java.io.FileReader;  
import java.io.FileWriter;  
import java.util.List;

import weka.clusterers.*;
import weka.core.*;  
import weka.core.converters.ConverterUtils.DataSource;


public class DBScanCluster {
	
	private Instances insdata;
	private String sourceFile;
	private String targetFile;
	private String[] assignment;
	
	public DBScanCluster(String sourceFile,String targetFile){
		this.sourceFile=sourceFile;
		this.targetFile=targetFile;
	}
	
	public void loadData() throws Exception{
		DataSource dataSource=new DataSource(sourceFile);
		insdata=dataSource.getDataSet();
	}
	
	//public List cluster() throws Exception{
	  public void cluster() throws Exception{
		DBScan db=new DBScan();
		
		//set options
		String[] options=new String[4];
		options[0]="-E";
		options[1]="0.1";
		options[2]="-M";
		options[3]="8";
		
		
		db.setOptions(options);
		db.buildClusterer(insdata);
		
		assignment=new String[insdata.numInstances()]; 
		//System.out.println(db.toString());
		
		ClusterEvaluation eval=new ClusterEvaluation();
		eval.setClusterer(db);
		eval.evaluateClusterer(insdata);
		double[] num=eval.getClusterAssignments();

		for(int j=0;j<insdata.numInstances();j++){
			assignment[j]=String.valueOf(num[j]);
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
		   DBScanCluster cluster=new DBScanCluster("YOUR_INPUT_PATH"+i+".csv","YOUR_OUTPUT_PATH"+i+".csv");
		   cluster.process();

		   
		}
	}
}
