package test;

import com.nep.entity.AqiFeedback;
import com.nep.io.FileIO;

import java.util.ArrayList;
import java.util.List;

public class Test5 {
    public static void main(String[] args) {
        List<AqiFeedback> afList = new ArrayList<AqiFeedback>();
        FileIO.writeObject("aqifeedback.txt", afList);
    }
}
