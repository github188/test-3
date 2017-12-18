package nari.MainGridService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        List<String> aList = new ArrayList<String>(Arrays.asList("1","3","5","7","9","11"));
        aList.remove(1);
        System.out.println(aList);
        for(int i = 0;i<aList.size();i++)
        {
        	if(aList.get(i) == "5" || aList.get(i)=="7")
        	{
        		aList.remove(i);
        		i--;
        	}
        }
        System.out.println(aList);
    }
}
