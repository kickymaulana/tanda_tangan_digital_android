package com.kickymaulana.tandatangandigital;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class PesanValidasi {


    String[] rule;
    JSONObject response;

    public PesanValidasi(String[] rule, JSONObject response) {
        this.rule = rule;
        this.response = response;
        this.setpesan();
    }

    String pesanvalidasi = "";

    public String getpesan(){
        return this.pesanvalidasi;
    }

    public void setpesan(){

        for(int i2 = 0; i2 < rule.length; i2++){
            try {
                if (response.getJSONObject("pesan").has(rule[i2])){
                    int total = response.getJSONObject("pesan").getJSONArray(rule[i2]).length();
                    for(int i = 0; i < total; i++){
                        pesanvalidasi = pesanvalidasi + response.getJSONObject("pesan").getJSONArray(rule[i2]).get(i) + "\n";
                    }

                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }

    }
    public int hitung(){
        return this.rule.length;
    }

}
