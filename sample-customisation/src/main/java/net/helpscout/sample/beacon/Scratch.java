package net.helpscout.sample.beacon;

import android.content.Context;

import com.helpscout.beacon.Beacon;
import com.helpscout.beacon.internal.core.model.BeaconContactForm;
import com.helpscout.beacon.model.BeaconConfigOverrides;
import com.helpscout.beacon.model.BeaconScreens;
import com.helpscout.beacon.model.PreFilledForm;
import com.helpscout.beacon.ui.BeaconActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.helpscout.sample.beacon.customisation.R;

import androidx.annotation.NonNull;

public class Scratch {
    private static final String BEACON_ID = "cf2102b5-0f3c-4214-972e-2a1d33c7fadb";
    private static final String ATTRIBUTE_KEY_USER_ID = "User ID";
    private static final String EMPTY = "";

    private Context context;
    private User currentUser = new User();

    public Scratch(Context context) {
        this.context = context;
    }

    public static class User {
        List<String> getEmails() {
            return Collections.emptyList();
        }

        String getId() {
            return "UserID123";
        }

    }

    public void setupAndLaunchBeacon() {
        new Beacon.Builder()
                .withBeaconId(BEACON_ID)
                .withLogsEnabled(true)
                .build();

        String currentEmail = EMPTY;
        if (currentUser == null || currentUser.getEmails().isEmpty()) {
            // If user has no email, then they can provide one themselves each time they send
            // feedback, so logout just in case they change emails; don't want them to accidentally
            // send feedback with the previous email.
            Beacon.logout();
        } else {
            // Can only log in manually if the user has an email to provide upon opening the activity.
            // If they don't we can't log them in automatically.
            currentEmail = currentUser.getEmails().get(0);
            Beacon.login(currentEmail);
        }

        // Initialize the "Contact Form" which has a Subject Line, a Logs field, and a message
        // field. TODO: re-enable custom fields when we can hide them for logs.
        BeaconContactForm contactForm = new BeaconContactForm(false, true,
                                                              true, false, false
        );

        if (currentUser != null) {
            setAttributes(currentUser);
        }

        // Configure with the contact form and our desired color.
        // The color expects a Hex String, hence converting to HexString.
        Beacon.setConfigOverrides(new BeaconConfigOverrides(false, true, false, contactForm,
                                                            "#" + Integer.toHexString(context.getResources().getColor(R.color.primary))
        ));

        // Pre-fill the form with the user's email (which isn't shown if they already have one pre-populated)
        Beacon.addPreFilledForm(new PreFilledForm(currentEmail, EMPTY, EMPTY, new HashMap<Integer, String>(), new ArrayList<String>()));

        BeaconActivity.open(context, BeaconScreens.CONTACT_FORM_SCREEN, new ArrayList<String>());
    }

    // Get the users specific attributes and add them to Beacon
    private void setAttributes(@NonNull final User user) {
        // ...
        Beacon.addAttributeWithKey(ATTRIBUTE_KEY_USER_ID, user.getId());
    }
}
