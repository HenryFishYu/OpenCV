import org.raistlic.common.permutation.Combination;

import java.util.*;

public class ACM2 {
    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        List<String> list = new LinkedList<String>();
        for (int i = 0; i < 2; i++) {
            list.add(scanner.nextLine());
        }
        int priceResults[] = StringToInt(list.get(1).split(" "));
        int minDistance=10000;
        List    maxList=null;
        List<Integer> tempList=new ArrayList<Integer>();
        for(Integer i:priceResults){
            tempList.add(i);
        }
        for(int i=0;i<priceResults.length+1;i++) {
            for (List<Integer> temp : Combination.of(tempList, i)){
                int tempMax=0;
                for(Integer ii:temp){
                    tempMax+=ii;
                }
                int tempDestance=Integer.valueOf(list.get(0))-tempMax;
                if(tempDestance<minDistance&&tempDestance>=0){
                    minDistance=tempDestance;
                    maxList=temp;
                }
            }
        }
        System.out.println(maxList);

}
    public static int[] StringToInt(String[] arrs){
        int[] ints = new int[arrs.length];
        for(int i=0;i<arrs.length;i++){
            ints[i] = Integer.parseInt(arrs[i]);
        }
        return ints;
    }
}
