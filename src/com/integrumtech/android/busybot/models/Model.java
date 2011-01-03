package com.integrumtech.android.busybot.models;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.integrumtech.android.busybot.Integrum;
import com.integrumtech.android.busybot.connection.Request;
import com.integrumtech.android.busybot.connection.Response;

import android.util.Log;


public class Model implements Serializable {
    private static final long serialVersionUID = 2664088878505586275L;

    private static HashMap<Integer, Model> cachedModels;

    protected static String ID = "id";
    protected static String GET_ALL = "";
    protected static String GET_ALL_BY_PARENT_ID = "";


    protected JSONObject jsonObject;
    protected int id;

    /**
     * Creates an instance of JsonObject from the JSON string passed in.
     *
     * @param json a string of well formatted JSON
     * @throws JSONException if the JSON response is malformed or the model attempts to access values that don't exist
     */
    public Model(String json) throws JSONException {
        this.jsonObject = new JSONObject(json);
    }

    public int getId() {
        return id;
    }

    public static Model build(String json) throws JSONException {
        return new Model(json);
    }


    /**
     * Retrieves an instance of the specified model, if the server response
     * status code is not in the 200's or 300's, it will return an empty ArrayList.
     * @param id of the instance to return
     * @return the instance of model
     * @throws java.io.UnsupportedEncodingException thrown if one of the key/value pairs in the request are improper
     * @throws org.json.JSONException if the JSON response is malformed or the model attempts to access values that don't exist
     */
    protected static Model getInstanceById(int id) throws JSONException, UnsupportedEncodingException {
        return getInstanceById(GET_ALL, Model.class, id);
    }

    /**
     * Retrieves an instance of the specified model, if the server response
     * status code is not in the 200's or 300's, it will return an empty ArrayList.
     * Should not be made public, should be called via the overridden accessor "getInstance(id)"
     * @param path to retrieve all instances of the model
     * @param subclass of model
     * @param id of the instance to return
     * @return instance of Model
     * @throws java.io.UnsupportedEncodingException thrown if one of the key/value pairs in the request are improper
     * @throws org.json.JSONException if the JSON response is malformed or the model attempts to access values that don't exist
     */
    protected static Model getInstanceById(String path, Class<?> subclass, int id) throws JSONException, UnsupportedEncodingException {
        if (Model.getCachedModel(id) == null)
            Model.getAll(path, subclass);

        return Model.getCachedModel(id);
    }

    /**
     * Retrieves all instances of the model.
     * @return ArrayList of models
     * @throws java.io.UnsupportedEncodingException thrown if one of the key/value pairs in the request are improper
     * @throws org.json.JSONException if the JSON response is malformed or the model attempts to access values that don't exist
     */
    public static <T extends Model> HashMap<Integer, T> getAll() throws UnsupportedEncodingException, JSONException {
        return getAll(GET_ALL, Model.class);
    }

    /**
     * Retrieves all instances of the specified model, if the server response
     * status code is not in the 200's or 300's, it will return an empty ArrayList.
     * Should not be made public, should be called via the overridden accessor "getAll()"
     *
     * @param path     path to retrieve all instances of this model
     * @param subclass class of the model
     * @return ArrayList of models
     * @throws java.io.UnsupportedEncodingException
     *                                thrown if one of the key/value pairs in the request are improper
     * @throws org.json.JSONException if the JSON response is malformed or the model attempts to access values that don't exist
     */
    @SuppressWarnings("unchecked")
    protected static <T extends Model> HashMap<Integer, T> getAll(String path, Class<?> subclass) throws JSONException, UnsupportedEncodingException {
        if (Model.getCachedModels() != null)
            return (HashMap<Integer, T>) Model.getCachedModels();

        Request request = new Request(path);
        Response response = request.execute();

        if (200 <= response.getStatusCode() && response.getStatusCode() < 400) {
            String json = response.getBody();
            JSONArray jsonArray = new JSONArray(json);

            HashMap<Integer, T> models = new HashMap<Integer, T>(jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    Constructor<?> constructor = subclass.getConstructor(String.class);

                    T model = (T) constructor.newInstance(jsonArray.getString(i));
                    models.put(model.getId(), model);
                    
                } catch (SecurityException e) {
                    Log.e(Integrum.TAG, e.toString());
                } catch (NoSuchMethodException e) {
                    Log.e(Integrum.TAG, e.toString());
                } catch (IllegalArgumentException e) {
                    Log.e(Integrum.TAG, e.toString());
                } catch (InstantiationException e) {
                    Log.e(Integrum.TAG, e.toString());
                } catch (IllegalAccessException e) {
                    Log.e(Integrum.TAG, e.toString());
                } catch (InvocationTargetException e) {
                    Log.e(Integrum.TAG, e.toString());
                }
            }

            setCachedModels(models);

            return models;
        }

        return new HashMap<Integer, T>();
    }

    /**
     * Retrieves an instance of the cached model if it exists, null otherwise. Must be
     * overridden by subclass to work
     *
     * @param id of the model instance to return
     * @return the cachedModels
     */
    private static Model getCachedModel(int id) {
        return cachedModels.get(id);
    }

    /**
     * Retrieves the cached models if they exist, null otherwise. Must be
     * overridden by subclass to work
     *
     * @return the cachedModels
     */
    @SuppressWarnings("unchecked")
    protected static <T extends Model> HashMap<Integer,T> getCachedModels() {
        return (HashMap<Integer, T>) cachedModels;
    }

    /**
     * Sets the cached models. Must be overridden by the subclass to work
     *
     * @param models to cache
     */
    @SuppressWarnings("unchecked")
    protected static void setCachedModels(HashMap<Integer,?> models) {
        cachedModels = (HashMap<Integer,Model>) models;
    }
}
 