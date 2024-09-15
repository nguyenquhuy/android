package com.example.btl.filters;

import android.widget.Filter;

import com.example.btl.adapters.AdapterCategory;
import com.example.btl.adapters.AdapterPdfAdmin;
import com.example.btl.models.ModelCategory;
import com.example.btl.models.ModelPdf;

import java.util.ArrayList;

public class FilterPdfAdmin extends Filter {
    ArrayList<ModelPdf> filterList;
    //adapter in which filter need to be implemented
    AdapterPdfAdmin adapterPdfAdmin;

    //constructor

    public FilterPdfAdmin(ArrayList<ModelPdf> filterList, AdapterPdfAdmin adapterPdfAdmin) {
        this.filterList = filterList;
        this.adapterPdfAdmin = adapterPdfAdmin;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();
        //gia tri khong duoc rong
        if(charSequence!=null && charSequence.length()>0){
            charSequence = charSequence.toString().toUpperCase();
            ArrayList<ModelPdf> filteredModels = new ArrayList<>();
            for(int i =0;i<filterList.size();i++){
                if(filterList.get(i).getTitle().toUpperCase().contains(charSequence)){
                    //add to filteredlist
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
        adapterPdfAdmin.pdfArrayList = (ArrayList<ModelPdf>)filterResults.values;

        adapterPdfAdmin.notifyDataSetChanged();
    }
}
