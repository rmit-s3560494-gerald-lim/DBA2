import java.io.*;
import java.util.*;

public class hashLoad
{
    // found this prime number from searching
    private static int hashSize = 3145739;
    private static int recordSize = 275;
    static int pageSize;
    // time computation
    static long start;
    public static void main(String[] args)
    {
        RandomAccessFile inputFile = null;
        RandomAccessFile outputFile = null;
        int pageCounter = 0;
        int noRecords = pageSize / recordSize;
        int currentRecord = 0;
        byte[] emptyArray = new byte[200];
        if(args.length == 1)
        {
            String page = args[0];
            try
            {
                // ensure only int is accepted as parameter
                pageSize = Integer.parseInt(page);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        else
        {
            System.err.println("Page size must be int");
        }
        try 
        {
            // start timer
            start = System.currentTimeMillis();
            System.out.println("Creating hash");
            // declare input and output files
            inputFile = new RandomAccessFile("heap." + pageSize + ".dat", "r");
            outputFile = new RandomAccessFile("hash." + pageSize + ".dat", "rw");
            // fill hash file
            for (int i = 0; i < hashSize; i++)
            {
                outputFile.writeInt(-1);
            }
            while(true)
            {
                int currentPos = (currentRecord * recordSize) + (pageCounter * pageSize);
                // find position in file to write to
                inputFile.seek(currentPos);
                // create byte array of size 200(name)
                byte[] buffer = new byte[200];
                inputFile.read(buffer);
                // if byte array is empty then break
                if(Arrays.equals(buffer, emptyArray))
                {
                    break;
                }
                int startpos = createHash(buffer) * 4;
                int currpos = startpos;
                while(true)
                {
                    outputFile.seek(currpos);
                    int bucket = outputFile.readInt();
                    outputFile.seek(currpos);
                    // check if bucket has been already filled, if -1 means not filled
                    if(bucket == -1)
                    {
                        outputFile.writeInt(currentPos);
                        break;
                    }
                    else
                    {
                        currpos = currpos + 4;
                        if(currpos > (hashSize - 1) * 4)
                        {
                            currpos = 0;
                        }
                        if(currpos == startpos)
                        {
                            System.err.println("hash file is full");
                            System.exit(0);
                        }
                    }
                }
                // increment current record counter
                currentRecord++;
                // reset currentRecord to 0 and increment the page count
                if(currentRecord == noRecords)
                {
                    currentRecord = 0;
                    pageCounter++;
                }
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally 
        {
            if (inputFile != null)
            {
                try 
                {
                    inputFile.close();
                    long end = System.currentTimeMillis();
                    System.out.println("Time taken to generate hash file: " + (end - start) + "ms");
                } catch (IOException e) 
                {
                    e.printStackTrace();
                }
            }
        }
    }
    // method to get hash position
    public static int createHash(byte[] arr)
    {
        return Math.abs((Arrays.hashCode(arr)) % hashSize);
    }
}