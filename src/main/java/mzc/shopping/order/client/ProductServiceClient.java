package mzc.shopping.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "product-service", url = "${service.product-url}")
public interface ProductServiceClient {

    @GetMapping("/api/products/{id}")
    ProductResponse getProduct(@PathVariable("id") Long id);

    @PostMapping("/api/products/{id}/stock/decrease")
    ProductResponse decreaseStock(@PathVariable("id") Long id, @RequestBody StockRequest request);

    @PostMapping("/api/products/{id}/stock/increase")
    ProductResponse increaseStock(@PathVariable("id") Long id, @RequestBody StockRequest request);
}