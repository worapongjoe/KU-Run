package kusrc.worapong.preyapron.sriwan.kurun;

/**
 * Created by masterUNG on 3/8/16 AD.
 */
public class FindAvata {

    public int myFindAvata(String strAvata) {

        int[] avataInts = {R.drawable.kon48, R.drawable.rat48,
        R.drawable.bird48, R.drawable.doremon48, R.drawable.nobita48};

        int intIndex = Integer.parseInt(strAvata);

        return avataInts[intIndex];
    }

}   // Main Class
