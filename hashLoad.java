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
        int currentRecord = 0;
        byte[] emptyArray = new byte[200];

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

        try 
        {
            start = System.currentTimeMillis();
            System.out.println("Creating hash");

            inputFile = new RandomAccessFile("heap." + pageSize + ".dat", "r");
            outputFile = new RandomAccessFile("hash." + pageSize + ".dat", "rw");
            
            for (int i = 0; i < hashSize; i++)
            {
                outputFile.writeInt(-1);
            }

            while(true)
            {
                int currentPos = (currentRecord * recordSize) + (pageOffset * pageSize);
                // find position in file to write to
                inputFile.seek(currentPos);
                byte[] readByte = new byte[200];
                inputFile.read(readByte);

                if(Arrays.equals(readByte, emptyArray))
                {
                    break;
                }
                int hashName = createHash(readByte) * 4;
                int hashOffset = hashName;
                while(true)
                {
                    outputFile.seek(hashOffset);
                    int bucket = outputFile.readInt();
                    outputFile.seek(hashOffset);

                    if(bucket == -1)
                    {
                        outputFile.writeInt(currentPos);
                        break;
                    }
                    else
                    {
                        hashOffset = hashOffset + 4;

                        if(hashOffset > (hashSize - 1) * 4)
                        {
                            hashOffset = 0;
                        }
                        if(hashOffset == hashName)
                        {
                            System.out.println("hash file is full")
                            System.exit(0);
                        }
                    }
                }
                currentRecord++;
                if(currentRecord == noRecords)
                {
                    currentRecord = 0;
                    pageOffset++;
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

    public static int createHash(byte[] arr)
    {
        return Math.abs((Arrays.hashCode(arr)) % hashSize);
    }
}