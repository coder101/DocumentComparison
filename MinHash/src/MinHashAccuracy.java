import java.util.Arrays;

/**
 * Created by pavi on 3/1/2016.
 */
public class MinHashAccuracy {

    public static void main( String [] args){

        MinHash Jac;
        Jac = new MinHash("D:\\subs\\coms535x\\pa2",4);
        //Jac = new MinHash("D:\\subs\\coms535x\\pa2\\space\\space",4);

        String [] str = Jac.allDocs();
        System.out.println("The files present are "+ Arrays.toString(str));

        Jac.exactJaccard("text1.txt","text2.txt");
        Jac.approximateJaccard("text1.txt","text2.txt");
    }
}
