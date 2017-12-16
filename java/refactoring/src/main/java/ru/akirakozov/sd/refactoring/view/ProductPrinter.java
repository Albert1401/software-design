package ru.akirakozov.sd.refactoring.view;

import ru.akirakozov.sd.refactoring.controller.Product;

public class ProductPrinter {
    public String toHtml(Product product){
        return product.getName() + '\t' + product.getPrice();
    }
}
