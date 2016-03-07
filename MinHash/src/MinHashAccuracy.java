import java.io.File;
import java.util.Arrays;

/**
 * Created by pavi on 3/1/2016.
 */
public class MinHashAccuracy {

    public static void main( String [] args){

        MinHash Jac;
        String folder;
        folder = "D:\\subs\\coms535x\\pa2\\space\\space";
        Jac = new MinHash(folder,400);

        //Jac.exactJaccard("text1.txt","text2.txt");
        //Jac.approximateJaccard("text1.txt","text2.txt");

        int jacError=0;

        Jac.minHashMatrix();
        double errorFactor = 0.04;
        int fileCount =Jac.fileList.size();
        String [] fileNames = Jac.allDocs();
        for(int i=0; i < fileCount ; i++)
        {
            for(int j=i+1;j<fileCount;j++)
            {
                double JacExact = Jac.exactJaccard(fileNames[i],fileNames[j]);
                double JacMin = Jac.approximateJaccard(fileNames[i],fileNames[j]);

                if(Math.abs(JacExact - JacMin) > errorFactor)
                {
                    jacError ++;
                    System.out.println(fileNames[i]+" vs " + fileNames[j] +" exact = "+JacExact+" approx ="+JacMin);


                }
            }
        }

        System.out.println("The jaccard similarity with errorfactor above the limit is "+jacError);
    }
}
