package hunmanfacecomponent;

import java.util.HashMap;
import java.util.Map;

public enum Singleton {
    INSTANCE;
    private Map<Integer,String> nameMap=new HashMap<Integer,String>();
    private Singleton(){
        nameMap.put(0,"ljj");
        nameMap.put(1,"yhl");
        nameMap.put(2,"zjl");
    }
    public String getNameById(Integer temp){
        return nameMap.get(temp);
    }
}
