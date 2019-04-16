package com.example.presentation.ui.main;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.domain.model.ImageData;
import com.example.presentation.MainApplication;
import com.example.presentation.R;
import com.example.presentation.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseActivity<MainViewModel> implements MainNavigator {

    private TextView textView;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    MainViewModel mainViewModel;

    String tag = "";
    GridView gridView;
    FrameLayout progressBarlayout;
    List<ImageData> imageDataList = new ArrayList<>();
    GridLayoutAdapter adapter;

    @Override
    public MainViewModel getViewModel() {
        return mainViewModel;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((MainApplication) getApplicationContext()).getComponent().inject(this);

        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
        mainViewModel.setNavigator(this);

        gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setOnScrollListener(new EndlessScrollListener());
        adapter = new GridLayoutAdapter(imageDataList, this);
        gridView.setAdapter(adapter);

        progressBarlayout = findViewById(R.id.progress_bar_layout);
        progressBarlayout.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Image Search");
        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.menuSearch);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setQueryHint("Search Images");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.trim().isEmpty()) {
                    imageDataList.clear();
                    tag = query;
                    progressBarlayout.setVisibility(View.VISIBLE);
                    mainViewModel.fetchPhotos(query, 1);
                } else {
                    Toast.makeText(MainActivity.this, "Please Enter text to Search", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuTwo:
                gridView.setNumColumns(2);
                break;

            case R.id.menuThree:
                gridView.setNumColumns(3);
                break;

            case R.id.menuFour:
                gridView.setNumColumns(4);
                break;

        }
        return true;
    }


    @Override
    public void onListLoaded(List<ImageData> imageData) {
        for (ImageData imageData1 : imageData) {
            if (imageData1.getImageBas64() != null) {
                imageDataList.add(imageData1);
            }
        }
        progressBarlayout.setVisibility(View.GONE);
        int index = gridView.getLastVisiblePosition();
        adapter.setImageDataList(imageDataList);
        adapter.notifyDataSetChanged();
        gridView.invalidate();

    }

    @Override
    public void noData() {
        progressBarlayout.setVisibility(View.GONE);
        Toast.makeText(this, "Not able to fetch data from Api and no data in db for this keyword", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dataLoadedFromDb() {
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    public class EndlessScrollListener implements AbsListView.OnScrollListener {

        private int visibleThreshold = 10;
        private int currentPage = 1;
        private int previousTotal = 0;
        private boolean loading = true;

        public EndlessScrollListener() {
        }

        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
//         

        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            int threshold = 1;
            int count = gridView.getCount();

            if (scrollState == SCROLL_STATE_IDLE) {
                if (gridView.getLastVisiblePosition() >= count - threshold ) {
                    // Execute LoadMoreDataTask AsyncTask
                    currentPage++;
                    progressBarlayout.setVisibility(View.VISIBLE);
                    mainViewModel.fetchPhotos(tag, currentPage);

                }
            }
        }
    }
}
