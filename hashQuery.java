import java.io.*;
import java.util.*;

public class hashQuery
{
    // found this prime number from searching
    private static int hashSize = 3145739;
    static int pageSize;
    // time computation
    static long start;
    // static so it can be accessed later
    static String query;
    // method to get hash position
    public static int createHash(byte[] arr)
    {
        return Math.abs((Arrays.hashCode(arr)) % hashSize);
    }
    public static void main(String[] args)
    {
        // declare hash and heap files
        RandomAccessFile hashFile = null;
        RandomAccessFile heapFile = null;
        // ensure program takes terminal args
        if(args.length == 2)
        {
            query = args[0];
            String page = args[1];
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
            // error message
            System.err.println("Page size must be int");
        }
        byte[] bQuery = Arrays.copyOf(query.getBytes(), 200);
        try
        {
            // start timer
            start = System.currentTimeMillis();
            // setting file names
            hashFile = new RandomAccessFile("hash." + pageSize + ".dat", "r");
            heapFile = new RandomAccessFile("heap." + pageSize + ".dat", "r");
            // getting starting position
            int startpos = createHash(bQuery) * 4;
            // set current position to starting position
            int currpos = startpos;
            // this boolean used to end search loop
            boolean search = true;
            while(search == true)
            {
                hashFile.seek(startpos);
                // pointer to read to heap
                int p = hashFile.readInt();
                // first var which is business name is 200 bytes long
                byte[] bName = new byte[200];
                // if pointer valid
                if(p > -1)
                {
                    // seek at pointer
                    heapFile.seek(p);
                    // read name into byte array
                    heapFile.read(bName);
                    if(Arrays.equals(bName, bQuery))
                    {
                        try 
                        {
                            String name = new String(bName);
                            String BN_NAME;
                            String BN_STATUS;
                            String BN_REG_DT;
                            String BN_CANCEL_DT;
                            String BN_RENEW_DT;
                            String BN_STATE_NUM;
                            String BN_STATE_OF_REG;
                            String BN_ABN;
                            BN_NAME = name;
                            // all additions from this point are just adding on the amount of bytes declared for each variable
                            heapFile.seek(p + 200);
                            byte[] status = new byte[12];
                            BN_STATUS = new String(status);
                            heapFile.seek(p + 200 + 12);
                            byte[] regdate = new byte[10];
                            heapFile.read(regdate);
                            BN_REG_DT = new String(regdate);
                            heapFile.seek(p + 200 + 12 + 10);
                            byte[] canceldate = new byte[10];
                            heapFile.read(canceldate);
                            BN_CANCEL_DT = new String(canceldate);
                            heapFile.seek(p + 200 + 12 + 10 + 10);
                            byte[] renewdate = new byte[10];
                            heapFile.read(renewdate);
                            BN_RENEW_DT = new String(renewdate);
                            heapFile.seek(p + 200 + 12 + 10 + 10 + 10);
                            byte[] statenum = new byte[10];
                            heapFile.read(statenum);
                            BN_STATE_NUM = new String(statenum);
                            heapFile.seek(p + 200 + 12 + 10 + 10 + 10 + 10);
                            byte[] statereg = new byte[3];
                            BN_STATE_OF_REG = new String(statereg);
                            heapFile.seek(p + 200 + 12 + 10 + 10 + 10 +10 + 3);
                            byte[] abn = new byte[20];
                            heapFile.read(abn);
                            BN_ABN = new String(abn);

                            long end = System.currentTimeMillis();
                            System.out.println("Time taken: " + (end - start) + "ms" +
                                "\nBN_NAME: " + BN_NAME +
                                "\nBN_STATUS: " + BN_STATUS +
                                "\nBN_REG_DT: " + BN_REG_DT +
                                "\nBN_CANCEL_DT: " + BN_CANCEL_DT +
                                "\nBN_RENEW_DT: " + BN_RENEW_DT +
                                "\nBN_STATE_NUM: " + BN_STATE_NUM +
                                "\nBN_STATE_OF_REG: " + BN_STATE_OF_REG +
                                "\nBN_ABN: " + BN_ABN +
                                "\n");
                            // stop search if found and printed result
                            search = false;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // add one int size to current position
                currpos += 4;
                // if this position goes past size of hash index
                if(currpos > (hashSize - 1) * 4)
                {
                    // set position to 0
                    currpos = 0;
                }
                // if search has gone through entire file without any results
                if(currpos == startpos)
                {
                    long end = System.currentTimeMillis();
                    System.out.println("EOF in: " + (end - start) + "ms");
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}