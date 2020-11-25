package com.mytrail.android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private List<String> mData1;
    private List<String> mData2;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private OnItemClickListener mOnItemClickListener;
    private OnItemClickListener mOnItemClickListener1;
    private OnItemClickListener mOnItemClickListener2;
    private OnItemClickListener mOnItemClickListener3;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    // data is passed into the constructor
    MyRecyclerViewAdapter(Calendar context, List<String> data,List<String> data1,List<String> data2,OnItemClickListener onItemClickListener,OnItemClickListener onItemClickListener1,OnItemClickListener onItemClickListener2) {
        mOnItemClickListener = onItemClickListener;
        mOnItemClickListener1 = onItemClickListener1;
        mOnItemClickListener2 = onItemClickListener2;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mData1 = data1;
        this.mData2 = data2;
    }

    MyRecyclerViewAdapter(Main3Activity context, List<String> data, List<String> data1, List<String> data2, OnItemClickListener onItemClickListener, OnItemClickListener onItemClickListener1, OnItemClickListener onItemClickListener2,OnItemClickListener onItemClickListener3) {
        mOnItemClickListener = onItemClickListener;
        mOnItemClickListener1 = onItemClickListener1;
        mOnItemClickListener2 = onItemClickListener2;
        mOnItemClickListener3 = onItemClickListener3;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mData1 = data1;
        this.mData2 = data2;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);

    }


    // binds the data to the TextView in each row

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {

        holder.mphone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });
        holder.mmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener1.onItemClick(v,position);
            }
        });
        holder.madd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener2.onItemClick(v,position);
            }
        });
        holder.myTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener3.onItemClick(v,position);
            }
        });


        String animal = mData.get(position);
        String animal1 = mData1.get(position);
        String animal2 = mData2.get(position);
        holder.setIsRecyclable(false);
        holder.myTextView.setText(animal);
        holder.myTextView1.setText(animal1);
        holder.myTextView2.setText(animal2);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView myTextView;
        TextView myTextView1;
        TextView myTextView2;
        public ImageView mphone;
        public ImageView mmsg;
        public ImageView madd;

        public  ViewHolder(View itemView) {

            super(itemView);
            myTextView = itemView.findViewById(R.id.tvAnimalName);
            myTextView1 = itemView.findViewById(R.id.tvAnimalName1);
            myTextView2 = itemView.findViewById(R.id.tvAnimalName2);
            mphone=itemView.findViewById(R.id.phn);
            mmsg=itemView.findViewById(R.id.msg);
            madd=itemView.findViewById(R.id.add);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);

    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}