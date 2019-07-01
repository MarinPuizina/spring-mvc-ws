package app.ws.ui.controller;

import app.ws.ui.model.request.LoginRequestModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    // TODO - There is an known issue with prob Spring 2 that prevents showing ApiResponses via swagger
    @ApiOperation("User login") // naming api to make it look good on swagger (overrides fakeLogin method name)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Response Headers",
                    responseHeaders = {
                            @ResponseHeader(name = "authorization",
                                    description = "Bearer <JWT value here>"),
                            @ResponseHeader(name = "userId",
                                    description = "<Public User Id value here>")
                    })
    })
    // adding login method that we can see request on swagger
    @PostMapping("/users/login")
    public void fakeLogin(@RequestBody LoginRequestModel loginRequestModel) {

        // Throwing this exception because this method should not be called if we send request to /login
        // Spring Security will catch the request sent to /login, it will override it and use his own login service
        throw new IllegalStateException("This method should not be called. This method is implemented by Spring Security");

    }

}
