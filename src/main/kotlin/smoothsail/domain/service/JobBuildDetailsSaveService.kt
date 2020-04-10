package smoothsail.domain.service

import org.springframework.stereotype.Component
import smoothsail.domain.JobBuildDetails
import smoothsail.repository.JobBuildDetailsRepository

@Component
class JobBuildDetailsSaveService(
    private val jobBuildDetailsRepository: JobBuildDetailsRepository
) {
  fun save(jobName: String, buildNumber: Long, buildBranchDetailsId: Long) {
    jobBuildDetailsRepository.save(
        JobBuildDetails(
            buildBranchDetailsId = buildBranchDetailsId,
            jobName = jobName,
            buildNumber = buildNumber
        )
    )
  }
}