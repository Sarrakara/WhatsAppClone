package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppUsersActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayList<String> wUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_users);
        setTitle("WhatsApp Users");

        final ListView listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        wUsers = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                wUsers);

        final SwipeRefreshLayout mySwipeRefreshLayout = findViewById(R.id.swipeContainer);

        // we use try-catch in the case if there is only the current user that's signed up
        try {
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if(objects.size() > 0 && e == null ){

                        for (ParseUser user : objects){
                            wUsers.add(user.getUsername());
                        }
                        listView.setAdapter(adapter);
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // sometimes we don't have any users
                try {
                    ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                    parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    parseQuery.whereNotContainedIn("username", wUsers);
                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if(objects.size() > 0 ){
                                if( e == null){
                                    for (ParseUser user : objects){
                                        wUsers.add(user.getUsername());
                                    }
                                    // update the adapter
                                    adapter.notifyDataSetChanged();
                                    // stop showing the refreshing process after updating the view
                                    if(mySwipeRefreshLayout.isRefreshing()){
                                        mySwipeRefreshLayout.setRefreshing(false);
                                    }
                                }
                            }else{ // if there is no new users that are signed up
                                if(mySwipeRefreshLayout.isRefreshing()){
                                    mySwipeRefreshLayout.setRefreshing(false);
                                }

                            }
                        }
                    });


                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logoutItem){
            ParseUser.getCurrentUser().logOut();
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(WhatsAppUsersActivity.this, WhatsAppChatActivity.class);
        intent.putExtra("selectedUser", wUsers.get(i));
        startActivity(intent);
    }
}