/*
 * Copyright (C) 2017 Vincent Peter
 * Licensed under the Apache License, Version 2.0 Smart Bet Tips
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.yoctopus.smarttips;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class Adapter<T> extends
        RecyclerView.Adapter<Adapter<T>.Holder> {
    private List<T> list;
    @LayoutRes
    private int layout;

    public Adapter(List<T> list,
                   @LayoutRes
                   int layout) {
        this.list = list;
        this.layout = layout;
    }

    public void add(T t) {
        list.add(t);
        notifyDataSetChanged();
    }

    public void add(List<T> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void remove(T t) {
        this.list.remove(t);
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent,
                                     int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout,
                        parent,
                        false);
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(Holder holder,
                                 int position) {
        T t = list.get(position);
        holder.bind(t);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public abstract void onInitViews(View parent);

    public abstract void onBind(T t);

    class Holder extends RecyclerView.ViewHolder {
        Holder(View itemView) {
            super(itemView);
            onInitViews(itemView);
        }

        void bind(T t) {
            onBind(t);
        }
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }
}
