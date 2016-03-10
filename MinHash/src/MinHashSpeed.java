import java.util.Arrays;

/**
 * Created by pavi on 3/7/2016.
 */
public class MinHashSpeed {
    public static void main( String [] args){

        MinHashSpeed exp = new MinHashSpeed();
        exp.experimentSpeed(400);
        exp.experimentSpeed(600);
        exp.experimentSpeed(800);


    }
    void experimentSpeed(int numOfPerm){
        MinHash Jac;
        String folder;
        folder = "D:\\subs\\coms535x\\pa2\\space\\space";
        Jac = new MinHash(folder,numOfPerm);
        //Jac = new MinHash("D:\\subs\\coms535x\\pa2\\space\\space",4);


        long startTime = System.currentTimeMillis();
        int fileCount =Jac.docList.size();
        String [] fileNames = Jac.allDocs();
        for(int i=0; i < fileCount ; i++)
        {
            for(int j=i;j<fileCount;j++)
            {
                double JacExact = Jac.exactJaccard(fileNames[i],fileNames[j]);
            }
        }

        long endTime = System.currentTimeMillis();

        long jacExact = endTime - startTime;


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
        System.out.println("Total execution time:perm :"+numOfPerm+" for jac is "+jacExact+" for approximate = " + (endTime - startTime) );
    }
}
