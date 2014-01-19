
package net.sehales.secon.utils.java;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class ObjectUtils {
    
    public static Object deserializeFromString(String serializedObject) throws IOException, ClassNotFoundException {
        
        byte[] pick = Base64Coder.decodeLines(serializedObject);
        InputStream in = new ByteArrayInputStream(pick);
        ObjectInputStream ois = new ObjectInputStream(in);
        Object deserializedObject = ois.readObject();
        ois.close();
        in.close();
        
        return deserializedObject;
    }
    
    public static Object loadHash(File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
                return new HashMap();
            }
            ObjectInputStream st = new ObjectInputStream(new FileInputStream(file));
            Object o = st.readObject();
            st.close();
            if (o == null) {
                return new HashMap();
            }
            return o;
        } catch (Exception e) {
            return new HashMap();
        }
    }
    
    public static void saveHash(Object hash, File file) {
        try {
            ObjectOutputStream st = new ObjectOutputStream(new FileOutputStream(file));
            st.writeObject(hash);
            st.flush();
            st.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String serializeToString(Object objectToWrite) throws IOException {
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(objectToWrite);
        oos.close();
        out.close();
        
        String output = Base64Coder.encodeLines(out.toByteArray());
        
        return output;
    }
}
