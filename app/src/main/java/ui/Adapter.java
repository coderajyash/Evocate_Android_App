package ui;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.evocate.EvocateList;
import com.app.evocate.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.Evocate;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private Context context;
    private List<Evocate> content_list;

    public Adapter(Context context, List<Evocate> content_list) {
        this.context = context;
        this.content_list = content_list;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context)
                .inflate(R.layout.content_row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
    Evocate evocate= content_list.get(position);
    String imageUrl=evocate.getImageURL();

        Picasso.get().load(imageUrl)
                .placeholder(R.drawable.add_icon).fit()
                .into(holder.image);
        String timeago= (String) DateUtils.getRelativeTimeSpanString(evocate.
                getTimeAdded().getSeconds()*1000);

    holder.title.setText(evocate.getTitle());
        holder.description.setText(evocate.getDescription());
         holder.date.setText(timeago);
    }

    @Override
    public int getItemCount() {
        return content_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title,description,date,name;
        public ImageView image;

         public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context=ctx;
            title= itemView.findViewById(R.id.title_list);
             description= itemView.findViewById(R.id.description_list);
             image= itemView.findViewById(R.id.image_list);
             date= itemView.findViewById(R.id.timestamp_list);

        }
    }
}
