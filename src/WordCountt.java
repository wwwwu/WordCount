import java.io.*;

public class WordCountt {
	public static int words = 1;
	public static int lines = 1;
	public static int chars = 0;

	public static boolean writeFile(String content, String filePath, boolean append){
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
		}
		if(!args[0].equals("-c")&&!args[0].equals("-w")
				&&!args[0].equals("-l")&&!args[0].equals("-s")
				&&!args[0].equals("-o")&&!args[0].equals("-e")
				&&!args[0].equals("-a")){
			throw new IllegalArgumentException();
		}
		int i = 0;
		int fc = 0,fo = 0,fw = 0,fl = 0,fs = 0,fe = 0,fa = 0;
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
		String res = readFile(fc, fw, fl, fa, filePath);
		writeFile(res, outPath, true);
		
	}
	public static String readFile(int fc, int fw, int fl, int fa, String fileP) throws IOException{
		{
			File file = new File(fileP);
			FileInputStream fin = new FileInputStream(file);
//			InputStreamReader in = new InputStreamReader(new FileInputStream(file));
			FileReader f = new FileReader(file);

					
 			int c = 0;
			boolean lastNotWhite = false;
			String whiteSpace = " ,\t\n\r";
			while((c = f.read()) != -1){
				chars++;
				if(c == '\n'){
					lines++;
				}
				if(whiteSpace.indexOf(c) != -1){
					if(lastNotWhite){
						words++;
					}
					lastNotWhite = false;
				}
				else{
					lastNotWhite = true;
				}
			}
			StringBuffer k = new StringBuffer();
			if(fc == 1){
				k.append(file.getName() + ", 字符数: " + chars + "\n");
			}
			if(fw == 1){
				k.append(file.getName() + ", 单词数: " + words + "\n");
			}
			if(fl == 1){
				k.append(file.getName() + ", 行数: " + lines + "\n");
			}
			String re = k.toString();
			return re;
			
			}
	}
}
