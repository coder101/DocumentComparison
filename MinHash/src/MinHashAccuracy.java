import java.io.File;
import java.util.Arrays;

/**
 * Created by pavi on 3/1/2016.
 */
public class MinHashAccuracy {

    public static void main( String [] args){

        MinHashAccuracy experiment = new MinHashAccuracy();

        experiment.runExperiment(400,0.04);
        experiment.runExperiment(400,0.07);
        experiment.runExperiment(400,0.09);
        experiment.runExperiment(600,0.04);
        experiment.runExperiment(600,0.07);
        experiment.runExperiment(600,0.09);
        experiment.runExperiment(800,0.04);
        experiment.runExperiment(800,0.07);
        experiment.runExperiment(800,0.09);
    }
    void runExperiment(int permInput,double errorFactor){

        MinHash Jac;
        String folder;
        folder = "D:\\subs\\coms535x\\pa2\\space\\space";
        Jac = new MinHash(folder,permInput);


        int jacError=0,jacErrorBit=0;

        Jac.minHashMatrix();

        int fileCount =Jac.docList.size();
        String [] fileNames = Jac.allDocs();
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

        System.out.println("The jaccard similarity differs for errorfactor "+ errorFactor+" and permutation by "+permInput +" "+jacError +" bit error "+ jacErrorBit);
    }
}
