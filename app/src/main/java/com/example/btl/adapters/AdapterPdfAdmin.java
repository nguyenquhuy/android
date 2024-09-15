package com.example.btl.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.MyApplication;
import com.example.btl.R;
import com.example.btl.activities.ImageAddActivity;
import com.example.btl.activities.PdfDetailActivity;
import com.example.btl.activities.PdfEditActivity;
import com.example.btl.databinding.RowPdfAdminBinding;
import com.example.btl.filters.FilterPdfAdmin;
import com.example.btl.models.ModelPdf;
import com.github.barteksc.pdfviewer.PDFView;

import java.util.ArrayList;

public class AdapterPdfAdmin extends RecyclerView.Adapter<AdapterPdfAdmin.HolderAdmin> implements Filterable {

    private Context context;
    public ArrayList<ModelPdf> pdfArrayList, filterList;

    private RowPdfAdminBinding binding;
    private FilterPdfAdmin filter;
    private static final String TAG = "PDF_ADAPTER_TAG";

    private ProgressDialog progressDialog;

    public AdapterPdfAdmin(Context context, ArrayList<ModelPdf> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
        this.filterList = pdfArrayList;

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @NonNull
    @Override
    public HolderAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowPdfAdminBinding.inflate(LayoutInflater.from(context),parent,false);
        return new HolderAdmin(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderAdmin holder, int position) {

        ModelPdf model = pdfArrayList.get(position);
        String title = model.getTitle();
        String description = model.getDescription();
        String pdfUrl = model.getUrl();
        String pdfId = model.getId();
        String categoryId = model.getCategoryId();
        long timestamp = model.getTimestamp();

        //convert timestamp ve dd/mm/yyyy
        String formattedDate = MyApplication.formatTimestamp(timestamp);

        //setdata
        holder.titleTv.setText(title);
        holder.descriptionTv.setText(description);
        holder.dateTv.setText(formattedDate);


        MyApplication.loadCategory(
                ""+categoryId,
                holder.categoryTv);
        MyApplication.loadPdfFromUrlSinglePage(
                ""+pdfUrl,
                ""+title,
                holder.pdfView,
                holder.progressBar,null);
        //holder.imageView.setImageResource(R.drawable.anh_1);
        MyApplication.loadPdfSize(
                ""+pdfUrl,
                ""+title,
                holder.sizeTv);

        //click hien ra 2 lua cho, 1 edit 2 xoa
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreOptionsDialog(model, holder);
            }
        });

        //click vao pdf
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PdfDetailActivity.class);
                intent.putExtra("bookId", pdfId);
                context.startActivity(intent);
            }
        });
        //---------------------------------------
        /*holder.imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edit click, open pdfeditactivity
                Intent intent = new Intent(context, ImageAddActivity.class);
                context.startActivity(intent);
            }
        });*/
        //--------------------------------------------
    }

    private void moreOptionsDialog(ModelPdf model, HolderAdmin holder) {
        String bookId = model.getId();
        String bookUrl = model.getUrl();
        String bookTitle = model.getTitle();

        String[] options = {"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose options")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0){
                            //edit click, open pdfeditactivity
                            Intent intent = new Intent(context, PdfEditActivity.class);
                            intent.putExtra("bookId", bookId);
                            context.startActivity(intent);

                        }
                        else if(i == 1){
                            //delete click
                            MyApplication.deleteBook(context,
                                    ""+bookId,
                                    ""+bookUrl,
                                    ""+bookTitle
                            );
                            //deleteBook(model, holder);
                        }
                    }
                })
                .show();
    }
    @Override
    public int getItemCount() {
        return pdfArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new FilterPdfAdmin(filterList, this);
        }
        return filter;
    }

    class HolderAdmin extends RecyclerView.ViewHolder{

        PDFView pdfView;
        ProgressBar progressBar;
        TextView titleTv, descriptionTv, categoryTv, sizeTv, dateTv;
        ImageButton moreBtn;
        //ImageView imageView;

        public HolderAdmin(@NonNull View itemView) {
            super(itemView);

            pdfView = binding.pdfView;
            progressBar = binding.progressBar;
            titleTv = binding.titleTv;
            descriptionTv = binding.descriptionTv;
            categoryTv = binding.categoryTv;
            sizeTv = binding.sizeTv;
            dateTv = binding.dateTv;
            moreBtn = binding.moreBtn;
            //imgBtn = binding.attachImgBtn;
            //imageView = binding.imageIv;
        }
    }

}
