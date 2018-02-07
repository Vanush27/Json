package am.user.json;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private DialogInterface mDialogInterface;
    private List<Space> mJsonSpaces;
    private Context mContext;

    Adapter(Context context, ArrayList<Space> jsonSpaces) {
        mJsonSpaces = jsonSpaces;
        mContext = context;
        mDialogInterface = (DialogInterface) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Space jsonSpace = mJsonSpaces.get(position);

        holder.nameRocket.setText(mJsonSpaces.get(position).getRocket_name());
        holder.details.setText(mJsonSpaces.get(position).getDetails());
        holder.time.setText(mContext.getString(R.string.time) + mJsonSpaces.get(position).getLaunch_date_unix());

        Picasso.with(mContext)
                .load(jsonSpace.getMission_patch())
                .resize(150, 150)
                .into(holder.image);

        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogInterface.showChooserDialog(jsonSpace.getArticle(), jsonSpace.getVideo_link());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mJsonSpaces.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView details;
        private TextView nameRocket;
        private TextView time;
        private ImageView image;
        private LinearLayout mLinearLayout;

        private ViewHolder(View itemView) {
            super(itemView);

            time = (TextView) itemView.findViewById(R.id.time);
            nameRocket = (TextView) itemView.findViewById(R.id.nameRocket);
            details = (TextView) itemView.findViewById(R.id.details);
            image = (ImageView) itemView.findViewById(R.id.imageV);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.linerLayout);
        }

    }
}
