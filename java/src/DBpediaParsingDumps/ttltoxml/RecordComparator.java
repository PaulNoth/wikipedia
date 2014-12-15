package ttltoxml;

import java.util.Comparator;
class RecordComparator implements Comparator<RecordModel>{
	
   public int compare(RecordModel o1,RecordModel o2){
       return o1.getValue().compareTo(o2.getValue());
   }
}
