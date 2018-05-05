package cloud.coders.sk.xchangecrypt.core;

/**
 * Created by V3502484 on 16. 9. 2016.
 */
public interface Constants {

    /* Info Dialog */
    String INFODIALOG_TITLE = "title_infodialog";
    String INFODIALOG_MESSAGE = "message_infodialog";
    String INFODIALOG_BUTTON_TEXT = "button_text_infodialog";

    int LEAGUE_TABLE_SIZE = 11;

    /* Azure AD b2c Configs */
    final static String AUTHORITY = "https://login.microsoftonline.com/tfp/%s/%s";
    final static String TENANT = "fabrikamb2c.onmicrosoft.com";
    final static String CLIENT_ID = "90c0fe63-bcf2-44d5-8fb7-b8bbc0b29dc6";
    final static String SCOPES = "https://fabrikamb2c.onmicrosoft.com/demoapi/demo.read";
    final static String API_URL = "https://fabrikamb2chello.azurewebsites.net/hello";

    final static String SISU_POLICY = "B2C_1_SUSI";
    final static String EDIT_PROFILE_POLICY = "B2C_1_edit_profile";


}
