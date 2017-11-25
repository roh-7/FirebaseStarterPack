package in.example.android.userandadmin;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by rohitramaswamy on 26/11/17.
 */

public class UsersViewHolder extends RecyclerView.ViewHolder
{
	public TextView name, email,role;
	
	public UsersViewHolder(View itemView)
	{
		super(itemView);
		name = (TextView) itemView.findViewById(R.id.user_name);
		email = (TextView) itemView.findViewById(R.id.user_email);
		role = (TextView) itemView.findViewById(R.id.user_role);
	}
}
