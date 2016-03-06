import javax.swing.text.html.parser.DocumentParser;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by pavi on 3/1/2016.
 */
public class MinHash {

    Map<String, Integer> fileList;
    ArrayList<Set<String>> terms;
    ArrayList<Set<Integer>> termIDs;
    Map<String,Integer> MasterTermIDTable;
    int [][] minHash;
    int fileCount,noOfPermutations,prime,currFileID,termsCollectionCount;
    DocumentParser parser;
    Integer[][] RandAB;
    int [] randomNumbers;

    MinHash(String folder, int numPermutations){

        fileCount = 0;
        fileList = new HashMap<String, Integer>();
        noOfPermutations = numPermutations;
        //parse each file - form a set and min signature
        ProcessFolder(folder);
    }
    String[] allDocs(){
        return fileList.keySet().toArray(new String[fileList.keySet().size()]);
    }
    //signature of the file given in argument
    int[] minHashSig(String fileName) {

        return new int[0];
    }

    //Jaccard Similarity of the files passed as arguments
    double exactJaccard(String file1, String file2){

        int file1ID = fileList.get(file1);
        int file2ID = fileList.get(file2);

        Set union = new TreeSet<String>();
        union.addAll(termIDs.get(file1ID));
        union.addAll(termIDs.get(file2ID));

        System.out.println("Set 1 is "+termIDs.get(file1ID).toString() +"  \n\n set 2 is "+termIDs.get(file2ID));
        Set intersection = new TreeSet<Integer>(termIDs.get(file1ID));
        intersection.retainAll(termIDs.get(file2ID));
        double jac = (double)intersection.size()/union.size();
        System.out.println(" the exact jaccard similarity is "+ intersection.size()+"- "+union.size() +" "+jac);
        return 0.0;
    }

    //approxJac of the files passed as args
    double  approximateJaccard(String file1, String file2){

        int file1ID = fileList.get(file1);
        int file2ID = fileList.get(file2);

        InitializePermutationFunction();
        ProcessMinHashSignature(file1ID);
        ProcessMinHashSignature(file2ID);

        int equalMinCount=0;
        for(int i = 0 ; i < numPermutations(); i ++)
        {
            if(minHash[i][file1ID] == minHash[i][file2ID])
            {
                equalMinCount++;
            }
        }
        double jac = (double)equalMinCount/noOfPermutations;
        System.out.println(" the approximate jaccard similarity is " +jac);
        return 0.0;
    }
    int[][] minHashMatrix(){

       // for(int i = 0; i <)
        return minHash;
    }
    // Returns the number of terms in the document collection.
    double numTerms(){

        return MasterTermIDTable.size();
    }
    //Returns the number of permutations used to construct the MinHash matrix.
    int numPermutations (){

        return noOfPermutations;
    }

    void ProcessFolder(String folder)
    {

        File[] files = new File(folder).listFiles();

        fileCount =0;
        for (File file : files) {
            if (file.isFile() && file.getName().contains(".txt")) {
               fileCount++;
            }
        }

        currFileID=0;
        minHash = new int[noOfPermutations][fileCount];

        terms = new ArrayList<Set <String>>(fileCount);

        termIDs = new ArrayList<Set <Integer>>(fileCount);
        MasterTermIDTable = new HashMap<String, Integer>();
        files = new File(folder).listFiles();
        int fileID = 0;
        for(File file : files){
            if (file.isFile() && file.getName().contains(".txt")) {
                ProcessFile(file);
                fileList.put(file.getName(),fileID );
                fileID++;
                currFileID++;
            }
        }
    }
    void ProcessFile(File file) {

        String fileContent =null;
        fileContent = FilterContent(file);
        if(fileContent != null)
        {
            terms.add(currFileID,new TreeSet<String>());
            termIDs.add(currFileID,new TreeSet<Integer>());
            fileContent.trim();
            for(String str : fileContent.split(" +")) {
                if(str.length() >2 && (!str.equalsIgnoreCase("the"))) {

                    Integer termid = null;


                    if(!MasterTermIDTable.containsKey(str) )
                    {
                        MasterTermIDTable.put(str,termsCollectionCount);
                        termid = termsCollectionCount;
                        termsCollectionCount++;
                    }
                    else
                    {
                        termid = MasterTermIDTable.get(str) ;
                    }
                    //find term id
                    // add it to termsset
                    //terms.get(currFileID).add(str);
                    termIDs.get(currFileID).add(termid);


                }
            }
        }
    }
    String FilterContent(File file){
        Scanner scanner=null;
        try {

            byte[] encoded = new byte[0];
            StringBuilder fileContents = new StringBuilder((int)file.length());
            scanner = new Scanner(file);

            while(scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine()+" ");
            }

            String fileContent = fileContents.toString();

            fileContent = fileContent.replaceAll("[.,:;\"']"," ");
            fileContent = fileContent.toLowerCase();
            return fileContent;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            scanner.close();
        }
        return null;
    }
    /*void printTerms()
    {
        System.out.println("Terms are "+terms.toString());
    }*/
    void ProcessMinHashSignature(int fileID){

        for(int i=0; i < noOfPermutations; i ++)
        {
            minHash[i][fileID] = Integer.MAX_VALUE;
        }
        for(int id : termIDs.get(fileID))
        {
            for(int i =0; i< noOfPermutations; i++) {

                int hashval = ((RandAB[i][0] * id) + RandAB[i][1]);
                hashval = hashval % prime;
                if (minHash[i][fileID] > hashval) {
                    minHash[i][fileID] = hashval;
                }
            }

        }

    }
    void InitializePermutationFunction()
    {
        RandAB = new Integer[noOfPermutations][2];
        Random rand = new Random();
        prime = getNextPrime(termsCollectionCount);
        for(int i=0; i< noOfPermutations; i++)
        {
            RandAB[i][0] = rand.nextInt(noOfPermutations  );
            RandAB[i][1] = rand.nextInt(noOfPermutations  );
        }
    }

    public int getNextPrime(int limit)
    {
        while(true)
        {
            Boolean isPrime = Boolean.TRUE;
            for(int i=2;2*i<limit;i++) {
                if(limit%i==0)
                    isPrime= Boolean.FALSE;
            }
            if(isPrime)
            {
                return limit;
            }
            else
                limit++;
        }
    }
}