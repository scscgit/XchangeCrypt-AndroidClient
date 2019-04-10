package bit.xchangecrypt.client.core;

/**
 * Created by V3502484 on 16. 9. 2016.
 */
public interface Constants {
    // Info Dialog
    String INFODIALOG_TITLE = "title_infodialog";
    String INFODIALOG_MESSAGE = "message_infodialog";
    String INFODIALOG_BUTTON_TEXT = "button_text_infodialog";

    // Azure AD B2C Configs (Mock, doesn't work)
//    String AUTHORITY = "https://login.microsoftonline.com/tfp/%s/%s";
//    String TENANT = "fabrikamb2c.onmicrosoft.com";
//    String CLIENT_ID = "90c0fe63-bcf2-44d5-8fb7-b8bbc0b29dc6";
//    String SCOPES = "https://fabrikamb2c.onmicrosoft.com/demoapi/demo.read";
//    String API_URL = "https://fabrikamb2chello.azurewebsites.net/hello";
//    String SISU_POLICY = "B2C_1_SUSI";
//    String EDIT_PROFILE_POLICY = "B2C_1_edit_profile";

    // Azure AD B2C authority details
    String AUTHORITY = "https://XchangeCryptTest.b2clogin.com/tfp/%s/%s";
    String TENANT = "XchangeCryptTest.onmicrosoft.com";
    String SISU_POLICY = "B2C_1_signupsignin";
//    String EDIT_PROFILE_POLICY = "b2c_1_edit_profile";

    // Azure AD B2C app configuration
    String CLIENT_ID = "aeb4f22f-00af-4b54-bf7b-5652684a2f03";
    String SCOPES = "openid https://XchangeCryptTest.onmicrosoft.com/testapi/user_impersonation";

    String API_URL = "userinfo API is not supported";

    // Azure AD Free authority details
//    String AUTHORITY = "https://login.microsoftonline.com/XchangeCryptTestAD.onmicrosoft.com";
//    String TENANT = "XchangeCryptTestAD.onmicrosoft.com";
//    String SISU_POLICY = "TODO";

    // Azure AD Free app configuration
//    String CLIENT_ID = "08bba3c7-46db-4d84-99c9-af5c00d617e5";
//    String SCOPES = "User.Read";

//    String API_URL = "https://graph.microsoft.com/oidc/userinfo";
}
