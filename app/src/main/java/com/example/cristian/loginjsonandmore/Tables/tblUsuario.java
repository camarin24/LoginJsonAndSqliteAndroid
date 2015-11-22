package com.example.cristian.loginjsonandmore.Tables;

/**
 * Created by Cristian on 21/11/2015.
 */
public class tblUsuario {

    //Tabla
    public static final String tblUsuario = "tblUsuario";
    //Colunas
    public static final String  idUsuario= "idUsuario";
    public static final String nombreUsuario = "nombreUsuario";
    public static final String contraseniaUsuario = "contraseniaUsuario";
    public static final String aliasUsuario = "aliasUsuario";
    public static final String longitudUsuario = "longitudUsuario";
    public static final String latitudUsuario = "latitudUsuario";

    //Script para crear tabla
    public static final String createTable = "CREATE TABLE "+ tblUsuario+ "( " +
            "" + idUsuario +" INTEGER PRIMARY KEY," +
            "" + nombreUsuario + " TEXT," +
            "" + contraseniaUsuario + " TEXT," +
            "" + aliasUsuario + " TEXT," +
            "" + latitudUsuario+ " REAL," +
            "" + longitudUsuario+ " REAL)";
}
