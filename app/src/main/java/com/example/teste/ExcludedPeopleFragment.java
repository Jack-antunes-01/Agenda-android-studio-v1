package com.example.teste;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.teste.ui.Adapter.ExcludedPeopleAdapter;
import com.example.teste.ui.BancoDeDados.contatos.Contatos;
import com.example.teste.ui.BancoDeDados.contatos.ContatosDAO;
import com.example.teste.ui.FragmentoInicial.HomeFragmentArgs;
import com.example.teste.ui.FragmentoInicial.HomeFragmentDirections;

import java.util.ArrayList;
import java.util.List;

public class ExcludedPeopleFragment extends Fragment {

    private ArrayList<Contatos> listItem = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.list_excluded_people, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarTitle("Contatos excluídos");

        if(getArguments() != null){

            ExcludedPeopleFragmentArgs args = ExcludedPeopleFragmentArgs.fromBundle(getArguments());

            String ra = args.getRa();

            configuraRecycler(view, ra);

            view.findViewById(R.id.fabBackExcludedPeople).setOnClickListener(v -> {
                Navigation.findNavController(view).popBackStack();
            });


        }

    }

    RecyclerView recyclerView;
    ExcludedPeopleAdapter adapter;
    public void configuraRecycler(View view, String ra){
        recyclerView = view.findViewById(R.id.excludedPeopleList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        ContatosDAO dao = new ContatosDAO(this.getContext());
        adapter = new ExcludedPeopleAdapter(dao.viewData(ra, 1));

//        List<Contatos> dadosStatus2 = dao.viewData(ra, 1);
//
//        for(Contatos value: dadosStatus2){
//
//                System.out.println("nomeContato: [" + value.getNome() + "] \nfoto: " + value.getFoto());
//
//        }

        TextView batata = view.findViewById(R.id.noExcludedContacts);
        if(dao.viewData(ra, 1).isEmpty()){
            batata.setVisibility(View.VISIBLE);
        } else {
            batata.setVisibility(View.GONE);
        }
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));
    }
}