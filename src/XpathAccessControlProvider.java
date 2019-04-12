import Dtos.AccessControlDto;

import java.io.IOException;
import java.util.*;

/**
 * Created by behnam on 4/9/19.
 */
public class XpathAccessControlProvider {

    public static void main(String[] arguments)
    {
        try {
            System.out.println(CheckAccess(Utilities.ReadFileAsString("data/AC.data"),"A/D[1]/G"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean CheckAccess(String accessControl,String xpath) throws IOException {

        String [] lines=accessControl.split("\\r?\\n|\\r");

        ArrayList<AccessControlDto> accessList=new ArrayList<AccessControlDto>();
        for (String line:lines) {

            Boolean accessibility=line.split(" ")[0].equals("+");
            String path=line.split(" ")[1];

            accessList.add(new AccessControlDto(accessibility,path));

            /*Collections.sort(accessList, new Comparator<>() {
                public int compare(AccessControlDto o1, AccessControlDto o2) {
                    if (o1.getPath().length() == o2.getPath().length())
                        return 0;
                    return o1.getPath().length() < o2.getPath().length() ? -1 : 1;
                }
            });*/
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
               int delimerLoc=item.getPath().lastIndexOf("/");
               int bracketLoc=item.getPath().lastIndexOf("[");
               if(bracketLoc>delimerLoc)
               {
                   String newXpath=item.getPath().substring(0,bracketLoc); //remvoe last ndoe index
                   if( item.getPath().equals(newXpath))
                       return item.getAccessibility();
               }
           }
            /*String[] ItemPathSplited=item.getPath().split("/");
            for(int i=0;i<=ItemPathSplited.length;i++)
            {
                if(mainXpathSplited[i]!=ItemPathSplited[i])
                {

                }
            }*/


        }

        if (xpath.indexOf("/")==-1)
            return  false;

        String newXpath =xpath.substring(0,xpath.lastIndexOf('/'));
        return Check(accessList,newXpath);
    }
}


