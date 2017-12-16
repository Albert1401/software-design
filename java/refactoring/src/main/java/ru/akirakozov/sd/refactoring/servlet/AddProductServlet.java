package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.controller.Product;
import ru.akirakozov.sd.refactoring.controller.ProductController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class AddProductServlet extends HttpServlet {

    private ProductController productController;

    public AddProductServlet(ProductController productController) {
        Objects.requireNonNull(productController);
        this.productController = productController;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        productController.createEntity(new Product(name, price));

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("OK");
    }
}
