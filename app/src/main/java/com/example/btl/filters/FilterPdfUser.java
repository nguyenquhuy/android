package com.example.btl.filters;

import android.widget.Filter;

import com.example.btl.adapters.AdapterPdfUser;
import com.example.btl.models.ModelPdf;

import java.util.ArrayList;
import java.util.Locale;

public class FilterPdfUser extends Filter {
    ArrayList<ModelPdf> filterList;
    AdapterPdfUser adapterPdfUser;

    public FilterPdfUser(ArrayList<ModelPdf> filterList, AdapterPdfUser adapterPdfUser) {
        this.filterList = filterList;
        this.adapterPdfUser = adapterPdfUser;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();
        if(charSequence!=null || charSequence.length()>0){
            //not null or empty
            //change to uppercase or lower case
            charSequence = charSequence.toString().toUpperCase();
            ArrayList<ModelPdf> filteredModels = new ArrayList<>();

            for(int i = 0; i <filterList.size();i++){
                if(filterList.get(i).getTitle().toUpperCase().contains(charSequence)){
                    filteredModels.add(filterList.get(i));
                }
            }

            results.count = filteredModels.size();
            results.values = filteredModels;

        }
        else{
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapterPdfUser.pdfArrayList = (ArrayList<ModelPdf>)filterResults.values;

        adapterPdfUser.notifyDataSetChanged();

    }
}
