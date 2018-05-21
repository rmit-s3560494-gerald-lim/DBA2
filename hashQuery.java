import java.io.*;
import java.util.*;

public class hashQuery
{
    private static int hashSize = 3145739;
    static int pageSize;
    static long start;
    static String query;
    // method to get hash position
    public static int createHash(byte[] arr)
    {
        return Math.abs((Arrays.hashCode(arr)) % hashSize);
    }
    public static void main(String[] args)
    {
        RandomAccessFile hashFile = null;
        RandomAccessFile heapFile = null;     
        if(args.length == 2)
        {
            query = args[0];
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
        byte[] bQuery = Arrays.copyOf(query.getBytes(), 200);
        try
        {
            start = System.currentTimeMillis();
            hashFile = new RandomAccessFile("hash." + pageSize + ".dat", "r");
            heapFile = new RandomAccessFile("heap." + pageSize + ".dat", "r");
            int startpos = createHash(bQuery) * 4;
            int currpos = startpos;
            boolean search = true;
            while(search == true)
            {
                hashFile.seek(startpos);
                int p = hashFile.readInt();
                byte[] bName = new byte[200];
                if(p > -1)
                {
                    heapFile.seek(p);
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
                            search = false;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                currpos += 4;
                if(currpos > (hashSize - 1) * 4)
                {
                    currpos = 0;
                }
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