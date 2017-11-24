package in.example.android.database;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class UpdatesViewHolder extends RecyclerView.ViewHolder
{
	public TextView update, count;
	
	public UpdatesViewHolder(View itemView)
	{
		super(itemView);
		update = (TextView) itemView.findViewById(R.id.update);
		count = (TextView) itemView.findViewById(R.id.update_count);
	}
}
