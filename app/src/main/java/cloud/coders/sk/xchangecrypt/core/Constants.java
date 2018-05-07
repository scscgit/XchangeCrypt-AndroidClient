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

//    "AzureAdB2C": {
//        "ClientId": "aeb4f22f-00af-4b54-bf7b-5652684a2f03",
//                "Tenant": "XchangeCryptTest.onmicrosoft.com",
//                "SignUpSignInPolicyId": "b2c_1_signupsignin",
//                //"ResetPasswordPolicyId": "b2c_1_reset",
//                //"EditProfilePolicyId": "b2c_1_edit_profile",
//                "RedirectUri": "https://xchangecrypttest-convergencebackend.azurewebsites.net/login",
//                "ClientSecret": "W\"sFO3083dUa2$\"JH-zRh22!",
//                "ApiUrl": "https://xchangecrypttest-convergencebackend.azurewebsites.net/api",
//                "ApiScopes": "https://XchangeCryptTest.onmicrosoft.com/testapi/user_impersonation"
//    }
//
//    /* Azure AD b2c Configs */
//    final static String AUTHORITY = "https://login.microsoftonline.com/tfp/%s/%s";
//    final static String TENANT = "fabrikamb2c.onmicrosoft.com";
//    final static String CLIENT_ID = "90c0fe63-bcf2-44d5-8fb7-b8bbc0b29dc6";
//    final static String SCOPES = "https://fabrikamb2c.onmicrosoft.com/demoapi/demo.read";
//    final static String API_URL = "https://fabrikamb2chello.azurewebsites.net/hello";


    final static String AUTHORITY = "https://login.microsoftonline.com/tfp/%s/%s";
    final static String TENANT = "XchangeCryptTest.onmicrosoft.com";
    final static String CLIENT_ID = "aeb4f22f-00af-4b54-bf7b-5652684a2f03";
    final static String SCOPES = "openid https://XchangeCryptTest.onmicrosoft.com/testapi/user_impersonation";
    final static String API_URL = "https://xchangecrypttest-convergencebackend.azurewebsites.net/api";

    final static String SISU_POLICY = "b2c_1_signupsignin";
    final static String EDIT_PROFILE_POLICY = "b2c_1_edit_profile";


}
