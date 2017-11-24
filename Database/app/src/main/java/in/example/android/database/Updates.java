package in.example.android.database;

/**
 * Created by rohitramaswamy on 17/11/17.
 */

public class Updates
{
	String title, count;
	
	public Updates()
	{
	}
	
	public Updates(String title, String count)
	{
		this.title = title;
		this.count = count;
	}
	
	public String getCount()
	{
		
		return count;
	}
	
	public void setCount(String count)
	{
		this.count = count;
	}
	
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
}
