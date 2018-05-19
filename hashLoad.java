import java.io.*;
import java.util.*;

public class hashLoad
{
    public static void main(String[] args)
    {
        if(args.length == 1)
        {
            String page = args[0];
            try
            {
                pageSize = Integer.parseInt(page);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("Page size must be int");
        }
    }
}