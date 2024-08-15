package com.example.weshops;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ProductHolder> {
    private static ArrayList<ShopProducts> Products;
    private static OnItemClickListener signalM;


    public interface OnItemClickListener{
        void onDeleteClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        signalM = listener;

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
        ShopProducts currentItem = Products.get(position);
        holder.ProdImage.setImageResource(currentItem.getProdImage());
        holder.ProdName.setText(currentItem.getProdName());
        holder.ProdPrice.setText(currentItem.getPrice());
        holder.ProdDesc.setText(currentItem.getdescription());
        holder.ProdDate.setText(currentItem.getProdDate());
    }

    public ShopAdapter(ArrayList<ShopProducts> ProdList) {
        Products = ProdList;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listv, parent, false);
        ProductHolder evh = new ProductHolder(view, signalM);
        return evh;
    }

    @Override
    public int getItemCount() {
        return Products.size();
    }
    // method for filtering our recyclerview items.
    public void filterList(ArrayList<ShopProducts> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        Products = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

}
