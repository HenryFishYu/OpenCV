import java.util.*;

public class ACM1 {
    public static void main(String args[]){
        Scanner scanner=new Scanner(System.in);
        List<String> list=new LinkedList<String>();
        List<String> defaultList=new ArrayList<String>();
        defaultList.add("ABCDEFGHI");
        defaultList.add("JKLMNOPQR");
        defaultList.add("STUVWXYZ ");
            for (int i = 0; i < 2; i++) {
                list.add(scanner.nextLine());
            }
            String result = "";
            String dateResults[] = list.get(0).split(" ");
            int forwardTimes = (Integer.valueOf(dateResults[0]) - 1) % 3;
            listMoveForward(defaultList, forwardTimes);
            for (int i = 0; i < list.get(1).length(); i++) {
                for (int t = 0; t < defaultList.size(); t++) {
                    int position = defaultList.get(t).indexOf(String.valueOf(list.get(1).charAt(i)));
                    if (position >= 0) {
                        result += String.valueOf(t + 1) + String.valueOf((position + 18 - (Integer.valueOf(dateResults[1]) - 1)) % 9 + 1) + " ";
                    }
                }

            }
            System.out.println(result.substring(0,result.length()-1));
    }
    public static void listMoveForward(List list,int times){
        for(int i=0;i<times;i++) {
            for(int t=0;t<list.size()-1;t++){
                Collections.swap(list,t,t+1);
            }

        }
    }
}
