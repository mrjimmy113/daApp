package com.quang.daapp.ui.expertSearch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Major;
import com.quang.daapp.data.model.ProblemRequest;
import com.quang.daapp.ui.dialog.LoaderDialogFragment;
import com.quang.daapp.ui.viewAdapter.ProblemRequestAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchRequestFragment extends Fragment {

    private SearchRequestViewModel viewModel;
    private List<Major> majorList;
    private RecyclerView recyclerView;
    private ProblemRequestAdapter adapter;
    private List<ProblemRequest> problemRequestList = new ArrayList<>();
    private NavController navController;
    private Spinner spnMajor ;
    private Spinner spnCity ;
    private Spinner spnLanguage ;
    private Spinner spnTime ;
    private TextView txtEmpty;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(SearchRequestViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search_request, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spnMajor = view.findViewById(R.id.spn_major);
        spnCity = view.findViewById(R.id.spn_city);
        spnLanguage = view.findViewById(R.id.spn_language);
        spnTime = view.findViewById(R.id.spn_time);
        final ImageButton btnOpen = view.findViewById(R.id.btnOpenSearch);
        final ImageButton btnClose = view.findViewById(R.id.btnCloseSearch);
        final LinearLayout layoutSearchTool = view.findViewById(R.id.layout_search_tool);
        recyclerView = view.findViewById(R.id.recyc_problem_request);
        navController = Navigation.findNavController(view);
        txtEmpty = view.findViewById(R.id.txtEmpty);

        adapter = new ProblemRequestAdapter(getView().getContext(), problemRequestList, new ProblemRequestAdapter.ProblemRequestAdapterEvent() {
            @Override
            public void OnMainLayoutClick(int id) {
                Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.key_request_id),id);
                bundle.putBoolean(getString(R.string.isExpert),true);
                navController.navigate(R.id.action_navigation_dashboard_to_problemRequestDetailFragment,bundle);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        List<String> cityList = new ArrayList<>();
        cityList.add(getString(R.string.adapter_all));
        cityList.addAll(Arrays.asList(getResources().getStringArray(R.array.city_arrays)));
        ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, cityList);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCity.setAdapter(adapterCity);

        List<String> lanList = new ArrayList<>();
        lanList.add(getString(R.string.adapter_all));
        lanList.addAll(Arrays.asList(getResources().getStringArray(R.array.language_arrays)));
        ArrayAdapter<String> adapterLan = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, lanList);
        adapterLan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLanguage.setAdapter(adapterLan);

        ArrayAdapter<String> adapterTime = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.time_arrays));
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTime.setAdapter(adapterTime);

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClose.setVisibility(View.VISIBLE);
                btnOpen.setVisibility(View.GONE);
                layoutSearchTool.setVisibility(View.VISIBLE);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOpen.setVisibility(View.VISIBLE);
                btnClose.setVisibility(View.GONE);
                layoutSearchTool.setVisibility(View.GONE);
            }
        });

        view.findViewById(R.id.btnSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
                btnOpen.setVisibility(View.VISIBLE);
                btnClose.setVisibility(View.GONE);
                layoutSearchTool.setVisibility(View.GONE);
            }
        });


        viewModel.getAllMajor();
        viewModel.getAllMajorResult().observe(getViewLifecycleOwner(), new Observer<List<Major>>() {
            @Override
            public void onChanged(List<Major> majors) {
                if(majors == null) return;

                majorList = majors;
                ArrayAdapter<Major> adapterMajor = new ArrayAdapter<Major>(getContext(),android.R.layout.simple_spinner_item, majorList);
                adapterMajor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnMajor.setAdapter(adapterMajor);
                search();

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void search() {
        final LoaderDialogFragment loaderDialog = new LoaderDialogFragment();
        loaderDialog.show(getParentFragmentManager(),getTag());
        viewModel.expertSearch(((Major) spnMajor.getSelectedItem()).getId(),
                spnCity.getSelectedItem().toString().equals(getString(R.string.adapter_all)) ? null : spnCity.getSelectedItem().toString(),
                spnLanguage.getSelectedItem().toString().equals(getString(R.string.adapter_all)) ? null : spnLanguage.getSelectedItem().toString(),
                spnTime.getSelectedItemPosition());

        viewModel.getExpertSearchResult().observe(getViewLifecycleOwner(), new Observer<List<ProblemRequest>>() {
            @Override
            public void onChanged(List<ProblemRequest> problemRequests) {
                if(problemRequests == null) return;
                if(problemRequests.isEmpty()) txtEmpty.setVisibility(View.VISIBLE);
                else txtEmpty.setVisibility(View.GONE);
                problemRequestList = problemRequests;
                adapter.setRequestList(problemRequestList);
                adapter.notifyDataSetChanged();
                loaderDialog.dismiss();

            }
        });
    }

}


