package reagodjj.example.com.homeworkfourth.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SharedPreferencesUtil {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private String fileName = "";
    private int mode;

    @SuppressLint("CommitPrefEdits")
    public SharedPreferencesUtil(Context context, String fileName) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.fileName = fileName;
        mode = Context.MODE_PRIVATE;
    }

    public void commitAccount() {
        editor.commit();
    }

    public void saveSerializableObject(String key, Object object) throws IOException {
        //先将序列化结果写到byte缓存中，其实就分配一个内存空间
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(object);//将对象序列化写入byte缓存
        //将序列化的数据转为16进制保存
        String bytesToHexString = bytesToHexString(bos.toByteArray());
        //保存该16进制数组
        editor.putString(key, bytesToHexString);
        editor.commit();
    }

    private String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public Object getSerializableObject(String key) throws IOException, ClassNotFoundException {
        if (sharedPreferences.contains(key)) {
            String string = sharedPreferences.getString(key, "");
            if (TextUtils.isEmpty(string)) {
                return null;
            } else {
                //将16进制的数据转为数组，准备反序列化
                byte[] stringToBytes = StringToBytes(string);
                ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
                ObjectInputStream is = new ObjectInputStream(bis);
                //返回反序列化得到的对象
                return is.readObject();
            }
        }
        return null;
    }

    private byte[] StringToBytes(String data) {
        String hexString = data.toUpperCase().trim();
        if (hexString.length() % 2 != 0) {
            return null;
        }
        byte[] retData = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i++) {
            int int_ch;  // 两位16进制数转化后的10进制数
            char hex_char1 = hexString.charAt(i); //两位16进制数中的第一位(高位*16)
            int int_ch1;
            if (hex_char1 >= '0' && hex_char1 <= '9')
                int_ch1 = (hex_char1 - 48) * 16;   // 0 的Ascll - 48
            else if (hex_char1 >= 'A' && hex_char1 <= 'F')
                int_ch1 = (hex_char1 - 55) * 16; // A 的Ascll - 65
            else
                return null;
            i++;
            char hex_char2 = hexString.charAt(i); //两位16进制数中的第二位(低位)
            int int_ch2;
            if (hex_char2 >= '0' && hex_char2 <= '9')
                int_ch2 = (hex_char2 - 48); // 0 的Ascll - 48
            else if (hex_char2 >= 'A' && hex_char2 <= 'F')
                int_ch2 = hex_char2 - 55; // A 的Ascll - 65
            else
                return null;
            int_ch = int_ch1 + int_ch2;
            retData[i / 2] = (byte) int_ch;//将转化后的数放入Byte里
        }
        return retData;
    }
}
