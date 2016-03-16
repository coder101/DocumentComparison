import java.util.ArrayList;

/**
 * Created by PAvithra Rajarathinam & Anubav on 3/9/2016.
 */
public class NearDuplicates {

    public static void main( String [] args) {


        String folder;
        int numPerm;
        numPerm = 400;
       // folder = "D:\\subs\\coms535x\\pa2\\pa2";
        folder = args[0];// folder name

        numPerm = Integer.valueOf(args[1]);//Number of Permutations to be used for MinHash
        int bands = Integer.valueOf(args[2]);//Number of Bands to be used in locality sensitive hashing

        Double similarity = Double.valueOf(args[3]);// Similarity threshold s.
        String docName = args[4];// Name of a document from the collection, docName
        MinHash mHash = new MinHash(folder,numPerm);

        int[][] matrix = mHash.minHashMatrix();
        String [] docs = mHash.allDocs();
        LSH lsh1 = new LSH(matrix,docs,bands);
        ArrayList<String> dup = lsh1.nearDuplicatesOf(docName);
        System.out.println("For the doc " +docName+ " bands  "+ bands+" similarity of "+similarity);
        System.out.println("Near Duplicates are - count "+dup.size()+" list "+ dup.toString());

        for(int i=0; i < dup.size(); i ++)
        {
             double jac = mHash.exactJaccard(docName,dup.get(i));
             if(jac < similarity){
                dup.remove(i);
             }
        }

        System.out.println("\n\nAfter removing  are "+dup.size()+" list "+ dup.toString());

    }

}
