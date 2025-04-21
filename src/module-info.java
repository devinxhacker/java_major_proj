module project2 {
	requires java.desktop;
	requires okhttp3;
	requires com.google.gson;
	
	opens main.api to com.google.gson;
}