package com.animaldex.animaldex;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Alex on 2016-03-30.
 */
public class Encryption {

    private static final byte[] key = "AnimalDex3A04".getBytes();
    private static final String transformation = "AES/ECB/PKCS5Padding";

    public static void encrypt(ArrayList input, String fileName){

        String filePath = "/tmp/"+fileName.toLowerCase()+".ser";

        try{

            //Create key
            SecretKeySpec sks = new SecretKeySpec(key, transformation);

            //Create cipher
            Cipher c = Cipher.getInstance(transformation);
            c.init(Cipher.ENCRYPT_MODE, sks);
            SealedObject sealObj = new SealedObject(input, c);

            //Create output streams to wrap and serialize input
            FileOutputStream fileOut = new FileOutputStream(filePath);
            CipherOutputStream ciphOut = new CipherOutputStream(fileOut, c);
            ObjectOutputStream objOut = new ObjectOutputStream(ciphOut);

            //Serialize input
            objOut.writeObject(input);

            objOut.close();
            ciphOut.close();
            fileOut.close();

            Log.v("Encryption", "Ecrypted data saved in " + filePath);



        }catch(Exception ex){
            Log.e("Encryption", "Could not ecrypt data to " + filePath + "." + ex.getMessage());
        }

    }

    public static ArrayList decrypt(String fileName){

        String filePath = "/tmp/"+fileName.toLowerCase()+".ser";
        ArrayList output = new ArrayList();

        try{

            //Create key
            SecretKeySpec sks = new SecretKeySpec(key, transformation);

            //Create cipher
            Cipher c = Cipher.getInstance(transformation);
            c.init(Cipher.ENCRYPT_MODE, sks);

            //Create output streams to wrap and serialize input
            FileInputStream fileIn = new FileInputStream(filePath);
            CipherInputStream ciphIn = new CipherInputStream(fileIn, c);
            ObjectInputStream objIn = new ObjectInputStream(ciphIn);

            //Deserialize and unwrap data
            SealedObject sealObj = (SealedObject) objIn.readObject();
            output = (ArrayList)sealObj.getObject(c);

            objIn.close();
            ciphIn.close();
            fileIn.close();

        }catch(Exception ex){
            Log.e("Decryption", "Could not decrypt data from " + filePath + "." + ex.getMessage());
        }

        return output;
    }


}
