package com.mytrail.android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class adapter2 extends RecyclerView.Adapter<adapter2.ViewHolder> {

    private List<String> mData;
    private List<String> mData1;
    private List<String> mData2;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener1;
    private OnItemClickListener mOnItemClickListener;
    private OnItemClickListener mOnItemClickListener1;


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    adapter2(NCT context, List<String> data, List<String> data1, List<String> data2, OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;

        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mData1 = data1;
        this.mData2 = data2;


    }







    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        holder.mresch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mOnItemClickListener.onItemClick(v, position);
            }
        });



        holder.setIsRecyclable(false);
        String nct1 = mData.get(position);
        String nct2 = mData1.get(position);
        String nct3 = mData2.get(position);

        holder.myTextViewa.setText(nct1);
        holder.myTextViewb.setText(nct2);
        holder.myTextViewc.setText(nct3);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextViewa;
        TextView myTextViewb;
        TextView myTextViewc;
        public Button mresch;
        public Button mreason;






        public  ViewHolder(View itemView) {
            super(itemView);
            myTextViewa = itemView.findViewById(R.id.nct1);
            myTextViewb = itemView.findViewById(R.id.nct2);
            myTextViewc = itemView.findViewById(R.id.nct3);

            mresch=itemView.findViewById(R.id.resch);
            mreason=itemView.findViewById(R.id.reason);



            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {


            if (mClickListener1 != null) mClickListener1.onItemClick1(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);

    }
    public void setClickListener1(adapter2.ItemClickListener itemClickListener) {
        this.mClickListener1 = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick1(View view, int position);
    }

}
