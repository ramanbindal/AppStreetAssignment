/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.presentation.ui.firstFragment;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.domain.model.ImageData;
import com.example.presentation.MainApplication;
import com.example.presentation.R;
import com.example.presentation.base.BaseFragment;
import com.example.presentation.ui.MainActivity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * A fragment for displaying a grid of images.
 */
public class GridFragment extends BaseFragment<GridFragmentViewModel> implements GridFragmentNavigator {


    @Inject
    @Named("GridFragment")
    ViewModelProvider.Factory viewModelFactory;

    GridFragmentViewModel gridFragmentViewModel;

    private RecyclerView recyclerView;
    boolean isLoading = false;
    FrameLayout progressBarlayout;
    GridAdapter adapter;

    String tag = "";
    List<ImageData> imageDataList = new ArrayList<>();
    int screenWidth = 600;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainApplication) getActivity().getApplicationContext()).getComponent().inject(this);
        gridFragmentViewModel = ViewModelProviders.of(this, viewModelFactory).get(GridFragmentViewModel.class);
        gridFragmentViewModel.setNavigator(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grid, container, false);


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), MainActivity.getColumnCount()));
        adapter = new GridAdapter(this, imageDataList, screenWidth, MainActivity.getColumnCount());
        recyclerView.setAdapter(adapter);


        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                screenWidth = view.getWidth();
                adapter.setScreenWidth(screenWidth);
            }
        });

        progressBarlayout = view.findViewById(R.id.progress_bar_layout);
        progressBarlayout.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Image Search");
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);


        prepareTransitions();
        postponeEnterTransition();
        initScrollListener();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.menuSearch);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setQueryHint("Search Images");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.trim().isEmpty()) {
                    imageDataList.clear();
                    tag = query;
                    progressBarlayout.setVisibility(View.VISIBLE);
                    gridFragmentViewModel.fetchPhotos(query, 1);
                } else {
                    Toast.makeText(getContext(), "Please Enter text to Search", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuTwo:
                MainActivity.setColumnCount(2);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                adapter.setColumnCount(2);
                adapter.notifyDataSetChanged();
                break;

            case R.id.menuThree:
                MainActivity.setColumnCount(3);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                adapter.setColumnCount(3);
                adapter.notifyDataSetChanged();
                break;

            case R.id.menuFour:
                MainActivity.setColumnCount(4);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
                adapter.setColumnCount(4);
                adapter.notifyDataSetChanged();
                break;

        }
        return true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scrollToPosition();
    }

    /**
     * Scrolls the recycler view to show the last viewed item in the grid. This is important when
     * navigating back from the grid.
     */
    private void scrollToPosition() {
        recyclerView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                recyclerView.removeOnLayoutChangeListener(this);
                final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                View viewAtPosition = layoutManager.findViewByPosition(MainActivity.currentPosition);
                // Scroll to position if the view for the current position is null (not currently part of
                // layout manager children), or it's not completely visible.
                if (viewAtPosition == null || layoutManager.isViewPartiallyVisible(viewAtPosition, false, true)) {
                    recyclerView.post(() -> layoutManager.scrollToPosition(MainActivity.currentPosition));
                }
            }
        });
    }

    private void initScrollListener() {
        final int[] currentPage = {1};
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

//                if (dy > 0) { // only when scrolling up

                final int visibleThreshold = 2;

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                int lastItem = layoutManager.findLastCompletelyVisibleItemPosition();
                int currentTotalCount = layoutManager.getItemCount();

                if (currentTotalCount <= lastItem + visibleThreshold && isLoading == false) {

                    isLoading = true;
                    currentPage[0]++;
                    progressBarlayout.setVisibility(View.VISIBLE);
                    gridFragmentViewModel.fetchPhotos(tag, currentPage[0]);

                }
//                }

//                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//
//                if (!isLoading) {
//                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size() - 1) {
//                        //bottom of list!
//                        loadMore();
//                        isLoading = true;
//                    }
//                }
            }
        });


    }


    /**
     * Prepares the shared element transition to the pager fragment, as well as the other transitions
     * that affect the flow.
     */
    private void prepareTransitions() {
        setExitTransition(TransitionInflater.from(getContext())
                .inflateTransition(R.transition.grid_exit_transition));

        // A similar mapping is set at the ImagePagerFragment with a setEnterSharedElementCallback.
        setExitSharedElementCallback(
                new SharedElementCallback() {
                    @Override
                    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                        // Locate the ViewHolder for the clicked position.
                        RecyclerView.ViewHolder selectedViewHolder = recyclerView
                                .findViewHolderForAdapterPosition(MainActivity.currentPosition);
                        if (selectedViewHolder == null || selectedViewHolder.itemView == null) {
                            return;
                        }

                        // Map the first shared element name to the child ImageView.
                        sharedElements
                                .put(names.get(0), selectedViewHolder.itemView.findViewById(R.id.card_image));
                    }
                });
    }

    @Override
    public GridFragmentViewModel getViewModel() {
        return gridFragmentViewModel;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onListLoaded(List<ImageData> imageData) {

        isLoading = false;
        List<ImageData> temp = new ArrayList<>();
        for (ImageData imageData1 : imageData) {
            if (imageData1.getImageBas64() != null) {
                temp.add(imageData1);
            }
        }
        temp.sort(new Comparator<ImageData>() {
            @Override
            public int compare(ImageData o1, ImageData o2) {
                return o1.getImageId().compareTo(o2.getImageId());
            }
        });
        imageDataList.addAll(temp);
        MainActivity.setImageDataList(imageDataList);
        progressBarlayout.setVisibility(View.GONE);
        adapter.setImageDataList(imageDataList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void noData() {
        progressBarlayout.setVisibility(View.GONE);
        Toast.makeText(getContext(), "Not able to fetch data from Api and no data in db for this keyword", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dataLoadedFromDb(List<ImageData> imageData) {
        Toast.makeText(getContext(), "Data Loaded from Db", Toast.LENGTH_SHORT).show();

        isLoading = true;

        List<ImageData> temp = new ArrayList<>();
        for (ImageData imageData1 : imageData) {
            if (imageData1.getImageBas64() != null) {
                temp.add(imageData1);
            }
        }
        imageDataList.addAll(temp);
        MainActivity.setImageDataList(imageDataList);
        progressBarlayout.setVisibility(View.GONE);
        adapter.setImageDataList(imageDataList);
        adapter.notifyDataSetChanged();
    }
}
