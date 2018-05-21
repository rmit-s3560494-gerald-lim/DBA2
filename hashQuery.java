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
        RandomAccessFile hashFile = null;
        RandomAccessFile heapFile = null;
        if(args.length == 2)
        {
            String query = args[0];
            String page = args[1];
            try
            {
                pageSize = Integer.parseInt(page);
            } catch (IndexOutOfBoundsException e) {
                
                e.printStackTrace();
            }
        }
        else
        {
            System.err.println("Page size must be int");
        }

        int pageCounter = 0;
        int noRecords = pageSize / recordSize;
        int currentRecord = 0;
        byte[] emptyArray = new byte[200];
        
        byte[] bQuery = Arrays.copyOf(query.getBytes(), 200);
        try
        {
            start = System.currentTimeMillis();
            hashFile = new RandomAccessFile("hash." + pageSize + ".dat", "r");
            heapFile = new RandomAccessFile("heap." + pageSize + ".dat", "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}