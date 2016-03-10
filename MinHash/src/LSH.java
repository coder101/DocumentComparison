import java.util.*;

/**
 * Created by pavi on 3/9/2016.
 */
public class LSH {


    List<HashMap<Integer,HashSet<String>> > lsh;

    public LSH(int[][] minHashMatrix, String[] docNames, int bands) {

        //build lsh
        lsh = new ArrayList<HashMap<Integer,HashSet<String>>>(bands) ;

        for(int i =0; i < bands; i++){
            lsh.add(new HashMap<Integer,HashSet<String>>());
        }

        int signatureLength = minHashMatrix.length;
        Random rand = new Random();
        int prime = getNextPrime(30000);
        int RandA =rand.nextInt(prime);
        int RandB =rand.nextInt(prime);


        int docCount = minHashMatrix[0].length;
        int bandRowLimit = signatureLength/bands;
        for(int i =0; i < docCount ; i++) {
            int hash = 0;
            int bandIndex=0;
            HashSet<String> dupDocs =  new HashSet<String>();
            for(int j = 0 ; j < signatureLength; j ++){


                hash += (minHashMatrix[j][i]*RandA)+RandB;
                hash = hash %prime;
                dupDocs.add(docNames[i]);
                if((j+1)%bandRowLimit ==0){

                    if(!lsh.get(bandIndex).containsKey(hash))
                    {
                        lsh.get(bandIndex).put(hash,dupDocs);
                    }
                    else
                    {
                        lsh.get(bandIndex).get(hash).addAll(dupDocs);
                    }
                    hash=0;
                    bandIndex++;
                    dupDocs =  new HashSet<String>();
                }



            }
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
    ArrayList<String> nearDuplicatesOf(String docName) {

        HashSet<String> nearDuplicateDocs;
        nearDuplicateDocs = new HashSet<String>();

        for(int i = 0; i < lsh.size();i++)
        {

           for(int j : lsh.get(i).keySet()){
               if(lsh.get(i).get(j).contains(docName)){
                   nearDuplicateDocs.addAll(lsh.get(i).get(j));
               }
            }
        }
        return new ArrayList<String>(nearDuplicateDocs);
    }



}
