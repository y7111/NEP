package com.nep.io;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable; // 确保 Serializable 导入

public class FileIO {

    private static Context applicationContext;

    // 这个方法需要在你的Application类或者主Activity中设置一次
    public static void setApplicationContext(Context context) {
        FileIO.applicationContext = context;
    }

    /**
     * 从内部存储读取对象。
     * @param filename 要读取的文件名
     * @return 读取到的对象，如果失败则返回null
     */
    public static Object readObject(String filename) {
        if (applicationContext == null) {
            System.err.println("Error: Application context not set for FileIO."); //
            return null;
        }
        File file = new File(applicationContext.getFilesDir(), filename); //
        Object obj = null;
        // 检查文件是否存在且可读
        if (!file.exists() || !file.canRead()) {
            System.err.println("File not found or not readable: " + file.getAbsolutePath()); //
            return null;
        }
        try (FileInputStream fis = new FileInputStream(file); //
             ObjectInputStream ois = new ObjectInputStream(fis)) { //
            obj = ois.readObject(); //
        } catch (IOException | ClassNotFoundException e) { //
            e.printStackTrace(); //
        }
        return obj;
    }

    /**
     * 将对象写入内部存储。
     * @param filename 要写入的文件名
     * @param obj 要写入的对象
     */
    public static void writeObject(String filename, Object obj) {
        if (applicationContext == null) {
            System.err.println("Error: Application context not set for FileIO."); //
            return;
        }
        File file = new File(applicationContext.getFilesDir(), filename); //
        try (FileOutputStream fos = new FileOutputStream(file); //
             ObjectOutputStream oos = new ObjectOutputStream(fos)) { //
            oos.writeObject(obj); //
            oos.flush(); //
        } catch (IOException e) { //
            e.printStackTrace(); //
        }
    }
}