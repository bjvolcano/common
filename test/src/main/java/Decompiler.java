/*
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;

import java.io.*;
import java.util.List;

public class Decompiler {
    public static void main(String[] args) throws IOException, InterruptedException {
        String rootPath = "D:\\workspace\\2021\\decode";
        List<String> fileNames = FileUtil.listFileNames(rootPath);
        if (CollectionUtil.isEmpty(fileNames)) {
            return;
        }

        for (String fileName : fileNames) {
            if(!fileName.contains(".jar")){
                return;
            }
            Process p = Runtime.getRuntime().exec(String.format("java -cp \"D:\\Program Files\\JetBrains\\IntelliJ IDEA 2018.3.5\\plugins\\java-decompiler\\lib\\java-decompiler.jar\" org.jetbrains.java.decompiler.main.decompiler.ConsoleDecompiler -dgs=true %s "+rootPath+"\\src", String.format("%s%s%s", rootPath, File.separator, fileName)));

            System.out.println(String.format("java -cp \"D:\\Program Files\\JetBrains\\IntelliJ IDEA 2018.3.5\\plugins\\java-decompiler\\lib\\java-decompiler.jar\" org.jetbrains.java.decompiler.main.decompiler.ConsoleDecompiler -dgs=true %s "+rootPath+"\\src", String.format("%s%s%s", rootPath, File.separator, fileName)));
            InputStream is = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            p.waitFor();
            is.close();
            reader.close();
            p.destroy();
        }
    }
}*/
