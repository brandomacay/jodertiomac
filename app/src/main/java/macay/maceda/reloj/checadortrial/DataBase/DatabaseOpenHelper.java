package macay.maceda.reloj.checadortrial.DataBase;


import android.app.*;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import macay.maceda.reloj.checadortrial.Model.Actividades_empleados;
import macay.maceda.reloj.checadortrial.Model.Empleados_admin;
import macay.maceda.reloj.checadortrial.R;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "relojchecador";
    private static final int DATABASE_VERSION = 2;

    //Tabla para los usuarios
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PERSON_NAME = "name";
    public static final String COLUMN_PERSON_LASTNAME = "lastname";
    public static final String COLUMN_PERSON_BIRTHDAY = "birthday";
    public static final String COLUMN_PERSON_EMAIL = "email";
    public static final String COLUMN_PERSON_PHONE = "phone";
    public static final String COLUMN_PERSON_OCCUPATION = "occupation";
    public static final String COLUMN_PERSON_ADDRESS = "address";
    public static final String COLUMN_PERSON_AREA = "area";
    public static final String COLUMN_PERSON_STARTEDDATE = "started_date";
    public static final String COLUMN_PERSON_IMAGE = "image";
    public static final String COLUMN_PERSON_PASSWORD = "password";
    public static final String COLUMN_PERSON_BLOCKED = "blocked";

    //Tabla para checar asistencia
    public static final String TABLE_CLOCKING_NAME = "clocking";
    public static final String COLUMN_CLOCKING_ID = "_id";
    public static final String COLUMN_CLOCKING_USERID = "userid";
    public static final String COLUMN_CLOCKING_SDATE = "sdate";
    public static final String COLUMN_CLOCKING_IN = "workin";
    public static final String COLUMN_CLOCKING_OUT = "workout";
    public static final String COLUMN_CLOCKING_BREAKIN = "breakin";
    public static final String COLUMN_CLOCKING_BREAKOUT = "breakout";
    public static final String COLUMN_CLOCKING_FORMATDATE = "formatdate";

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PERSON_NAME + " TEXT NOT NULL, " +
                COLUMN_PERSON_LASTNAME + " TEXT  NOT NULL, " +
                COLUMN_PERSON_BIRTHDAY + " TEXT NOT NULL, " +
                COLUMN_PERSON_EMAIL + " TEXT NOT NULL, " +
                COLUMN_PERSON_PHONE + " NUMBER NOT NULL, " +
                COLUMN_PERSON_OCCUPATION + " TEXT NOT NULL, " +
                COLUMN_PERSON_ADDRESS + " TEXT NOT NULL, " +
                COLUMN_PERSON_AREA + " TEXT NOT NULL, " +
                COLUMN_PERSON_STARTEDDATE + " TEXT NOT NULL, " +
                COLUMN_PERSON_IMAGE + " TEXT  NOT NULL, " +
                COLUMN_PERSON_BLOCKED + " NUMBER NOT NULL, " +
                COLUMN_PERSON_PASSWORD + " NUMBER NOT NULL);"

        );

        db.execSQL(" CREATE TABLE IF NOT EXISTS " + TABLE_CLOCKING_NAME + " (" +
                COLUMN_CLOCKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CLOCKING_USERID + " NUMBER NOT NULL, " +
                COLUMN_CLOCKING_SDATE + " DATE , " +
                COLUMN_CLOCKING_IN + " DATETIME , " +
                COLUMN_CLOCKING_OUT + " DATETIME , " +
                COLUMN_CLOCKING_BREAKIN + " DATETIME , " +
                COLUMN_CLOCKING_BREAKOUT + " DATETIME , " +
                COLUMN_CLOCKING_FORMATDATE + " TEXT );"

        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        switch (oldVersion){
            case 1:
                db.execSQL("ALTER TABLE "+ TABLE_CLOCKING_NAME+" ADD COLUMN "+ COLUMN_CLOCKING_FORMATDATE +" TEXT ");
                break;
        }
        this.onCreate(db);
    }
    public void insertPerson(Empleados_admin person) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PERSON_NAME, person.getName());
        values.put(COLUMN_PERSON_LASTNAME, person.getLastname());
        values.put(COLUMN_PERSON_BIRTHDAY, person.getBirthday());
        values.put(COLUMN_PERSON_EMAIL, person.getEmail());
        values.put(COLUMN_PERSON_PHONE, person.getNumber_phone());
        values.put(COLUMN_PERSON_OCCUPATION, person.getOccupation());
        values.put(COLUMN_PERSON_ADDRESS, person.getAddress());
        values.put(COLUMN_PERSON_AREA, person.getArea());
        values.put(COLUMN_PERSON_STARTEDDATE, person.getDatework());
        values.put(COLUMN_PERSON_IMAGE, person.getImage());
        values.put(COLUMN_PERSON_BLOCKED, person.getBlocked());
        values.put(COLUMN_PERSON_PASSWORD, person.getPassword());


        // insert
        db.insert(TABLE_NAME,null, values);
        db.close();
    }



    /**Query records, give options to filter results**/
    public List<Empleados_admin> peopleList_by_search(String filter) {
        String query;
        if(filter.equals("")){
            //regular query
            query = "SELECT  * FROM " + TABLE_NAME;
        }else{
            //filter results by filter option provided
            //  query = "SELECT  * FROM " + TABLE_NAME + " WHERE name LIKE '"+ filter + "%' OR lastname LIKE '"
            //+ filter + "%'";
            query = "SELECT  * FROM " + TABLE_NAME + " WHERE name LIKE '"+ filter+ "%'" ;
        }

        List<Empleados_admin> personLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Empleados_admin person;

        if (cursor.moveToFirst()) {
            do {
                person = new Empleados_admin();

                person.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                person.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_NAME)));
                person.setLastname(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_LASTNAME)));
                person.setNumber_phone(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_PHONE)));
                person.setOccupation(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_OCCUPATION)));
                person.setArea(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_AREA)));
                person.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_EMAIL)));
                person.setBirthday(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_BIRTHDAY)));
                person.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_ADDRESS)));
                person.setDatework(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_STARTEDDATE)));
                person.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_IMAGE)));
                person.setBlocked(cursor.getInt(cursor.getColumnIndex(COLUMN_PERSON_BLOCKED)));
                person.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_PASSWORD)));

                personLinkedList.add(person);
            } while (cursor.moveToNext());

            db.close();
            cursor.close();
        }


        return personLinkedList;
    }



    /**Query records, give options to filter results**/
    public List<Empleados_admin> peopleList(String filter) {
        String query;
        if(filter.equals("")){
            //regular queryC:\Users\Brandon\Desktop\RelojTrial\app\build\outputs\apk\debug
            query = "SELECT  * FROM " + TABLE_NAME ;
        }else{
            //filter results by filter option provided
            query = "SELECT  * FROM " + TABLE_NAME + " ORDER BY "+ filter + " DESC";
        }

        List<Empleados_admin> personLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Empleados_admin person;

        if (cursor.moveToFirst()) {
            do {
                person = new Empleados_admin();

                person.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                person.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_NAME)));
                person.setLastname(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_LASTNAME)));
                person.setNumber_phone(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_PHONE)));
                person.setOccupation(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_OCCUPATION)));
                person.setArea(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_AREA)));
                person.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_EMAIL)));
                person.setBirthday(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_BIRTHDAY)));
                person.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_ADDRESS)));
                person.setDatework(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_STARTEDDATE)));
                person.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_IMAGE)));
                person.setBlocked(cursor.getInt(cursor.getColumnIndex(COLUMN_PERSON_BLOCKED)));

                person.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_PASSWORD)));

                personLinkedList.add(person);
            } while (cursor.moveToNext());
            db.close();
            cursor.close();
        }


        return personLinkedList;
    }

    /**Query only 1 record
     * @param id**/
    public Empleados_admin getPerson(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME + " WHERE _id="+ id;
        Cursor cursor = db.rawQuery(query, null);

        Empleados_admin receivedPerson = new Empleados_admin();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            receivedPerson.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_NAME)));
            receivedPerson.setLastname(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_LASTNAME)));
            receivedPerson.setNumber_phone(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_PHONE)));
            receivedPerson.setOccupation(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_OCCUPATION)));
            receivedPerson.setArea(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_AREA)));
            receivedPerson.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_EMAIL)));
            receivedPerson.setBirthday(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_BIRTHDAY)));
            receivedPerson.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_ADDRESS)));
            receivedPerson.setDatework(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_STARTEDDATE)));
            receivedPerson.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_IMAGE)));
            receivedPerson.setBlocked(cursor.getInt(cursor.getColumnIndex(COLUMN_PERSON_BLOCKED)));

            receivedPerson.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_PASSWORD)));
        }
        return receivedPerson;
    }



    public List<Actividades_empleados> getActividades(String filter,long id) {
        String query;
        if(filter.equals("")){
            query = "SELECT  * FROM " + TABLE_CLOCKING_NAME + " WHERE userid="+ id + " ORDER BY " + filter + " DESC";
        }else{

            query = "SELECT  * FROM " + TABLE_CLOCKING_NAME + " WHERE userid="+ id + " ORDER BY " + filter + " DESC";
        }

        List<Actividades_empleados> personLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Actividades_empleados person;

        if (cursor.moveToFirst()) {
            do {
                person = new Actividades_empleados();

                person.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_CLOCKING_ID)));
                person.setUserid(cursor.getString(cursor.getColumnIndex(COLUMN_CLOCKING_USERID)));
                person.setWorking(cursor.getString(cursor.getColumnIndex(COLUMN_CLOCKING_IN)));
                person.setWorkout(cursor.getString(cursor.getColumnIndex(COLUMN_CLOCKING_OUT)));
                person.setBreaking(cursor.getString(cursor.getColumnIndex(COLUMN_CLOCKING_BREAKIN)));
                person.setBreakout(cursor.getString(cursor.getColumnIndex(COLUMN_CLOCKING_BREAKOUT)));
                person.setFormatdate(cursor.getString(cursor.getColumnIndex(COLUMN_CLOCKING_FORMATDATE)));
                personLinkedList.add(person);
            } while (cursor.moveToNext());
            db.close();
            cursor.close();
        }


        return personLinkedList;
    }


    public List<Actividades_empleados> getActivityByDate(String filter,long id, String initial_date, String final_date) {
        String query;
        if(filter.equals("")){
            query = "SELECT  * FROM " + TABLE_CLOCKING_NAME + " WHERE userid="+ id + " ORDER BY " + filter + " DESC";
        }else{

            query = "SELECT  * FROM " + TABLE_CLOCKING_NAME + " WHERE userid="+ id + " ORDER BY " + filter + " DESC";
        }

        List<Actividades_empleados> personLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Actividades_empleados person;

        if (cursor.moveToFirst()) {
            do {
                person = new Actividades_empleados();

                person.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_CLOCKING_ID)));
                person.setUserid(cursor.getString(cursor.getColumnIndex(COLUMN_CLOCKING_USERID)));
                person.setWorking(cursor.getString(cursor.getColumnIndex(COLUMN_CLOCKING_IN)));
                person.setWorkout(cursor.getString(cursor.getColumnIndex(COLUMN_CLOCKING_OUT)));
                person.setBreaking(cursor.getString(cursor.getColumnIndex(COLUMN_CLOCKING_BREAKIN)));
                person.setBreakout(cursor.getString(cursor.getColumnIndex(COLUMN_CLOCKING_BREAKOUT)));

                personLinkedList.add(person);
            } while (cursor.moveToNext());
            db.close();
            cursor.close();
        }


        return personLinkedList;
    }

    public List<Actividades_empleados> getAllActividades(String filter) {
        String query;
        if(filter.equals("")){
            query = "SELECT  * FROM " + TABLE_CLOCKING_NAME + "ORDER BY " + filter + " DESC";
        }else{

            query = "SELECT  * FROM " + TABLE_CLOCKING_NAME +" ORDER BY " + filter + " DESC";
        }

        List<Actividades_empleados> personLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Actividades_empleados person;

        if (cursor.moveToFirst()) {
            do {
                person = new Actividades_empleados();

                person.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_CLOCKING_ID)));
                person.setUserid(cursor.getString(cursor.getColumnIndex(COLUMN_CLOCKING_USERID)));
                person.setWorking(cursor.getString(cursor.getColumnIndex(COLUMN_CLOCKING_IN)));
                person.setWorkout(cursor.getString(cursor.getColumnIndex(COLUMN_CLOCKING_OUT)));
                person.setBreaking(cursor.getString(cursor.getColumnIndex(COLUMN_CLOCKING_BREAKIN)));
                person.setBreakout(cursor.getString(cursor.getColumnIndex(COLUMN_CLOCKING_BREAKOUT)));
                person.setFormatdate(cursor.getString(cursor.getColumnIndex(COLUMN_CLOCKING_FORMATDATE)));
                personLinkedList.add(person);
            } while (cursor.moveToNext());
            db.close();
            cursor.close();
        }


        return personLinkedList;
    }
    //Query only 1 record by id and password

    public Empleados_admin getEmpleado(String id, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME + " WHERE _id="+ id + " AND password=" + password;
        Cursor cursor = db.rawQuery(query, null);

        Empleados_admin receivedPerson = new Empleados_admin();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            receivedPerson.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_NAME)));
            receivedPerson.setLastname(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_LASTNAME)));
            receivedPerson.setNumber_phone(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_PHONE)));
            receivedPerson.setOccupation(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_OCCUPATION)));
            receivedPerson.setArea(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_AREA)));
            receivedPerson.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_EMAIL)));
            receivedPerson.setBirthday(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_BIRTHDAY)));
            receivedPerson.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_ADDRESS)));
            receivedPerson.setDatework(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_STARTEDDATE)));
            receivedPerson.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_IMAGE)));
            receivedPerson.setBlocked(cursor.getInt(cursor.getColumnIndex(COLUMN_PERSON_BLOCKED)));

            receivedPerson.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_PASSWORD)));
            return receivedPerson;
        }
        return null;
    }

    /**delete record**/
    public void deletePerson(long id, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+TABLE_CLOCKING_NAME+" WHERE userid='"+id+"'");
        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE _id='"+id+"'");
        Toast.makeText(context, context.getString(R.string.emple_borrado), Toast.LENGTH_SHORT).show();

    }

    /**update record**/
    public void updatePerson(long personId, Context context, Empleados_admin updatedperson) {
        SQLiteDatabase db = this.getWritableDatabase();
        //you can use the constants above instead of typing the column names
        db.execSQL("UPDATE  "+TABLE_NAME+
                " SET name ='"+ updatedperson.getName() +
                "', lastname ='" + updatedperson.getLastname()+
                "', image ='" + updatedperson.getImage()+
                "', email ='" + updatedperson.getEmail()+
                "', phone ='" + updatedperson.getNumber_phone()+
                "', address ='" + updatedperson.getAddress()+
                "', occupation ='" + updatedperson.getOccupation()+
                "', area ='"+ updatedperson.getArea() +
                "', blocked ='"+ updatedperson.getBlocked() +
                "', password ='"+ updatedperson.getPassword() +
                "', started_date ='"+ updatedperson.getDatework() +
                "'  WHERE _id='" + personId + "'");
        Toast.makeText(context, context.getString(R.string.datos_actualizados), Toast.LENGTH_SHORT).show();


    }
    public void updatePasswordPerson(long personId, Context context, Empleados_admin updatedperson) {
        SQLiteDatabase db = this.getWritableDatabase();
        //you can use the constants above instead of typing the column names
        db.execSQL("UPDATE  "+TABLE_NAME+
                " SET name ='"+ updatedperson.getName() +
                "', lastname ='" + updatedperson.getLastname()+
                "', image ='" + updatedperson.getImage()+
                "', email ='" + updatedperson.getEmail()+
                "', phone ='" + updatedperson.getNumber_phone()+
                "', address ='" + updatedperson.getAddress()+
                "', occupation ='" + updatedperson.getOccupation()+
                "', area ='"+ updatedperson.getArea() +
                "', blocked ='"+ updatedperson.getBlocked() +

                "', password ='"+ updatedperson.getPassword() +
                "', started_date ='"+ updatedperson.getDatework() +
                "'  WHERE _id='" + personId + "'");
        Toast.makeText(context, context.getString(R.string.pin_cambiado), Toast.LENGTH_SHORT).show();


    }

    //insertar entradas y salidas por fecha
    public void insert_user_workin(long userid, String datex, String datetimex,String formate) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CLOCKING_USERID, userid);
        values.put(COLUMN_CLOCKING_SDATE, datex);
        values.put(COLUMN_CLOCKING_IN, datetimex);
        values.put(COLUMN_CLOCKING_FORMATDATE,formate);

        // insert
        db.insert(TABLE_CLOCKING_NAME,null, values);
        db.close();
    }


    //insertar entradas y salidas por fecha
    /**update record**/
    public void insert_user_workout (String user_id, String workin, String workout, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        //you can use the constants above instead of typing the column names
        db.execSQL("UPDATE  "+TABLE_CLOCKING_NAME+
                " SET workout ='" + workout +
                "'  WHERE userid='" + user_id + "' AND workin='" + workin +
                "'");
        Toast.makeText(context, context.getString(R.string.salida_registrada), Toast.LENGTH_LONG).show();


    }

    public void insert_user_breakout (String user_id, String workin, String breakout, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        //you can use the constants above instead of typing the column names
        db.execSQL("UPDATE  "+TABLE_CLOCKING_NAME+
                " SET breakout ='" + breakout +
                "'  WHERE userid='" + user_id + "' AND workin='" + workin +
                "'");
        Toast.makeText(context, context.getString(R.string.salida_registrada), Toast.LENGTH_LONG).show();

    }

    public void insert_user_breakin (String user_id, String workin, String breakin, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        //you can use the constants above instead of typing the column names
        db.execSQL("UPDATE  "+ TABLE_CLOCKING_NAME +
                " SET breakin ='" + breakin +
                "'  WHERE userid='" + user_id + "' AND workin='" + workin +
                "'");

        Toast.makeText(context, context.getString(R.string.vuelta_trabajo), Toast.LENGTH_LONG).show();

    }

    public Cursor already_workin_today(String id, String date){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT  * FROM " + TABLE_CLOCKING_NAME + " WHERE userid="+ id;

        //   String query = "SELECT  * FROM " + TABLE_CLOCKING_NAME + " WHERE userid="+ id + " AND date='" + date + "'";
        Cursor cursor = db.rawQuery(query, null);

        //Empleados_admin receivedPerson = new Empleados_admin();
        // if(cursor.getCount() > 0) {


        return cursor;


    }
    public Cursor get_all_users(){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT  * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);

        return cursor;


    }

    public Cursor get_all_users_report(){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT  * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);

        return cursor;


    }

    public  Cursor user_activity_from_date1 (String _id, String idate, String fdate) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_CLOCKING_NAME + " WHERE userid="+ _id
                + " AND  strftime('%d-%m-%Y', workin) BETWEEN strftime('%d-%m-%Y', '" + idate + "') " +
                "AND strftime('%d-%m-%Y', '" + fdate + "') " +
                " ORDER BY _id " + " DESC";

        //  Cursor mCursor = db.query(TABLE_CLOCKING_NAME, null,
        //        "userid = ? AND workin BETWEEN strftime('%d-%m-%Y', ?) AND strftime('%d-%m-%Y', ?)",
        //      new String[] {_id, idate, fdate} ,
        //    null, null, "_id DESC");



        Cursor c = db.rawQuery(query, null);

        //return mCursor;
        return c;
    }
    public  Cursor user_activity_from_date (String _id, String idate, String fdate) {
        SQLiteDatabase db = this.getReadableDatabase();

        // String query = "SELECT * FROM " + TABLE_CLOCKING_NAME + " WHERE userid="+ _id
        //       +
        //     " ORDER BY _id " + " DESC";

        Cursor mCursor = db.query(TABLE_CLOCKING_NAME, null,
                "userid = ? AND sdate BETWEEN ? AND ?",
                new String[] {_id, idate, fdate} ,
                null, null, "_id DESC");



        //Cursor c = db.rawQuery(query, null);

        return mCursor;
        //return c;
    }
    public Cursor get_all_activitys () {
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor mCursor = db.query(TABLE_CLOCKING_NAME, null,
                null,
                null,
                null, null, null);
        return mCursor;

    }

}
