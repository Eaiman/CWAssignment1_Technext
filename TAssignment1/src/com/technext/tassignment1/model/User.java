package com.technext.tassignment1.model;

import com.technext.tassignment1.http.Client;

public class User {
	
	private Long id;
	private String email;
	private String firstname;
	private String lastname;
	private String profile_pic_url;
	private String session_token;
	private String profile_pic_extension;
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getProfile_pic_url() {
		return Client.URL_BASE + profile_pic_url+"."+getProfile_pic_extension();
	}
	public void setProfile_pic_url(String profile_pic_url) {
		this.profile_pic_url = profile_pic_url;
	}
	
	public String getProfile_pic_url_value(){
		return profile_pic_url;
	}
	
	public String getProfile_pic_url_300x300(){
		return Client.URL_BASE + profile_pic_url + "_300x300"+"."+getProfile_pic_extension();
	}
	public String getProfile_pic_url_600x600(){
		return Client.URL_BASE + profile_pic_url + "_600x600"+"."+getProfile_pic_extension();
	}
	
	public String getSession_token() {
		return session_token;
	}
	public void setSession_token(String session_token) {
		this.session_token = session_token;
	}
	
	public String getProfile_pic_extension() {
		return profile_pic_extension;
	}
	public void setProfile_pic_extension(String profile_pic_extension) {
		this.profile_pic_extension = profile_pic_extension;
	}
	
}
