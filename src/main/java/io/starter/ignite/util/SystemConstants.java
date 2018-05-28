package io.starter.ignite.util;

public interface SystemConstants {

	public static String DB_NAME = (System.getProperty("RDS_DB_NAME") != null ? System.getProperty("RDS_DB_NAME")
			: "NONE");
	public static String DB_URL = (System.getProperty("RDS_HOSTNAME") != null ? System.getProperty("RDS_HOSTNAME")
			: "NONE");
	public static String DB_USER = (System.getProperty("RDS_USERNAME") != null ? System.getProperty("RDS_USERNAME")
			: "NONE");
	public static String DB_PASSWORD = (System.getProperty("RDS_PASSWORD") != null ? System.getProperty("RDS_PASSWORD")
			: "NONE");
	public static String JNDI_DB_LOOKUP_STRING = null;

	// aws
	public static String S3_STARTER_MEDIA_BUCKET = null;
	public static String S3_STARTER_SYSTEM_BUCKET = "";
	public static String S3_STARTER_SERVICE = "";
	public static String AWS_ACCESS_KEY = null;
	public static String AWS_SECRET_KEY = null;

	// encryption
	public static String SECURE_KEY_PROPERTY = "io.starter.ignite.secure_key";
	public static String SECRET_KEY = System.getProperty(SECURE_KEY_PROPERTY);
	public static String CIPHER_NAME = "AES/CBC/PKCS5PADDING";
	public static String S3_STARTER_MEDIA_FOLDER = null;
	public static Integer ANON_USERID = null;
	public final static int KEY_SIZE = 256;

	public static final String SESSION_VAR_USER = null;
	public static final String SESSION_VAR_SQLSESSION = null;

}
