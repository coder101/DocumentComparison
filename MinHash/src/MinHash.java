import javax.swing.text.html.parser.DocumentParser;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by pavi on 3/1/2016.
 */
public class MinHash {

    BigInteger [] fileTermsBinFre;
    String[] docListNamesInOrder;
    Boolean bisMHashFormed= false;
    public Map<String, Integer> docList;
    ArrayList<Set<String>> terms;
    ArrayList<Set<Integer>> termIDs;
    Map<String,Integer> MasterTermIDTable;
    int [][] minHash;
    int fileCount,noOfPermutations,prime,currFileID,termsCollectionCount;
    DocumentParser parser;
    int[][] RandAB;
    int [] randomNumbers;

    MinHash(String folder, int numPermutations){

        fileCount = 0;
        docList = new HashMap<String, Integer>();
        noOfPermutations = numPermutations;
        //parse each file - form a set and min signature
        ProcessFolder(folder);
         InitializePermutationFunction();
    }
    String[] allDocs(){

        return docListNamesInOrder;
    }
    //signature of the file given in argument
    int[] minHashSig(String fileName) {

        return new int[0];
    }

    //Jaccard Similarity of the files passed as arguments
    double exactJaccard(String file1, String file2){

        int file1ID = docList.get(file1);
        int file2ID = docList.get(file2);

        int unionSize = termIDs.get(file1ID).size()+termIDs.get(file2ID).size();

       // System.out.println("Set 1 is "+termIDs.get(file1ID).toString() +"  \n\n set 2 is "+termIDs.get(file2ID));
        Set intersection = new HashSet<Integer>(termIDs.get(file1ID));
        intersection.retainAll(termIDs.get(file2ID));
        unionSize = unionSize - intersection.size();
        double jac = (double)intersection.size()/unionSize;

       // System.out.println(" the exact jaccard similarity is "+jac +"  "+jac1);
        return jac;
    }
    //Jaccard Similarity of the files passed as arguments
    double exactJaccardBit(String file1, String file2){

        int file1ID = docList.get(file1);
        int file2ID = docList.get(file2);

        BigInteger intersectionbit = fileTermsBinFre[file1ID];
        intersectionbit = intersectionbit.and(fileTermsBinFre[file2ID]);
        int unionSizebit = fileTermsBinFre[file1ID].bitCount()+fileTermsBinFre[file2ID].bitCount();
        unionSizebit= unionSizebit - intersectionbit.bitCount();

        double jacBit = (double)intersectionbit.bitCount()/unionSizebit;

        // System.out.println(" the exact jaccard similarity is "+jac +"  "+jac1);
        return jacBit;
    }

    //approxJac of the files passed as args
    double  approximateJaccard(String doc1, String doc2){

        if(!bisMHashFormed){
            minHashMatrix();
        }
        int doc1ID = docList.get(doc1);
        int doc2ID = docList.get(doc2);

        int equalMinCount=0;
        for(int i = 0 ; i < noOfPermutations; i ++)
        {
            if(minHash[i][doc1ID] == minHash[i][doc2ID])
            {
                equalMinCount++;
            }
        }
        double jac = (double)equalMinCount/noOfPermutations;
        return jac;
    }
    int[][] minHashMatrix(){

        if(bisMHashFormed)
            return minHash;

        bisMHashFormed= true;
        docListNamesInOrder = new String[fileCount];
        for(int docId : docList.values()) {
            ProcessMinHashSignature(docId);
        }
        for(String key :docList.keySet()){
            docListNamesInOrder[docList.get(key)]= key;
        }
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

        fileTermsBinFre = new BigInteger[fileCount];
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
                docList.put(file.getName(),fileID );
                fileID++;
                currFileID++;
            }
        }
    }
    void ProcessFile(File file) {

        String fileContent =null;
        fileContent = FilterContent(file);

        fileTermsBinFre[currFileID] = BigInteger.ZERO;

        if(fileContent != null)
        {
            //terms.add(currFileID,new TreeSet<String>());
            termIDs.add(currFileID,new HashSet<Integer>());
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

                    fileTermsBinFre[currFileID] = fileTermsBinFre[currFileID].setBit(termid);

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
        Map<Integer,Integer> RandABHash = new HashMap<Integer, Integer>(noOfPermutations);
        Random rand = new Random();
        prime = getNextPrime(termsCollectionCount);

        while(RandABHash.size() != noOfPermutations)
        {
            int ranint = rand.nextInt(noOfPermutations );
            RandABHash.put(ranint,rand.nextInt(noOfPermutations  ));
        }

        RandAB = new int[noOfPermutations][2];
        int j =0;
        for(int i : RandABHash.keySet())
        {
            RandAB[j][0]= i;
            RandAB[j][1]= RandABHash.get(i);
            j++;
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