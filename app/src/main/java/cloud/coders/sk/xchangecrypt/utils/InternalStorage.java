package cloud.coders.sk.xchangecrypt.utils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Peter on 01.05.2018.
 */

public final class InternalStorage{

    private InternalStorage() {}

    public static void writeObject(Context context, String key, Object object) {
        try {
            FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object readObject(Context context, String key){

        FileInputStream fis = null;
        try {
        fis = context.openFileInput(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        return object;
        } catch (IOException|ClassNotFoundException e) {
        e.printStackTrace();
        }
        return null;
    }
}