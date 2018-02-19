package com.example.izzy.themoviedb_iak.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.izzy.themoviedb_iak.R;
import com.example.izzy.themoviedb_iak.fragments.GridFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.search_button)
    ImageButton searchButton;

    private static final String SEARCH_URL= "https://api.themoviedb.org/3/search/movie?api_key="+MainActivity.API_KEY+"&query=";
    public static final String KEY = "KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);


        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    GridFragment gridFragment = new GridFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(KEY,SEARCH_URL+search.getText());
                    gridFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.search_container, gridFragment).commit();
                    search.requestFocus();
                    return true;
                }
                return false;
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GridFragment gridFragment = new GridFragment();
                Bundle bundle = new Bundle();
                bundle.putString(KEY,SEARCH_URL+search.getText());
                gridFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.search_container, gridFragment).commit();
            }
        });

    }

}
