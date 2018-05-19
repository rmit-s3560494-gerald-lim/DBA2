import java.io.*;
import java.util.*;

public class hashLoad
{
    // found this prime number from searching
    private static int hashSize = 3145739;
    private static int recordSize = 275;
    static int pageSize;
    static long start;

    public static void main(String[] args)
    {
        RandomAccessFile inputFile = null;
        RandomAccessFile outputFile = null;
        int pageOffset = 0;
        int recordOffset = 0;
        int noRecords = pageSize / recordSize;
        int remainingPages = pageSize % recordSize;
        int currRec = 0;
        byte[] emptyByte = new byte[200];

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