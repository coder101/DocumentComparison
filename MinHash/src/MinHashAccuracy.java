import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by PAvithra Rajarathinam & Anubav on 3/1/2016.
 */
public class MinHashAccuracy {

    String folder;
    public static void main( String [] args){ // arg 0- folder ; 1 - permutation ;2 error factor

        MinHashAccuracy experiment = new MinHashAccuracy();


        experiment.folder = args[0];
        experiment.runExperiment(Integer.valueOf(args[1]),Integer.valueOf(2));
/*
        experiment.runExperiment(400,0.07);
        experiment.runExperiment(400,0.09);
        experiment.runExperiment(600,0.04);
        experiment.runExperiment(600,0.07);
        experiment.runExperiment(600,0.09);
        experiment.runExperiment(800,0.04);
        experiment.runExperiment(800,0.07);
        experiment.runExperiment(800,0.09);
*/
    }
    void runExperiment(int permInput,double errorFactor){

        MinHash Jac;
        //String folder;
        //folder = "D:\\subs\\coms535x\\pa2\\space\\space";
        Jac = new MinHash(folder,permInput);


        int jacError=0,jacErrorBit=0;

        Jac.minHashMatrix();


        String [] fileNames = Jac.allDocs();
        int fileCount = fileNames.length;
        for(int i=0; i < fileCount ; i++)
        {
            for(int j=i+1;j<fileCount;j++)
            {
                double JacExact = Jac.exactJaccard(fileNames[i],fileNames[j]);
                double JacExactBit = Jac.exactJaccardBit(fileNames[i],fileNames[j]);
                double JacMin = Jac.approximateJaccard(fileNames[i],fileNames[j]);

                if(Math.abs(JacExact - JacMin) > errorFactor)
                {
                    jacError ++;
                    // System.out.println(fileNames[i]+" vs " + fileNames[j] +" exact = "+JacExact+" approx ="+JacMin);
                }
                if(Math.abs(JacExactBit - JacMin) > errorFactor)
                {
                    jacErrorBit ++;
                    // System.out.println(fileNames[i]+" vs " + fileNames[j] +" exact = "+JacExact+" approx ="+JacMin);


                }
            }
        }

        System.out.println("The jaccard similarity differs for errorfactor "+ errorFactor+" and permutation by "+permInput +" "+jacError );
    }
}
