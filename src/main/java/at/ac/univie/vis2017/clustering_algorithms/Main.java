package at.ac.univie.vis2017.clustering_algorithms;

import ch.netzwerg.paleo.DataFrame;
import ch.netzwerg.paleo.io.Parser;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

public class Main {

    public Main() throws IOException {
    }

    // read data from txt-file
    // http://people.cs.nctu.edu.tw/~rsliang/dbscan/testdatagen.html
    public static DataFrame getDataFromTxt(String filePath, String fileName) throws IOException {

        // create dataframe where txt file is saved
        DataFrame data = null;
        String file = filePath + fileName;


        // create buffered reader
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line;
        String datasetString = "";

        try {
            while ((line = br.readLine()) != null) {

                datasetString+=line+"\n";

            }
            br.close();
        }
        catch (IOException e) {
            System.out.println("ERROR: unable to read file " + file);
            e.printStackTrace();
        }
        
        System.out.println(datasetString);
        
        data = Parser.tsv(new StringReader(datasetString));

        return data;
    }




    public static void main(String[] args) throws IOException {


        String fileName = "dbscan2.txt";
        String filePath = System.getProperty("user.dir") + "/src/main/data/";

        DataFrame dat = getDataFromTxt(filePath, fileName);
        
        System.out.println("TEST " + dat.getRowCount());
        //dat.getMetaData().

    }
}
