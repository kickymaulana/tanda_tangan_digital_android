package com.kickymaulana.tandatangandigital.sessionmanager;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final String SESSION = "tanda_tangan_digital";
    private Context context;

    public SessionManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.context = context;
    }

    public String getServer(){
        return sharedPreferences.getString("server", "http://10.0.2.2:8081/tanda_tangan_digital_laravel/public/");
        //return sharedPreferences.getString("server", "http://192.168.8.100:8081/tanda_tangan_digital_laravel/public/");
    }

    public void setServer(String server){
        editor.putString("server", server);
        editor.commit();
    }
    public void setUsername(String username){
        editor.putString("username", username);
        editor.commit();
    }

    public String getUsername(){
        return sharedPreferences.getString("username", "kosong");
    }

    public void setToken(String token){
        editor.putString("token", token);
        editor.commit();
    }
    public String getToken(){
        return sharedPreferences.getString("token", "kosong");
    }
    public void setNama(String nama){
        editor.putString("nama", nama);
        editor.commit();
    }
    public String getNama(){
        return sharedPreferences.getString("nama", "kosong");
    }
    public void logout(){
        editor.putString("username", "kosong");
        editor.putString("token", "kosong");
        editor.putString("nama", "kosong");
        editor.commit();
    }

    public void setCari(String cari){
        editor.putString("cari", cari);
        editor.commit();
    }

    public String getCari(){
        return sharedPreferences.getString("cari", "");
    }


}
