package matrixgenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author carolynguzman
 */
public class MatrixGenerator {

    private final File file;
    private final String outputDirectory;
    private ArrayList<String> barcodeArray, geneArray, valueArray;

    public MatrixGenerator(File file, String outputDirectory) {
        this.file = file;
        this.outputDirectory = outputDirectory + "/seurat";
    }

    void readFile() throws mtxGenException, FileNotFoundException, IOException {
        //Throws an exception if the file type is not a text file 
        if (!file.getPath().endsWith("txt")) {
            throw new mtxGenException("format");
        }
        //Throws an exception if the path exists 
        File tmpDir = new File(outputDirectory);
        if(tmpDir.exists()) {
            throw new mtxGenException("exists");
        }        

        (new File(outputDirectory)).mkdirs();
        
        barcodeArray = new ArrayList<>();
        geneArray = new ArrayList<>();

        Scanner sc = new Scanner(file);
        boolean firstLine = true;
        int count = 0;

        while (sc.hasNextLine()) {

            String line = sc.nextLine();
            Scanner scLine = new Scanner(line);

            if (firstLine) {
                while (scLine.hasNext()) {
                    barcodeArray.add(scLine.next());
                }
                firstLine = false;
                Files.write(Paths.get(outputDirectory + "/barcodes.tsv"),
                        barcodeArray,
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND);
                continue;
            }

            count++;
            geneArray.add(count + "\t" + scLine.next());

        }

        Files.write(Paths.get(outputDirectory + "/genes.tsv"),
                geneArray,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
        generateArray();
    }

    private void generateArray() throws FileNotFoundException, IOException {
        valueArray = new ArrayList<>();
        int row = 1;
        int column = 1;
        double value;

        Scanner sc = new Scanner(file);
        sc.nextLine();
        
        valueArray.add("%%MatrixMarket matrix coordinate real general\n%");
        valueArray.add(geneArray.size() + " " + barcodeArray.size());

        while (sc.hasNextLine()) {

            String line = sc.nextLine();
            Scanner scLine = new Scanner(line);
            scLine.next();

            while (scLine.hasNext()) {
                
                value = scLine.nextDouble();
                if(value > 0) {
                    valueArray.add(row + " " + column + " " + value);
                }
                column++;
            }
            column = 1;
            row++;
        }
        
        valueArray.set(1, geneArray.size() + " " + (barcodeArray.size() - 1) + " " + (valueArray.size() - 2));
        
        Files.write(Paths.get(outputDirectory + "/matrix.mtx"),
                valueArray,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

}
