package com.gcme.addischurch.addischurch.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by kzone on 8/25/2016.
 */
public class DatabaseAdaptor {
    DatabaseHelper helper;

    String TABLE1_NAME = "church_table";
    String TABLE2_NAME = "events_table";
    String TABLE3_NAME = "denominations_table";
    String TABLE4_NAME = "fav_table";
    String TABLE5_NAME = "sermons_category_table";

    public String ID  = "_id";

    public String NAME  = "_church_name";
    public String CHURCH_LOCATION  = "_location";
    public String CONTACTS  = "_contacts";
    public String WEB="_website";
    public String LONGITUDE="_longitude";
    public String LATITUDE="_latitude";
    public String SERMONS="_sermons";
    private static final String ImageLoction="_imagesLocation";
    private static final String ImageUrl="_ImageUrl";


//    public String DATE="_date";
//    public String TIME="_time";
//    public String EVENTNAME="_event_name";
//    public String EVENTCHURCHNAME="_event_church_name";
//    public String EVENTADDRESS="_event_address";




    public String CATEGORY="_church_category";
    public String IDCAT  = "_cat_id";
    public String CAT_IMG_URL  = "_cat_img_url";
    public String CAT_IMG_LOC  = "_cat_img_loc";



    public String FavSelected  = "Fav_Selected";



    public DatabaseAdaptor(Context context) {
        helper = new DatabaseHelper(context);
    }







    /**INSERT DATA FROM JSON **/

    /**Church Info**/

    public long InsertChurch(String _id,String name ,  String churchlocation , String contacts,String web, String sermons, String category, String longitude,String latitude, String ImageLoction , String ImageUrl) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ID, _id);
        contentValues.put(DatabaseHelper.NAME, name);
        contentValues.put(DatabaseHelper.CHURCH_LOCATION, churchlocation);
        contentValues.put(DatabaseHelper.CONTACTS, contacts);
        contentValues.put(DatabaseHelper.WEB, web);
        contentValues.put(DatabaseHelper.SERMONS, sermons);
        contentValues.put(DatabaseHelper.CATEGORY, category);
        contentValues.put(DatabaseHelper.LONGITUDE, longitude);
        contentValues.put(DatabaseHelper.LATITUDE, latitude);
        contentValues.put(DatabaseHelper.ImageLoction, ImageLoction);
        contentValues.put(DatabaseHelper.ImageUrl, ImageUrl);



        long id = db.insert(DatabaseHelper.TABLE1_NAME, null, contentValues);
        return id;
    }






    /**Church denomination**/

    public long InsertChurchdenomination(String _id, String category,String url,String image_location) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.IDCAT, _id);
        contentValues.put(DatabaseHelper.CATEGORY, category);
        contentValues.put(DatabaseHelper.CAT_IMG_URL, url);
        contentValues.put(DatabaseHelper.CAT_IMG_LOC, image_location);


        long id = db.insert(DatabaseHelper.TABLE3_NAME, null, contentValues);
        return id;
    }





    public long Insertfav(String favid) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.FavSelected, favid);
        long id = db.insert(DatabaseHelper.TABLE4_NAME, null, contentValues);
        return id;
    }


    public Cursor getAllfav(){

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+TABLE4_NAME,null);
        return c;

    }


    public long deletefavData(String Id) {

        SQLiteDatabase db = helper.getWritableDatabase();

        long id= db.delete(TABLE4_NAME, FavSelected+"=?", new String[] {Id});
        return id;


    }





//    /**Event Info**/
//    public long InsertEvent(String _id,String event_name ,  String eventchurchname , String eventaddress, String date, String time) {
//
//        SQLiteDatabase db = helper.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(DatabaseHelper.ID, _id);
//        contentValues.put(DatabaseHelper.EVENTNAME, event_name);
//        contentValues.put(DatabaseHelper.EVENTCHURCHNAME, eventchurchname);
//        contentValues.put(DatabaseHelper.EVENTADDRESS, eventaddress);
//        contentValues.put(DatabaseHelper.DATE, date);
//        contentValues.put(DatabaseHelper.TIME, time);
//
//        long id = db.insert(DatabaseHelper.TABLE2_NAME, null, contentValues);
//        return id;
//    }

    /**GET ALL ROWS**/

    public Cursor getAll(){

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+TABLE1_NAME,null);
        return c;

    }


    /**GET ALL ROWS of table3**/
    public Cursor getAlldenominations(){


        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor  c = db.rawQuery("select * from "+TABLE3_NAME,null);
        return c;

    }


    /**GET ALL ROWS of table2**/
    public Cursor getAlltabel2(){


        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor  c = db.rawQuery("select * from "+TABLE2_NAME,null);
        return c;

    }


    /**GET MARKER DATA**/


    public Cursor getMarkerDataRowByID(String churchId) {



        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE1_NAME + " WHERE " + ID + " = '" + churchId + "'", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;

    }



    /**GET MARKER DATA by name**/


    public Cursor getMarkerDataRowByname(String churchname) {



        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE1_NAME + " WHERE " + NAME + " = '" + churchname + "'", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;

    }




    /**GET ROWS BY CATEGORY**/

    public Cursor getSelectedRows(String CatName){



        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor  c=db.rawQuery("SELECT * FROM " + TABLE1_NAME +" WHERE " + CATEGORY + " = '" + CatName +"'", null);
        if (c!=null){
            c.moveToFirst();
        }
        return c;

    }


    /**look for specific image name**/

    public Cursor getID(String rowID) {
        String ID = "_id";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE1_NAME + " WHERE " + ID + " = '" + rowID + "'", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;

    }


    /**Delete from database**/
    public long deleteData(String Id) {

        SQLiteDatabase db = helper.getWritableDatabase();

        long id= db.delete(TABLE1_NAME, "_id=?", new String[] {Id});
        return id;


    }

    /**Update from database**/
    public long updateData(String Id,String name ,  String churchlocation , String contacts, String sermons, String category, String longitude,String latitude, String ImageLoction , String ImageUrl) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ID, Id);
        contentValues.put(DatabaseHelper.NAME, name);
        contentValues.put(DatabaseHelper.CHURCH_LOCATION, churchlocation);
        contentValues.put(DatabaseHelper.CONTACTS, contacts);

        contentValues.put(DatabaseHelper.SERMONS, sermons);
        contentValues.put(DatabaseHelper.CATEGORY, category);
        contentValues.put(DatabaseHelper.LONGITUDE, longitude);
        contentValues.put(DatabaseHelper.LATITUDE, latitude);
        contentValues.put(DatabaseHelper.ImageLoction, ImageLoction);
        contentValues.put(DatabaseHelper.ImageUrl, ImageUrl);


        long id= db.update(TABLE1_NAME, contentValues, "_id = ?", new String[]{ID});
        return id;



    }








    /**FOR SEARCH**/

    public Cursor getCategoryListByKeyword(String search) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String selectQuery =  "SELECT  rowid as " +
                "_id" + "," +
                NAME + "," +
                CATEGORY +
                " FROM " + TABLE1_NAME +
                " WHERE " +  NAME + "  LIKE  '%" +search + "%' "
                ;

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

/**END OF SEARCH**/



    /**FOR SEARCH**/

    public Cursor getCategoryList(String search) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String selectQuery =  "SELECT  rowid as " +
                "_id" + "," +
                NAME + "," +
                CATEGORY +
                " FROM " + TABLE1_NAME +
                " WHERE " +  CATEGORY + "  LIKE  '%" +search + "%' "
                ;

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

/**END OF SEARCH**/





    /**GET SINGLE ROW**/

  /*  public Cursor getRowByID(String rowID) {

        String TABLE_NAME = "CATEGORIES";

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = '" + rowID + "'", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;

    }*/




    public static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "Addis_churches";

        private static final Integer DATABASE_VERSION = 6;

        private static final String TABLE1_NAME = "church_table";
        private static final String TABLE2_NAME = "events_table";
        private static final String TABLE3_NAME = "denominations_table";
        private static final String TABLE4_NAME = "schedules_table";
        private static final String TABLE5_NAME = "schedules_category_table";


        private static final String ID  = "_id";

        private static final String IDCAT  = "_cat_id";
        private static final String CAT_IMG_URL  = "_cat_img_url";
        private static final String CAT_IMG_LOC  = "_cat_img_loc";
        private static final String CATEGORY="_church_category";

        private static final String NAME  = "_church_name";
        private static final String CHURCH_LOCATION  = "_location";
        private static final String CONTACTS  = "_contacts";
        private static final String WEB="_website";
        private static final String SERMONS="_sermons";
        private static final String LONGITUDE="_longitude";
        private static final String LATITUDE="_latitude";
        private static final String ImageLoction="_imagesLocation";
        private static final String ImageUrl="_ImageUrl";


        private static final String EVENTNAME="_event_name";
        private static final String EVENTADDRESS="_event_address";
        private static final String DATE="_date";
        private static final String TIME="_time";
        private static final String EVENTCHURCHNAME="_event_church_name";


        private static final String FavSelected  = "Fav_Selected";

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE1_NAME + "(" + ID + " integer PRIMARY KEY AUTOINCREMENT," + NAME + " VARCHAR(255), " + CHURCH_LOCATION + " VARCHAR(255)," + CONTACTS +" VARCHAR(255)," + WEB + " VARCHAR(255), "+ SERMONS + " VARCHAR(255), " + CATEGORY + " VARCHAR(255), " + LONGITUDE + " VARCHAR(255)," + LATITUDE + " VARCHAR(255)," + ImageLoction + " VARCHAR(255),"+ ImageUrl + " VARCHAR(255));";
        //private static final String CREATE_TABLE2 = "CREATE TABLE " + TABLE2_NAME + "(" + ID + " integer PRIMARY KEY AUTOINCREMENT," + EVENTNAME + " VARCHAR(255), " + EVENTCHURCHNAME + " VARCHAR(255)," + EVENTADDRESS +" VARCHAR(255), "+ DATE + " VARCHAR(255), " + TIME + " VARCHAR(255));";
        private static final String CREATE_TABLE3 = "CREATE TABLE " + TABLE3_NAME + "(" + IDCAT + " integer PRIMARY KEY AUTOINCREMENT," + CATEGORY + " VARCHAR(255)," + CAT_IMG_URL + " VARCHAR(255)," + CAT_IMG_LOC + " VARCHAR(255));";

        private static final String CREATE_TABLE4 = "CREATE TABLE " + TABLE4_NAME + "(" + ID + " integer PRIMARY KEY AUTOINCREMENT," + FavSelected + " VARCHAR(255));";



        private static final String DROPE_TABLE = "DROP TABLE IF EXISTS " + TABLE1_NAME;
        // private static final String DROPE_TABLE2 = "DROP TABLE IF EXISTS " + TABLE2_NAME;
        private static final String DROPE_TABLE3 = "DROP TABLE IF EXISTS " + TABLE3_NAME;
        private static final String DROPE_TABLE4 = "DROP TABLE IF EXISTS " + TABLE4_NAME;
        private Context context;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        /**CREATE TABLE**/

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE);
                // db.execSQL(CREATE_TABLE2);
                db.execSQL(CREATE_TABLE3);
                db.execSQL(CREATE_TABLE4);
                Toast.makeText(context, "Database Created !", Toast.LENGTH_LONG).show();


                //Message.message(context, "create");
            } catch (SQLException e) {
                //  Message.message(context, "" + e);
            }
        }

        /**UPGRADE TABLE**/

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROPE_TABLE);
                // db.execSQL(DROPE_TABLE2);
                db.execSQL(DROPE_TABLE3);
                db.execSQL(DROPE_TABLE4);

                onCreate(db);
                // Message.message(context, "upgrade");
            } catch (SQLException e) {
                e.printStackTrace();
                // Message.message(context, "upgrade" + e);
            }

        }


    }
}


