package smoothsail.controllers

import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckControllerImpl : HealthCheckController {
  override fun health() =
      "OK"
}