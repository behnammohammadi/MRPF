import Dtos.AccessControlDto;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by behnam on 4/9/19.
 */
public class XpathAccessControlProvider {
    public static boolean CheckAccess(String accessControl,String xpath) throws IOException {

        String [] lines=accessControl.split("\\r?\\n|\\r");

        ArrayList<AccessControlDto> accessList=new ArrayList<AccessControlDto>();
        for (String line:lines) {

            Boolean accessibility=line.split(" ")[0].equals("+");
            String path=line.split(" ")[1];

            accessList.add(new AccessControlDto(accessibility,path));
        }

       return Check(accessList,xpath);
    }

    private static boolean Check(ArrayList<AccessControlDto> accessList,String xpath)
    {
        if (xpath.equals(""))
            return false;

        for (AccessControlDto item:accessList) {

           if( item.getPath().equals(xpath))
               return item.getAccessibility();
        }

        if (xpath.indexOf("/")==-1)
            return  false;

        String newXpath =xpath.substring(0,xpath.lastIndexOf('/'));
        return Check(accessList,newXpath);
    }
}


