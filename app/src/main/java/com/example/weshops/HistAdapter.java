package com.example.weshops;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistAdapter extends RecyclerView.Adapter<HistAdapter.ProductHolder> {
    private static ArrayList<HistProducts> HistoryList;
    private static OnItemClickListener Histlistener;

    public interface OnItemClickListener{
        void onDeleteClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        Histlistener = listener;

    }
    public static class ProductHolder extends RecyclerView.ViewHolder {
        public ImageView ProdImage;
        public TextView ProdPrice;
        public TextView ProdDate;
        public TextView ProdName;
        public ImageView DeleteProd;
        public TextView ProdDesc;



        public ProductHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            ProdImage = itemView.findViewById(R.id.prod_image);
            ProdName = itemView.findViewById(R.id.prod_name);
            ProdDesc = itemView.findViewById(R.id.prod_details);
            ProdPrice = itemView.findViewById(R.id.prod_price);
            ProdDate = itemView.findViewById(R.id.prod_date);

            DeleteProd = itemView.findViewById(R.id.pic_del);
            DeleteProd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);

                        }
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        HistProducts currentItem = HistoryList.get(position);
        holder.ProdImage.setImageResource(currentItem.getHistImage());
        holder.ProdName.setText(currentItem.getHistName());
        holder.ProdPrice.setText(currentItem.getPrice());
        holder.ProdDesc.setText(currentItem.getdescription());
        holder.ProdDate.setText(currentItem.getHistDate());
    }

    public HistAdapter(ArrayList<HistProducts> ProdList) {
        HistoryList = ProdList;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listv, parent, false);
        ProductHolder evh = new ProductHolder(view, Histlistener);
        return evh;
    }

    @Override
    public int getItemCount() {
        return HistoryList.size();
    }


}
