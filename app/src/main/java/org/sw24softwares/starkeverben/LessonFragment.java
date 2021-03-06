package org.sw24softwares.starkeverben;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.Set;
import java.util.Collections;

import android.content.Intent;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ExpandableListView;
import android.widget.SearchView;

public class LessonFragment extends Fragment {
        ExpandableListAdapter listAdapter;
        ExpandableListView expListView;
        List<String> listDataHeader;
        HashMap<String, List<String>> listDataChild;
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.activity_lesson, container, false);

                SearchView searchView = (SearchView) view.findViewById(R.id.lesson_search);
                ((MainActivity)getActivity()).initSearch(searchView); // Initializing the search bar with the SearchManager 

                expListView = (ExpandableListView) view.findViewById(R.id.lesson_list);
                search(new String()); //Initiates the ListView with all the verbs
                
                return view;
        }
 
        private void prepareListData(String search_word) {
                listDataHeader = new ArrayList<String>();
                listDataChild = new HashMap<String, List<String>>();

                Vector<Verb> verbs = Settings.getSingleton().getVerbs();
                // Adding child data
                for(int i = 0; i < verbs.size(); i++) {
                        Boolean contain = false;
                        List<String> details = new ArrayList<String>();
                        for(int j = 0; j < verbs.get(i).getAllForms().size(); j++) {
                                String caseVerb = Verb.formToWord(j) + " : ";
                                for(int k = 0; k < verbs.get(i).getAllForms().get(j).size(); k++) {
                                        if(verbs.get(i).getAllForms().get(j).get(k).contains(search_word))
                                                contain = true;
                                        caseVerb += verbs.get(i).getAllForms().get(j).get(k);
                                        if(k < verbs.get(i).getAllForms().get(j).size() - 1)
                                                caseVerb += ", ";
                                }
                                details.add(caseVerb);
                        }
                        String caseVerb = Verb.formToWord(5) + " : " + Verb.boolToAux(verbs.get(i).getAuxiliary());
                        details.add(caseVerb);
                        caseVerb = Verb.formToWord(4) + " : ";
                        for(int j = 0; j < Settings.getSingleton().getTranslations().get(i).getTranslations().size(); j++) {
                                if(Settings.getSingleton().getTranslations().get(i).getTranslations().get(j).contains(search_word))
                                        contain = true;
                                caseVerb += Settings.getSingleton().getTranslations().get(i).getTranslations().get(j);
                                if(j < Settings.getSingleton().getTranslations().get(i).getTranslations().size() - 1)
                                        caseVerb += ", ";
                        }
                        details.add(caseVerb);
                        if(contain || search_word == new String()) {
                                String suffix = "";
                                int n = Collections.frequency(listDataHeader,verbs.get(i).getAllForms().get(0).get(0));
                                for(int j = 0; j < n; j++)
                                        suffix += "\0";
                                listDataHeader.add(verbs.get(i).getAllForms().get(0).get(0) + suffix);
                                listDataChild.put(listDataHeader.get(listDataHeader.size()-1), details);
                        }
                }
        }
        protected void search(String word) {
                prepareListData(word);
                listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
                expListView.setAdapter(listAdapter);
        }
}
