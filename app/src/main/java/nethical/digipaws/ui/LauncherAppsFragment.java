package nethical.digipaws.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import nethical.digipaws.R;
import nethical.digipaws.utils.FragmentChangeListener;
import nethical.digipaws.utils.KeyboardUtils;
import nethical.digipaws.utils.ListApps;
import nethical.digipaws.utils.adapters.AppAdapter;

import java.util.ArrayList;
import java.util.List;

public class LauncherAppsFragment extends Fragment implements FragmentChangeListener {

    private RecyclerView appsRecyclerView;
    private EditText searchApp;
    private AppAdapter appAdapter;
    private List<ListApps.AppModel> allAppsList;

    public LauncherAppsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_launcher_apps, container, false);

        searchApp = rootView.findViewById(R.id.searchApp);
        appsRecyclerView = rootView.findViewById(R.id.appListView);

        resetStatusBarColor();
        return rootView;
    }

    private void resetStatusBarColor() {
        // Get the hosting activity and reset status bar color as well as the navigation bar color
        Window window = requireActivity().getWindow();

        // Set the background color of the window
        window.setBackgroundDrawableResource(
                R.color.app_background_color); // Clear existing background (optional)
        window.setStatusBarColor(R.color.app_background_color); // Set status bar color (optional)
    }

    @Override
    public void onViewCreated(View arg0, Bundle arg1) {
        super.onViewCreated(arg0, arg1);

        initializeAppList();
        setupSearchListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        resetStatusBarColor();
        KeyboardUtils.showKeyboard(requireContext(), searchApp);
    }

    @Override
    public void onFragmentChange(Fragment fragment) {
        if (getActivity() != null) {
            getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.fade_enter, R.anim.fade_exit) // Use 0 for no exit animation
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void initializeAppList() {

        ListApps.getUserAccessibleApps(
                requireContext(),
                appList -> {
                    allAppsList = appList;

                    CustomLinearLayoutManager layoutManager =
                            new CustomLinearLayoutManager(requireContext());
                    appsRecyclerView.setLayoutManager(layoutManager);
                    layoutManager.setRecyclerView(
                            appsRecyclerView); // Assign the RecyclerView to the layout manager

                    appAdapter = new AppAdapter(requireContext(), appList, this);
                    appsRecyclerView.setAdapter(appAdapter);
                });

        appsRecyclerView.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    boolean onTop = false;

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

                        switch (newState) {
                            case RecyclerView.SCROLL_STATE_DRAGGING:
                                onTop = !recyclerView.canScrollVertically(-1);
                                if (onTop) {
                                    // Assuming 'binding' is your view binding object
                                    // and 'search' is the view you want to manipulate
                                    KeyboardUtils.hideKeyboard(
                                            requireContext(), searchApp); // 'this' is the context
                                }
                                break;

                            case RecyclerView.SCROLL_STATE_IDLE:
                                if (!recyclerView.canScrollVertically(1)) {
                                    KeyboardUtils.hideKeyboard(
                                            requireContext(), searchApp); // 'this' is the context

                                } else if (!recyclerView.canScrollVertically(-1)) {
                                    if (!onTop) {
                                        // Assuming 'binding' is your view binding object
                                        // and 'search' is the view you want to manipulate
                                        KeyboardUtils.showKeyboard(
                                                requireContext(),
                                                searchApp); // 'this' is the context
                                    }
                                }
                                break;
                        }
                    }
                });
    }

    private void setupSearchListener() {
        searchApp.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        appsRecyclerView.setVisibility(View.VISIBLE);
                    }
                });

        searchApp.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String searchText = s.toString().toLowerCase();
                        filterApps(searchText);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });
    }

    private void filterApps(String searchText) {
        List<ListApps.AppModel> filteredList = new ArrayList<>();
        for (ListApps.AppModel appInfo : allAppsList) {
            if (appInfo.getAppLabel().toLowerCase().contains(searchText)) {
                filteredList.add(appInfo);
            }
        }
        appAdapter.filterList(filteredList);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Show keyboard when fragment resumes (if needed)
        KeyboardUtils.showKeyboard(requireContext(), searchApp);
    }

    // Optional method to hide keyboard

    public void toggleAppListVisibility() {

        LauncherHomeFragment launcherHomeFragment = new LauncherHomeFragment();

        // Replace current fragment with LauncherAppsFragment
        getParentFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.fade_enter, R.anim.fade_exit) // Use 0 for no exit animation
                .replace(R.id.fragment_container, launcherHomeFragment)
                .addToBackStack(null)
                .commit();
    }

    public class CustomLinearLayoutManager extends LinearLayoutManager {

        private RecyclerView recyclerView;

        public CustomLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public int scrollVerticallyBy(
                int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
            int scrollRange = super.scrollVerticallyBy(dy, recycler, state);
            int overScroll = dy - scrollRange;

            if (overScroll < -10
                    && recyclerView != null
                    && recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING) {
                // move back to home activity when recycler is overscrolled towards the bottom
                // direction
                toggleAppListVisibility();
            }
            return scrollRange;
        }

        public void setRecyclerView(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }
    }
}
