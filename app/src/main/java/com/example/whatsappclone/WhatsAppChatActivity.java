package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppChatActivity extends AppCompatActivity implements View.OnClickListener {

    private String selectedUser;
    private ListView chatListView;
    private ArrayList<String> chatsList;
    private ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_chat);

        selectedUser = getIntent().getStringExtra("selectedUser");
        FancyToast.makeText(this, "chat with " + selectedUser + " now!",
                FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

        findViewById(R.id.btnSend).setOnClickListener(this);
        chatListView = findViewById(R.id.chatListView);
        chatsList = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,chatsList);
        chatListView.setAdapter(adapter);

        try {

            ParseQuery<ParseObject> firstUserChatQuery = ParseQuery.getQuery("Chat");
            ParseQuery<ParseObject> secondUserChatQuery = ParseQuery.getQuery("Chat");

            // check where the sender is the current user
            firstUserChatQuery.whereEqualTo("wSender", ParseUser.getCurrentUser().getUsername());
            // and check where the recipient is the selected user
            firstUserChatQuery.whereEqualTo("wTargetRecipient", selectedUser);

            // check where the sender is the selected user
            secondUserChatQuery.whereNotEqualTo("wSender", selectedUser);
            // and check where the recipient is the current user
            secondUserChatQuery.whereEqualTo("wRecipient", ParseUser.getCurrentUser().getUsername());

            ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
            allQueries.add(firstUserChatQuery);
            allQueries.add(secondUserChatQuery);

            ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);
            // myQuery contains all the previous queries
            myQuery.orderByAscending("createdAt");
            myQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseObject chatObject : objects) {

                            String wMessage = chatObject.get("wMessage") + "";

                        /* we use the 2 if statements instead of if-else because we want to execute
                        the 2 statement to show all the messages */
                            if (chatObject.get("wSender").equals(ParseUser.getCurrentUser().getUsername())) {
                                wMessage = ParseUser.getCurrentUser().getUsername() + ": " + wMessage;
                            }
                            if (chatObject.get("wSender").equals(selectedUser)) {
                                wMessage = selectedUser + ": " + wMessage;
                            }
                            chatsList.add(wMessage);
                        }
                    /* we don't put the notifyDataSetChanged inside a for loop because
                    It's not a good programming practice */
                        adapter.notifyDataSetChanged();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        /* when we click on this button an object of type editText is created and then
        when the program reach to end of this method this local variable(editText) will
         die and it won't occupy the memory so it's more memory friendly*/

        /**send a message */
        final EditText edtMessage = findViewById(R.id.edtMessage);

        ParseObject chat =  new ParseObject("Chat");
        chat.put("wSender", ParseUser.getCurrentUser().getUsername());
        chat.put("wTargetRecipient", selectedUser );
        chat.put("wMessage", edtMessage.getText().toString());
        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    if (edtMessage.getText().toString().equals("")) {

                        FancyToast.makeText(WhatsAppChatActivity.this, "Nothing to sent!",
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    } else {
                        FancyToast.makeText(WhatsAppChatActivity.this, "Message from " +
                                        ParseUser.getCurrentUser().getUsername() + " sent to " + selectedUser,
                                FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        chatsList.add(ParseUser.getCurrentUser().getUsername() + ": " +
                                edtMessage.getText().toString());
                        //update the list view
                        adapter.notifyDataSetChanged();
                        edtMessage.setText("");
                    }
                }
            }
        });
    }
}