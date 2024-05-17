package com.kickymaulana.com.tandatangandigital.penolong;

public class BilanganPrimaHelper {

    private int i = 0;
    int num = 0;
    String primeNumbers = "";

    public String tampilkan(){
        for (i = 1; i <= 100; i++){
            int counter = 0;
            for (num = i; num >= 1; num--){
                if (i%num == 0){
                    counter = counter + 1;
                }
            }
            if (counter == 2){
                //appended the prime number to the string
                primeNumbers = primeNumbers + i + " ";
            }
        }
        return primeNumbers;

    }

}
