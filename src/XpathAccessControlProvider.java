import Dtos.AccessControlDto;

import java.io.IOException;
import java.util.*;

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

        String [] mainXpathSplited=xpath.split("/");
        for (AccessControlDto item:accessList) {
            String currentPath=item.getPath();
           if( item.getPath().equals(xpath))
               return item.getAccessibility();
            else
           {
               if(xpath.endsWith("*")==true)
                xpath=xpath.substring(0,xpath.length()-2);

               int delimerLoc=item.getPath().lastIndexOf("/");
               int bracketLoc=item.getPath().lastIndexOf("[");
               if(bracketLoc>delimerLoc)
               {
                   String newXpath=item.getPath().substring(0,bracketLoc); //remvoe last ndoe index
                   if( item.getPath().equals(newXpath))
                       return item.getAccessibility();
               }
           }
        }

        if (xpath.indexOf("/")==-1)
            return  false;

        String newXpath =xpath.substring(0,xpath.lastIndexOf('/'));
        return Check(accessList,newXpath);
    }
}


