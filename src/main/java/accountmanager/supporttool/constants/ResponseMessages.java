package accountmanager.supporttool.constants;

public class ResponseMessages {

    //---------------------- GENERAL ----------------------------------
    public static final String UNKNOWN_ERROR = "Server Error ... a problem occurred while we are processing your data";

    //---------------------- LOGIN ------------------------------------
    public static final String SUCCESSFUL_LOGIN = "Logged In Successfully";
    public static final String BAD_Credentials = "Username or password is wrong, please enter them again";


    //---------------------- SIGNUP ------------------------------------
    public static final String NO_USERNAME_PROVIDED = "Please provide a username to complete registration";
    public static final String USERNAME_EXISTS = "Username provided already exists, please choose a different username";
    public static final String NO_EMAIL_PROVIDED = "Please provide an email to complete registration";
    public static final String EMAIL_EXISTS = "Email provided already exists, please choose a different email";
    public static final String SUCCESSFUL_SIGNUP = "You Have Successfully Registered in MBBM";

    //---------------------- USER ------------------------------------
    public static final String USER_NOT_AVAILABLE = "The required user is not available";
    public static final String USER_INFO_RETURNED_SUCCESSFULLY = "user information returned successfully";

    //---------------------- PROFILE ------------------------------------
    public static final String PROFILE_AVATAR_UPDATED_SUCCESSFULLY = "profile avatar was updated successfully";
    public static final String PROFILE_AVATAR_UPDATING_FAILED = "profile avatar updating failed";

    public static final String PROFILE_ACTIVATED_SUCCESSFULLY = "profile was activated successfully";
    public static final String PROFILE_ACTIVATING_FAILED = "profile activating failed";

    public static final String PROFILE_BLOCKED_SUCCESSFULLY = "profile was blocked successfully";
    public static final String PROFILE_BLOCKING_FAILED = "profile blocking failed";

}
