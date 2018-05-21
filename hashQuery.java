import java.io.*;
import java.util.*;

public class hashQuery
{
    private static int hashSize = 3145739;
    private static int recordSize = 275;
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
            
            while(true)
            {
                hashFile.seek(startpos);
                int p = hashFile.readInt();
                byte[] heapName = new byte[200];
                if(p > -1)
                {
                    heapFile.seek(p);
                    heapFile.read(heapName);
                    if(Arrays.equals(heapName, bQuery))
                    {
                        try 
                        {
                            String name = new String(heapName);
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
                            byte[] readStatus = new byte[12];
                            BN_STATUS = new String(readStatus);
                            heapFile.seek(p + 200 + 12);
                            byte[] readRegDate = new byte[10];
                            heapFile.read(readRegDate);
                            BN_REG_DT = new String(readRegDate);
                            heapFile.seek(p + 200 + 12 + 10);
                            byte[] readCancelDate = new byte[10];
                            heapFile.read(readCancelDate);
                            BN_CANCEL_DT = new String(readCancelDate);
                            heapFile.seek(p + 200 + 12 + 10 + 10);
                            byte[] readRenewDate = new byte[10];
                            heapFile.read(readRenewDate);
                            BN_RENEW_DT = new String(readRenewDate);
                            heapFile.seek(p + 200 + 12 + 10 + 10 + 10);
                            byte[] readStateNum = new byte[10];
                            heapFile.read(readStateNum);
                            BN_STATE_NUM = new String(readStateNum);
                            heapFile.seek(p + 200 + 12 + 10 + 10 + 10 + 10);
                            byte[] readStateReg = new byte[3];
                            BN_STATE_OF_REG = new String(readStateReg);
                            heapFile.seek(p + 200 + 12 + 10 + 10 + 10 +10 + 3);
                            byte[] readABN = new byte[20];
                            heapFile.read(readABN);
                            BN_ABN = new String(readABN);

                            long end = System.currentTimeMillis();
                            System.out.println("Time taken" + (end - start) + "ms" +
                                "\nBN_NAME: " + BN_NAME +
                                "\nBN_STATUS: " + BN_STATUS +
                                "\nBN_REG_DT: " + BN_REG_DT +
                                "\nBN_CANCEL_DT: " + BN_CANCEL_DT +
                                "\nBN_RENEW_DT: " + BN_RENEW_DT +
                                "\nBN_STATE_NUM: " + BN_STATE_NUM +
                                "\nBN_STATE_OF_REG: " + BN_STATE_OF_REG +
                                "\nBN_ABN: " + BN_ABN +
                                "\n");
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