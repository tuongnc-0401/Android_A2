package rmit.tuong.s3818196;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    // constant for SITE TABLE
    public static final String SITE_TABLE = "SITE_TABLE";
    public static final String COLUMN_SITE_ID = "SITE_ID";
    public static final String COLUMN_SITE_NAME = "SITE_NAME";
    public static final String COLUMN_SITE_LONGITUDE = "SITE_LONGITUDE";
    public static final String COLUMN_SITE_LATITUDE = "SITE_LATITUDE";
    public static final String COLUMN_SITE_LEADER_NAME = "SITE_LEADER_NAME";
    public static final String COLUMN_SITE_LEADER_ID = "SITE_LEADER_ID";
    public static final String COLUMN_SITE_NUM_PEOPLE_TESTED = "SITE_NUM_PEOPLE_TESTED";
    public static final String COLUMN_SITE_LIST_VOLUNTEER = "COLUMN_SITE_LIST_VOLUNTEER";
    public static final String COLUMN_SITE_NUM_OF_VOLUNTEER = "COLUMN_SITE_NUM_OF_VOLUNTEER";
    public static final String COLUMN_SITE_NUM_OF_POSITIVE = "COLUMN_SITE_NUM_OF_POSITIVE";
    public static final String COLUMN_SITE_NUM_OF_NEGATIVE = "COLUMN_SITE_NUM_OF_NEGATIVE";
    private static final String TAG = "DatabaseHelper";
    // constant for USER TABLE
    public static final String USER_TABLE = "USER_TABLE";
    public static final String COLUMN_USER_ID = "COLUMN_USER_ID";
    public static final String COLUMN_USER_USERNAME = "COLUMN_USER_USERNAME";
    public static final String COLUMN_USER_PASSWORD = "COLUMN_USER_PASSWORD";
    public static final String COLUMN_USER_ROLE = "COLUMN_USER_ROLE";

    // constant for MEMBERSHIP SITE
    public static final String MEMBERSHIP_TABLE = "MEMBERSHIP_TABLE";
    public static final String COLUMN_MEMBERSHIP_ID = "COLUMN_MEMBERSHIP_ID";
    public static final String COLUMN_MEMBERSHIP_USER_ID = "COLUMN_MEMBERSHIP_USER_ID";
    public static final String COLUMN_MEMBERSHIP_USERNAME = "COLUMN_MEMBERSHIP_USERNAME";
    public static final String COLUMN_MEMBERSHIP_SITE_ID = "COLUMN_MEMBERSHIP_SITE_ID";


    public DatabaseHelper(@Nullable Context context) {
        super(context, "s3818196.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createSiteTableStatement = "CREATE TABLE " + SITE_TABLE+
                " (" + COLUMN_SITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_SITE_NAME + " TEXT, "
                + COLUMN_SITE_LONGITUDE + " REAL, "
                + COLUMN_SITE_LATITUDE + " REAL, "
                + COLUMN_SITE_LEADER_NAME + " TEXT, "
                + COLUMN_SITE_LEADER_ID + " INT, "
                + COLUMN_SITE_NUM_PEOPLE_TESTED + " INT, "
                + COLUMN_SITE_LIST_VOLUNTEER + " TEXT, "
                + COLUMN_SITE_NUM_OF_VOLUNTEER + " INT, "
                + COLUMN_SITE_NUM_OF_POSITIVE + " INT, "
                + COLUMN_SITE_NUM_OF_NEGATIVE + " INT)";
               sqLiteDatabase.execSQL(createSiteTableStatement);

        String createUserTableStatement = "CREATE TABLE " + USER_TABLE+
                       " (" + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                       + COLUMN_USER_USERNAME + " TEXT, "
                       + COLUMN_USER_PASSWORD + " TEXT, "
                       + COLUMN_USER_ROLE + " TEXT)";
        sqLiteDatabase.execSQL(createUserTableStatement);
        String createMembershipTableStatement = "CREATE TABLE " + MEMBERSHIP_TABLE+
                " (" + COLUMN_MEMBERSHIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_MEMBERSHIP_USER_ID + " INT, "
                + COLUMN_MEMBERSHIP_USERNAME + " TEXT, "
                + COLUMN_MEMBERSHIP_SITE_ID + " INT)";
        sqLiteDatabase.execSQL(createMembershipTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SITE_TABLE);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
//        onCreate(sqLiteDatabase);
    }

    /**
     *
     * SITE TABLES
     */
    public boolean addOneSite(SiteModel siteModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Log.d(TAG, "addOneSite: "+ siteModel.toString());
        cv.put(COLUMN_SITE_NAME, siteModel.getName());
        cv.put(COLUMN_SITE_LONGITUDE, siteModel.getLongitude());
        cv.put(COLUMN_SITE_LATITUDE, siteModel.getLatitude());
        cv.put(COLUMN_SITE_LEADER_ID, siteModel.getLeaderID());
        cv.put(COLUMN_SITE_LEADER_NAME, siteModel.getLeaderName());
        cv.put(COLUMN_SITE_NUM_PEOPLE_TESTED, siteModel.getNumOfPeopleTested());
        cv.put(COLUMN_SITE_LIST_VOLUNTEER, siteModel.getListVolunteer());
        cv.put(COLUMN_SITE_NUM_OF_VOLUNTEER, siteModel.getNumOfVolunteer());
        cv.put(COLUMN_SITE_NUM_OF_POSITIVE, siteModel.getNumOfPositive());
        cv.put(COLUMN_SITE_NUM_OF_NEGATIVE, siteModel.getNumOfNegative());
        Log.d(TAG, "addOneSite: "+cv.toString());
        long insert = db.insert(SITE_TABLE,null, cv);
        return insert != -1;
    }

//    public boolean deleteOne (CustomerModel customerModel){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "DELETE FROM "+ CUSTOMER_TABLE+" WHERE "+ COLUMN_ID+ " = "+customerModel.getId();
//        Cursor cursor = db.rawQuery(query, null);
//        if(cursor.moveToFirst()){
//            return true;
//        } else {
//            return false;
//        }
//    }

    public List<SiteModel> getAllSite() {
        List<SiteModel> returnedList = new ArrayList<>();
        String queryString = "SELECT * FROM "+ SITE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            do {
                int siteID = cursor.getInt(0);
                String siteName = cursor.getString(1);
                double siteLongitude = cursor.getDouble(2);
                double siteLatitude = cursor.getDouble(3);
                String siteLeaderName = cursor.getString(4);
                int siteLeaderID = cursor.getInt(5);
                int siteNumOfPeopleID = cursor.getInt(6);
                String siteListVolunteer = cursor.getString(7);
                int siteNumOfVolunteer = cursor.getInt(8);
                int siteNumOfPositive = cursor.getInt(9);
                int siteNumOfNegative = cursor.getInt(10);
                SiteModel site = new SiteModel(siteID,siteName,siteLongitude,siteLatitude,siteLeaderID,siteLeaderName,siteNumOfPeopleID,siteListVolunteer,siteNumOfVolunteer,siteNumOfPositive,siteNumOfNegative);

                returnedList.add(site);

            } while (cursor.moveToNext());
        }else {

        }
        cursor.close();
        db.close();
        return  returnedList;
    }

    public int getNumOfAllSite() {

        String queryString = "SELECT * FROM "+ SITE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        int num = cursor.getCount();
        cursor.close();
        db.close();
        return  num;
    }
    public SiteModel getSiteByID(int idSite){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from "+SITE_TABLE+" where "+COLUMN_SITE_ID+" = "+idSite,null);

        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            int siteID = cursor.getInt(0);
            String siteName = cursor.getString(1);
            double siteLongitude = cursor.getDouble(2);
            double siteLatitude = cursor.getDouble(3);
            String siteLeaderName = cursor.getString(4);
            int siteLeaderID = cursor.getInt(5);
            int siteNumOfPeopleID = cursor.getInt(6);
            String siteListVolunteer = cursor.getString(7);
            int siteNumOfVolunteer = cursor.getInt(8);
            int siteNumOfPositive = cursor.getInt(9);
            int siteNumOfNegative = cursor.getInt(10);
            SiteModel site = new SiteModel(siteID,siteName,siteLongitude,siteLatitude,siteLeaderID,siteLeaderName,siteNumOfPeopleID,siteListVolunteer,siteNumOfVolunteer,siteNumOfPositive,siteNumOfNegative);

            return  site;
        } else {
            return null;
        }

    }


    /**
     * MEMBERSHIP TABLE
     */
    public Boolean createMembership(int userID, String username, int siteID){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COLUMN_MEMBERSHIP_USER_ID, userID);
        contentValues.put(COLUMN_MEMBERSHIP_USERNAME, username);
        contentValues.put(COLUMN_MEMBERSHIP_SITE_ID, siteID);
        long result = MyDB.insert(MEMBERSHIP_TABLE, null, contentValues);
        return result != -1;
    }

    public Boolean checkMembership(String userID, String username, String siteID) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from "+MEMBERSHIP_TABLE +" where "+COLUMN_MEMBERSHIP_USER_ID+" = ? and "+COLUMN_MEMBERSHIP_USERNAME+" = ? and "+ COLUMN_MEMBERSHIP_SITE_ID+" = ?", new String[]{ userID, username, siteID});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkMembershipByUserID(String userID, String siteID) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from "+MEMBERSHIP_TABLE +" where "+COLUMN_MEMBERSHIP_USER_ID+" = ? and "+ COLUMN_MEMBERSHIP_SITE_ID+" = ?", new String[]{ userID, siteID});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public int getNumOfVolunteer(String siteID){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from "+MEMBERSHIP_TABLE +" where "+ COLUMN_MEMBERSHIP_SITE_ID+" = ?", new String[]{siteID});
        int count = cursor.getCount() ;
        return count;
    }

    public int getNumOfSiteByUserID(String userID){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from "+MEMBERSHIP_TABLE +" where "+ COLUMN_MEMBERSHIP_USER_ID +" = ?", new String[]{userID});
        int count = cursor.getCount() ;
        return count;
    }

    public int getNumOfSiteByLeader(String userID){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from "+SITE_TABLE +" where "+ COLUMN_SITE_LEADER_ID +" = ?", new String[]{userID});
        int count = cursor.getCount() ;
        return count;
    }

    // list all site which this user are volunteer
    public List<String> getAllSiteOfVolunteer(String userID){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from "+MEMBERSHIP_TABLE +" where "+ COLUMN_MEMBERSHIP_USER_ID +" = ?", new String[]{userID});
        List<String> list = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                int ID = cursor.getInt(0);
                int userID1 = cursor.getInt(1);
                String username = cursor.getString(2);
                int idOfSite = cursor.getInt(3);

                list.add(idOfSite+"");



            } while (cursor.moveToNext());
        }else {

        }
        cursor.close();
        MyDB.close();
        return  list;
    }


    // list all site which this user are leader
    public List<SiteModel> getAllSiteOfLeader(String userID){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from "+SITE_TABLE +" where "+ COLUMN_SITE_LEADER_ID +" = ?", new String[]{userID});
        List<SiteModel> list = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                int siteID = cursor.getInt(0);
                String siteName = cursor.getString(1);
                double siteLongitude = cursor.getDouble(2);
                double siteLatitude = cursor.getDouble(3);
                String siteLeaderName = cursor.getString(4);
                int siteLeaderID = cursor.getInt(5);
                int siteNumOfPeopleID = cursor.getInt(6);
                String siteListVolunteer = cursor.getString(7);
                int siteNumOfVolunteer = cursor.getInt(8);
                int siteNumOfPositive = cursor.getInt(9);
                int siteNumOfNegative = cursor.getInt(10);
                SiteModel site = new SiteModel(siteID,siteName,siteLongitude,siteLatitude,siteLeaderID,siteLeaderName,siteNumOfPeopleID,siteListVolunteer,siteNumOfVolunteer,siteNumOfPositive,siteNumOfNegative);
                list.add(site);



            } while (cursor.moveToNext());
        }else {

        }
        cursor.close();
        MyDB.close();
        return  list;
    }

    public List<UserModel> getAllVolunteerOfOneSite(String siteID) {
        List<UserModel> returnedList = new ArrayList<>();
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from "+MEMBERSHIP_TABLE +" where "+ COLUMN_MEMBERSHIP_SITE_ID+" = ?", new String[]{siteID});

        if(cursor.moveToFirst()){
            do {
                int ID = cursor.getInt(0);
                int userID = cursor.getInt(1);
                String username = cursor.getString(2);
                int idOfSite = cursor.getInt(3);

                UserModel userModel = new UserModel(ID,username,userID+"");

                returnedList.add(userModel);

            } while (cursor.moveToNext());
        }else {

        }
        cursor.close();
        MyDB.close();
        return  returnedList;
    }




        public boolean deleteOneMembership (String userID, String siteID){
//            SQLiteDatabase MyDB = this.getWritableDatabase();
//            Cursor cursor = MyDB.rawQuery("DELETE from "+MEMBERSHIP_TABLE +" where "+COLUMN_MEMBERSHIP_USER_ID+" = ? and "+ COLUMN_MEMBERSHIP_SITE_ID+" = ?", new String[]{ userID, siteID});

            SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM "+ MEMBERSHIP_TABLE+" WHERE "+ COLUMN_MEMBERSHIP_USER_ID+ " = "+userID + " and "+COLUMN_MEMBERSHIP_SITE_ID+" = "+ siteID ;
       Cursor cursor = db.rawQuery(query, null);

            if(cursor.moveToFirst()){
            return true;
        } else {
            return false;
        }
    }



    /**
     *USER TABLE
     */

    public Boolean createUser(String username, String password, String role){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COLUMN_USER_USERNAME, username);
        contentValues.put(COLUMN_USER_PASSWORD, password);
        contentValues.put(COLUMN_USER_ROLE, role);
        long result = MyDB.insert(USER_TABLE, null, contentValues);
        return result != -1;
    }

    public Boolean checkUsername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from "+USER_TABLE +" where "+COLUMN_USER_USERNAME+" = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public UserModel getUser(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from "+USER_TABLE+" where "+COLUMN_USER_USERNAME+" = ? and "+COLUMN_USER_PASSWORD+" = ?", new String[] {username,password});
        UserModel user;
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            String username1 = cursor.getString(1);
            String role = cursor.getString(3);
            user= new UserModel(id,username1, role);
            return  user;
        } else {
            return null;
        }

    }


    public UserModel getUserByID(String idUser){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from "+USER_TABLE+" where "+COLUMN_USER_ID+" = ?", new String[] {idUser});
        UserModel user;
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            String username1 = cursor.getString(1);
            String role = cursor.getString(3);
            user= new UserModel(id,username1, role);
            return  user;
        } else {
            return null;
        }

    }



    public Boolean checkUsernamePassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from "+USER_TABLE+" where "+COLUMN_USER_USERNAME+" = ? and "+COLUMN_USER_PASSWORD+" = ?", new String[] {username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

}