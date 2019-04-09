import Dtos.AccessControlDto;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by behnam on 4/9/19.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        String all=Utilities.ReadFileAsString("data/AC.data");
        String [] lines=all.split("\\r?\\n|\\r");

        ArrayList<AccessControlDto> accessList=new ArrayList<AccessControlDto>();
        for (String line:lines) {

            Boolean accessibility=line.split(" ")[0].equals("Y");
            String path=line.split(" ")[1];

            accessList.add(new AccessControlDto(accessibility,path));
        }

        System.out.println(CheckAccess(accessList,"A/B/E/L"));
    }

    public static boolean CheckAccess(ArrayList<AccessControlDto> accessList,String xpath)
    {
        if (xpath.equals(""))
            return false;

        for (AccessControlDto item:accessList) {

           if( item.getPath().equals(xpath))
               return item.getAccessibility();
        }

        String newXpath =xpath.substring(0,xpath.lastIndexOf('/'));
        return CheckAccess(accessList,newXpath);
    }
}


