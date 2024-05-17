package com.kickymaulana.com.tandatangandigital.penolong;

import java.math.BigInteger;

public class KunciDeskripsiHelper {

    public BigInteger jalankan(BigInteger fn, BigInteger e){
        // Menghitung nilai d menggunakan invers modular
        BigInteger d = e.modInverse(fn);
        return d;

    }

}
