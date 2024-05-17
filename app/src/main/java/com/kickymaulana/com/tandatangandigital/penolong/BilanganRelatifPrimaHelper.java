package com.kickymaulana.com.tandatangandigital.penolong;

public class BilanganRelatifPrimaHelper {

    String hasil = "";

    public String tampilkan_bilangan_relatif_prima_terhadap(int n) {
        for (int i = 1; i < n; i++) {
            if (isRelatifPrima(n, i)) {
                //System.out.print(i + " ");
                hasil += i + ", ";
            }
        }
        return hasil;

    }

    public static boolean isRelatifPrima(int a, int b) {
        // Fungsi ini akan mengembalikan true jika a dan b relatif prima, yaitu tidak memiliki faktor prima yang sama selain 1.
        // Faktor prima dari a dan b tidak boleh sama, selain dari 1.

        int gcd = hitungFPB(a, b);
        return gcd == 1;
    }

    public static int hitungFPB(int a, int b) {
        // Fungsi ini menghitung Faktor Persekutuan Terbesar (FPB) dari dua bilangan a dan b menggunakan algoritma Euclidean.
        if (b == 0) {
            return a;
        } else {
            return hitungFPB(b, a % b);
        }
    }
}
