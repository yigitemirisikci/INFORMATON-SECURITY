import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class IOservice {
    public static ArrayList<String> getInput(String filename) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        BufferedReader brParts= new BufferedReader(
                new FileReader(filename));
        String line;
        while ((line = brParts.readLine()) != null) {
            lines.add(line);
        }
        for(int i=0;i<lines.size()-1;i++){
            lines.set(i,lines.get(i)+"\n");
        }
        ArrayList<String> eightByteWords = new ArrayList<>();
        for(String l:lines){
            for(int i=0;i<l.length();i+=8){ // 0 8 16 24
                if(i+8<l.length()){
                    eightByteWords.add(l.substring(i,i+8));
                }
                else{
                    int len = l.length()-i;
                    String word = l.substring(i,i+len);
                    while(8-len > 0){
                        word += " ";
                        len++;
                    }
                    eightByteWords.add(word);
                }
            }
        }
        return eightByteWords;
    }

    public static ArrayList<String> getKey(String filename){
        ArrayList<String> rArr = new ArrayList<>();
        try {
            Scanner myReader = new Scanner(new File(filename));
            String[] all = myReader.nextLine().split(" - ");
            rArr.add(all[1]);
            rArr.add(all[0]);
            rArr.add(all[2]);
            myReader.close();
        } catch (Exception e) {
            System.out.println("error");
        }
        return rArr;
    }
    public static byte[] readByBytes(String filename) throws IOException {
        File file = new File(filename);
        byte[] rArr = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(rArr);
        fis.close();
        return rArr;
    }
}
