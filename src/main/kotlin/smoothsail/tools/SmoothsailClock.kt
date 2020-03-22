package smoothsail.tools

import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class SmoothsailClock {
  fun now(): LocalDateTime =
      LocalDateTime.now(Clock.systemUTC())
}