package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.controller.Product;
import ru.akirakozov.sd.refactoring.controller.ProductController;
import ru.akirakozov.sd.refactoring.view.ProductPrinter;
import ru.akirakozov.sd.refactoring.view.SimpleHtmlConstructor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {

    private final ProductController productController;

    public QueryServlet(ProductController productController) {
        Objects.requireNonNull(productController);
        this.productController = productController;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        ProductPrinter printer = new ProductPrinter();
        SimpleHtmlConstructor constructor = new SimpleHtmlConstructor();

        Product p = null;
        switch (command) {
            case "max":
                p = productController.getMax();
                constructor.theme("Product with max price:")
                        .text(printer.toHtml(p));
                break;
            case "min":
                p = productController.getMin();
                constructor.theme("Product with min price:")
                        .text(printer.toHtml(p));
                break;
            case "sum":
                int sum = productController.getSum();
                constructor.theme("Summary price: ")
                        .text(sum + "");
                break;
            case "count":
                int count = productController.getCount();
                constructor.theme("Number of products: ")
                        .text(count + "");
                break;
            default:
                constructor.theme("Unknown command: " + command);
                break;
        }
        response.getWriter().println(constructor.toString());

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
