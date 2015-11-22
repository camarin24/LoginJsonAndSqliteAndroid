package com.example.cristian.loginjsonandmore.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cristian.loginjsonandmore.Tables.tblUsuario;
import com.example.cristian.loginjsonandmore.ViewModels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian on 21/11/2015.
 */


public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Login2";
    private static final int DB_VERSION = 4;
    private SQLiteDatabase db;


    public DBHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tblUsuario.createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tblUsuario.tblUsuario);
        onCreate(db);
    }

    //Metodos DML
    //Validar usuario login
    public Boolean validUser(String name, String pass) throws Exception{
        String nombre = name;
        String password = pass;
        String [] datos = new String[]{tblUsuario.nombreUsuario};
        Cursor c;
        c = db.query(tblUsuario.tblUsuario,datos,tblUsuario.nombreUsuario +" = ? and "+
                tblUsuario.contraseniaUsuario + " = ?",new String[]{nombre,password},null,null,null);
        if(c.getCount() != 0){
            return true;
        }
        c.close();
        return false;
    }
    //Crear usuario
    public void addUser(List<UserViewModel> models) throws Exception{
        for (int i = 0; i < models.size(); i++){
            //Creando contenedor de valores
            ContentValues cv = new ContentValues();
            cv.put(tblUsuario.idUsuario,models.get(i).getId());
            cv.put(tblUsuario.nombreUsuario,models.get(i).getName());
            cv.put(tblUsuario.contraseniaUsuario,models.get(i).getPassword());
            cv.put(tblUsuario.aliasUsuario,models.get(i).getAlias());
            cv.put(tblUsuario.latitudUsuario,models.get(i).getLatitud());
            cv.put(tblUsuario.longitudUsuario,models.get(i).getLongitud());
            db.insert(tblUsuario.tblUsuario,null,cv);
        }
    }
    //Traer informacion usuario
    public List<UserViewModel> getUser(String nombre){

        List<UserViewModel> modelList = new ArrayList<>();
        String [] datos = new String[]{
                tblUsuario.nombreUsuario,
                tblUsuario.aliasUsuario,
                tblUsuario.latitudUsuario,
                tblUsuario.longitudUsuario};

        Cursor c = db.query(tblUsuario.tblUsuario,datos,tblUsuario.nombreUsuario + " = ?",new String[]{nombre},null,null,null);
        if(c.moveToFirst()){
            do {
                UserViewModel model = new UserViewModel();
                model.setName(c.getString(0));
                model.setAlias(c.getString(1));
                model.setLatitud(c.getFloat(2));
                model.setLongitud(c.getFloat(3));
                modelList.add(model);
            }while(c.moveToNext());
        }
        return modelList;
    }
}
