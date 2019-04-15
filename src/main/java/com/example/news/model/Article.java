package com.example.news.model;

import java.sql.Date;

public class Article {

	private long id;
	private String title;
	private String content;
	private String author;
	private Date date;

	public Article(long id, String title, String content, String author, Date date) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.author = author;
		this.date = date;
	}

	public Article() {
		
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
