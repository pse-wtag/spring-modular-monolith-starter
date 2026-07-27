package mu.welldev.rest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(version = "1.0", value = "/api/v1/auth", produces = "application/json")
public class AuthenticationController {

}
