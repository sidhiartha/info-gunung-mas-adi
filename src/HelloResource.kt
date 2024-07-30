import jakarta.ws.rs.GET
import jakarta.ws.rs.Path

@Path("/hello")
class HelloResource {
    @GET
    fun sayHello(): String {
        return "Hello World"
    }
}
