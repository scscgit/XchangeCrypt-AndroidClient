package bit.xchangecrypt.client.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import bit.xchangecrypt.client.R;

/**
 * Created by Peter on 21.04.2018.
 */
public class LoginFragment extends BaseFragment {
    private ImageView googleSignButton;
    //    private Button signButton;
//    private Button registerButton;
    private Button offlineButton;
//    private EditText emailEditText;
//    private EditText passwordEditText;

    public static LoginFragment newInstance(Bundle args) {
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        hideButtonLayout();
        setActionBar();
        setViews();
        setViewContents();
        return rootView;
    }

    @Override
    protected void setActionBar() {
        setToolbarTitle("");
        googleSignButton = rootView.findViewById(R.id.button_google_sign);
//        signButton = (Button) rootView.findViewById(R.id.button_sign_in);
//        registerButton =(Button) rootView.findViewById(R.id.button_register);
        offlineButton = (Button) rootView.findViewById(R.id.button_offline);
//        emailEditText = (EditText) rootView.findViewById(R.id.edit_text_email);
//        passwordEditText = (EditText) rootView.findViewById(R.id.edit_text_password);
    }

    @Override
    protected void setViews() {
        googleSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().onClickedActiveDirectorySignIn();
                //   switchToFragmentAndClear(FRAGMENT_EXCHANGE,null);
            }
        });
//
//        signButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switchToFragmentAndClear(FRAGMENT_EXCHANGE,null);
//            }
//        });
//
//        registerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switchToFragmentAndClear(FRAGMENT_EXCHANGE,null);
//            }
//        });

        offlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().getContentRefresher().setUser(null);
            }
        });
    }

    @Override
    protected void setViewContents() {
    }

    @Override
    public void refreshFragment() {
        // Ignored
    }
}
