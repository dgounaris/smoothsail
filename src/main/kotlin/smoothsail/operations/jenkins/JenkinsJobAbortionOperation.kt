package smoothsail.operations.jenkins

import org.springframework.stereotype.Component
import smoothsail.domain.JobBuildDetails

@Component
class JenkinsJobAbortionOperation {
  fun abort(jobBuildDetails: JobBuildDetails) {
    TODO()
  }
}