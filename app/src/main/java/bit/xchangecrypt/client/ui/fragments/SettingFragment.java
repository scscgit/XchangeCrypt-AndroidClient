package bit.xchangecrypt.client.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import bit.xchangecrypt.client.R;

/**
 * Created by Peter on 21.04.2018.
 */
public class SettingFragment extends BaseFragment {
    private TextView nameTextView;
    private TextView surnameTextView;
    private TextView emailTextView;
    private ToggleButton notificationTogleButton;
    private Button signOutButton;
    private Button passwordChangeButton;

    public static SettingFragment newInstance(Bundle args) {
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        setActionBar();
        setViews();
        setViewContents();
        return rootView;
    }

    @Override
    protected void setActionBar() {
        setToolbarTitle("Nastavenia");
    }

    @Override
    protected void setViews() {
        nameTextView = rootView.findViewById(R.id.name_text);
        surnameTextView = rootView.findViewById(R.id.surname_text);
        emailTextView = rootView.findViewById(R.id.email_text);
        notificationTogleButton = rootView.findViewById(R.id.notification_toogle);
        signOutButton = rootView.findViewById(R.id.button_sign_out);
        passwordChangeButton = rootView.findViewById(R.id.button_password_change);
    }

    @Override
    protected void setViewContents() {
        notificationTogleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                //TODO zapinanie notifikacii
            }
        });
        String name[] = getContentProvider().getUser().getRealName().split(" ", 1);
        nameTextView.setText(name[0]);
        surnameTextView.setText(name.length > 1 ? name[1] : "");
        emailTextView.setText(getContentProvider().getUser().getEmailAddress());
        notificationTogleButton.setChecked(false);
        passwordChangeButton.setEnabled(false);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().activeDirectorySignOutClearCache();
            }
        });
    }
}
