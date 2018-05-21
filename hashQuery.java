import java.io.*;
import java.util.*;

public class hashQuery
{
    private static int hashSize = 3145739;
    private static int recordSize = 275;
    static int pageSize;
    static long start;
    public static void main(String[] args)
    {
        if(args.length == 2)
        {
            String query = args[0];
            String page = args[1];
            try
            {
                pageSize = Integer.parseInt(page);
            } catch (IndexOutOfBoundsException e) {
                System.err.println("Page size must be int");
            }
        }
    }
}