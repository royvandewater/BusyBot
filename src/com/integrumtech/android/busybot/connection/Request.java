package com.integrumtech.android.busybot.connection;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Set;

/**
 * Request class for use with the Connection class
 * @author Roy van de Water
 *
 */
public class Request {
    
    private static final long serialVersionUID = -8839295913516286080L;
    private String path;
    private Method method;
    private HashMap<String, String> data;
    private Connection connection;

    /**
     * Creates a GET request for the url
     * 
     * @param path to connect to
     */
    public Request(String path) {
        this(path, Method.GET);
    }
    
    /**
     * Creates a request of the given method
     * 
     * @param path to connect to
     * @param method one of GET, POST, PUT or DELETE
     */
    public Request(String path, Method method) {
        super();
        
        this.path = path;
        this.method = method;
        this.data = new HashMap<String,String>();
        this.connection = Connection.build(this);
    }
    
    /**
     * @return the url
     */
    public String getPath()
    {
        return path;
    }
    
    /**
     * @param path the url to set
     */
    public void setPath(String path)
    {
        this.path = path;
    }
    
    /**
     * @return the method
     */
    public Method getMethod()
    {
        return method;
    }
    
    /**
     * @param method the method to set
     */
    public void setMethod(Method method)
    {
        this.method = method;
    }
    
    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }
    
    /**
     * Enumerator describing the Http Connection method
     * 
     * @author Roy van de Water
     *
     */
    public static enum Method {
        
        GET, POST, PUT, DELETE, NOVALUE;

        public static Method toMethod(String str)
        {
            try {
                return valueOf(str.toUpperCase());
            } catch (Exception e) {
                return NOVALUE;
            }
        }
        
    }


    /**
     * Used to determine if this can be cast to HttpEntityEnclosingRequestBase
     * 
     * @return true if the method is POST or PUT
     */
    public boolean canEncloseData()
    {
        return (method == Method.POST) || (method == Method.PUT);
    }
    
    /**
     * Will determine if there is any data to enclose in the HttpRequest
     * 
     * @return true if there is enclosing data present
     */
    public boolean hasData()
    {
        return !data.isEmpty();
    }


    /**
     * Appends string to the path
     * @param string to append to the path
     */
    public void appendToPath(String string)
    {
        path += string;
    }

    public void put(String key, String value)
    {
        data.put(key, value);
    }

    public Set<String> keySet()
    {
        return data.keySet();
    }

    public String get(String key)
    {
        return data.get(key);
    }
    
    /**
     * Executes the request
     * 
     * @return the server response
     * @throws UnsupportedEncodingException Thrown if one of the key/value pairs in the request are improper
     */
    public Response execute() throws UnsupportedEncodingException {
    	return connection.execute();
    }
}