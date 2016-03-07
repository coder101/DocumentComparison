import java.util.Arrays;

/**
 * Created by pavi on 3/7/2016.
 */
public class MinHashSpeed {
    public static void main( String [] args){

        MinHash Jac;
        String folder;
        int numOfPerm =400;
        folder = "D:\\subs\\coms535x\\pa2\\space\\space";
        Jac = new MinHash(folder,numOfPerm);
        //Jac = new MinHash("D:\\subs\\coms535x\\pa2\\space\\space",4);


        long startTime = System.currentTimeMillis();
        int fileCount =Jac.fileList.size();
        String [] fileNames = Jac.allDocs();
        for(int i=0; i < fileCount ; i++)
        {
            for(int j=i;j<fileCount;j++)
            {
                double JacExact = Jac.exactJaccard(fileNames[i],fileNames[j]);
            }
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Total execution time: for exact = " + (endTime - startTime) );


        startTime = System.currentTimeMillis();

        Jac.minHashMatrix();
        int[][] minHashMatrix = Jac.minHashMatrix();
        for(int i=0; i < fileCount ; i++) {
            for (int j = i; j < fileCount; j++) {

                int jacL=0;
                for(int p = 0 ; p < numOfPerm; p++)
                {
                    if(minHashMatrix[p][i]==minHashMatrix[p][j])
                    {
                        jacL++;
                    }
                }
                double JacSim = (double)jacL/numOfPerm;
            }
        }

        endTime = System.currentTimeMillis();
        System.out.println("Total execution time: for approximate = " + (endTime - startTime) );
    }
}
