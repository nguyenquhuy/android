package com.example.btl.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.databinding.RowCategoryBinding;
import com.example.btl.models.ModelCategory;
import com.example.btl.models.ModelTest;

import java.util.ArrayList;

public class AdapterTest extends RecyclerView.Adapter<AdapterTest.HolderTest>{

    private Context context;
    private ArrayList<ModelTest> testArrayList;

    private RowCategoryBinding binding;
    public AdapterTest(Context context, ArrayList<ModelTest> testArrayList) {
        this.context = context;
        this.testArrayList = testArrayList;
    }

    @NonNull
    @Override
    public HolderTest onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderTest(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderTest holder, int position) {
        ModelTest model = testArrayList.get(position);
        holder.categoryTv.setText(model.getName());
        holder.motaTv.setText(model.getMota());
        holder.dongiaTv.setText(model.getDongia());
        holder.soluongTV.setText(model.getSoluong());
    }

    @Override
    public int getItemCount() {
        return testArrayList.size();
    }

    class HolderTest extends RecyclerView.ViewHolder{

        TextView categoryTv, motaTv, dongiaTv, soluongTV;
        ImageButton deleteBtn;
        ImageButton editBtn;

        public HolderTest(@NonNull View itemView) {
            super(itemView);

            categoryTv = binding.categoryTv;
            motaTv = binding.motaTv;
            dongiaTv = binding.dongiaTv;
            soluongTV = binding.soluongTv;
            deleteBtn = binding.deleteBtn;
            editBtn = binding.editBtn;
        }
    }
}
