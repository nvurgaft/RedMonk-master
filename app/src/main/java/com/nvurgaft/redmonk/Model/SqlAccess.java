package com.nvurgaft.redmonk.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nvurgaft.redmonk.Entities.Contact;
import com.nvurgaft.redmonk.Entities.DailyConsumption;
import com.nvurgaft.redmonk.Entities.Reminder;
import com.nvurgaft.redmonk.Entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koby on 26-Jun-15.
 */
public class SqlAccess extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RedMonk";
    private static final int DATABASE_VERSION = 1;
    private static final String USER_TABLE = "users";
    private static final String REMINDER_TABLE = "reminders";
    private static final String DAILY_CONSUMPTION_TABLE = "daily_consumption";
    private static final String CONTACTS_TABLE = "emergency_contacts";

    private static final String GENDER = "gender";
    private static final String HEIGHT = "height";
    private static final String WEIGHT = "weight";
    private static final String DIABETES_TYPE = "diabetes_type";

    private static final String RID = "r_id";
    private static final String TODO = "todo";
    private static final String RESOLVED = "resolve";

    private static final String DATE = "date";
    private static final String HOUR = "time_hour";
    private static final String MINUTE = "time_minute";
    private static final String CALORIES = "calories";
    private static final String CARBOHYDRATES = "carbohydrates";
    private static final String PROTEINS = "proteins";
    private static final String FATS = "fats";

    private static final String CONTACT_NAME = "contact_name";
    private static final String CONTACT_ROLE = "contact_role";
    private static final String FIRST_NUMBER = "first_number";
    private static final String SECOND_NUMBER = "second_number";
    private static final String THIRD_NUMBER = "third_number";


    public SqlAccess(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // create the users table
        db.execSQL("CREATE TABLE " + USER_TABLE + " (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DATE + " TEXT NOT NULL," +
                        GENDER + " TEXT NOT NULL," +
                        HEIGHT + " INTEGER NOT NULL," +
                        WEIGHT + " INTEGER NOT NULL," +
                        DIABETES_TYPE + " INTEGER NOT NULL " +
                        ");"
        );

        // create the reminders table
        db.execSQL("CREATE TABLE " + REMINDER_TABLE + " (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RID + " INTEGER NOT NULL," +
                        HOUR + " INTEGER NOT NULL," +
                        MINUTE + " INTEGER NOT NULL," +
                        TODO + " TEXT," +
                        RESOLVED + " TEXT" +
                        ");"
        );

        // create the daily consumption table
        db.execSQL("CREATE TABLE " + DAILY_CONSUMPTION_TABLE + " (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DATE + " TEXT NOT NULL, " +
                        CALORIES + " INTEGER, " +
                        CARBOHYDRATES + " INTEGER, " +
                        PROTEINS + " INTEGER, " +
                        FATS + " INTEGER NOT NULL" +
                        ");"
        );

        // create the contacts table
        db.execSQL("CREATE TABLE " + CONTACTS_TABLE + " (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        CONTACT_NAME + " TEXT NOT NULL, " +
                        CONTACT_ROLE + " INTEGER, " +
                        FIRST_NUMBER + " INTEGER, " +
                        SECOND_NUMBER + " INTEGER, " +
                        THIRD_NUMBER + " INTEGER" +
                        ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.setVersion(newVersion);
        }
    }

    /************************
     *  USERS TABLE METHODS *
     ************************/

    /**
     * Inserts a new user
     *
     * @param db
     * @param newUser
     * @return the id of the inserted row or -1 if error
     */
    public long insertNewUserLog(SQLiteDatabase db, User newUser) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE, newUser.getDate());
        contentValues.put(GENDER, newUser.getGender());
        contentValues.put(HEIGHT, newUser.getHeight());
        contentValues.put(WEIGHT, newUser.getWeight());
        contentValues.put(DIABETES_TYPE, newUser.getDiabetesType());
        return db.insert(USER_TABLE, null, contentValues);
    }

    /**
     * Gets a user log by date
     *
     * @param db
     * @param date
     * @return the requested user object (if exists)
     */
    public User getUserByDate(SQLiteDatabase db, String date) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE " + date + " = '" + date + "';", null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            User user = new User();
            user.setDate(cursor.getString(0));
            user.setGender(cursor.getString(1));
            user.setHeight(cursor.getInt(2));
            user.setWeight(cursor.getInt(3));
            user.setDiabetesType(cursor.getString(4));
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    /**
     * Gets all users
     *
     * @param db
     * @return all users in database
     */
    public List<User> getAllUserLogs(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE + ";", null);

        ArrayList<User> users = new ArrayList<>();
        while (cursor.moveToNext()) {
            User user = new User();
            user.setDate(cursor.getString(0));
            user.setGender(cursor.getString(1));
            user.setHeight(cursor.getInt(2));
            user.setWeight(cursor.getInt(3));
            user.setDiabetesType(cursor.getString(4));
            users.add(user);
        }
        cursor.close();
        return users;
    }

    /**
     * Returns all user logs
     *
     * @param db
     * @return a cursor of all user logs from the database
     */
    public Cursor getAllUserLogsCursor(SQLiteDatabase db) {
        return db.rawQuery("SELECT * FROM " + USER_TABLE + ";", null);
    }

    /****************************
     *  REMINDERS TABLE METHODS *
     ****************************/

    /**
     * Inserts a new reminder record to databse
     *
     * @param db
     * @param newReminder
     * @return
     */
    public long insertNewReminder(SQLiteDatabase db, Reminder newReminder) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RID, newReminder.getReminderId());
        contentValues.put(HOUR, newReminder.getHour());
        contentValues.put(MINUTE, newReminder.getMinute());
        contentValues.put(TODO, newReminder.getTodo());
        contentValues.put(RESOLVED, newReminder.getResolved());
        return db.insert(REMINDER_TABLE, null, contentValues);
    }

    /**
     * Edits a reminder using the r_id type
     *
     * @param db
     * @param newReminder
     * @return
     */
    public long updateReminder(SQLiteDatabase db, Reminder newReminder) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RID, newReminder.getReminderId());
        contentValues.put(HOUR, newReminder.getHour());
        contentValues.put(MINUTE, newReminder.getMinute());
        contentValues.put(TODO, newReminder.getTodo());
        contentValues.put(RESOLVED, newReminder.getResolved());
        return db.update(REMINDER_TABLE, contentValues, RID + " = " + newReminder.getReminderId(), null);
    }

    /**
     * Deletes a reminders using the _id type
     *
     * @param db
     * @param id
     * @return
     */
    public long removeReminder(SQLiteDatabase db, int id) {
        return db.delete(REMINDER_TABLE, "_id = " + id, null);
    }

    /**
     * Truncates the reminders table
     *
     * @param db
     * @return
     */
    public int deleteAllReminders(SQLiteDatabase db) {
        return db.delete(REMINDER_TABLE, "1", null);
    }

    /**
     * Returns a cursor holding all reminders records
     *
     * @param db
     * @return
     */
    public Cursor getRemindersCursor(SQLiteDatabase db) {
        return db.rawQuery("SELECT * FROM " + REMINDER_TABLE + ";", null);
    }

    /**
     * Sets a specified row (using r_id) to be resolved or not
     *
     * @param db
     * @param r_id
     * @param resolved
     */
    public void setResolved(SQLiteDatabase db, long r_id, boolean resolved) {
        db.execSQL("UPDATE " + REMINDER_TABLE + " SET " + RESOLVED + "  = " + (resolved ? "'true'" : "'false'") + " WHERE " + RID + " = " + r_id + ";");
    }

    /************************************
     *  DAILY CONSUMPTION TABLE METHODS *
     ************************************/

    /**
     * Inserts the daily consumption for the past day
     *
     * @param db
     * @param dailyConsumption
     * @returns the inserted row id, or -1 if error
     */
    public long insertDailyConsumption(SQLiteDatabase db, DailyConsumption dailyConsumption) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE, dailyConsumption.getDate());
        contentValues.put(CALORIES, dailyConsumption.getCalories());
        contentValues.put(CARBOHYDRATES, dailyConsumption.getCarbohydrates());
        contentValues.put(PROTEINS, dailyConsumption.getProteins());
        contentValues.put(FATS, dailyConsumption.getFats());
        return db.insert(DAILY_CONSUMPTION_TABLE, null, contentValues);
    }

    /**
     * Updates a daily consumption record by date
     *
     * @param db
     * @param dailyConsumption
     * @return retuns the amount of records affected by the update (should always be 0 or 1)
     */
    public long updateDailyConsumptionByDate(SQLiteDatabase db, DailyConsumption dailyConsumption) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE, dailyConsumption.getDate());
        contentValues.put(CALORIES, dailyConsumption.getCalories());
        contentValues.put(CARBOHYDRATES, dailyConsumption.getCarbohydrates());
        contentValues.put(PROTEINS, dailyConsumption.getProteins());
        contentValues.put(FATS, dailyConsumption.getFats());
        return db.update(DAILY_CONSUMPTION_TABLE, contentValues, DATE + " = " + dailyConsumption.getDate(), null);
    }

    /**
     * Returns the daily consumption for a provided date for a user
     *
     * @param db
     * @param date
     * @return return the daily consumption for a user for a specific date
     */
    public Cursor getDailyConsumptionForDate(SQLiteDatabase db, String date) {
        return db.rawQuery("SELECT * FROM " + DAILY_CONSUMPTION_TABLE + " WHERE " + DATE + " = '" + date + "';", null);
    }

    /**
     * Returns all daily consumption dates for a user
     *
     * @param db
     * @return a cursor of all daily consumption for a user
     */
    public List<DailyConsumption> getAllDailyConsumptions(SQLiteDatabase db) {

        Cursor cursor = db.rawQuery("SELECT * FROM " + DAILY_CONSUMPTION_TABLE + ";", null);
        ArrayList<DailyConsumption> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            DailyConsumption dailyConsumption = new DailyConsumption();
            dailyConsumption.setDate(cursor.getInt(0));
            dailyConsumption.setCalories(cursor.getInt(1));
            dailyConsumption.setCarbohydrates(cursor.getInt(2));
            dailyConsumption.setProteins(cursor.getInt(3));
            dailyConsumption.setFats(cursor.getInt(4));
            list.add(dailyConsumption);
        }
        cursor.close();
        return list;
    }

    /**
     * Returns all daily consumptions
     *
     * @param db
     * @return all daily consumptions from the database
     */
    public Cursor getAllDailyConsumptionsCursor(SQLiteDatabase db) {
        return db.rawQuery("SELECT * FROM " + DAILY_CONSUMPTION_TABLE + ";", null);
    }

    /**
     *  Deletes a consumption record using a date value
     * @param db
     * @param date
     * @return
     */
    public int deleteConsumptionLogByDate(SQLiteDatabase db, long date) {
        return db.delete(DAILY_CONSUMPTION_TABLE, DATE + " = '" + date + "'", null);
    }

    /**
     *  Truncates the Consumptions logs table
     * @param db
     * @return
     */
    public int deleteAllConsumptionLogs(SQLiteDatabase db) {
        return db.delete(DAILY_CONSUMPTION_TABLE, "1", null);
    }

    /********************************
     *  EMERGENCY CONTACTS METHODS  *
     ********************************/

    /**
     * Inserts a new contact
     *
     * @param db
     * @param newContact
     * @returns the inserted row id, or -1 if error
     */
    public long insertNewContact(SQLiteDatabase db, Contact newContact) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACT_NAME, newContact.getContactName());
        contentValues.put(CONTACT_ROLE, newContact.getContactRole());
        contentValues.put(FIRST_NUMBER, newContact.getFirstNumber());
        contentValues.put(SECOND_NUMBER, newContact.getSecondNumber());
        contentValues.put(THIRD_NUMBER, newContact.getThirdNumber());
        return db.insert(CONTACTS_TABLE, null, contentValues);
    }

    /**
     * Updates an existing contact
     *
     * @param db
     * @param contact
     * @return number of rows updated
     */
    public int updateContact(SQLiteDatabase db, Contact contact) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACT_NAME, contact.getContactName());
        contentValues.put(CONTACT_ROLE, contact.getContactRole());
        contentValues.put(FIRST_NUMBER, contact.getFirstNumber());
        contentValues.put(SECOND_NUMBER, contact.getSecondNumber());
        contentValues.put(THIRD_NUMBER, contact.getThirdNumber());
        return db.update(CONTACTS_TABLE, contentValues, CONTACT_NAME + " = '" + contact.getContactName() + "'", null);
    }

    /**
     * Returns all contacts for a user name
     *
     * @param db
     * @return a list of all contacts for a user
     */
    public ArrayList<Contact> getContacts(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + CONTACTS_TABLE + ";", null);
        ArrayList<Contact> contacts = new ArrayList<>();
        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            contact.setContactName(cursor.getString(0));
            contact.setContactRole(cursor.getString(1));
            contact.setFirstNumber(cursor.getString(2));
            contact.setSecondNumber(cursor.getString(3));
            contact.setThirdNumber(cursor.getString(4));
            contacts.add(contact);
        }
        cursor.close();
        return null;
    }

    /**
     * Returns a cursor of all contacts
     *
     * @param db
     * @return cursor of all contacts in database
     */
    public Cursor getContactsCursor(SQLiteDatabase db) {
        return db.rawQuery("SELECT * FROM " + CONTACTS_TABLE + ";", null);
    }

    /**
     * Removes a contact by the contact name
     *
     * @param db
     * @param contactName
     * @return number of rows deleted
     */
    public int removeContactByName(SQLiteDatabase db, String contactName) {
        return db.delete(CONTACTS_TABLE, CONTACT_NAME + " = '" + contactName + "'", null);
    }

    /**
     * Removes all contacts for the user
     *
     * @param db
     * @return number of rows deleted
     */
    public int deleteAllContacts(SQLiteDatabase db) {
        return db.delete(CONTACTS_TABLE, "1", null);
    }

}
