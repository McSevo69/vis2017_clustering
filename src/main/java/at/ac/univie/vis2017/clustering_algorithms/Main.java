package at.ac.univie.vis2017.clustering_algorithms;

import ch.netzwerg.paleo.DataFrame;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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

        try {
            while ((line = br.readLine()) != null) {

                System.out.println(line + "\n");

            }
            br.close();
        }
        catch (IOException e) {
            System.out.println("ERROR: unable to read file " + file);
            e.printStackTrace();
        }

        return data;
    }




    public static void main(String[] args) throws IOException {


        String fileName = "dbscan1.txt";
        String filePath = System.getProperty("user.dir") + "/src/main/data/";

        DataFrame dat = getDataFromTxt(filePath, fileName);

    }
}
