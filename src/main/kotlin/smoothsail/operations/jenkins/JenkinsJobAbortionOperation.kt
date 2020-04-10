package smoothsail.operations.jenkins

import org.springframework.stereotype.Component
import smoothsail.domain.JobBuildDetails

@Component
class JenkinsJobAbortionOperation {
  fun abort(jobBuildDetails: JobBuildDetails) {
    // todo call for job abortion. if a call fails, I need to capture and think what to do with it
    TODO()
  }
}