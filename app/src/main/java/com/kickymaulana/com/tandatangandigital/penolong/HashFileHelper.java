package com.kickymaulana.com.tandatangandigital.penolong;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;


public class HashFileHelper {

    public static String calculateFileHash(String filePath, String algorithm) throws Exception {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        FileInputStream fis = new FileInputStream(filePath);
        byte[] dataBytes = new byte[1024];
        int bytesRead;

        while ((bytesRead = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, bytesRead);
        }

        byte[] hashBytes = md.digest();

        StringBuilder hashHex = new StringBuilder();
        for (byte b : hashBytes) {
            hashHex.append(String.format("%02x", b));
        }

        fis.close();

        return hashHex.toString();
    }

    public String hash_file(String path){
        String filePath = path;
        String algorithm = "SHA-256";

        try {
            String fileHash = calculateFileHash(filePath, algorithm);
            return fileHash;
        } catch (Exception e) {
            e.printStackTrace();
            return "kosong";
        }
    }

    public void hash_to_integer(){
        String sha256Hash = "76bac5cc82930ccef85ab8f12149502577c7efba1fb4e3519ec3d2c7150fa434";

        // Mengonversi hash heksadesimal menjadi BigInteger
        BigInteger bigIntegerHash = new BigInteger(sha256Hash, 16);

        System.out.println("Hash as BigInteger: " + bigIntegerHash);
    }

    public void ulang(){
        String sha256Hash = "76bac5cc82930ccef85ab8f12149502577c7efba1fb4e3519ec3d2c7150fa434";

        // Konversi hash menjadi string
        String hashString = sha256Hash;

        // Loop melalui setiap karakter dalam string hash
        for (int i = 0; i < hashString.length(); i++) {
            char character = hashString.charAt(i);
            int kode_ascii = (int) character;
            String kode_ascii3digit = String.format("%03d", kode_ascii);
            BigInteger m = new BigInteger(kode_ascii3digit);
            BigInteger e = new BigInteger("7");
            BigInteger n = new BigInteger("187");
            BigInteger d = new BigInteger("23");
            BigInteger c = m.modPow(e, n);
            BigInteger m1 = new BigInteger(c.modPow(d, n).toString());
            char character2 = (char) m1.intValue();

            System.out.println("Karakter ke-" + i + ": " + character + " kode ascii nya adalah " + kode_ascii3digit + " kalau dienkripsi RSA menjadi " + c + " dan kalau dideskripsi menjadi " + String.format("%03d", m1) + " ke char lagi " + character2);
        }
    }

}
