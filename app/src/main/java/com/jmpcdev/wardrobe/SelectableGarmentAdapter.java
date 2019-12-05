package com.jmpcdev.wardrobe;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SelectableGarmentAdapter extends RecyclerView.Adapter implements SelectableViewHolder.OnItemSelectedListener {
    private final List<SelectableGarment> mValues;
    private boolean isMultiSelectionEnabled = false;
    SelectableViewHolder.OnItemSelectedListener listener;


    public SelectableGarmentAdapter( SelectableViewHolder.OnItemSelectedListener listener,
                              List<SelectableGarment> items,boolean isMultiSelectionEnabled) {
        this.listener = listener;
        this.isMultiSelectionEnabled = isMultiSelectionEnabled;

        mValues = new ArrayList<>();
        for (SelectableGarment item : items) {
            mValues.add(new SelectableGarment(item, false));
        }
    }

    @Override
    public SelectableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selectable_garment_card, parent, false);

        return new SelectableViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        SelectableViewHolder holder = (SelectableViewHolder) viewHolder;
        SelectableGarment selectableItem = mValues.get(position);
        String name = selectableItem.getName();
        holder.textView.setText(name);
        if (isMultiSelectionEnabled) {
            TypedValue value = new TypedValue();
            holder.textView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorMultiple, value, true);
            int checkMarkDrawableResId = value.resourceId;
            holder.textView.setCheckMarkDrawable(checkMarkDrawableResId);
        } else {
            TypedValue value = new TypedValue();
            holder.textView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorSingle, value, true);
            int checkMarkDrawableResId = value.resourceId;
            holder.textView.setCheckMarkDrawable(checkMarkDrawableResId);
        }

        holder.selectableGarment = selectableItem;
        holder.setChecked(holder.selectableGarment.isSelected());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public List<SelectableGarment> getSelectedItems() {

        List<SelectableGarment> selectedItems = new ArrayList<>();
        for (SelectableGarment item : mValues) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    @Override
    public int getItemViewType(int position) {
        if(isMultiSelectionEnabled){
            return SelectableViewHolder.MULTI_SELECTION;
        }
        else{
            return SelectableViewHolder.SINGLE_SELECTION;
        }
    }

    @Override
    public void onItemSelected(SelectableGarment item) {
        if (!isMultiSelectionEnabled) {

            for (SelectableGarment selectableItem : mValues) {
                if (!selectableItem.equals(item)
                        && selectableItem.isSelected()) {
                    selectableItem.setSelected(false);
                } else if (selectableItem.equals(item)
                        && item.isSelected()) {
                    selectableItem.setSelected(true);
                }
            }
            notifyDataSetChanged();
        }
        listener.onItemSelected(item);
    }
}

