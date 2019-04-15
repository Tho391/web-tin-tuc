package com.example.news.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.example.news.model.Article;

@Repository
public class ArticleDAO extends JdbcDaoSupport {

	@Autowired
	public ArticleDAO(DataSource dataSource) {
		this.setDataSource(dataSource);
	}

	private class ArticleMapper implements RowMapper<Article> {

		@Override
		public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
			long id = rs.getLong("id");
			String title = rs.getString("title");
			String content = rs.getString("content");
			String author = rs.getString("author");
			Date date = rs.getDate("date");

			return new Article(id, title, content, author, date);
		}

	}

	public List<Article> getArticles() {
		String sql = "select * from articles order by date desc";

		Object[] params = new Object[] {};
		ArticleMapper mapper = new ArticleMapper();
		List<Article> articles = this.getJdbcTemplate().query(sql, params, mapper);

		return articles;
	}

	public boolean updateArticle(Article article) {
		String sql = "update articles set title = ? , content = ?,author = ? , date = ? where id = ?";
		Connection connection = null;

		System.out.println(article.getContent());
		
		try {
			connection = this.getDataSource().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, article.getTitle());
			preparedStatement.setString(2, article.getContent());
			preparedStatement.setString(3, article.getAuthor());
			preparedStatement.setDate(4, article.getDate());
			preparedStatement.setLong(5, article.getId());
			preparedStatement.executeUpdate();
			preparedStatement.close();

		} catch (SQLException err) {
			err.printStackTrace();
			return false;
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	public Article getArticle(long id) {
		String sql = "select * from articles where id = ?";

		Object[] params = new Object[] { id };
		ArticleMapper mapper = new ArticleMapper();

		try {
			Article article = this.getJdbcTemplate().queryForObject(sql, params, mapper);
			return article;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public boolean addArticle(Article article) {
		String sql = "insert into articles(title,content,author,date) values(?,?,?,?);";

		Connection connection = null;
		try {
			connection = this.getDataSource().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, article.getTitle());
			preparedStatement.setString(2, article.getContent());
			preparedStatement.setString(3, article.getAuthor());
			preparedStatement.setDate(4, article.getDate());
			preparedStatement.executeUpdate();
			preparedStatement.close();

		} catch (SQLException err) {
			err.printStackTrace();
			return false;
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return true;
	}

	public List<Article> searchArticle(String keyword) {
		// TODO Lỗi

		return null;
	}

	public boolean deleteArticle(long id) {
		String sql = "delete from articles where id=?";

		Connection connection = null;
		try {
			connection = this.getDataSource().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setLong(1, id);
			preparedStatement.executeUpdate();
			preparedStatement.close();

		} catch (SQLException err) {
			err.printStackTrace();
			return false;
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

}
