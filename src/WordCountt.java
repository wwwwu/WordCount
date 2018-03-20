import java.io.*;
import java.util.*;

public class WordCountt {
	public static int words = 0;
	public static int lines = 1;
	public static int chars = 0;
	public static int whiteL = 0;
	public static int commentL = 0;
	public static int codeL = 0;

	public static boolean writeFile(String content, String filePath, boolean append){
		//生成结果文件的方法
		boolean flag = false;
		File thisFile = new File(filePath);
		
			try{
				if(!thisFile.exists()){
					thisFile.createNewFile();
				}
			
			FileWriter fw = new FileWriter(filePath, append); //append表示可追加
			fw.write(content);
			fw.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
			flag = true;
		
		return flag;
	}

	public static void main(String[] args) throws Exception{
		if(args.length < 2) {
			throw new IllegalArgumentException();
		} //如果输入参数小于2个长度则抛出异常
		if(!args[0].equals("-c")&&!args[0].equals("-w")
				&&!args[0].equals("-l")&&!args[0].equals("-s")
				&&!args[0].equals("-o")&&!args[0].equals("-e")
				&&!args[0].equals("-a")){
			throw new IllegalArgumentException();
		} //如果输入的参数不是有效的则抛出异常
		int i = 0;
		int fc = 0,fo = 0,fw = 0,fl = 0,fs = 0,fe = 0,fa = 0;
		//根据输入对有效参数的状态进行处理
		while(args[i].equals("-c")||args[i].equals("-w")||args[i].equals("-l")||args[i].equals("-s")||args[i].equals("-a")){
		switch(args[i]){
		case "-c" :
			fc = 1;
			i++;
			break;
		case "-w" :
			fw = 1;
			i++;
			break;
		case "-l" :
			fl = 1;
			i++;
			break;
		case "-s" :
			fs = 1;
			i++;
			break;
		case "-a" :
			fa = 1;
			i++;
			break;
		}
		}
		String filePath = null;
		String outPath = null;
		String stopPath = null;
		filePath = args[i];
		i = i + 1;
		//由于要给地址赋值，单独处理-o和-e
		while(i < args.length){
			if(args[i].equals("-o") && fo == 0){
				fo = 1;
				i++;
				if(i == args.length){
					throw new IllegalArgumentException();
				}
				outPath = args[i];
				i++;
			}
			else if(args[i].equals("-e") && fe == 0){
				fe = 1;
				i++;
				if(i == args.length){
					throw new IllegalArgumentException();
				}
				stopPath = args[i] ;
				i++;
				
			}
		}
		//停用表处理
		ArrayList<String> stopL = new ArrayList<>(0);
		if(fe == 1){
			BufferedReader stopBr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(stopPath))));
			String para = null;
			for(;(para = stopBr.readLine()) != null;){
				String resultArr[] = para.split(" ");
				for(i=0;i<resultArr.length;i++){
                
					if(!resultArr[i].isEmpty())
                    stopL.add(resultArr[i]);
					
				}
			}
			stopBr.close();	
		}
		ArrayList<String> fileP = new ArrayList<String>();
		File file = new File(filePath);
		//生成结果文件
		if(fs == 0){
		String res = readFile(fc, fw, fl, fa, filePath, stopL);
		if(fo == 1){
		writeFile(res, outPath, true);
			}
		}
		else{
			//处理同目录下的文件
			String fileName = file.getName();
			String suffix = fileName.substring(fileName.lastIndexOf("*") + 1);
			String pa[] = filePath.split("/");
			//如果是根目录
			if(pa.length == 1){
				file = new File(System.getProperty("user.dir"));
				File l[] = file.listFiles();
				for(i = 0; i < l.length; i++){
					if(l[i].getName().endsWith(suffix)){
						fileP.add(l[i].getPath());
					}
				}
				}
			//如果不是根目录
			else{
			File l[] = file.listFiles();
			for(i = 0; i < l.length; i++){
				if(l[i].getName().endsWith(suffix)){
					fileP.add(l[i].getPath());
				}
			}
			
			}for(i = 0; i < fileP.size(); i++){
				String res = readFile(fc, fw, fl, fa, fileP.get(i), stopL);
				if(fo == 1){
				writeFile(res, outPath, true);
				}
				
			}
		}
	}
	        //读取文件的方法
	public static String readFile(int fc, int fw, int fl, int fa, String fileP, ArrayList<String> stopL) throws IOException{
		
			File file = new File(fileP);

			FileReader f = new FileReader(file);
			BufferedReader br =  new BufferedReader(new FileReader(file));  
					
 			int c = 0;
 			int k = 0;
 			int m = 0;
 			int fes = 0;
 			String str = null;
//			boolean lastNotWhite = false;
//			String whiteSpace = " ,\t\n\r";
 			//统计单词数
			while((str = br.readLine()) != null){
			String[] r = str.split("\\s+|,");	
			for(m = 0; m < r.length; m++){	
				if(r[m].length() != 0){
			if(stopL.size()!=0){ //如果使用了停用表
                 fes=0;
                 for(k=0;k<stopL.size();k++) {
                     if (r[k].equals(stopL.get(k))) {
                         fes = 1;
                         break;
                     }
                 }
                 if(fes!=1) { //如果单词不在停用表里
                 
                	 words++;
                 }
                 }
                 else{
                	 words++;
                 }
				}
		    }
	}
			//统计行数和字符数
			while((c = f.read()) != -1){
				chars++;
				if(c == '\n'){
					lines++;
				}
				
//				if(whiteSpace.indexOf(c) != -1){
//					if(lastNotWhite){
////					  words++;
//					}
//					lastNotWhite = false;
//				}
//				else{
//					lastNotWhite = true;
//				}

			}
			//统计注释行和空行、代码行
			String line = "";   
			boolean comment = false;
            try {   
                while ((line = br.readLine()) != null) {  
                	
                    line = line.trim();   
                    if (line.matches("^[\\s&&[^\\n]]*$")) {   
                        whiteL++;   
                    } else if (line.startsWith("/*") && !line.endsWith("*/")) {   
                        commentL++;   
                        comment = true;   
                    } else if (true == comment) {   
                        commentL++;   
                        if (line.endsWith("*/")) {   
                            comment = false;   
                        }   
                    } else if (line.startsWith("//")) {   
                            commentL++;   
                    } else {   
                        codeL++;   
                    }   
                }   
            } catch (IOException e) {   
                    e.printStackTrace();   
            }   
            //根据输入将统计结果写入文件
			StringBuffer t = new StringBuffer();
			if(fc == 1){
				t.append(file.getName() + ", 字符数: " + chars + "\n");
			}
			if(fw == 1){
				t.append(file.getName() + ", 单词数: " + words + "\n");
			}
			if(fl == 1){
				t.append(file.getName() + ", 行数: " + lines + "\n");
			}
			if(fa == 1){
				t.append(file.getName() + ", 代码行/空行/注释行：" + codeL + "/" + whiteL + "/" + commentL + "\n");
			}
			String re = t.toString();
			return re;
			
		}
		}
	


