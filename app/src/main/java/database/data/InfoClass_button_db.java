package database.data;

public class InfoClass_button_db {
	public int _id;
	public String button_name;
	public String contact;
	public String email;


	public String date;
	public int x_location;
	public int y_location;
	public String coding_contents;



	public InfoClass_button_db(int _id , String button_name ,
							   String date, int x_location, int y_location, String coding_contents){
		this._id = _id;
		this.button_name = button_name;
		this.date = date;
		this.x_location = x_location;
		this.y_location = y_location;
		this.coding_contents = coding_contents;
	}
	
}
