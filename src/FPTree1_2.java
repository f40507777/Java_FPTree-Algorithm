import java.io.*;
import java.util.*;

import javax.swing.tree.DefaultMutableTreeNode;

public class FPTree1_2 {
	static long time,lastime=0;
	static int support=500;
	static int taltaol=0;
	static DefaultMutableTreeNode FPTree=new DefaultMutableTreeNode();
	static HashMap<Integer, Integer> datacount =new HashMap<Integer, Integer>();
	static HashMap<Object, Integer> nodecount =new HashMap<Object, Integer>();
	static HashMap<Integer, ArrayList<Object>> nodelist =new HashMap<Integer, ArrayList<Object>>();
	public static void main(String[] args)throws IOException {
		// TODO Auto-generated method stub
		clockstart();
		DefaultMutableTreeNode node=new DefaultMutableTreeNode("root");
		FPTree=node;
		readBinary();
		clockend();
	}
	public static void readBinary()throws IOException{	//Ū��binary�òέp����
    	File infile = new File("./T20I5N100KD100K.data");
        byte[] buffer = new byte[(int)infile.length()];
        FileInputStream inputStream = new FileInputStream(infile);
        int nRead = 0;
        int bigIndex;
        int [] count = new int[65536];
        while((nRead = inputStream.read(buffer)) != -1)
        {
        	for (int i = 0; i<nRead; i=i+4) {
        		bigIndex =buffer[i]<<8 | buffer[i+1];
        	    if(bigIndex<0){//�p�G�p��0�hmask ��unsign
        	    	bigIndex=bigIndex &0xFFFF;
        	    }
        	    count[bigIndex]++;
        	}
        }
        inputStream.close();
		System.out.print("�p������ ");
		spendclock();
		System.out.println(count[46357]);
        sort(count);
        sortItem(count);
	}
	public static void sort(int count[]) throws IOException{//Ū���C��
    	File infile = new File("./T20I5N100KD100K.data");
        byte[] buffer = new byte[(int)infile.length()];
        FileInputStream inputStream = new FileInputStream(infile);
        int nRead = 0;
        int enter=0;
        int bigIndex;
        HashMap<Integer, Integer> sortmap=new HashMap<Integer, Integer>();
        while((nRead = inputStream.read(buffer)) != -1)
        {
        	for (int i = 0; i<nRead; i=i+4) {
        		bigIndex =buffer[i]<<8 | buffer[i+1];
        	    if(bigIndex<0){//�p�G�p��0�hmask ��unsign
        	    	bigIndex=bigIndex &0xFFFF;
        	    }
        	    if(enter!=(buffer[i+2]<<8 | buffer[i+3])){
        	    	enter=(buffer[i+2]<<8 | buffer[i+3]);  
        	    	if(sortmap.size()!=0){
	        	    	sortmaps(sortmap);
        	    	}
        	    	sortmap=new HashMap<Integer, Integer>();
        	    }
        	    if(count[bigIndex]>support){
        	    	sortmap.put(bigIndex, count[bigIndex]);
        	    }
        	}
        }
        inputStream.close();
		System.out.print("�ؾ𧹦� ");
		spendclock();
	}
	public static void sortmaps(HashMap<Integer, Integer> sortmap){//�C��i��Ƨ�
		
		List<Map.Entry<Integer, Integer>> Sortlist =new ArrayList<Map.Entry<Integer, Integer>>(sortmap.entrySet());
		Collections.sort(Sortlist, new Comparator<Map.Entry<Integer, Integer>>(){
            public int compare(Map.Entry<Integer, Integer> entry1,
                               Map.Entry<Integer, Integer> entry2){
                return (entry2.getValue() - entry1.getValue());
            }
        });
		int[] array=new int[Sortlist.size()];
		for(int i=0;i<Sortlist.size();i++){
			array[i]=Sortlist.get(i).getKey();
		}
		FPTree=(DefaultMutableTreeNode) FPTree.getRoot();
		createTree(array);

	}
	public static void createTree(int[] array){//�ؾ�
		outerloop:
		for(int i=0;i<array.length;i++){
			DefaultMutableTreeNode childnode=new DefaultMutableTreeNode(array[i]);
			for(int search=0;search<FPTree.getChildCount();search++){
				if(FPTree.getChildAt(search).toString().equals(Integer.toString(array[i]))){
					FPTree=(DefaultMutableTreeNode) FPTree.getChildAt(search);
					nodecount.put(FPTree,nodecount.get(FPTree)+1);
					continue outerloop;
				}
			}
			FPTree.add(childnode);
			FPTree=childnode;

			nodecount.put(FPTree,1);
			if(nodelist.get(array[i])==null){
				nodelist.put(array[i], new ArrayList<Object>());
			}
			nodelist.get(array[i]).add(FPTree);
		}
	}
	
	public static void sortItem(int[] array){//�`ITEM���Ƨ�
		int temp=0;
		for(int i=0;i<array.length;i++){
			if((temp=array[i])>support){
				datacount.put(i, temp);
			}
		}
		List<Map.Entry<Integer, Integer>> Sortlist =new ArrayList<Map.Entry<Integer, Integer>>(datacount.entrySet());
		Collections.sort(Sortlist, new Comparator<Map.Entry<Integer, Integer>>(){
            public int compare(Map.Entry<Integer, Integer> entry1,
                               Map.Entry<Integer, Integer> entry2){
                return -(entry2.getValue() - entry1.getValue());
            }
        });

		int[] rearray=new int[Sortlist.size()];
		for(int i=0;i<Sortlist.size();i++){
			rearray[i]=Sortlist.get(i).getKey();
		}
		datamining(rearray);
	}
	public static void datamining(int[] array){//�q�̤p�}�l���W����
		int markcount=0;
		for(int i=0;i<array.length;i++){
			HashMap<String, Integer> apri =new HashMap<String, Integer>();
			for(int j=0;j<nodelist.get(array[i]).size();j++){
				DefaultMutableTreeNode searchnode=new DefaultMutableTreeNode(nodelist.get(array[i]).get(j));	
				searchnode=(DefaultMutableTreeNode) searchnode.getUserObject();
				markcount=nodecount.get(searchnode);

				while( searchnode.getParent()!=null){
					if(apri.get(searchnode.toString())==null){
						apri.put(searchnode.toString(),0);
					}
					apri.put(searchnode.toString(), apri.get(searchnode.toString())+markcount);
					searchnode=(DefaultMutableTreeNode) searchnode.getParent();
				}
			}
			ArrayList finalitem = new ArrayList();
			System.out.print("\n"+array[i]+"=	");
			//System.out.print(apri);
			Iterator iter = apri.entrySet().iterator(); 
			while (iter.hasNext()) { 
			    Map.Entry entry = (Map.Entry) iter.next(); 
			    if((int) entry.getValue()>support-2){
			    	finalitem.add(entry.getKey());
			    }  
			}
			System.out.print(finalitem);
			int w=0;
			for(int z=0;z<finalitem.size();z++){
				w=w*2+1;
			}
			System.out.print("	size="+w);
			taltaol=taltaol+w;
		}
		System.out.println();
		System.out.println("�`�@���p��="+taltaol);
		System.out.print("�������� ");
		spendclock();
	}
	public static void clockstart(){
		time=System.currentTimeMillis();//�p�ɶ}�l
	}
	public static void spendclock(){
		System.out.println("	�Ӯ�= "+((System.currentTimeMillis()-time)-lastime)/1000f+" �� ");//�������ɶ�
		lastime=System.currentTimeMillis()-time;
	}
	public static void clockend(){
		System.out.println("�`�@����Ӯ�= "+(System.currentTimeMillis()-time)/1000f+" �� ");//�������ɶ�	
	}
}
