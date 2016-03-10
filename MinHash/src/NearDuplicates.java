import java.util.ArrayList;

/**
 * Created by pavi on 3/9/2016.
 */
public class NearDuplicates {

    public static void main( String [] args) {


        String folder;
        int numPerm;
        numPerm = 400;
        folder = "D:\\subs\\coms535x\\pa2\\pa2\\pa2";
        MinHash mHash = new MinHash(folder,600);

        int[][] matrix = mHash.minHashMatrix();
        String [] docs = mHash.allDocs();
        LSH lsh1 = new LSH(matrix,docs,10);
        ArrayList<String> dup = lsh1.nearDuplicatesOf("space-0.txt");
        System.out.println("Duplicates are - count "+dup.size()+" list "+ dup.toString());

        for(int i=0; i < dup.size(); i ++)
        {
             double jac = mHash.exactJaccard("space-0.txt",dup.get(i));
             if(jac < 0.9){
                dup.remove(i);
             }
        }

        System.out.println("After removing  are "+dup.size()+" list "+ dup.toString());

    }

}
