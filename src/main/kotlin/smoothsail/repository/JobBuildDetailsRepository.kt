package smoothsail.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import smoothsail.domain.JobBuildDetails

@Repository
interface JobBuildDetailsRepository: JpaRepository<JobBuildDetails, Long> {
  fun findByJobNameAndBuildNumber(
      jobName: String,
      buildNumber: Long
  ): JobBuildDetails?

  fun findByBuildBranchDetailsId(id: Long): List<JobBuildDetails>
}