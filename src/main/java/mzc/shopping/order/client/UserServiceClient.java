package mzc.shopping.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${service.user-url}")
public interface UserServiceClient {

    @GetMapping("/api/users/{id}")
    UserResponse getUser(@PathVariable("id") Long id);
}