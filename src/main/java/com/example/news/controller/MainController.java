package com.example.news.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.news.dao.ArticleDAO;
import com.example.news.model.Article;

@Controller
public class MainController {

	@Autowired
	private ArticleDAO articleDAO;

	@GetMapping("/")
	public String showHomePage(HttpServletRequest req, Model model) {
		Article latestArt = null;

		// Get article by id
		try {
			long id = Long.parseLong(req.getParameter("id"));
			latestArt = articleDAO.getArticle(id);
		} catch (Exception e) {

		}

		List<Article> articles = articleDAO.getArticles();
		model.addAttribute("articles", articles);

		// Get first article if there is exception above or directing home page
		if (latestArt == null && articles.size() > 0) {
			latestArt = articles.get(0);
		}
		model.addAttribute("latestArt", latestArt);

		String datetime = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
		model.addAttribute("datetime", datetime);

		return "index";
	}

	@GetMapping("/login")
	public String showLoginPage() {
		return "login";
	}

	@GetMapping("/admin/add")
	public String showAdminPage(Model model) {
		Article article = new Article();
		model.addAttribute("article", article);

		return "add_update";
	}

	@PostMapping("/admin/save")
	public String saveArticle(@Valid Article article, BindingResult result, RedirectAttributes redirect) {

		long millis = System.currentTimeMillis();
		article.setDate(new java.sql.Date(millis));
		// Kiểm tra add or update
		if (article.getId() != 0) {
			articleDAO.updateArticle(article);
		} else {
			articleDAO.addArticle(article);
		}

		redirect.addFlashAttribute("message", "Saved article successfully!");
		return "redirect:/admin";
	}

	@PostMapping("/search")
	public String searchArticle(HttpServletRequest req, Model model) {
		Article latestArt = null;
		String key = req.getParameter("search");

		List<Article> articles = articleDAO.searchArticle(key);
		model.addAttribute("articles", articles);

		// Get first article if there is exception above or directing home page
		if (latestArt == null && articles.size() > 0) {
			latestArt = articles.get(0);
		}
		model.addAttribute("latestArt", latestArt);
		return "index";
	}

	@GetMapping("/admin")
	public String showAdminPage(HttpServletRequest req, Model model) {

		List<Article> articles = articleDAO.getArticles();
		model.addAttribute("articles", articles);

		return "articles";
	}

	@GetMapping("/admin/edit")
	public String updateArticle(HttpServletRequest req, Model model) {
		Article article = null;

		// Get article by id
		try {
			long id = Long.parseLong(req.getParameter("id"));
			article = articleDAO.getArticle(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("article", article);
		return "add_update";
	}

	@GetMapping("/admin/delete")
	public String deleteArticle(HttpServletRequest req, RedirectAttributes redirect) {
		try {
			long id = Long.parseLong(req.getParameter("id"));
//			Boolean result = articleDAO.deleteArticle(id);
			articleDAO.deleteArticle(id);
			redirect.addFlashAttribute("message", "Deleted article successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute("message", "Deleted contact failure!");
		}

		return "redirect:/admin";
	}
	
	@RequestMapping("/logo")
	public String logo() {
		return "logo";
	}
	
}
