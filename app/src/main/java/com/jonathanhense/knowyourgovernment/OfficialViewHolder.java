package com.jonathanhense.knowyourgovernment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class OfficialViewHolder extends RecyclerView.ViewHolder {

    TextView officeTitle;
    TextView officeHolder;
    ImageView separator;


    public OfficialViewHolder(@NonNull View itemView) {
        super(itemView);

        officeTitle = itemView.findViewById(R.id.officeTitleID);
        officeHolder = itemView.findViewById(R.id.nameID);
        separator = itemView.findViewById(R.id.separator);

    }
}
