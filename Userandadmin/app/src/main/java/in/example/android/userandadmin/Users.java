package in.example.android.userandadmin;

/**
 * Created by rohitramaswamy on 26/11/17.
 */

public class Users
{
	String name;
	String email;
	String role;
	
	public Users(String name, String email, String role)
	{
		this.name = name;
		this.email = email;
		this.role = role;
	}
	
	public String getRole()
	{
	
		return role;
	}
	
	public void setRole(String role)
	{
		this.role = role;
	}
	
	public Users(String name, String email)
	{
		this.name = name;
		this.email = email;
	}
	
	public Users()
	
	{
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
}
