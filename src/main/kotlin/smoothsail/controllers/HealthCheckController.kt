package smoothsail.controllers

import org.springframework.web.bind.annotation.GetMapping

interface HealthCheckController {
  @GetMapping("/health")
  fun health(): String
}