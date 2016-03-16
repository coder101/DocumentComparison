import java.math.BigInteger;
import java.util.*;

/**
 * Created by PAvithra Rajarathinam & Anubav on 3/9/2016.
 */
public class LSH {


    List<HashMap<BigInteger,HashSet<String>> > lsh;
    private static final BigInteger FNV_64_INIT = new BigInteger("01000193",16);
    private static final BigInteger FNV_64_PRIME = new BigInteger("100000001b3",16);

    private static final BigInteger MOD2POW64   = new BigInteger("2").pow(64);

    public static BigInteger FNV64(final String str) {
        BigInteger  rv = FNV_64_INIT;
        final int len = str.length();
        for(int i = 0; i < len-1; i++) {

            rv = rv.xor(BigInteger.valueOf(str.charAt(i)));
            rv = rv.multiply(FNV_64_PRIME).mod(MOD2POW64);

        }
        return rv;
    }

    public LSH(int[][] minHashMatrix, String[] docNames, int bands) {

        //build lsh

        lsh = new ArrayList<HashMap<BigInteger,HashSet<String>>>(bands) ;

        for(int i =0; i < bands; i++){
            lsh.add(new HashMap<BigInteger,HashSet<String>>());
        }

        int signatureLength = minHashMatrix.length;

        int docCount = minHashMatrix[0].length;
        int bandRowLimit = signatureLength/bands;
        for(int i =0; i < docCount ; i++) {

            String hashStr= new String("");
            int bandIndex=0;
            HashSet<String> dupDocs =  new HashSet<String>();
            for(int j = 0 ; j < signatureLength; j ++){

                hashStr = hashStr + Integer.toString(minHashMatrix[j][i]);
                dupDocs.add(docNames[i]);
                if((j+1)%bandRowLimit ==0){

                    BigInteger hash = FNV64(hashStr);
                    if(!lsh.get(bandIndex).containsKey(hash))
                    {
                        lsh.get(bandIndex).put(hash,new HashSet<String>(dupDocs));
                    }
                    else
                    {
                        lsh.get(bandIndex).get(hash).addAll(new HashSet<String>(dupDocs));
                    }
                    hashStr="";
                     bandIndex++;
                    dupDocs.clear();
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

           for(BigInteger j : lsh.get(i).keySet()){
               if(lsh.get(i).get(j).contains(docName)){
                   nearDuplicateDocs.addAll(lsh.get(i).get(j));
               }
            }
        }
        return new ArrayList<String>(nearDuplicateDocs);
    }



}
