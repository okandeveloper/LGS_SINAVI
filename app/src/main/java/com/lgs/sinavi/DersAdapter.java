package com.lgs.sinavi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DersAdapter extends RecyclerView.Adapter<DersAdapter.ViewHolder> {

    private final List<DersProgrami> dersList;
    private final Context context;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDelete(int position);
        void onEdit(int position);
    }

    public DersAdapter(Context context, List<DersProgrami> dersList, OnItemClickListener listener) {
        this.context = context;
        this.dersList = dersList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ders, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DersAdapter.ViewHolder holder, int position) {
        DersProgrami ders = dersList.get(position);
        holder.dersAdi.setText(ders.getDersAdi());
        holder.konu.setText(ders.getKonu());
        holder.sure.setText(ders.getSure() + " dk");
        holder.gun.setText(ders.getGun());

        holder.deleteBtn.setOnClickListener(v -> listener.onDelete(position));
        holder.editBtn.setOnClickListener(v -> listener.onEdit(position));
    }

    @Override
    public int getItemCount() {
        return dersList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dersAdi, konu, sure, gun;
        ImageButton deleteBtn, editBtn;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dersAdi = itemView.findViewById(R.id.dersAdi);
            konu = itemView.findViewById(R.id.konu);
            sure = itemView.findViewById(R.id.sure);
            gun = itemView.findViewById(R.id.gun);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            editBtn = itemView.findViewById(R.id.editBtn);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
