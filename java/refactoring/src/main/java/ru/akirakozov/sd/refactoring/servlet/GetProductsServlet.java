package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.controller.Product;
import ru.akirakozov.sd.refactoring.controller.ProductController;
import ru.akirakozov.sd.refactoring.view.ProductPrinter;
import ru.akirakozov.sd.refactoring.view.SimpleHtmlConstructor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    private final ProductController productController;

    public GetProductsServlet(ProductController productController) {
        Objects.requireNonNull(productController);
        this.productController = productController;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ProductPrinter printer = new ProductPrinter();

        String html = new SimpleHtmlConstructor()
                .theme("All products:")
                .list(productController.getAll().stream()
                        .map(printer::toHtml)
                        .collect(Collectors.toList()))
                .toString();

        response.getWriter().print(html);

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
