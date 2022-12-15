package com.example.test;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecyAdapt extends RecyclerView.Adapter<RecyAdapt.Myviewholder> implements Filterable {
    List<Cont> contList;
    List<Cont> back;
    EditText editText;
    boolean isSelectedAll;

    public RecyAdapt(List<Cont> contList,EditText editText) {
        this.contList = contList;
        this.editText=editText;
        back=new ArrayList<>(contList);
    }


    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle,parent,false);
        Myviewholder myviewholder=new Myviewholder(view);
        myviewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText(contList.get(myviewholder.getAdapterPosition()).getNum());
            }
        });



        return myviewholder;
    }
    public void selectAll(){
        isSelectedAll=true;
        notifyDataSetChanged();
    }
    public void unselectall(){
        isSelectedAll=false;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {

        holder.textView.setText(contList.get(position).getName());
        holder.textView2.setText(contList.get(position).getNum());
        if (!isSelectedAll){
            holder.checkBox.setChecked(false);
        }
        else  holder.checkBox.setChecked(true);
    }

    @Override
    public int getItemCount() {
        return contList.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView2;
        CheckBox checkBox;
        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textItem1);
            textView2=itemView.findViewById(R.id.textItem2);
            checkBox=itemView.findViewById(R.id.checkBox);
        }





    }
    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Cont> filtedata=new ArrayList<>();
            if (charSequence.toString().isEmpty()){
                filtedata.addAll(back);
            }else{
                for (Cont n:back) {
                    if(n.getName().toString().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filtedata.add(n);
                    }
                }
            }

            FilterResults results=new FilterResults();
            results.values=filtedata;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            contList.clear();
            contList.addAll((ArrayList<Cont>)filterResults.values);
            notifyDataSetChanged();
        }
    };


}
