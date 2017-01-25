package com.gcme.addischurch.addischurch.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by kzone on 8/25/2016.
 */
public class DatabaseAdaptor {
    DatabaseHelper helper;

    String TABLE1_NAME = "church_table";
    String TABLE2_NAME = "schedules_table";
    String TABLE3_NAME = "denominations_table";
    String TABLE4_NAME = "fav_table";
    String TABLE5_NAME = "my_church";

    public String ID  = "_id";

    public String NAME  = "_church_name";
    public String CHURCH_LOCATION  = "_location";
    public String CONTACTS  = "_contacts";
    public String WEB="_website";
    public String LONGITUDE="_longitude";
    public String LATITUDE="_latitude";
    public String ImageLoction="_imagesLocation";
    public String ImageUrl="_ImageUrl";



    public String ChurchId = "church_id";
    public String SheduleDate = "schedule_date";
    public String ScheduleTime= "schedule_time";
    public String ScheduleCategory= "schedule_category";

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

    public String Home_Church  = "home_church";

    public DatabaseAdaptor(Context context) {
        helper = new DatabaseHelper(context);
    }







    /**INSERT DATA FROM JSON **/

    /**Church Info**/

    public long InsertChurch(String Id,String name ,  String churchlocation , String contacts,String web, String sermons, String category, String longitude,String latitude, String ImageLoction , String ImageUrl) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ID, Id);
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
        db.close();
        return id;
    }

    public long InsertChurchSchedule(String Id, String churchId, String scheduledate, String scheduletime, String scheduleCategory){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ID,Id);
        contentValues.put(DatabaseHelper.ChurchId, churchId);
        contentValues.put(DatabaseHelper.SheduleDate,scheduledate);
        contentValues.put(DatabaseHelper.ScheduleTime, scheduletime);
        contentValues.put(DatabaseHelper.ScheduleCategory, scheduleCategory);

        long id = db.insert(DatabaseHelper.TABLE2_NAME, null, contentValues);
        db.close();
        return id;
    }




    public Cursor getScheduleDataRowById(String churchid) {
//        SQLiteDatabase db = helper.getReadableDatabase();
//        String selectQuery =  "SELECT  rowid as " +
//                ScheduleCategory + "," +
//                SheduleDate + "," +
//                ScheduleTime +
//                " FROM " + TABLE2_NAME +
//                " WHERE " +  ChurchId + "  LIKE  '%" +churchid + "%' "
//                ;
//
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        // looping through all rows and adding to list
//
//
//        if (cursor != null) {
//            cursor.moveToFirst();
//        }
////        if (cursor == null) {
////            return null;
////        } else if (!cursor.moveToFirst()) {
////            cursor.close();
////            return null;
////        }
//        return cursor;


        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE2_NAME + " WHERE " + ChurchId + " = '" + churchid + "'", null);
        if (c != null) {
            c.moveToFirst();
        }
        db.close();
        return c;

    }


    public Cursor CheckhomeRowById(String churchid) {



        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + Home_Church + " FROM " + TABLE5_NAME + " WHERE " + Home_Church + " = '" + churchid + "'", null);
        if (c != null) {
            c.moveToFirst();
        }
        db.close();
        return c;

    }



    public Cursor checkfavDataRowById(String churchid) {



        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + FavSelected + " FROM " + TABLE4_NAME + " WHERE " + FavSelected + " = '" + churchid + "'", null);
        if (c != null) {
            c.moveToFirst();
        }
        db.close();
        return c;

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



    /**Delete from database**/
    public long deletefavData(String Id) {

        SQLiteDatabase db = helper.getWritableDatabase();

        long id= db.delete(TABLE4_NAME, FavSelected+"=?", new String[] {Id});
        return id;

    }




    public long InsertHome(String newhomeid) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.Home_Church, newhomeid);
        long id = db.insert(DatabaseHelper.TABLE5_NAME, null, contentValues);
        return id;
    }




    public Cursor gethome(){

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+TABLE5_NAME,null);
        return c;

    }


    public void changehome() {

        SQLiteDatabase db = helper.getWritableDatabase();


        db.execSQL("delete from "+ TABLE5_NAME);




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

    public Cursor getAllSchedules(){

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+TABLE2_NAME,null);
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
                ID + "," +
                NAME + "," +
                CHURCH_LOCATION +
                " FROM " + TABLE1_NAME +
                " WHERE " +  NAME + "  LIKE  '%" +search + "%' "
                ;

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

//        if (cursor == null) {
//
//            return null;
//        } else if (!cursor.moveToFirst()) {
//            cursor.close();
//
//            return null;
//        }
        return cursor;
    }

/**END OF SEARCH**/



    /**FOR SEARCH**/

    public Cursor gedenominationListByKeyword(String search,String category) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String selectQuery =  "SELECT  rowid as " +
                ID + "," +
                NAME + "," +
                CHURCH_LOCATION+ "," +
                 CATEGORY +
                " FROM " + TABLE1_NAME +
                " WHERE " +  NAME + "  LIKE  '%" +search + "%' "+ " and " + CATEGORY + "  LIKE  '%" +category + "%' "
                ;

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

//        if (cursor == null) {
//
//            return null;
//        } else if (!cursor.moveToFirst()) {
//            cursor.close();
//
//            return null;
//        }
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

    public Cursor getChurchName(String favid) {




        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor  c=db.rawQuery("SELECT * FROM " + TABLE1_NAME +" WHERE " + NAME + " = '" + favid +"'", null);
//        if (c!=null){
//            c.moveToFirst();
//        }
        return c;
//        SQLiteDatabase db = helper.getReadableDatabase();
//        String selectQuery =  "SELECT  rowid as " +
//                "_id" + "," +
//                NAME + "," +
//                CATEGORY +
//                " FROM " + TABLE1_NAME +
//                " WHERE " +  ID + "  LIKE  '%" +favid + "%' "
//                ;
//
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        // looping through all rows and adding to list
//
//        if (cursor == null) {
//            return null;
//        } else if (!cursor.moveToFirst()) {
//            cursor.close();
//            return null;
//        }
//        return cursor;

    }

/**END OF SEARCH**/

public void deletefav(String favid)
{
    SQLiteDatabase db =helper.getWritableDatabase();
    try
    {
        db.delete(TABLE4_NAME, FavSelected +" = ?", new String[] { favid });
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    finally
    {
        db.close();
    }
}



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

    public boolean hasObjectChurch(String id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE1_NAME + " WHERE " + ID + " =?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[] {id});

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;

            //region if you had multiple records to check for, use this region.

            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }
            //here, count is records found
            Log.d(TAG, String.format("%d records found", count));

            //endregion

        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }

    public boolean hasObjectSchedule(String id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE2_NAME + " WHERE " + ID + " =?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[] {id});

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;

            //region if you had multiple records to check for, use this region.

            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }
            //here, count is records found
            Log.d(TAG, String.format("%d records found", count));

            //endregion

        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "Addis_churches";

        private static final Integer DATABASE_VERSION = 11;

        private static final String TABLE1_NAME = "church_table";
        private static final String TABLE2_NAME = "schedules_table";
        private static final String TABLE3_NAME = "denominations_table";
        private static final String TABLE4_NAME = "fav_table";
        private static final String TABLE5_NAME = "my_church";


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


        private static final String ChurchId = "church_id";
        private static final String SheduleDate = "schedule_date";
        private static final String ScheduleTime= "schedule_time";
        private static final String ScheduleCategory= "schedule_category";


        private static final String EVENTNAME="_event_name";
        private static final String EVENTADDRESS="_event_address";
        private static final String DATE="_date";
        private static final String TIME="_time";
        private static final String EVENTCHURCHNAME="_event_church_name";


        private static final String Home_Church  = "home_church";


        private static final String FavSelected  = "Fav_Selected";

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE1_NAME + "(" + ID + " integer PRIMARY KEY ," + NAME + " VARCHAR(255), " + CHURCH_LOCATION + " VARCHAR(255)," + CONTACTS +" VARCHAR(255)," + WEB + " VARCHAR(255), "+ SERMONS + " VARCHAR(255), " + CATEGORY + " VARCHAR(255), " + LONGITUDE + " VARCHAR(255)," + LATITUDE + " VARCHAR(255)," + ImageLoction + " VARCHAR(255),"+ ImageUrl + " VARCHAR(255));";
        private static final String CREATE_TABLE2 = "CREATE TABLE " + TABLE2_NAME + "(" + ID + " integer PRIMARY KEY ," + ChurchId + " VARCHAR(255), " + SheduleDate + " VARCHAR(255)," + ScheduleTime +" VARCHAR(255), "+ ScheduleCategory + " VARCHAR(255));";
        private static final String CREATE_TABLE3 = "CREATE TABLE " + TABLE3_NAME + "(" + IDCAT + " integer PRIMARY KEY AUTOINCREMENT," + CATEGORY + " VARCHAR(255)," + CAT_IMG_URL + " VARCHAR(255)," + CAT_IMG_LOC + " VARCHAR(255));";

        private static final String CREATE_TABLE4 = "CREATE TABLE " + TABLE4_NAME + "(" + ID + " integer PRIMARY KEY AUTOINCREMENT," + FavSelected + " VARCHAR(255));";
        private static final String CREATE_TABLE5 = "CREATE TABLE " + TABLE5_NAME + "(" + Home_Church + " VARCHAR(255));";



        private static final String DROPE_TABLE = "DROP TABLE IF EXISTS " + TABLE1_NAME;
         private static final String DROPE_TABLE2 = "DROP TABLE IF EXISTS " + TABLE2_NAME;
        private static final String DROPE_TABLE3 = "DROP TABLE IF EXISTS " + TABLE3_NAME;
        private static final String DROPE_TABLE4 = "DROP TABLE IF EXISTS " + TABLE4_NAME;
        private static final String DROPE_TABLE5 = "DROP TABLE IF EXISTS " + TABLE5_NAME;
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
                 db.execSQL(CREATE_TABLE2);
                db.execSQL(CREATE_TABLE3);
                db.execSQL(CREATE_TABLE4);
                db.execSQL(CREATE_TABLE5);
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
                db.execSQL(DROPE_TABLE2);
                db.execSQL(DROPE_TABLE3);
                db.execSQL(DROPE_TABLE4);
                db.execSQL(DROPE_TABLE5);
                onCreate(db);
                // Message.message(context, "upgrade");
            } catch (SQLException e) {
                e.printStackTrace();
                // Message.message(context, "upgrade" + e);
            }

        }


    }
}


