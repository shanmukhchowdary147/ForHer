package com.example.locationapp;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationapp.Model.Requests;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterClass extends RecyclerView.Adapter<com.example.locationapp.AdapterClass.MyViewHolder> {
    private static final String TAG ="Shannuxx";

    ArrayList<Requests> list;

    public AdapterClass(ArrayList<Requests> list)
    {

        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_holder,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        String latLongt=list.get(i).getLatitude()+" "+list.get(i).getLongitude();
        Log.i(TAG,latLongt);
        myViewHolder.title.setText(list.get(i).getName());
        myViewHolder.additionalInfo.setText("Latitude and Longitude :- "+latLongt);
        myViewHolder.contactNumber.setText("Contact Number :- "+list.get(i).getPhone());
        myViewHolder.days.setText("Date :- "+list.get(i).getDate());
        myViewHolder.cost.setText("Time :- "+list.get(i).getTime());
        Picasso.get().load(list.get(i).getImage()).into(myViewHolder.image);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,days,cost,additionalInfo,contactNumber;
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri=Uri.parse("https://www.google.co.in/maps/dir/"+"/"+list.get(getAdapterPosition()).getLatitude()+" "+list.get(getAdapterPosition()).getLongitude());
                    Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                    intent.setPackage("com.google.android.apps.maps");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });
            title=itemView.findViewById(R.id.Equip_name);
            additionalInfo=itemView.findViewById(R.id.Equip_description);
            contactNumber=itemView.findViewById(R.id.Equip_CNumber);
            cost=itemView.findViewById(R.id.Equip_cost);
            days=itemView.findViewById(R.id.Equip_days);
            image=(ImageView)itemView.findViewById(R.id.rProfileImg);

        }
    }
}
