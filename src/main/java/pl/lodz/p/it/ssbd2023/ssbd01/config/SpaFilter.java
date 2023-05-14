package pl.lodz.p.it.ssbd2023.ssbd01.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.extern.java.Log;

@Log
public class SpaFilter implements Filter {
  private FilterConfig config;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
    this.config = filterConfig;
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    String uri = request.getRequestURI();

    // fixme
    if (uri.matches("^/[a-zA-Z0-9\\-_]+$")) {
      RequestDispatcher dispatcher = request.getRequestDispatcher("index.html");
      dispatcher.forward(servletRequest, servletResponse);
    } else {
      filterChain.doFilter(servletRequest, servletResponse);
    }
  }

  @Override
  public void destroy() {
    config = null;
    Filter.super.destroy();
  }
}
