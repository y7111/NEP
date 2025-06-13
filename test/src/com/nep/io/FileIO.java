package com.nep.io;

import java.io.*;

public class FileIO {
    /**
     * 读取文件
     */
    public static Object readObject(String filepath){
        File file = new File(filepath);
        InputStream is = null;
        ObjectInputStream ois = null;
        Object obj = null;
        try{
            is = new FileInputStream(file);
            ois = new ObjectInputStream(is);
            obj = ois.readObject();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            try {
                if(ois != null){
                    ois.close();
                }
                if(is != null){
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return obj ;
    }

    /**
     * 写入文件
     */
    public static void writeObject(String filepath,Object obj){
        File file = new File(filepath);
        OutputStream os = null;
        ObjectOutputStream oos = null;
        try {
            os = new FileOutputStream(file);
            oos = new ObjectOutputStream(os);
            oos.writeObject(obj);
            oos.flush();
        } catch (Exception ex) {
            // TODO: handle exception
            ex.printStackTrace();
        }finally{
            try {
                if(oos != null){
                    oos.close();
                }
                if(os != null){
                    os.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }
}
