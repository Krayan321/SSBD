package pl.lodz.p.it.ssbd2023.ssbd01.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

import lombok.extern.java.Log;

@Log
public class SpaFilter implements Filter {
  private FilterConfig config;

  private String listIgnoredExtensionsRegex;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
    this.config = filterConfig;
    String path = config.getServletContext().getRealPath("index.html");
    File index = new File(path);
    File dir = index.getParentFile();
    HashSet<String> ignoredExtensions = getAllExtensions(dir);
    ignoredExtensions.remove("html");

    StringJoiner joiner = new StringJoiner(")|(", "(", ")");
    for(String s : ignoredExtensions) {
      joiner.add(s);
    }
    listIgnoredExtensionsRegex = joiner.toString();
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    String uri = request.getRequestURI();

    // fixme
    if (uri.matches("^/((api)|(.*\\.(" + listIgnoredExtensionsRegex + ")$))")) {
      filterChain.doFilter(servletRequest, servletResponse);
    } else {
      RequestDispatcher dispatcher = request.getRequestDispatcher("index.html");
      dispatcher.forward(servletRequest, servletResponse);
    }
  }

  @Override
  public void destroy() {
    config = null;
    Filter.super.destroy();
  }

  public static HashSet<String> getAllExtensions(File folder) {
    HashSet<String> extensions = new HashSet<>();

    if (folder == null || !folder.isDirectory()) {
      return extensions;
    }

    File[] files = folder.listFiles();
    if (files == null) {
      return extensions;
    }

    for (File file : files) {
      if (file.isDirectory()) {
        extensions.addAll(getAllExtensions(file));
      } else {
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex != -1) {
          extensions.add(fileName.substring(lastDotIndex + 1).toLowerCase());
        }
      }
    }

    return extensions;
  }
}
